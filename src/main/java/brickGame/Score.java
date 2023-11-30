package brickGame;

import brickGame.screens.GameOverScreen;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The Score class is responsible for displaying score-related information and animations in the game.
 * It provides methods to show score updates and in game messages.
 */
public class Score {

    private static final Duration ANIMATION_DURATION = Duration.millis(200);

    /**
     * Creates a styled label for displaying text with specified coordinates and CSS style.
     *
     * @param text  The text to be displayed on the label.
     * @param x     The X-coordinate of the label's position.
     * @param y     The Y-coordinate of the label's position.
     * @param style The CSS style to be applied to the label.
     * @return A styled label with the specified text, position, and style.
     */
    private Label createStyledLabel(String text, double x, double y, String style) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.getStyleClass().add(style);
        return label;
    }

    /**
     * Shows a score update animation at the block-broken position on the screen.
     *
     * @param x     The X-coordinate where the score update animation will be displayed.
     * @param y     The Y-coordinate where the score update animation will be displayed.
     * @param score The score value to be displayed.
     * @param main  The Main instance associated with the game.
     */
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

    /**
     * Shows a flash message effect on the screen.
     *
     * @param main The Main instance associated with the game.
     */
    public void showMessage(final Main main) {
        Rectangle flash = new Rectangle(0, 0, main.primaryStage.getWidth(), main.primaryStage.getHeight());
        flash.setFill(Color.WHITE); // Set color of the flash
        Platform.runLater(() -> main.root.getChildren().add(flash));

        // Fade out effect for the flash
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), flash);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> main.root.getChildren().remove(flash));
        fadeTransition.play();
    }

    /**
     * Shows the game over screen.
     *
     * @param main The Main instance associated with the game.
     */
    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            SoundManager.gameOver();
            GameOverScreen.display(main, main.primaryStage); // Display the Game Over Screen
        });
    }
}
