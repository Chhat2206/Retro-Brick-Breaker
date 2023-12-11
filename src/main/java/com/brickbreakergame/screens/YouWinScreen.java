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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Represents the "You Win" screen in the Brick Breaker game.
 * This class is responsible for managing and displaying the victory screen when a player successfully completes the game.
 * It offers a congratulatory interface with options for the player to either restart the game or return to the main menu.
 */
public class YouWinScreen {

    private static Stage youWinStage;
    private static VBox youWinLayout;
    private static final AnimationManager animationManager = new AnimationManager();


    /**
     * Displays the "You Win" screen with options to restart the game or return to the main menu.
     * Sets up the user interface elements for the screen, such as the victory image, score display,
     * restart button, and main menu button. This method also initializes and positions the "You Win" screen relative to the primary stage.
     *
     * @param main          The Main instance representing the game, used for accessing game data like score.
     * @param primaryStage  The primary stage of the game, used for positioning the "You Win" screen.
     */
    public static void display(Main main, Stage primaryStage) {
        initializeYouWinStage();
        configureYouWinLayout();
        LevelManager levelManager = new LevelManager(main, primaryStage);
        addElementsToLayout(main, primaryStage, levelManager);

        Scene youWinScene = new Scene(youWinLayout, 250, 400);
        youWinScene.setFill(Color.TRANSPARENT);
        youWinScene.getStylesheets().addAll("/css/youWinScreen.css");
        youWinStage.setScene(youWinScene);

        positionYouWinMenuOverGame(primaryStage);
        animationManager.initializeBlur(primaryStage);
        animationManager.fadeInMenu(youWinLayout);

        youWinStage.showAndWait();
    }

    /**
     * Initializes the "You Win" stage with specific settings for modality and style.
     * Sets up the stage to display the "You Win" screen as a modal dialog over the game.
     */
    private static void initializeYouWinStage() {
        youWinStage = new Stage();
        youWinStage.initModality(Modality.APPLICATION_MODAL);
        youWinStage.initStyle(StageStyle.TRANSPARENT);
        youWinStage.setTitle("You Win");
    }

    /**
     * Configures the layout for the "You Win" screen.
     * Sets alignment, spacing, and styling for the VBox layout that contains the "You Win" screen's UI elements.
     */
    private static void configureYouWinLayout() {
        youWinLayout = new VBox(20);
        youWinLayout.setAlignment(Pos.CENTER);
        youWinLayout.getStyleClass().add("you-win-layout");
    }

    /**
     * Adds UI elements to the "You Win" layout, including the victory image, score label, restart button, and main menu button.
     * Configures each element and adds them to the layout to create a comprehensive "You Win" screen.
     *
     * @param main          The Main instance representing the game, used for accessing game data.
     * @param primaryStage  The primary stage of the game, used for positioning buttons and labels.
     * @param levelManager  The LevelManager instance, used for handling level-related operations like restarting the game.
     */
    private static void addElementsToLayout(Main main, Stage primaryStage, LevelManager levelManager) {
        // Load the "You Win" image
        Image youWinImage = new Image("/images/Screens/youWin.png");
        ImageView youWinImageView = new ImageView(youWinImage);
        youWinImageView.setFitWidth(150);
        youWinImageView.setFitHeight(140);

        int score = main.getScore();

        Label scoreLabel = new Label("Score: " + score);
        Button restartButton = createButton("Restart", e -> animationManager.startTransition(primaryStage, () -> {
            System.out.println("\u001B[36m" + "Game Restarted" + "\u001B[0m"); // Cyan colored text
            levelManager.restartGame();
            youWinStage.close(); // Close the game over stage
        }));

        Button returnButton = createButton("Return to Main Menu", e -> animationManager.startTransition(primaryStage, () -> {
            System.out.println("\u001B[34m" + "Returned to Main Menu" + "\u001B[0m"); // Blue
            MainMenu mainMenu = new MainMenu(primaryStage, main);
            mainMenu.display();
            youWinStage.close();
        }));

        youWinLayout.getChildren().addAll(youWinImageView, scoreLabel, restartButton, returnButton);
    }

    /**
     * Creates a default JavaFX button with default action handler.
     *
     * @param text   The text to be displayed on the button.
     * @param action The event handler to be executed when the button is clicked.
     * @return The created button.
     */
    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(e -> animationManager.coolButtonAnimation(button, () -> action.handle(e)));
        button.getStyleClass().add("refined-button-effect");
        return button;
    }


    /**
     * Positions the "You Win" menu over the game's primary stage.
     *
     * @param primaryStage The primary stage of the game.
     */
    private static void positionYouWinMenuOverGame(Stage primaryStage) {
        youWinStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.8 - youWinLayout.getPrefWidth() / 2);
        youWinStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4 - youWinLayout.getPrefHeight() / 2);
    }

}
