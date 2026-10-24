package view;

import controller.MenaxhoKlientetController;
import controller.MenaxhoPronatController;
import controller.VizitaController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Klient;
import model.Prona;
import model.Vizite;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PlanifikoVizitaForm extends Application {
    private final VizitaController vizitaController = new VizitaController();
    private final MenaxhoPronatController pronaController = new MenaxhoPronatController();
    private final MenaxhoKlientetController klientController = new MenaxhoKlientetController();
    private final TableView<Vizite> table = new TableView<>();
    private final ObservableList<Vizite> vizitaData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        Label title = new Label("Planifikimi i Vizitave");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox header = new HBox(title);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50;");
        root.setTop(header);

        TableColumn<Vizite, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMinWidth(50);

        TableColumn<Vizite, String> pronaColumn = new TableColumn<>("Prona");
        pronaColumn.setCellValueFactory(cellData -> {
            Prona prona = pronaController.getPronaById(cellData.getValue().getPronaId());
            return javafx.beans.binding.Bindings.concat(prona.getEmri(), " - ", prona.getAdresa());
        });
        pronaColumn.setMinWidth(200);

        TableColumn<Vizite, String> klientColumn = new TableColumn<>("Klienti");
        klientColumn.setCellValueFactory(cellData -> {
            Klient klient = klientController.getKlientById(cellData.getValue().getKlientId());
            return javafx.beans.binding.Bindings.concat(klient.getEmer(), " (", klient.getTipi(), ")");
        });
        klientColumn.setMinWidth(200);

        TableColumn<Vizite, Date> dataColumn = new TableColumn<>("Data & Ora");
        dataColumn.setCellValueFactory(new PropertyValueFactory<>("dataOra"));
        dataColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });
        dataColumn.setMinWidth(150);

        TableColumn<Vizite, String> statusiColumn = new TableColumn<>("Statusi");
        statusiColumn.setCellValueFactory(new PropertyValueFactory<>("statusi"));
        statusiColumn.setMinWidth(100);

        TableColumn<Vizite, String> shenimeColumn = new TableColumn<>("Shënime");
        shenimeColumn.setCellValueFactory(new PropertyValueFactory<>("shenime"));
        shenimeColumn.setMinWidth(200);

        table.getColumns().addAll(idColumn, pronaColumn, klientColumn, dataColumn, statusiColumn, shenimeColumn);
        table.setItems(vizitaData);

        Button btnAdd = createStyledButton("Planifiko Vizitë", "#2ecc71");
        Button btnEdit = createStyledButton("Ndrysho", "#3498db");
        Button btnDelete = createStyledButton("Fshi", "#e74c3c");
        Button btnRefresh = createStyledButton("Refresh", "#9b59b6");

        btnAdd.setOnAction(e -> addVizita());
        btnEdit.setOnAction(e -> editVizita());
        btnDelete.setOnAction(e -> deleteVizita());
        btnRefresh.setOnAction(e -> loadVizitat());

        HBox buttonBox = new HBox(10, btnAdd, btnEdit, btnDelete, btnRefresh);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: #ecf0f1;");
        root.setBottom(buttonBox);
        root.setCenter(table);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Planifikimi i Vizitave - Livelle");
        primaryStage.setScene(scene);
        primaryStage.show();

        loadVizitat();
    }

    private void loadVizitat() {
        vizitaData.setAll(vizitaController.getAllVizite());
    }

    private void addVizita() {
        VizitaDialog dialog = new VizitaDialog(null);
        Optional<Vizite> result = dialog.showAndWait();
        result.ifPresent(vizita -> {
            vizitaController.addVizite(vizita);
            loadVizitat();
        });
    }

    private void editVizita() {
        Vizite selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Gabim", "Ju lutem zgjidhni një vizitë për të ndryshuar", Alert.AlertType.WARNING);
            return;
        }

        VizitaDialog dialog = new VizitaDialog(selected);
        Optional<Vizite> result = dialog.showAndWait();
        result.ifPresent(vizita -> {
            vizitaController.updateVizite(vizita);
            loadVizitat();
        });
    }

    private void deleteVizita() {
        Vizite selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Gabim", "Ju lutem zgjidhni një vizitë për të fshirë", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Konfirmim");
        confirmation.setHeaderText("Jeni i sigurt që dëshironi të fshini këtë vizitë?");
        confirmation.setContentText("Ky veprim nuk mund të zhbëhet.");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            vizitaController.deleteVizite(selected.getId());
            loadVizitat();
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
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-text-fill: white; -fx-font-weight: bold;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;"));
        return btn;
    }

    private class VizitaDialog extends Dialog<Vizite> {
        private final ComboBox<Prona> cmbProna = new ComboBox<>();
        private final ComboBox<Klient> cmbKlient = new ComboBox<>();
        private final ComboBox<String> cmbStatusi = new ComboBox<>();
        private final DatePicker datePicker = new DatePicker();
        private final Spinner<LocalTime> timeSpinner = new Spinner<>();
        private final TextArea txtShenime = new TextArea();

        public VizitaDialog(Vizite vizita) {
            setTitle(vizita == null ? "Planifiko Vizitë të Re" : "Ndrysho Vizitë");
            
            ButtonType saveButtonType = new ButtonType("Ruaj", ButtonBar.ButtonData.OK_DONE);
            getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            initializeComponents();

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setPadding(new Insets(20));

            grid.addRow(0, new Label("Prona:"), cmbProna);
            grid.addRow(1, new Label("Klienti:"), cmbKlient);
            grid.addRow(2, new Label("Data:"), datePicker);
            grid.addRow(3, new Label("Ora:"), timeSpinner);
            grid.addRow(4, new Label("Statusi:"), cmbStatusi);
            grid.addRow(5, new Label("Shënime:"), txtShenime);

            getDialogPane().setContent(grid);
            Platform.runLater(cmbProna::requestFocus);

            setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    Vizite resultVizita = vizita != null ? vizita : new Vizite();
                    resultVizita.setPronaId(cmbProna.getValue().getId());
                    resultVizita.setKlientId(cmbKlient.getValue().getId());
                    resultVizita.setAgjentId(1);
                    
                    LocalDate date = datePicker.getValue();
                    LocalTime time = timeSpinner.getValue();
                    Date dataOra = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant());
                    resultVizita.setDataOra(dataOra);
                    
                    resultVizita.setStatusi(cmbStatusi.getValue());
                    resultVizita.setShenime(txtShenime.getText());
                    
                    return resultVizita;
                }
                return null;
            });

            if (vizita != null) {
                loadVizitaData(vizita);
            }
        }

        private void initializeComponents() {

            cmbProna.setItems(FXCollections.observableArrayList(pronaController.getAllPronat()));
            cmbProna.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Prona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getEmri() + " - " + item.getAdresa());
                    }
                }
            });
            cmbProna.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Prona item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getEmri() + " - " + item.getAdresa());
                    }
                }
            });
            
            cmbKlient.setItems(FXCollections.observableArrayList(klientController.getAllKlientet()));
            cmbKlient.setCellFactory(param -> new ListCell<>() {
                @Override
                protected void updateItem(Klient item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getEmer() + " (" + item.getTipi() + ")");
                    }
                }
            });
            cmbKlient.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(Klient item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getEmer() + " (" + item.getTipi() + ")");
                    }
                }
            });
            
            cmbStatusi.setItems(FXCollections.observableArrayList("PLANIFIKUAR", "KRYER", "ANULUAR"));
            cmbStatusi.setValue("PLANIFIKUAR");
            
            datePicker.setValue(LocalDate.now());
            
            SpinnerValueFactory<LocalTime> timeFactory = 
                new SpinnerValueFactory<LocalTime>() {
                    {
                        setValue(LocalTime.of(9, 0));
                    }
                    
                    @Override
                    public void decrement(int steps) {
                        setValue(getValue().minusMinutes(30 * steps));
                    }
                    
                    @Override
                    public void increment(int steps) {
                        setValue(getValue().plusMinutes(30 * steps));
                    }
                };
            timeSpinner.setValueFactory(timeFactory);
            timeSpinner.setEditable(true);
            
            txtShenime.setPrefRowCount(3);
            txtShenime.setWrapText(true);
        }

        private void loadVizitaData(Vizite vizita) {
            for (Prona prona : cmbProna.getItems()) {
                if (prona.getId() == vizita.getPronaId()) {
                    cmbProna.setValue(prona);
                    break;
                }
            }
            
            for (Klient klient : cmbKlient.getItems()) {
                if (klient.getId() == vizita.getKlientId()) {
                    cmbKlient.setValue(klient);
                    break;
                }
            }
            
            LocalDate date = vizita.getDataOra().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime time = vizita.getDataOra().toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            datePicker.setValue(date);
            timeSpinner.getValueFactory().setValue(time);
            
            cmbStatusi.setValue(vizita.getStatusi());
            txtShenime.setText(vizita.getShenime());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}