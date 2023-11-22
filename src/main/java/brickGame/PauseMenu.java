package brickGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.animation.*;
import javafx.util.Duration;

import javafx.scene.effect.GaussianBlur;

public class PauseMenu {
    private static Stage pauseStage;
    private static VBox pauseLayout;
    private static Button soundButton;

    public static void display(Main main, GameEngine engine, Stage primaryStage) {

        initializePauseStage();
        configurePauseLayout();
        addButtonsToLayout(main, engine);

        Scene scene = new Scene(pauseLayout, 200, 400);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll("/css/defaultMenu.css", "/css/pauseMenu.css");
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

        disablePauseMenuBlur(primaryStage);
        pauseStage.showAndWait();
    }

    private static void initializePauseStage() {
        pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.TRANSPARENT);
    }

    private static void configurePauseLayout() {
        pauseLayout = new VBox(20);
        pauseLayout.setAlignment(Pos.CENTER);
        pauseLayout.getStyleClass().add("pause-menu-gradient");
    }

    private static void addButtonsToLayout(Main main, GameEngine engine) {

        Button resumeButton = createButton("Resume", e -> {
//            fadeOutMenu();
//            SoundManager.buttonClickSound();
            engine.start();
            pauseStage.close();
        });

        soundButton = createButton("Sound Settings", e -> {
            soundMenuOpen();
            SoundMenu.display();
        });


        Button saveButton = new Button("Save Game");

        saveButton.setOnAction(e -> {
            SoundManager.buttonClickSound();
            main.saveGame();
            saveButton.setText("Game Saved");
            saveButton.setStyle("-fx-background-color: #f0f0f0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0); -fx-text-fill: black;");



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
            main.loadGame(main.primaryStage);
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
        pauseLayout.getChildren().addAll(resumeButton, soundButton, saveButton, loadButton, restartButton, quitButton);
        SoundManager.pauseMenuMusic();
    }

    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    private static void positionPauseMenuOverGame(Stage primaryStage) {
        pauseStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.3 - pauseLayout.getPrefWidth() / 2);
        pauseStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4.5 - pauseLayout.getPrefHeight() / 2);
    }

    public static void initializePauseMenuBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

    public static void disablePauseMenuBlur(Stage primaryStage) {
        primaryStage.getScene().getRoot().setEffect(null);
    }

    protected static void fadeInMenu() {
        System.out.println("In fadeInMenu");
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), pauseLayout);
        fadeIn.setFromValue(0.3);
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

    protected static void soundMenuOpen() {
        System.out.println("In soundMenuOpen");
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), pauseLayout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0.3); // Fade to a lower opacity instead of completely invisible
        fadeOut.play();
        highlightSoundSystemButton();
    }

    public static void highlightSoundSystemButton() {
        soundButton.setStyle("-fx-background-color: #f0f0f0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
    }

    private static void animateButtonPress(Button button) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), button);
        st.setToX(0.9);
        st.setToY(0.9);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }

    public static double getPauseStageX() {
        return pauseStage.getX();
    }

    public static double getPauseStageY() {
        return pauseStage.getY();
    }

    public static double getPauseStageWidth() {
        return pauseLayout.getWidth();
    }

    public static void resetSoundButtonStyle() {
        soundButton.setStyle("");
    }
}