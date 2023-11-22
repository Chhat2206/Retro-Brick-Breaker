//package brickGame;
//
//import javafx.animation.FadeTransition;
//import javafx.animation.ScaleTransition;
//import javafx.application.Platform;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.util.Duration;
//
//public class Score {
//    public void show(final double x, final double y, int score, final Main main) {
//        String sign = score >= 0 ? "+" : "";
//        final Label label = new Label(sign + score);
//        label.setTranslateX(x);
//        label.setTranslateY(y);
//
//        Platform.runLater(() -> main.root.getChildren().add(label));
//
//        ScaleTransition st = new ScaleTransition(Duration.millis(315), label);
//        st.setToX(20);
//        st.setToY(20);
//
//        FadeTransition ft = new FadeTransition(Duration.millis(315), label);
//        ft.setFromValue(1.0);
//        ft.setToValue(0.0);
//
//        st.play();
//        ft.play();
//    }
//
//    public void showMessage(String message, final Main main) {
//        SoundManager.levelUp();
//        final Label label = new Label(message);
//        label.setTranslateX(220);
//        label.setTranslateY(340);
//
//        Platform.runLater(() -> main.root.getChildren().add(label));
//
//        ScaleTransition st = new ScaleTransition(Duration.millis(315), label);
//        st.setToX(10);
//        st.setToY(10);
//
//        FadeTransition ft = new FadeTransition(Duration.millis(315), label);
//        ft.setFromValue(1.0);
//        ft.setToValue(0.0);
//
//        st.play();
//        ft.play();
//    }

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

    private Label createStyledLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setStyle("-fx-font-size: 20px; -fx-text-fill: rgba(255,0,153,0.8); -fx-font-weight: bold;");
        return label;
    }

    public void show(final double x, final double y, int score, final Main main) {
        String sign = score >= 0 ? "+" : "";
        final Label label = createStyledLabel(sign + score, x, y);

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
        final Label label = createStyledLabel(message, 220, 340);

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
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> main.restartGame());

            main.root.getChildren().addAll(label, restart);
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(() -> {
            SoundManager.winSound();
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);
            main.root.getChildren().addAll(label);
        });
    }
}
