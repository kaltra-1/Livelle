package view;

import controller.MenaxhoKlientetController;
import controller.MenaxhoPronatController;
import controller.VizitaController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Klient;
import model.Prona;
import model.Vizite;

import java.util.List;

public class StatistikaForm extends Application {
    
    private final MenaxhoPronatController pronaController = new MenaxhoPronatController();
    private final MenaxhoKlientetController klientController = new MenaxhoKlientetController();
    private final VizitaController vizitaController = new VizitaController();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");

        Label title = new Label("Statistikat");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox header = new HBox(title);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50;");
        root.setTop(header);

        GridPane statsGrid = new GridPane();
        statsGrid.setPadding(new Insets(20));
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        
        try {
            statsGrid.add(createStatCard("Prona në Shitje", numriPronaveNeShitje(), "#3498db"), 0, 0);
            statsGrid.add(createStatCard("Total Klientë", numriKlienteve(), "#2ecc71"), 1, 0);
            statsGrid.add(createStatCard("Vizita të Planifikuara", numriVizitavePlanifikuara(), "#e74c3c"), 2, 0);
            
            statsGrid.add(createStatCard("Prona me Qira", numriPronaveMeQira(), "#f39c12"), 0, 1);
            statsGrid.add(createStatCard("Prona të Shitura", numriPronaveTeShitura(), "#1abc9c"), 1, 1);
            statsGrid.add(createStatCard("Vizita të Kryera", numriVizitaveKryera(), "#9b59b6"), 2, 1);
            
            statsGrid.add(createStatCard("Prona të Lira", numriPronaveTeLira(), "#27ae60"), 0, 2);
            statsGrid.add(createStatCard("Vizita të Anulluara", numriVizitaveAnulluara(), "#e67e22"), 1, 2);
            statsGrid.add(createStatCard("Total Prona", numriTotalPronave(), "#8e44ad"), 2, 2);
        } catch (Exception e) {
            showErrorAlert("Gabim në ngarkimin e të dhënave", e.getMessage());
        }

        Button btnRefresh = createStyledButton("Refresh", "#3498db");
        btnRefresh.setOnAction(e -> refreshStats(statsGrid));
        
        HBox buttonBox = new HBox(btnRefresh);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: #ecf0f1;");
        
        VBox centerContainer = new VBox(20, statsGrid);
        centerContainer.setPadding(new Insets(10));
        
        root.setCenter(centerContainer);
        root.setBottom(buttonBox);

        Scene scene = new Scene(root, 600, 600); 
        primaryStage.setTitle("Statistikat - Livelle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private VBox createStatCard(String title, int value, String color) {
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label valueLabel = new Label(String.valueOf(value));
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        VBox card = new VBox(5, titleLabel, valueLabel);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-border-color: #ecf0f1; -fx-border-radius: 5; " +
                     "-fx-background-radius: 5; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 5, 0, 0);");
        
        return card;
    }
    
    private Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + color + ", 20%); -fx-text-fill: white; -fx-font-weight: bold;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold;"));
        return btn;
    }
    
    private void refreshStats(GridPane grid) {
        grid.getChildren().clear();
        try {
            grid.add(createStatCard("Prona në Shitje", numriPronaveNeShitje(), "#3498db"), 0, 0);
            grid.add(createStatCard("Total Klientë", numriKlienteve(), "#2ecc71"), 1, 0);
            grid.add(createStatCard("Vizita të Planifikuara", numriVizitavePlanifikuara(), "#e74c3c"), 2, 0);
            
            grid.add(createStatCard("Prona me Qira", numriPronaveMeQira(), "#f39c12"), 0, 1);
            grid.add(createStatCard("Prona të Shitura", numriPronaveTeShitura(), "#1abc9c"), 1, 1);
            grid.add(createStatCard("Vizita të Kryera", numriVizitaveKryera(), "#9b59b6"), 2, 1);
            
            grid.add(createStatCard("Prona të Lira", numriPronaveTeLira(), "#27ae60"), 0, 2);
            grid.add(createStatCard("Vizita të Anulluara", numriVizitaveAnulluara(), "#e67e22"), 1, 2);
            grid.add(createStatCard("Total Prona", numriTotalPronave(), "#8e44ad"), 2, 2);
        } catch (Exception e) {
            showErrorAlert("Gabim në rifreskimin e të dhënave", e.getMessage());
        }
    }
    
    private int numriPronaveNeShitje() {
        List<Prona> pronat = pronaController.getAllPronat();
        if (pronat == null || pronat.isEmpty()) {
            return 0;
        }
        return (int) pronat.stream()
                         .filter(p -> p.getStatusi() != null && p.getStatusi().equalsIgnoreCase("NË SHITJE"))
                         .count();
    }
    
    private int numriPronaveMeQira() {
        List<Prona> pronat = pronaController.getAllPronat();
        if (pronat == null || pronat.isEmpty()) {
            return 0;
        }
        return (int) pronat.stream()
                         .filter(p -> p.getStatusi() != null && p.getStatusi().equalsIgnoreCase("ME QIRA"))
                         .count();
    }
    
    private int numriKlienteve() {
        List<Klient> klientet = klientController.getAllKlientet();
        return klientet != null ? klientet.size() : 0;
    }
    
    private int numriVizitavePlanifikuara() {
        List<Vizite> vizitat = vizitaController.getAllVizite();
        if (vizitat == null || vizitat.isEmpty()) {
            return 0;
        }
        return (int) vizitat.stream()
                          .filter(v -> v.getStatusi() != null && v.getStatusi().equalsIgnoreCase("PLANIFIKUAR"))
                          .count();
    }
    
    private int numriVizitaveKryera() {
        List<Vizite> vizitat = vizitaController.getAllVizite();
        if (vizitat == null || vizitat.isEmpty()) {
            return 0;
        }
        return (int) vizitat.stream()
                          .filter(v -> v.getStatusi() != null && v.getStatusi().equalsIgnoreCase("KRYER"))
                          .count();
    }
    
    private int numriVizitaveAnulluara() {
        List<Vizite> vizitat = vizitaController.getAllVizite();
        if (vizitat == null || vizitat.isEmpty()) {
            return 0;
        }
        return (int) vizitat.stream()
                          .filter(v -> v.getStatusi() != null && v.getStatusi().equalsIgnoreCase("ANULUAR"))
                          .count();
    }
    
    private int numriPronaveTeLira() {
        List<Prona> pronat = pronaController.getAllPronat();
        if (pronat == null || pronat.isEmpty()) {
            return 0;
        }
        return (int) pronat.stream()
                         .filter(p -> p.getStatusi() != null && p.getStatusi().equalsIgnoreCase("E LIRË"))
                         .count();
    }
    
    private int numriPronaveTeShitura() {
        List<Prona> pronat = pronaController.getAllPronat();
        if (pronat == null || pronat.isEmpty()) {
            return 0;
        }
        return (int) pronat.stream()
                         .filter(p -> p.getStatusi() != null && p.getStatusi().equalsIgnoreCase("E SHITUR"))
                         .count();
    }
    
    private int numriTotalPronave() {
        List<Prona> pronat = pronaController.getAllPronat();
        return pronat != null ? pronat.size() : 0;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}