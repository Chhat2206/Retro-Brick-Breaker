package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class UIManager {

    private final Pane root;
    private Label heartLabel;
    private Label scoreLabel;


    public UIManager(Pane root) {
        this.root = root;
    }

    public void makeBackgroundImage(String imagePath) {
        Image bgImage = new Image(imagePath);
        BackgroundImage backgroundImage = new BackgroundImage(bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        this.root.setBackground(new Background(backgroundImage));
    }

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


    public Label getHeartLabel() {
        return heartLabel;
    }

    public void setScore(int score) {
        Platform.runLater(() -> {
            if (scoreLabel != null) {
                scoreLabel.setText("Coins: " + score);
            }
        });
    }



}
