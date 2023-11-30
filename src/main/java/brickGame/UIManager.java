package brickGame;

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

    /**
     * The root pane where the UI elements are displayed.
     */
    private final Pane root;

    /**
     * The label that displays the current heart count.
     */
    private Label heartLabel;

    /**
     * The label that displays the current score.
     */
    private Label scoreLabel;


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
        Image levelImage = new Image("/images/Level.png");
        ImageView levelImageView = new ImageView(levelImage);
        levelImageView.setFitHeight(25);
        levelImageView.setFitWidth(25);
        Label levelLabel = new Label("Level: " + level, levelImageView);

        // Score label setup
        Image coinImage = new Image("/images/Coins.png");
        ImageView coinImageView = new ImageView(coinImage);
        coinImageView.setFitHeight(25);
        coinImageView.setFitWidth(25);
        scoreLabel = new Label("Coins: " + score, coinImageView);

        // Heart label setup
        Image heartImage = new Image("/images/heart.png");
        ImageView heartImageView = new ImageView(heartImage);
        heartImageView.setFitHeight(25);
        heartImageView.setFitWidth(25);
        heartLabel = new Label("Heart: " + heart, heartImageView);

        // Create an HBox container for the labels
        HBox labelsContainer = new HBox(10); // Adjust spacing as needed
        labelsContainer.setTranslateX(44);
        labelsContainer.getChildren().addAll(levelLabel, scoreLabel, heartLabel);

        // Apply the CSS style to the container
        labelsContainer.getStyleClass().add("label-container"); // Define the style in CSS
        // Add the container to the root
        root.getChildren().add(labelsContainer);
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

}
