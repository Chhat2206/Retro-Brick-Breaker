package brickGame;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

import java.util.Objects;

public class UIManager {

    private Pane root;
    private Label heartLabel;
    private final int SCENE_WIDTH;
    private final int SCENE_HEIGHT;

    public UIManager(Pane root, int sceneWidth, int sceneHeight) {
        this.root = root;
        this.SCENE_WIDTH = sceneWidth;
        this.SCENE_HEIGHT = sceneHeight;
    }

    public void makeHeartScore(int heartCount) {
        Image heartImage = new Image("/images/heart.png");
        ImageView heartImageView = new ImageView(heartImage);
        heartImageView.setFitHeight(20);
        heartImageView.setFitWidth(20);

        heartLabel = new Label("Heart: " + heartCount, heartImageView);
        heartLabel.getStyleClass().add("heart-label-gradient");
        heartLabel.setTranslateX(SCENE_WIDTH - 90);

        root.getChildren().add(heartLabel);
    }

    public void updateHeartCount(int heartCount) {
        heartLabel.setText("Heart: " + heartCount);
    }

    public void makeBackgroundImage() {
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Background Images/backgroundImage-4.png")));
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(SCENE_WIDTH);
        backgroundView.setFitHeight(SCENE_HEIGHT);
        root.getChildren().add(backgroundView);
    }
}
