package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import controller.AuthController;
import model.Agjent;

public class LoginForm extends Application {
	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();

		root.setStyle("-fx-background-color: #f5f0e6;");

		VBox loginBox = new VBox(20);
		loginBox.setAlignment(Pos.CENTER);
		loginBox.setPadding(new Insets(30));
		loginBox.setStyle("-fx-background-color: white; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
		loginBox.setMaxWidth(350);
		loginBox.setMaxHeight(300); 

		Label titleLabel = new Label("Hyrje në Sistem");
		titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

		TextField txtUsername = new TextField();
		txtUsername.setPromptText("Përdoruesi");
		txtUsername.setStyle("-fx-padding: 10;");

		PasswordField txtPassword = new PasswordField();
		txtPassword.setPromptText("Fjalëkalimi");
		txtPassword.setStyle("-fx-padding: 10;");

		Button btnLogin = new Button("HYR");
		btnLogin.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
		btnLogin.setMaxWidth(Double.MAX_VALUE);

		Label lblMessage = new Label();

		loginBox.getChildren().addAll(titleLabel, txtUsername, txtPassword, btnLogin, lblMessage);
		root.getChildren().add(loginBox);
		AuthController authController = new AuthController();
		btnLogin.setOnAction(e -> {
			Agjent agjent = authController.login(txtUsername.getText(), txtPassword.getText());
			if (agjent != null) {
				new Dashboard(agjent).start(new Stage());
				primaryStage.close();
			} else {
				lblMessage.setText("Përdoruesi ose fjalëkalimi i gabuar");
				lblMessage.setStyle("-fx-text-fill: #e74c3c;");
			}
		});

		Scene scene = new Scene(root, 600, 600);
		primaryStage.setTitle("Livelle - Hyrje në Sistem");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}