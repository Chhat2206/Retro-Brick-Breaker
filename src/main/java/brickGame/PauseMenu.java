package brickGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;

import javafx.animation.*;
import javafx.util.Duration;

import javafx.scene.effect.GaussianBlur;

import java.io.File;

public class PauseMenu {
    private static boolean isMuted = false;
    private static Stage pauseStage;
    private static VBox pauseLayout;

    public static void display(Main main, GameEngine engine, Stage primaryStage) {
        initializePauseStage();
        configurePauseLayout();

        addButtonsToLayout(main, engine);

        Scene scene = new Scene(pauseLayout, 200, 600);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("/css/pauseMenu.css");
        pauseStage.setScene(scene);

        positionPauseMenuOverGame(primaryStage);
        initializePauseMenuBlur(primaryStage);

        engine.stop();
        fadeInMenu();

        // Brings the primary stage up when scene is minimized and reopened
        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && pauseStage.isShowing()) {
                pauseStage.toFront();
            }
        });

        pauseStage.showAndWait();
        disablePauseMenuBlur(primaryStage);
    }

    private static void initializePauseStage() {
        pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.TRANSPARENT);
    }

    private static void configurePauseLayout() {
        pauseLayout = new VBox(20);
        pauseLayout.setAlignment(Pos.CENTER);
        pauseLayout.getStyleClass().add("pause-menu-box");
    }

    private static void addButtonsToLayout(Main main, GameEngine engine) {
        HBox volumeControls = new HBox(10);
        volumeControls.setAlignment(Pos.CENTER);
        Slider volumeSlider = createVolumeSlider();
        Button muteButton = createMuteButton();
        volumeControls.getChildren().addAll(volumeSlider, muteButton);

        Button resumeButton = createButton("Resume", e -> {
            SoundManager.buttonClickSound();
            engine.start();
            pauseStage.close();
        });


        Button saveButton = new Button("Save Game");
        // Initializes the saveButton the action is set

        saveButton.setOnAction(e -> {
            SoundManager.buttonClickSound();
            main.saveGame();
            saveButton.setText("Game Saved");
            saveButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");


            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> {
                SoundManager.buttonClickSound();
                saveButton.setText("Save Game");
                saveButton.setStyle(""); // Reset to original style
            });
            pause.play();
        });

        Button loadButton = createButton("Load Game", e -> {
//            fadeOutMenu();
//            SoundManager.buttonClickSound();
            main.loadGame();
            System.out.println("Game Loaded");
            pauseStage.close();
        });

        Button restartButton = createButton("Restart Game", e -> {
            SoundManager.buttonClickSound();
            main.restartGame();
            pauseStage.close();
        });

        Button quitButton = createButton("Quit", e -> {
            SoundManager.buttonClickSound();
            System.out.println("Game Quit");
            System.exit(0);
        });

        // Add all elements to pauseLayout
        pauseLayout.getChildren().addAll(resumeButton, saveButton, loadButton, restartButton, quitButton, volumeControls);
        SoundManager.pauseMenuSound();
    }

    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    private static void positionPauseMenuOverGame(Stage primaryStage) {
        pauseStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.3 - pauseLayout.getPrefWidth() / 2);
        pauseStage.setY(primaryStage.getY() + primaryStage.getHeight() / 10 - pauseLayout.getPrefHeight() / 2);
    }

    public static void initializePauseMenuBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

    public static void disablePauseMenuBlur(Stage primaryStage) {
        primaryStage.getScene().getRoot().setEffect(null);
    }

    private static void fadeInMenu() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), pauseLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static void fadeOutMenu() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), pauseLayout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> pauseStage.close());
        fadeOut.play();
    }

    private static void animateButtonPress(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    private static Slider createVolumeSlider() {
        Slider volumeSlider = new Slider(0, 1, SoundManager.getVolume());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setVolume(newValue.doubleValue());
        });
        return volumeSlider;
    }

    private static Button createMuteButton() {
        ImageView muteIcon = new ImageView(new Image("/images/muteMusic.png"));
        muteIcon.setFitWidth(60);
        muteIcon.setFitHeight(60);
        muteIcon.setPreserveRatio(true);
        Button muteButton = new Button("", muteIcon);
        muteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        muteButton.setOnAction(e -> SoundManager.toggleMuteBackgroundMusic());
        muteButton.setOnAction(e -> {
            // Toggle the mute state
            isMuted = !isMuted;
            SoundManager.toggleMuteBackgroundMusic();
            SoundManager.muteSoundPauseMenu();

            if (isMuted) {
                muteIcon.setImage(new Image("/images/playMusic.png"));
            } else {
                muteIcon.setImage(new Image("/images/muteMusic.png"));
            }
        });
        return muteButton;
    }
}