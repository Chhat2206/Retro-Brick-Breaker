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
 * The YouWinScreen class is responsible for displaying the "You Win" screen when the player wins the game.
 * It provides methods to configure and display this screen.
 */
public class YouWinScreen {

    private static Stage youWinStage;
    private static VBox youWinLayout;
    private static final AnimationManager animationManager = new AnimationManager();


    /**
     * Displays the "You Win" screen, allowing the player to restart the game or return to the main menu.
     *
     * @param main        The Main instance associated with the game.
     * @param primaryStage The primary stage of the game.
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
     * Initializes the "You Win" stage with a transparent background.
     */
    private static void initializeYouWinStage() {
        youWinStage = new Stage();
        youWinStage.initModality(Modality.APPLICATION_MODAL);
        youWinStage.initStyle(StageStyle.TRANSPARENT);
        youWinStage.setTitle("You Win");
    }

    /**
     * Configures the layout for the "You Win" screen, setting alignment and style.
     */
    private static void configureYouWinLayout() {
        youWinLayout = new VBox(20);
        youWinLayout.setAlignment(Pos.CENTER);
        youWinLayout.getStyleClass().add("you-win-layout");
    }

    /**
     * Adds UI elements (such as images, labels, and buttons) to the "You Win" layout.
     *
     * @param main        The Main instance associated with the game.
     * @param primaryStage The primary stage of the game.
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
