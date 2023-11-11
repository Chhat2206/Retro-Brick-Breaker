package brickGame;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PauseMenu {

    public static void display() {
        Stage pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.UNDECORATED);

        VBox pauseLayout = new VBox(10);
        Button resumeButton = new Button("Resume");
        Button saveButton = new Button("Save Game");
        Button quitButton = new Button("Quit");

        resumeButton.setOnAction(e -> pauseStage.close());
        saveButton.setOnAction(e -> {
            // Add your save game logic here
            System.out.println("Game Saved");
            pauseStage.close();
        });

        quitButton.setOnAction(e -> {
            // Add your quit game logic here
            System.out.println("Game Quit");
            System.exit(0);
        });

        pauseLayout.getChildren().addAll(resumeButton, saveButton, quitButton);

        Scene scene = new Scene(pauseLayout, 200, 200);
        pauseStage.setScene(scene);
        pauseStage.showAndWait();
    }
}
