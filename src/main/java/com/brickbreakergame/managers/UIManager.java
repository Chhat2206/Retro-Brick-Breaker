package com.brickbreakergame.managers;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

/**
 * The UIManager class manages the user interface elements for a game.
 * It provides methods for setting background images and displaying labels for
 * game-related information such as level, score, and hearts.
 */
public class UIManager {

    private final Pane root;
    private Label heartLabel;
    private Label scoreLabel;
    private Label levelLabel;


    /**
     * Creates a new UIManager with the specified root pane.
     *
     * @param root The root pane where UI elements will be displayed.
     */
    public UIManager(Pane root) {
        this.root = root;
    }

    /**
     * Sets the background image for the UI.
     *
     * @param imagePath The path to the background image.
     */
    public void makeBackgroundImage(String imagePath) {
        Image bgImage = new Image(imagePath);
        BackgroundImage backgroundImage = new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        this.root.setBackground(new Background(backgroundImage));
    }

    /**
     * Displays labels for the heart count, score, and level.
     *
     * @param heart The current heart count.
     * @param score The current score.
     * @param level The current level.
     */
    public void makeHeartScore(int heart, int score, int level) {
        // Level label setup
        createLevelLabel(level);

        // Score label setup
        createScoreLabel(score);

        // Heart label setup
        createHeartLabel(heart);

        // Create an HBox container for the labels and add them to the root
        arrangeLabelsOnUI();
    }

    /**
     * Gets the label displaying the heart count.
     *
     * @return The heart count label.
     */
    public Label getHeartLabel() {
        return heartLabel;
    }

    /**
     * Sets the score and updates the score label.
     *
     * @param score The new score to be displayed.
     */
    public void setScore(int score) {
        Platform.runLater(() -> {
            if (scoreLabel != null) {
                scoreLabel.setText("Coins: " + score);
            }
        });
    }

    private void createLevelLabel(int level) {
        Image levelImage = new Image("/images/level.png");
        ImageView levelImageView = new ImageView(levelImage);
        levelImageView.setFitHeight(25);
        levelImageView.setFitWidth(25);
        levelLabel = new Label("Level: " + level, levelImageView);
    }


    private void createScoreLabel(int score) {
        Image coinImage = new Image("/images/coins.png");
        ImageView coinImageView = new ImageView(coinImage);
        coinImageView.setFitHeight(25);
        coinImageView.setFitWidth(25);
        scoreLabel = new Label("Coins: " + score, coinImageView);
        root.getChildren().add(scoreLabel); // Adding the score label to the root pane
    }

    private void createHeartLabel(int heart) {
        Image heartImage = new Image("/images/heart.png");
        ImageView heartImageView = new ImageView(heartImage);
        heartImageView.setFitHeight(25);
        heartImageView.setFitWidth(25);
        heartLabel = new Label("Hearts: " + heart, heartImageView);
        root.getChildren().add(heartLabel); // Adding the heart label to the root pane
    }

    private void arrangeLabelsOnUI() {
        HBox labelsContainer = new HBox(10); // Adjust spacing as needed
        labelsContainer.setTranslateX(44);
        labelsContainer.getChildren().addAll(levelLabel, scoreLabel, heartLabel); // Include levelLabel here
        labelsContainer.getStyleClass().add("label-container"); // Define the style in CSS
        root.getChildren().add(labelsContainer); // Adding the container to the root pane
    }

    /**
     * Updates the background image of the game based on the current level.
     * @param level The current level of the game.
     */
    public void updateBackgroundImage(int level) {
        String backgroundImagePath = "/images/Background Images/backgroundImage-" + level + ".png";
        // Check if the resource exists
        if (getClass().getResource(backgroundImagePath) == null) {
            System.err.println("Background image not found for level " + level + ": " + backgroundImagePath);
            // Optionally, set a default background image
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