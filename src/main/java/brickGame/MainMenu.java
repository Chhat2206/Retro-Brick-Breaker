package brickGame;

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainMenu extends VBox {
    private Stage primaryStage;

    public MainMenu(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> startGame());

        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> loadGame());

        Button settingsButton = new Button("Settings");
        settingsButton.setOnAction(e -> openSettings());

        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        this.getChildren().addAll(startButton, loadButton, settingsButton, quitButton);
    }

    private void startGame() {
    }

    private void loadGame() {
    }

    private void openSettings() {
    }
}
