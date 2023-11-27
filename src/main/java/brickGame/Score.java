package brickGame;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Score {

    private static final Duration ANIMATION_DURATION = Duration.millis(200);

    private Label createStyledLabel(String text, double x, double y, String style) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.getStyleClass().add(style);
        return label;
    }

    public void show(final double x, final double y, int score, final Main main) {
        String sign = score >= 0 ? "+" : "";
        final Label label = createStyledLabel(sign + score, x, y, "score-label"); // Apply CSS class "score-label"

        Platform.runLater(() -> main.root.getChildren().add(label));

        ScaleTransition scaleTransition = new ScaleTransition(ANIMATION_DURATION, label);
        scaleTransition.setFromX(0.5);
        scaleTransition.setFromY(0.5);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);

        FadeTransition fadeTransition = new FadeTransition(ANIMATION_DURATION, label);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(2);
        fadeTransition.setAutoReverse(true);

        SequentialTransition sequentialTransition = new SequentialTransition(label, scaleTransition, fadeTransition);
        sequentialTransition.play();
    }

    public void showMessage(String message, final Main main) {
        SoundManager.levelUp();
        final Label label = createStyledLabel(message, 220, 340, "message-label"); // Apply CSS class "message-label"

        Platform.runLater(() -> main.root.getChildren().add(label));

        ScaleTransition scaleTransition = new ScaleTransition(ANIMATION_DURATION, label);
        scaleTransition.setToX(1.5);
        scaleTransition.setToY(1.5);

        FadeTransition fadeTransition = new FadeTransition(ANIMATION_DURATION, label);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        SequentialTransition sequentialTransition = new SequentialTransition(label, scaleTransition, fadeTransition);
        sequentialTransition.play();
    }

    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            SoundManager.gameOver();
            GameOverScreen.display(main, main.primaryStage); // Display the Game Over Screen
        });
    }
}
