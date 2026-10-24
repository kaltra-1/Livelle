package util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.imageio.ImageIO;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageUtil {

    public static Image createImage(byte[] imageData) {
        if (imageData == null || imageData.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(imageData)) {
            return new Image(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ImageView createImageView(byte[] imageData, double width, double height) {
        Image image = createImage(imageData);
        if (image == null) return null;
        
        ImageView imageView = new ImageView(image);
        if (width > 0) imageView.setFitWidth(width);
        if (height > 0) imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public static byte[] readImageToBytes(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            if (bufferedImage == null) {
                throw new IOException("Unsupported image format");
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String format = getFileExtension(file).toLowerCase();
            ImageIO.write(bufferedImage, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] resizeImage(byte[] original, int targetWidth, int targetHeight, boolean preserveRatio) {
        try {
            BufferedImage originalImage = SwingFXUtils.fromFXImage(createImage(original), null);
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g = resizedImage.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            if (preserveRatio) {
                double ratio = Math.min((double)targetWidth/originalImage.getWidth(), 
                                      (double)targetHeight/originalImage.getHeight());
                int width = (int)(originalImage.getWidth() * ratio);
                int height = (int)(originalImage.getHeight() * ratio);
                g.drawImage(originalImage, 0, 0, width, height, null);
            } else {
                g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            }
            
            g.dispose();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1) return "jpg"; 
        return name.substring(lastDot + 1);
    }

    public static byte[] imageToBytes(Image image, String format) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, format, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}