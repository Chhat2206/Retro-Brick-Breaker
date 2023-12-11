package com.brickbreakergame.menus;
import com.brickbreakergame.GameController;
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
 * Represents the in-game pause menu for the Brick Breaker game.
 * This class is responsible for managing the pause menu interface, providing players with various options
 * during gameplay interruption. The menu offers functionalities such as resuming the game, saving progress,
 * loading a saved game, restarting the current game, or quitting the game entirely. Additionally, the pause menu
 * allows players to access and modify sound settings.
 */
public class PauseMenu {
    private static Stage pauseStage;
    private static VBox pauseLayout;
    private static Button soundButton;
    private static final AnimationManager animationManager = new AnimationManager();


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
     * Initializes the pause stage with specific modality and style settings.
     * Sets up the pause menu stage as a modal window with a transparent style.
     */
    private static void initializePauseStage() {
        pauseStage = new Stage();
        pauseStage.initModality(Modality.APPLICATION_MODAL);
        pauseStage.initStyle(StageStyle.TRANSPARENT);
    }

    /**
     * Configures the layout for the pause menu.
     * Sets alignment and styling for the VBox layout that contains the pause menu's UI elements.
     */
    private static void configurePauseLayout() {
        pauseLayout = new VBox(20);
        pauseLayout.setAlignment(Pos.CENTER);
        pauseLayout.getStyleClass().add("pause-menu-gradient");
    }

    /**
     * Adds buttons and interactive elements to the pause menu's layout.
     * Configures the buttons for various functionalities like resume, save, load, restart, and quit.
     *
     * @param main         The Main instance associated with the game.
     * @param engine       The GameEngine instance controlling the game.
     * @param levelManager The LevelManager instance for handling level-related operations.
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
            main.getGameState().saveGame(main);
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
            SoundManager.buttonClickSound();
            animationManager.fadeOutMenu(pauseLayout, pauseStage);
            GameController gameController = new GameController();
            gameController.loadGame(main, Main.getPrimaryStage());
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

        pauseLayout.getChildren().addAll(resumeButton, soundButton, saveButton, loadButton, restartButton, quitButton);
        SoundManager.pauseMenuMusic();
    }

    /**
     * Creates a button with a specified label and action.
     * Sets up a JavaFX button with the provided text and an event handler for click actions.
     *
     * @param text   The text to display on the button.
     * @param action The event handler to be executed when the button is clicked.
     * @return A Button object configured with the specified text and action.
     */
    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    /**
     * Positions the pause menu stage over the game's primary stage.
     * Aligns the pause menu in relation to the primary stage's position and size.
     *
     * @param primaryStage The primary stage of the game.
     */
    private static void positionPauseMenuOverGame(Stage primaryStage) {
        pauseStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.3 - pauseLayout.getPrefWidth() / 2);
        pauseStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4.5 - pauseLayout.getPrefHeight() / 2);
    }

    /**
     * Initializes a Gaussian blur effect on the primary stage's scene.
     * Applies a visual blur effect to the game window when the pause menu is displayed.
     *
     * @param primaryStage The primary stage of the game.
     */
    public static void initializePauseMenuBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

    /**
     * Disables the Gaussian blur effect on the primary stage's scene.
     * Removes the blur effect from the game window when the pause menu is closed.
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