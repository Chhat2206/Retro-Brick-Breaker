package brickGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class PauseMenu {

    private static Stage pauseStage;
    private static VBox pauseLayout;

    public static void display(Main main, GameEngine engine, Stage primaryStage) {
        initializePauseStage();
        configurePauseLayout();

        addButtonsToLayout(main, engine);

        Scene scene = new Scene(pauseLayout, 200, 300);
        scene.setFill(Color.TRANSPARENT); // Make the scene background transparent
        scene.getStylesheets().add("/css/pause-menu.css");
        pauseStage.setScene(scene);

        positionPauseMenuOverGame(primaryStage);

        engine.stop();
        pauseStage.showAndWait();
    }

    private static void initializePauseStage() {
        pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.TRANSPARENT);
    }

    private static void configurePauseLayout() {
        pauseLayout = new VBox(30);
        pauseLayout.setAlignment(Pos.CENTER);
        pauseLayout.getStyleClass().add("pause-menu-box");
    }

    private static void addButtonsToLayout(Main main, GameEngine engine) {
        Button resumeButton = createButton("Resume", e -> {
            engine.start();
            pauseStage.close();
        });

        Button saveButton = createButton("Save Game", e -> {
            main.saveGame();
            System.out.println("Game Saved");
        });

        Button loadButton = createButton("Load Game", e -> {
            main.loadGame();
            System.out.println("Game Loaded");
            pauseStage.close();
        });

        Button restartButton = createButton("Restart Game", e -> {
            main.restartGame();
            pauseStage.close();
        });

        Button quitButton = createButton("Quit", e -> {
            System.out.println("Game Quit");
            System.exit(0);
        });

        pauseLayout.getChildren().addAll(resumeButton, saveButton, loadButton, restartButton, quitButton);
        menuSound();
    }

    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    private static void positionPauseMenuOverGame(Stage primaryStage) {
        pauseStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.3 - pauseLayout.getPrefWidth() / 2);
        pauseStage.setY(primaryStage.getY() + primaryStage.getHeight() / 3.3 - pauseLayout.getPrefHeight() / 2);
    }

    private static void menuSound() {
        String musicFile = "src/main/resources/Sound Effects/pause-menu.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(0.37);
        mediaPlayer.setCycleCount(1);
        mediaPlayer.play();
    }
}