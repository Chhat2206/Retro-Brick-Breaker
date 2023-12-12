package com.brickbreakergame.managers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * Manages the user interface elements for a Brick Breaker game.
 * This class provides methods for setting background images and displaying labels for
 * game-related information such as level, score, and hearts. It handles the dynamic
 * creation and arrangement of UI components on the game screen.
 */
public class UIManager {
    private final Pane root;
    private Label heartLabel;
    private Label scoreLabel;
    private Label levelLabel;


    /**
     * Constructs a UIManager with a specified root pane.
     * Initializes the UIManager with the pane where UI elements will be displayed.
     *
     * @param root The root pane intended for displaying UI elements.
     */
    public UIManager(Pane root) {
        this.root = root;
    }

    /**
     * Creates and displays labels for the heart count, score, and level on the UI.
     * Arranges these labels in a visually appealing manner for the player.
     *
     * @param heart The current heart count to display.
     * @param score The current score to display.
     * @param level The current level to display.
     */
    public void makeHeartScore(int heart, int score, int level) {
        createLevelLabel(level);
        createScoreLabel(score);
        createHeartLabel(heart);
        arrangeLabelsOnUI();
    }

    /**
     * Retrieves the label that displays the current heart count in the game.
     * This label is used to show the player's remaining lives or health. It is dynamically updated during
     * the game to reflect changes in the player's heart count. The method ensures easy access to the label
     * for modifications such as updating the displayed heart count.
     *
     * @return The Label object representing the heart count in the game's UI.
     */
    public Label getHeartLabel() {
        return heartLabel;
    }

    /**
     * Updates the score displayed on the UI.
     * Modifies the score label to reflect the current score.
     *
     * @param score The updated score to display.
     */
    public void setScore(int score) {
        Platform.runLater(() -> {
            if (scoreLabel != null) {
                scoreLabel.setText("Coins: " + score);
            }
        });
    }

    /**
     * Creates the level label with an accompanying image icon.
     * Sets up the level label to display the current game level alongside a graphical icon.
     * The label is configured with specific dimensions to ensure visual consistency.
     *
     * @param level The current game level to be displayed on the label.
     */
    private void createLevelLabel(int level) {
        Image levelImage = new Image("/images/level.png");
        ImageView levelImageView = new ImageView(levelImage);
        levelImageView.setFitHeight(25);
        levelImageView.setFitWidth(25);
        levelLabel = new Label("Level: " + level, levelImageView);
    }

    /**
     * Creates the score label with an accompanying coin icon.
     * Sets up the score label to display the current score alongside a graphical coin icon.
     * The label is configured with specific dimensions for the icon to ensure visual consistency.
     * This label is then added to the root pane for display in the game UI.
     *
     * @param score The current score to be displayed on the label.
     */
    private void createScoreLabel(int score) {
        Image coinImage = new Image("/images/coins.png");
        ImageView coinImageView = new ImageView(coinImage);
        coinImageView.setFitHeight(25);
        coinImageView.setFitWidth(25);
        scoreLabel = new Label("Coins: " + score, coinImageView);
        root.getChildren().add(scoreLabel);
    }

    /**
     * Creates the heart label with an accompanying heart icon.
     * Sets up the heart label to display the current heart count alongside a graphical heart icon.
     * The label is configured with specific dimensions for the icon to ensure visual consistency.
     * This label is then added to the root pane for display in the game UI.
     *
     * @param heart The current heart count to be displayed on the label.
     */
    private void createHeartLabel(int heart) {
        Image heartImage = new Image("/images/heart.png");
        ImageView heartImageView = new ImageView(heartImage);
        heartImageView.setFitHeight(25);
        heartImageView.setFitWidth(25);
        heartLabel = new Label("Hearts: " + heart, heartImageView);
        root.getChildren().add(heartLabel);
    }

    /**
     * Arranges the level, score, and heart labels within an HBox container.
     * This method positions the labels in a visually appealing manner and adds them to the game's UI.
     * The labels are spaced consistently and their styles are defined in the game's CSS.
     */
    private void arrangeLabelsOnUI() {
        HBox labelsContainer = new HBox(10);
        labelsContainer.setTranslateX(44);
        labelsContainer.getChildren().addAll(levelLabel, scoreLabel, heartLabel);
        labelsContainer.getStyleClass().add("label-container");
        root.getChildren().add(labelsContainer);
    }

    /**
     * Updates the background image based on the current level of the game.
     * Sets a default image if the specific level image is not found.
     *
     * @param level The current level for which the background image needs to be updated.
     */
    public void updateBackgroundImage(int level) {
        String backgroundImagePath = "/images/Background Images/backgroundImage-" + level + ".png";
        if (getClass().getResource(backgroundImagePath) == null) {
            System.err.println("Background image not found for level " + level + ": " + backgroundImagePath);
            backgroundImagePath = "/images/Background Images/defaultBackground.png";
        }
        Image bgImage = new Image(backgroundImagePath);
        BackgroundImage backgroundImage = new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        this.root.setBackground(new Background(backgroundImage));
    }
}