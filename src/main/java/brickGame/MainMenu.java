package brickGame;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    public static Scene createMainMenu(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-background-color: lightblue;");

        Button startButton = new Button("Start New Game");
        Button loadButton = new Button("Load Game");
        Button exitButton = new Button("Exit");

        startButton.setOnAction(e -> {
            // Start a new game
            primaryStage.setScene(createGameScene(primaryStage));
        });

        loadButton.setOnAction(e -> {
            // Load a saved game
            // Implement your load game logic here
        });

        exitButton.setOnAction(e -> Platform.exit());

        menuLayout.getChildren().addAll(startButton, loadButton, exitButton);

        return new Scene(menuLayout, 300, 200);
    }

    private static Scene createGameScene(Stage primaryStage) {
        return null;
    }
}
