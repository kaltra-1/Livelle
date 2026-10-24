package view;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SplashScreen extends Application {

	@Override
	public void start(Stage primaryStage) {
		Image backgroundImage = new Image(getClass().getResourceAsStream("/images/logo.png"),
				0, 0, true, true);

		ImageView backgroundImageView = new ImageView(backgroundImage);
		backgroundImageView.setPreserveRatio(true); 
		backgroundImageView.setSmooth(true); 

		backgroundImageView.fitWidthProperty().bind(Bindings.createDoubleBinding(
				() -> primaryStage.getScene() != null ? primaryStage.getScene().getWidth() : 800,
						primaryStage.sceneProperty()));

		backgroundImageView.fitHeightProperty().bind(Bindings.createDoubleBinding(
				() -> primaryStage.getScene() != null ? primaryStage.getScene().getHeight() : 600,
						primaryStage.sceneProperty()));

		Text welcomePart1 = new Text("Mirëserdhët në ");
		welcomePart1.setFont(Font.font("Euclid", 24));
		welcomePart1.setStyle("-fx-fill: black; -fx-font-weight: bold;");

		Text welcomePart2 = new Text("Sistemin Livelle");
		welcomePart2.setFont(Font.font("Euclid", 24));
		welcomePart2.setStyle("-fx-fill: black; -fx-font-weight: bold;");

		TextFlow welcomeTextFlow = new TextFlow(welcomePart1, welcomePart2);

		Button loginButton = new Button("Log In");
		loginButton.setFont(Font.font(16));
		loginButton.setOnAction(e -> {
			try {
				new LoginForm().start(new Stage());
				primaryStage.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		HBox bottomBar = new HBox(20, welcomeTextFlow, loginButton);
		bottomBar.setAlignment(Pos.CENTER_RIGHT);
		bottomBar.setStyle("-fx-padding: 20;");
		
		BorderPane overlay = new BorderPane();
		overlay.setBottom(bottomBar);

		StackPane root = new StackPane();
		root.getChildren().addAll(backgroundImageView, overlay);

		Scene scene = new Scene(root, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Livelle");
		primaryStage.show();
	}
}
