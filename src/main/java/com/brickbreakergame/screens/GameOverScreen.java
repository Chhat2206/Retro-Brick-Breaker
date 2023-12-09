package com.brickbreakergame.screens;

import com.brickbreakergame.Main;
import com.brickbreakergame.managers.AnimationManager;
import com.brickbreakergame.managers.LevelManager;
import com.brickbreakergame.menus.MainMenu;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Represents the game over screen in the Brick Breaker game.
 * This class is dedicated to managing and displaying the game over interface when the player loses the game.
 * It provides a user interface that offers options to restart the game or return to the main menu, allowing
 * players to seamlessly transition from the end of one game to the beginning of another or exit to the main menu.
 */
public class GameOverScreen {

    private static Stage gameOverStage;
    private static VBox gameOverLayout;
    private static final AnimationManager animationManager = new AnimationManager();

    /**
     * Displays the game over screen, offering options to restart the game or return to the main menu.
     * This method sets up and positions the game over screen, complete with UI elements like the score display,
     * restart button, and main menu button. The game over screen is presented as a modal dialog over the primary game window.
     *
     * @param main          The Main instance representing the game, used to access game data like the score.
     * @param primaryStage  The primary stage of the game, used as a reference for positioning the game over screen.
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
        animationManager.initializeBlur(primaryStage);
        animationManager.fadeInMenu(gameOverLayout);

        gameOverStage.showAndWait();
    }

    /**
     * Initializes the game over stage with appropriate settings for modality, style, and title.
     * This stage is configured to display the game over screen as a modal dialog, focusing player attention on end-game options.
     */
    private static void initializeGameOverStage() {
        gameOverStage = new Stage();
        gameOverStage.initModality(Modality.APPLICATION_MODAL);
        gameOverStage.initStyle(StageStyle.TRANSPARENT);
        gameOverStage.setTitle("Game Over");
    }

    /**
     * Configures the layout for the game over screen, setting the alignment, spacing, and styling.
     * This layout is used to organize and present the game over screen's UI elements effectively.
     */
    private static void configureGameOverLayout() {
        gameOverLayout = new VBox(20);
        gameOverLayout.setAlignment(Pos.CENTER);
        gameOverLayout.getStyleClass().add("game-over-layout");
    }

    /**
     * Adds essential elements to the game over screen layout, such as the game over image, score label, restart button, and return button.
     * Each element is configured for its role in the game over screen, contributing to a comprehensive and informative end-game interface.
     *
     * @param main          The Main instance of the game, providing access to game data.
     * @param primaryStage  The primary stage of the game, used for contextual positioning and actions.
     * @param levelManager  The LevelManager instance, used for restarting the game and other level-related actions.
     */
    private static void addElementsToLayout(Main main, Stage primaryStage, LevelManager levelManager) {
        // Load the game over image
        Image gameOverImage = new Image("/images/Screens/youLose.png");
        ImageView gameOverImageView = new ImageView(gameOverImage);
        gameOverImageView.setFitWidth(150);
        gameOverImageView.setFitHeight(140);

        int score = main.getScore();

        Label scoreLabel = new Label("Score: " + score);
        Button restartButton = createButton("Restart", e -> animationManager.startTransition(primaryStage, () -> {
            System.out.println("\u001B[36m" + "Game Restarted" + "\u001B[0m"); // Cyan colored text
            levelManager.restartGame();
            gameOverStage.close(); // Close the game over stage
        }));

        Button returnButton = createButton("Return to Main Menu", e -> animationManager.startTransition(primaryStage, () -> {
            System.out.println("\u001B[34m" + "Returned to Main Menu" + "\u001B[0m"); // Blue
            MainMenu mainMenu = new MainMenu(primaryStage, main);
            mainMenu.display();
            gameOverStage.close();
        }));

        gameOverLayout.getChildren().addAll(gameOverImageView, scoreLabel, restartButton, returnButton);
    }

    /**
     * Creates a styled button with specified text and an associated action.
     * Applies custom styles and animations to enhance the user interaction with the button.
     *
     * @param text   The text to be displayed on the button.
     * @param action The event handler to be executed when the button is clicked.
     * @return A Button object styled and configured with the specified text and action.
     */
    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(e -> animationManager.coolButtonAnimation(button, () -> action.handle(e)));
        button.getStyleClass().add("refined-button-effect"); // Apply the button style
        return button;
    }

    /**
     * Positions the game over menu over the primary game window, aligning it based on the primary stage's dimensions.
     * This positioning ensures the game over screen is prominently and appropriately displayed relative to the game window.
     *
     * @param primaryStage The primary stage of the game.
     */
    private static void positionGameOverMenuOverGame(Stage primaryStage) {
        gameOverStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.8 - gameOverLayout.getPrefWidth() / 2);
        gameOverStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4 - gameOverLayout.getPrefHeight() / 2);
    }
}
