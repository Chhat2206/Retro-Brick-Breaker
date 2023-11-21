package brickGame;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainMenu {

    private Stage primaryStage;

    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void display() {
        Pane root = new Pane();
        VBox menuOptions = new VBox(10);
        menuOptions.setTranslateX(100);
        menuOptions.setTranslateY(200);

        Button startNewGameButton = new Button("Start New Game");
        startNewGameButton.setOnAction(e -> startNewGame());

        Button loadGameButton = new Button("Load Game");
        loadGameButton.setOnAction(e -> loadGame());

//        Button settingsButton = new Button("Settings");
//        settingsButton.setOnAction(e -> showSettings());

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> Platform.exit());

        menuOptions.getChildren().addAll(startNewGameButton, loadGameButton, exitButton);
        root.getChildren().add(menuOptions);

        Scene scene = new Scene(root, 500, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/mainMenu.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void startNewGame() {
        // Logic to start a new game
        Main game = new Main();
        game.newGame(primaryStage);
    }

    private void loadGame() {
        // Logic to load a game
        Main game = new Main();
        game.loadGame();
    }
}
