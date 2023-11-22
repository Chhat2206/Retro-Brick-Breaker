package brickGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import java.util.Objects;

public class UIManager {

    private Pane root;
    private Label heartLabel;
    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 700;

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

    public void makeHeartScore(int heart) {
        Image heartImage = new Image("/images/heart.png");
        ImageView heartImageView = new ImageView(heartImage);
        heartImageView.setFitHeight(20);
        heartImageView.setFitWidth(20);

        heartLabel = new Label("Heart: " + heart, heartImageView);
        heartLabel.getStyleClass().add("heart-label-gradient");
        heartLabel.setTranslateX(SCENE_WIDTH - 90);

        root.getChildren().add(heartLabel);
    }

    public Label getHeartLabel() {
        return heartLabel;
    }

}
