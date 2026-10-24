package view;

import controller.MenaxhoPronatController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;
import javafx.util.Callback;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.*;
import java.util.List;
import java.util.Optional;
import model.Prona;

public class MenaxhoPronatForm extends Application {
    private TableView<Prona> table = new TableView<>();
    private MenaxhoPronatController controller = new MenaxhoPronatController();
    private final TextField txtSearch = new TextField();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        Label title = new Label("Menaxhimi i Pronave");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox header = new HBox(title);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50;");

        Button btnSearch = new Button("Kërko");
        btnSearch.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnSearch.setOnAction(e -> searchProna());

        HBox searchPanel = new HBox(10, new Label("Kërko Pronë:"), txtSearch, btnSearch);
        searchPanel.setPadding(new Insets(10));
        searchPanel.setStyle("-fx-background-color: #ecf0f1;");

        VBox topContainer = new VBox(header, searchPanel);
        root.setTop(topContainer);

        TableColumn<Prona, String> emriCol = new TableColumn<>("Emri");
        emriCol.setCellValueFactory(new PropertyValueFactory<>("emri"));
        emriCol.setMinWidth(150);

        TableColumn<Prona, String> adresaCol = new TableColumn<>("Adresa");
        adresaCol.setCellValueFactory(new PropertyValueFactory<>("adresa"));
        adresaCol.setMinWidth(200);

        TableColumn<Prona, Double> cmimiCol = new TableColumn<>("Çmimi");
        cmimiCol.setCellValueFactory(new PropertyValueFactory<>("cmimi"));
        cmimiCol.setMinWidth(100);

        TableColumn<Prona, String> statusiCol = new TableColumn<>("Statusi");
        statusiCol.setCellValueFactory(new PropertyValueFactory<>("statusi"));
        statusiCol.setMinWidth(100);

        TableColumn<Prona, Integer> nrDhomaCol = new TableColumn<>("Nr. Dhoma");
        nrDhomaCol.setCellValueFactory(new PropertyValueFactory<>("nrDhoma"));
        nrDhomaCol.setMinWidth(80);

        TableColumn<Prona, Double> siperfaqeCol = new TableColumn<>("Sipërfaqja");
        siperfaqeCol.setCellValueFactory(new PropertyValueFactory<>("siperfaqe"));
        siperfaqeCol.setMinWidth(100);

        TableColumn<Prona, String> pershkrimiCol = new TableColumn<>("Përshkrimi");
        pershkrimiCol.setCellValueFactory(new PropertyValueFactory<>("pershkrimi"));
        pershkrimiCol.setMinWidth(200);

        TableColumn<Prona, byte[]> fotoCol = new TableColumn<>("Foto");
        fotoCol.setCellValueFactory(new PropertyValueFactory<>("foto"));
        fotoCol.setCellFactory(createImageCellFactory());
        fotoCol.setMinWidth(200);

        table.getColumns().addAll(emriCol, adresaCol, cmimiCol, statusiCol, nrDhomaCol, siperfaqeCol, pershkrimiCol, fotoCol);
        table.setItems(FXCollections.observableArrayList(controller.getAllPronat()));

        Button btnAdd = createStyledButton("Shto Pronë", "#2ecc71");
        Button btnEdit = createStyledButton("Ndrysho", "#3498db");
        Button btnDelete = createStyledButton("Fshi", "#e74c3c");
        Button btnRefresh = createStyledButton("Refresh", "#9b59b6");

        btnAdd.setOnAction(e -> showAddEditDialog(null));
        btnEdit.setOnAction(e -> {
            Prona selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showAddEditDialog(selected);
            else showAlert("Kujdes", "Ju lutem zgjidhni një pronë për të ndryshuar", Alert.AlertType.WARNING);
        });
        
        btnDelete.setOnAction(e -> deleteProna());
        btnRefresh.setOnAction(e -> refreshTable());

        HBox buttonBox = new HBox(10, btnAdd, btnEdit, btnDelete, btnRefresh);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: #ecf0f1;");
        root.setBottom(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setTitle("Menaxhimi i Pronave - Livelle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadProna() {
        List<Prona> prona = controller.getAllPronat();
        table.setItems(FXCollections.observableArrayList(prona));
    }
    
    private void searchProna() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadProna();
        } else {
            List<Prona> prona = controller.searchProna(keyword);
            table.setItems(FXCollections.observableArrayList(prona));
        }
    }
    
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(controller.getAllPronat()));
    }

    private void deleteProna() {
        Prona selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmim Fshirjeje");
            confirmation.setHeaderText("Jeni i sigurt që dëshironi të fshini këtë pronë?");
            confirmation.setContentText("Kjo do të fshijë të gjitha vizitat e lidhura me këtë pronë.");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    controller.deleteProna(selected.getId());
                    refreshTable();
                    showAlert("Sukses", "Prona u fshi me sukses", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    showAlert("Gabim", "Ndodhi një gabim gjatë fshirjes: " + ex.getMessage(), Alert.AlertType.ERROR);
                    ex.printStackTrace();
                }
            }
        } else {
            showAlert("Kujdes", "Ju lutem zgjidhni një pronë për të fshirë", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private Callback<TableColumn<Prona, byte[]>, TableCell<Prona, byte[]>> createImageCellFactory() {
        return column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(byte[] item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image(new ByteArrayInputStream(item));
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        };
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-text-fill: white; -fx-font-weight: bold;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;"));
        return btn;
    }

    private void showAddEditDialog(Prona prona) {
        Dialog<Prona> dialog = new Dialog<>();
        dialog.setTitle(prona == null ? "Shto Pronë" : "Ndrysho Pronë");

        ButtonType saveButtonType = new ButtonType("Ruaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtEmri = new TextField();
        TextField txtAdresa = new TextField();
        TextField txtCmimi = new TextField();
        ComboBox<String> cmbStatusi = new ComboBox<>();
        cmbStatusi.getItems().addAll("NË SHITJE", "ME QIRA", "E ZËNË", "E SHITUR");
        TextField txtNrDhoma = new TextField();
        TextField txtSiperfaqe = new TextField();
        TextArea txtPershkrimi = new TextArea();
        txtPershkrimi.setPrefRowCount(3);

        Button btnUploadFoto = new Button("Ngarko Foto");
        Label lblFoto = new Label();
        lblFoto.setPrefSize(100, 100);
        lblFoto.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5;");
        Image[] imageHolder = new Image[1];

        btnUploadFoto.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Zgjidhni një foto");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imazhet", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                try {
                    Image image = new Image(selectedFile.toURI().toString());
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);
                    imageView.setPreserveRatio(true);
                    lblFoto.setGraphic(imageView);
                    imageHolder[0] = image;
                } catch (Exception ex) {
                    showAlert("Gabim", "Gabim në ngarkimin e fotos: " + ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });

        if (prona != null) {
            txtEmri.setText(prona.getEmri());
            txtAdresa.setText(prona.getAdresa());
            txtCmimi.setText(String.valueOf(prona.getCmimi()));
            cmbStatusi.setValue(prona.getStatusi());
            txtNrDhoma.setText(String.valueOf(prona.getNrDhoma()));
            txtSiperfaqe.setText(String.valueOf(prona.getSiperfaqe()));
            txtPershkrimi.setText(prona.getPershkrimi());

            if (prona.getFoto() != null) {
                Image img = new Image(new ByteArrayInputStream(prona.getFoto()));
                ImageView imageView = new ImageView(img);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                lblFoto.setGraphic(imageView);
                imageHolder[0] = img;
            }
        }

        grid.addRow(0, new Label("Emri:"), txtEmri);
        grid.addRow(1, new Label("Adresa:"), txtAdresa);
        grid.addRow(2, new Label("Çmimi:"), txtCmimi);
        grid.addRow(3, new Label("Statusi:"), cmbStatusi);
        grid.addRow(4, new Label("Nr. Dhoma:"), txtNrDhoma);
        grid.addRow(5, new Label("Sipërfaqja:"), txtSiperfaqe);
        grid.addRow(6, new Label("Përshkrimi:"), txtPershkrimi);
        grid.addRow(7, new Label("Foto:"), btnUploadFoto);
        grid.add(lblFoto, 1, 8);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(txtEmri::requestFocus);

        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                try {
                    Prona p = (prona != null) ? prona : new Prona();
                    p.setEmri(txtEmri.getText());
                    p.setAdresa(txtAdresa.getText());
                    p.setCmimi(Double.parseDouble(txtCmimi.getText()));
                    p.setStatusi(cmbStatusi.getValue());
                    p.setNrDhoma(Integer.parseInt(txtNrDhoma.getText()));
                    p.setSiperfaqe(Double.parseDouble(txtSiperfaqe.getText()));
                    p.setPershkrimi(txtPershkrimi.getText());

                    if (imageHolder[0] != null) {
                        try {
                            byte[] fotoBytes = convertImageToBytes(imageHolder[0]);
                            p.setFoto(fotoBytes);
                        } catch (Exception ex) {
                            showAlert("Gabim", "Gabim në konvertimin e fotos.", Alert.AlertType.ERROR);
                        }
                    }

                    return p;
                } catch (Exception ex) {
                    showAlert("Gabim", "Ju lutem jepni të dhëna valide.", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Prona> result = dialog.showAndWait();
        result.ifPresent(p -> {
            if (prona == null) controller.addProna(p);
            else controller.updateProna(p);
            refreshTable();
        });
    }
    
    private byte[] convertImageToBytes(Image image) {
        try {
            PixelReader reader = image.getPixelReader();
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            WritableImage writableImage = new WritableImage(reader, width, height);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = writableImage.getPixelReader().getColor(x, y);
                    int rgb = ((int)(color.getRed() * 255) << 16) |
                              ((int)(color.getGreen() * 255) << 8) |
                              ((int)(color.getBlue() * 255));
                    bufferedImage.setRGB(x, y, rgb);
                }
            }
            
            ImageIO.write(bufferedImage, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}