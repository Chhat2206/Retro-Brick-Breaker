package com.brickbreakergame.screens;
import com.brickbreakergame.managers.AnimationManager;
import com.brickbreakergame.managers.LevelManager;

import com.brickbreakergame.Main;
import com.brickbreakergame.menus.MainMenu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The `GameOverScreen` class represents the game over screen displayed when the game ends.
 * It provides methods for displaying the screen, handling user interactions, and transitioning
 * back to the main menu or restarting the game.
 */
public class GameOverScreen {

    private static Stage gameOverStage;
    private static VBox gameOverLayout;
    private static AnimationManager animationManager = new AnimationManager();

    /**
     * Displays the game over screen.
     *
     * @param main        The `Main` instance representing the game.
     * @param primaryStage The primary stage of the game.
     */
    public static void display(Main main, Stage primaryStage) {
        initializeGameOverStage();
        configureGameOverLayout();
        LevelManager levelManager = new LevelManager(main, primaryStage);
        addElementsToLayout(main, primaryStage, levelManager);

        Scene gameOverScene = new Scene(gameOverLayout, 250, 400);
        gameOverScene.setFill(Color.TRANSPARENT); // Make the scene transparent
        gameOverScene.getStylesheets().addAll("/css/gameOverScreen.css");
        gameOverStage.setScene(gameOverScene);

        positionGameOverMenuOverGame(primaryStage);
        initializeBlur(primaryStage);

        animationManager.fadeInMenu(gameOverLayout);

        gameOverStage.showAndWait();
    }

    /**
     * Initializes the game over stage.
     */
    private static void initializeGameOverStage() {
        gameOverStage = new Stage();
        gameOverStage.initModality(Modality.APPLICATION_MODAL);
        gameOverStage.initStyle(StageStyle.TRANSPARENT);
        gameOverStage.setTitle("Game Over");
    }

    /**
     * Configures the layout for the game over screen.
     */
    private static void configureGameOverLayout() {
        gameOverLayout = new VBox(20);
        gameOverLayout.setAlignment(Pos.CENTER);
        gameOverLayout.getStyleClass().add("game-over-layout");
    }

    /**
     * Adds a logo, restart button, score, and main menu button.
     *
     * @param main        The `Main` instance representing the game.
     * @param primaryStage The primary stage of the game.
     */
    private static void addElementsToLayout(Main main, Stage primaryStage, LevelManager levelManager) {
        // Load the game over image
        Image gameOverImage = new Image("/images/Screens/youLose.png");
        ImageView gameOverImageView = new ImageView(gameOverImage);
        gameOverImageView.setFitWidth(150);
        gameOverImageView.setFitHeight(140);

        int score = main.getScore();

        Label scoreLabel = new Label("Score: " + score);
        Button restartButton = createButton("Restart", e -> {
            animationManager.fadeOutMenu(gameOverLayout, gameOverStage);
            levelManager.restartGame();
        });

        Button returnButton = createButton("Return to Main Menu", e -> {
            animationManager.fadeOutMenu(gameOverLayout, gameOverStage);
            MainMenu mainMenu = new MainMenu(primaryStage, main);
            mainMenu.display();
        });

        gameOverLayout.getChildren().addAll(gameOverImageView, scoreLabel, restartButton, returnButton);
    }

    /**
     * Creates a styled button with the specified text and action.
     *
     * @param text   The text to display on the button.
     * @param action The action to be executed when the button is clicked.
     * @return The created button.
     */
    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.getStyleClass().add("refined-button-effect"); // Apply the button style
        return button;
    }

    /**
     * Positions the game over menu over the primary game window.
     *
     * @param primaryStage The primary stage of the game.
     */
    private static void positionGameOverMenuOverGame(Stage primaryStage) {
        gameOverStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.8 - gameOverLayout.getPrefWidth() / 2);
        gameOverStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4 - gameOverLayout.getPrefHeight() / 2);
    }

    /**
     * Initializes a Gaussian blur effect on the primary game window to create a visual overlay effect.
     *
     * @param primaryStage The primary stage of the game.
     */
    private static void initializeBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

}
