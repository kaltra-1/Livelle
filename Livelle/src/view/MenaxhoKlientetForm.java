package view;

import controller.MenaxhoKlientetController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Klient;

import java.util.List;
import java.util.Optional;

public class MenaxhoKlientetForm extends Application {
    private final MenaxhoKlientetController controller = new MenaxhoKlientetController();
    private final TableView<Klient> table = new TableView<>();
    private final TextField txtSearch = new TextField();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        Label title = new Label("Menaxhimi i Klientëve");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox header = new HBox(title);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50;");

        Button btnSearch = new Button("Kërko");
        btnSearch.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        btnSearch.setOnAction(e -> searchKlient());

        HBox searchPanel = new HBox(10, new Label("Kërko Klient:"), txtSearch, btnSearch);
        searchPanel.setPadding(new Insets(10));
        searchPanel.setStyle("-fx-background-color: #ecf0f1;");

        VBox topContainer = new VBox(header, searchPanel);
        root.setTop(topContainer);

        TableColumn<Klient, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMinWidth(80);

        TableColumn<Klient, String> emriColumn = new TableColumn<>("Emri");
        emriColumn.setCellValueFactory(new PropertyValueFactory<>("emer"));
        emriColumn.setMinWidth(150);

        TableColumn<Klient, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setMinWidth(200);

        TableColumn<Klient, String> telefonColumn = new TableColumn<>("Telefon");
        telefonColumn.setCellValueFactory(new PropertyValueFactory<>("telefon"));
        telefonColumn.setMinWidth(120);

        TableColumn<Klient, String> tipiColumn = new TableColumn<>("Tipi");
        tipiColumn.setCellValueFactory(new PropertyValueFactory<>("tipi"));
        tipiColumn.setMinWidth(100);

        TableColumn<Klient, String> interesimeColumn = new TableColumn<>("Interesime");
        interesimeColumn.setCellValueFactory(new PropertyValueFactory<>("interesime"));
        interesimeColumn.setMinWidth(200);

        table.getColumns().addAll(idColumn, emriColumn, emailColumn, telefonColumn, tipiColumn, interesimeColumn);
        table.setItems(FXCollections.observableArrayList(controller.getAllKlientet()));

        Button btnAdd = createStyledButton("Shto Klient", "#2ecc71");
        Button btnEdit = createStyledButton("Ndrysho", "#3498db");
        Button btnDelete = createStyledButton("Fshi", "#e74c3c");
        Button btnRefresh = createStyledButton("Refresh", "#9b59b6");

        btnAdd.setOnAction(e -> showAddEditDialog(null));
        btnEdit.setOnAction(e -> {
            Klient selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showAddEditDialog(selected);
            else showAlert("Kujdes", "Ju lutem zgjidhni një klient për të ndryshuar", Alert.AlertType.WARNING);
        });
        
        btnDelete.setOnAction(e -> deleteKlient());
        btnRefresh.setOnAction(e -> refreshTable());

        HBox buttonBox = new HBox(10, btnAdd, btnEdit, btnDelete, btnRefresh);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: #ecf0f1;");
        root.setBottom(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Menaxhimi i Klientëve - Livelle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadKlientet() {
        List<Klient> klientet = controller.getAllKlientet();
        table.setItems(FXCollections.observableArrayList(klientet));
    }
    
    private void searchKlient() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            loadKlientet();
        } else {
            List<Klient> klientet = controller.searchKlient(keyword);
            table.setItems(FXCollections.observableArrayList(klientet));
        }
    }
    
    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(controller.getAllKlientet()));
    }

    private void deleteKlient() {
        Klient selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmim Fshirjeje");
            confirmation.setHeaderText("Jeni i sigurt që dëshironi të fshini këtë klient?");
            confirmation.setContentText("Ky veprim nuk mund të zhbëhet.");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    controller.deleteKlient(selected.getId());
                    refreshTable();
                    showAlert("Sukses", "Klienti u fshi me sukses", Alert.AlertType.INFORMATION);
                } catch (Exception ex) {
                    showAlert("Gabim", "Ndodhi një gabim gjatë fshirjes: " + ex.getMessage(), Alert.AlertType.ERROR);
                    ex.printStackTrace();
                }
            }
        } else {
            showAlert("Kujdes", "Ju lutem zgjidhni një klient për të fshirë", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", 20%); "
        		+ "-fx-text-fill: white; -fx-font-weight: bold;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; "
        		+ "-fx-font-weight: bold;"));
        return btn;
    }

    private void showAddEditDialog(Klient klient) {
        Dialog<Klient> dialog = new Dialog<>();
        dialog.setTitle(klient == null ? "Shto Klient" : "Ndrysho Klient");

        ButtonType saveButtonType = new ButtonType("Ruaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField txtEmer = new TextField();
        TextField txtEmail = new TextField();
        TextField txtTelefon = new TextField();
        ComboBox<String> cmbTipi = new ComboBox<>(FXCollections.observableArrayList("BLERËS", "QIRAMARRËS"));
        TextArea txtInteresime = new TextArea();
        txtInteresime.setPrefRowCount(3);

        if (klient != null) {
            txtEmer.setText(klient.getEmer());
            txtEmail.setText(klient.getEmail());
            txtTelefon.setText(klient.getTelefon());
            cmbTipi.setValue(klient.getTipi());
            txtInteresime.setText(klient.getInteresime());
        }

        grid.addRow(0, new Label("Emri:"), txtEmer);
        grid.addRow(1, new Label("Email:"), txtEmail);
        grid.addRow(2, new Label("Telefon:"), txtTelefon);
        grid.addRow(3, new Label("Tipi:"), cmbTipi);
        grid.addRow(4, new Label("Interesime:"), txtInteresime);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(txtEmer::requestFocus);

        dialog.setResultConverter(btn -> {
            if (btn == saveButtonType) {
                try {
                    Klient k = (klient != null) ? klient : new Klient();
                    k.setEmer(txtEmer.getText());
                    k.setEmail(txtEmail.getText());
                    k.setTelefon(txtTelefon.getText());
                    k.setTipi(cmbTipi.getValue());
                    k.setInteresime(txtInteresime.getText());
                    return k;
                } catch (Exception ex) {
                    showAlert("Gabim", "Ju lutem jepni të dhëna valide.", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        Optional<Klient> result = dialog.showAndWait();
        result.ifPresent(k -> {
            if (klient == null) controller.addKlient(k);
            else controller.updateKlient(k);
            refreshTable();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}