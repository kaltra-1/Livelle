package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Agjent;
import controller.MenaxhoPronatController;
import controller.MenaxhoKlientetController;
import controller.VizitaController;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Dashboard extends Application {
    private Agjent agjent;
    private MenaxhoPronatController pronaController = new MenaxhoPronatController();
    private MenaxhoKlientetController klientController = new MenaxhoKlientetController();
    private VizitaController vizitaController = new VizitaController();
    
    public Dashboard(Agjent agjent) {
        this.agjent = agjent;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f7fa;");
        
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50; -fx-spacing: 20;");
        
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);
        
        Label welcomeLabel = new Label("Mirësevini, " + agjent.getEmer() + "!");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        topBar.getChildren().addAll(logo, welcomeLabel);
        root.setTop(topBar);
        
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #34495e;");
        sidebar.setPrefWidth(200);
        
        Button btnPronat = createMenuButton("Pronat", "#3498db");
        Button btnKlientet = createMenuButton("Klientet", "#2ecc71");
        Button btnVizitat = createMenuButton("Vizitat", "#e74c3c");
        Button btnStatistikat = createMenuButton("Statistikat", "#9b59b6");
        Button btnDil = createMenuButton("Dil", "#95a5a6");
        
        btnPronat.setOnAction(e -> new MenaxhoPronatForm().start(new Stage()));
        btnKlientet.setOnAction(e -> new MenaxhoKlientetForm().start(new Stage()));
        btnVizitat.setOnAction(e -> new PlanifikoVizitaForm().start(new Stage()));
        btnStatistikat.setOnAction(e -> new StatistikaForm().start(new Stage()));
        btnDil.setOnAction(e -> System.exit(0));
        
        sidebar.getChildren().addAll(btnPronat, btnKlientet, btnVizitat, btnStatistikat, btnDil);
        root.setLeft(sidebar);
        
        GridPane dashboardGrid = new GridPane();
        dashboardGrid.setPadding(new Insets(20));
        dashboardGrid.setHgap(20);
        dashboardGrid.setVgap(20);
        
        dashboardGrid.add(createStatCard("Total Prona", String.valueOf(pronaController.getAllPronat().size()),
        		"#3498db"), 0, 0);
        dashboardGrid.add(createStatCard("Klientë të Rinj", String.valueOf(klientController.getAllKlientet().size()),
        		"#2ecc71"), 1, 0);
        dashboardGrid.add(createStatCard("Vizita Sot", String.valueOf(countVizitatSot()), "#e74c3c"), 2, 0);
        dashboardGrid.add(createStatCard("Shitje Muajin", String.valueOf(countShitjeMuajin()), "#9b59b6"), 0, 1);
        
        root.setCenter(dashboardGrid);
        
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Paneli i Administrimit - Livelle");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private int countVizitatSot() {
        LocalDate today = LocalDate.now();
        return (int) vizitaController.getAllVizite().stream()
            .filter(v -> {
                LocalDate visitDate = v.getDataOra().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return visitDate.equals(today);
            })
            .count();
    }
    
    private int countShitjeMuajin() {
        LocalDate now = LocalDate.now();
        return (int) pronaController.getAllPronat().stream()
            .filter(p -> p.getStatusi().equalsIgnoreCase("E ZËNË"))
            .count();
    }
    
    private Button createMenuButton(String text, String color) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(" + color + ", 20%); "
        		+ "-fx-text-fill: white; " + "-fx-font-weight: bold; -fx-padding: 10;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: " + color + ";"
        		+ " -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;"));
        return button;
    }
    
    private VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 5; -fx-border-radius: 5;"
        		+ " -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);");
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 14px;");
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 24px; -fx-font-weight: bold;");
        
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }
}