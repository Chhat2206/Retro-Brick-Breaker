package brickGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

    public void makeBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Background Images/backgroundImage-1.png")));
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(SCENE_WIDTH);
        backgroundView.setFitHeight(SCENE_HEIGHT);
        root.getChildren().add(backgroundView);
    }

    public Label getHeartLabel() {
        return heartLabel;
    }

}
