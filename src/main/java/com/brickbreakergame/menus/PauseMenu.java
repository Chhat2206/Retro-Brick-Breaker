package com.brickbreakergame.menus;
import com.brickbreakergame.managers.AnimationManager;
import com.brickbreakergame.managers.LevelManager;

import com.brickbreakergame.GameEngine;
import com.brickbreakergame.Main;
import com.brickbreakergame.managers.SoundManager;
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

/**
 * The PauseMenu class represents the in-game pause menu that provides options to resume, save, load, restart, or quit the game.
 * It also includes sound settings and blur effects for the background when the menu is displayed.
 */
public class PauseMenu {
    private static Stage pauseStage;
    private static VBox pauseLayout;
    private static Button soundButton;
    private static AnimationManager animationManager = new AnimationManager();


    /**
     * Displays the pause menu with options for resuming the game, managing sound settings, saving, loading, restarting, and quitting.
     *
     * @param main        The Main instance associated with the game.
     * @param engine      The GameEngine instance controlling the game.
     * @param primaryStage The primary stage of the game.
     */
    public static void display(Main main, GameEngine engine, Stage primaryStage) {
        initializePauseMenuBlur(primaryStage);
        initializePauseStage();
        configurePauseLayout();
        LevelManager levelManager = new LevelManager(main, primaryStage);
        addButtonsToLayout(main, engine, levelManager);

        Scene scene = new Scene(pauseLayout, 200, 400);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll("/css/defaultMenu.css", "/css/pauseMenu.css");
        pauseStage.setScene(scene);


        positionPauseMenuOverGame(primaryStage);


        engine.stop();
        animationManager.fadeInMenu(pauseLayout);

        // Brings the primary stage up when scene is minimized and reopened
        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && pauseStage.isShowing()) {
                pauseStage.toFront();
            }
        });


        pauseStage.showAndWait();
        disablePauseMenuBlur(primaryStage);
    }

    /**
     * Initializes the pause stage with appropriate settings.
     */
    private static void initializePauseStage() {
        pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.TRANSPARENT);
    }

    /**
     * Configures the layout for the pause menu, setting alignment and style.
     */
    private static void configurePauseLayout() {
        pauseLayout = new VBox(20);
        pauseLayout.setAlignment(Pos.CENTER);
        pauseLayout.getStyleClass().add("pause-menu-gradient");
    }

    /**
     * Adds buttons and elements to the pause menu layout.
     *
     * @param main   The Main instance associated with the game.
     * @param engine The GameEngine instance controlling the game.
     */
    private static void addButtonsToLayout(Main main, GameEngine engine, LevelManager levelManager) {

        Button resumeButton = createButton("Resume", e -> {
            SoundManager.buttonClickSound();
            engine.start();
            pauseStage.close();
        });

        soundButton = createButton("Sound Settings", e -> {
            SoundManager.buttonClickSound();
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
            animationManager.fadeOutMenu(pauseLayout, pauseStage);
            SoundManager.buttonClickSound();
            main.loadGame(Main.getPrimaryStage());
            System.out.println("\u001B[34m" + "Game Loaded" + "\u001B[0m"); // Blue text
            pauseStage.close();
        });

        Button restartButton = createButton("Restart Game", e -> {
            SoundManager.buttonClickSound();
            animationManager.startTransition(Main.getPrimaryStage(), () -> {
                levelManager.restartGame();
                System.out.println("\u001B[36m" + "Game Restarted" + "\u001B[0m"); // Cyan colored text
            });

            pauseStage.close();
        });

        Button quitButton = createButton("Quit", e -> {
            SoundManager.buttonClickSound();
            System.exit(0);
        });

        // Add all elements to pauseLayout
        pauseLayout.getChildren().addAll(resumeButton, soundButton, saveButton, loadButton, restartButton, quitButton);
        SoundManager.pauseMenuMusic();
    }

    /**
     * Creates a JavaFX button with a specified text and action handler.
     *
     * @param text   The text to be displayed on the button.
     * @param action The event handler to be executed when the button is clicked.
     * @return The created button.
     */
    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    /**
     * Positions the pause menu over the game's primary stage.
     *
     * @param primaryStage The primary stage of the game.
     */
    private static void positionPauseMenuOverGame(Stage primaryStage) {
        pauseStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.3 - pauseLayout.getPrefWidth() / 2);
        pauseStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4.5 - pauseLayout.getPrefHeight() / 2);
    }

    /**
     * Initializes a Gaussian blur effect on the game's primary stage, providing a visual effect for the pause menu.
     *
     * @param primaryStage The primary stage of the game.
     */
    public static void initializePauseMenuBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

    /**
     * Disables the Gaussian blur effect on the game's primary stage, restoring the original appearance when the pause stage has been exit.
     *
     * @param primaryStage The primary stage of the game.
     */
    public static void disablePauseMenuBlur(Stage primaryStage) {
        primaryStage.getScene().getRoot().setEffect(null);
    }

    /**
     * Opens the sound settings menu with a fade-out effect on the pause menu.
     */
    protected static void soundMenuOpen() {
        animationManager.fadeOutPartially(pauseLayout, 0.3); // Fade to a lower opacity
        highlightSoundSystemButton();
    }

    /**
     * Highlights the sound system button in the pause menu when the sound system is opened.
     */
    public static void highlightSoundSystemButton() {
        soundButton.setStyle("-fx-background-color: #f0f0f0; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0, 0, 0);");
    }

    /**
     * Gets the X-coordinate of the pause menu stage.
     *
     * @return The X-coordinate of the pause menu stage.
     */
    public static double getPauseStageX() {
        return pauseStage.getX();
    }

    /**
     * Gets the Y-coordinate of the pause menu stage.
     *
     * @return The Y-coordinate of the pause menu stage.
     */
    public static double getPauseStageY() {
        return pauseStage.getY();
    }

    /**
     * Gets the width of the pause menu stage layout.
     *
     * @return The width of the pause menu stage layout.
     */
    public static double getPauseStageWidth() {
        return pauseLayout.getWidth();
    }

    /**
     * Resets the style of the sound button to its original appearance.
     */
    public static void resetSoundButtonStyle() {
        soundButton.setStyle("");
    }

    /**
     * Retrieves the VBox layout used in the pause menu.
     * This layout is the container for all UI elements displayed in the pause menu.
     *
     * @return The VBox layout of the pause menu.
     */
    public static VBox getPauseLayout() {
        return pauseLayout;
    }
}