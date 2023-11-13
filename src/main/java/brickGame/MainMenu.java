package brickGame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    public static void display(Stage primaryStage) {
        VBox menuLayout = new VBox(10);
        Scene menuScene = new Scene(menuLayout, 300, 200);

        Button startButton = new Button("Start New Game");
        Button loadButton = new Button("Load Game");
        Button exitButton = new Button("Exit");

        startButton.setOnAction(e -> {
            try {
                // Start a new game
                new Main().start(primaryStage);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        loadButton.setOnAction(e -> {
            // Load game logic here
            System.out.println("Load Game");
        });

        exitButton.setOnAction(e -> primaryStage.close());

        menuLayout.getChildren().addAll(startButton, loadButton, exitButton);

        primaryStage.setScene(menuScene);
        primaryStage.show();
    }
}