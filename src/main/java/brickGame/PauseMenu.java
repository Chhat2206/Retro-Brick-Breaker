package brickGame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PauseMenu {

    public static void display(Main main, GameEngine engine) {
        Stage pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.TRANSPARENT);

        VBox pauseLayout = new VBox(10);
        Button resumeButton = new Button("Resume");
        Button saveButton = new Button("Save Game");
        Button loadButton = new Button("Load Game");
        Button quitButton = new Button("Quit");

        resumeButton.setOnAction(e -> {
            engine.start();
            pauseStage.close();
        });

        saveButton.setOnAction(e -> {
            main.saveGame();
            System.out.println("Game Saved");
        });

        loadButton.setOnAction(e -> {
            main.loadGame();
            System.out.println("Game Loaded");
            pauseStage.close();
        });

        quitButton.setOnAction(e -> {
            System.out.println("Game Quit");
            System.exit(0);
        });

        pauseLayout.getChildren().addAll(resumeButton, saveButton, loadButton, quitButton);

        Scene scene = new Scene(pauseLayout, 200, 200);
        pauseStage.setScene(scene);
        engine.stop();
        pauseStage.setOnCloseRequest(event -> {
            engine.start();
            pauseStage.close();
        });
        pauseStage.showAndWait();
    }
}
