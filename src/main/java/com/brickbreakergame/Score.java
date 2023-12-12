package com.brickbreakergame;
import com.brickbreakergame.screens.YouWinScreen;

import com.brickbreakergame.managers.SoundManager;
import com.brickbreakergame.screens.GameOverScreen;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * The Score class is dedicated to the presentation and animation of score-related elements within the game.
 * This class offers functionalities for displaying score updates, in-game notifications, and executing associated animations,
 * thereby enhancing the user experience with visual feedback on game events.
 */
public class Score {

    private static final Duration ANIMATION_DURATION = Duration.millis(200);
    private final Main main;

    /**
     * Constructs and styles a label for displaying text within the game's graphical interface.
     * This method creates a label with specified content and positions it at provided coordinates.
     *
     * @param text The textual content to be displayed on the label.
     * @param x    The X-coordinate of the label's desired position.
     * @param y    The Y-coordinate of the label's desired position.
     * @return A configured and styled {@code Label} object.
     */
    private Label createStyledLabel(String text, double x, double y) {
        Label label = new Label(text);
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.getStyleClass().add("score-label");
        return label;
    }

    /**
     * Constructor for the Score class.
     *
     * @param main The Main instance of the game.
     */
    public Score(Main main) {
        this.main = main;
    }

    /**
     * Displays an animated score update at the specified screen coordinates.
     * This method animates a score increment or decrement at the point of a game event, such as block destruction.
     *
     * @param x     The X-coordinate for the animation's screen position.
     * @param y     The Y-coordinate for the animation's screen position.
     * @param score The score value to be displayed in the animation.
     * @param main  The Main instance of the game, used for graphical context.
     */
    public void show(final double x, final double y, int score, final Main main) {
        String sign = score >= 0 ? "+" : "";
        final Label label = createStyledLabel(sign + score, x, y); // Apply CSS class "score-label"

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
     * Checks the current game level and performs actions based on the level.
     * It may display messages, handle level progression, or trigger win/lose screens.
     */
    public void checkLevels() {
        if (main.getLevel() > 1) {
            showMessage();
        }
    }

    /**
     * Displays the game over screen.
     * This method is invoked upon the completion of the game, transitioning to the game over interface.
     *
     * @param main The Main instance of the game, used to access and modify the game's stage and scenes.
     */
    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            SoundManager.gameOver();
            GameOverScreen.display(main, main.getPrimaryStage()); // Display the Game Over Screen
        });
    }

    /**
     * Displays the "You Win" screen when the player successfully completes the game.
     * This method stops the game engine and then initiates the display of the win screen.
     * It's designed to be invoked when the player meets the win condition of the game,
     * such as completing all levels or achieving a specific objective.
     *
     * @param main The Main instance of the game, which contains the game's state and
     *             controls, including the game engine. It's used to access the game's
     *             primary stage for displaying the win screen.
     */
    public void showYouWinScreen(final Main main) {
        Platform.runLater(() -> {
            main.getEngine().stop();
            SoundManager.winSound();
            YouWinScreen.display(main, main.getPrimaryStage());
        });
    }

    /**
     * Presents a brief flash message effect on the entire game screen.
     * This method is typically used for dramatic effect in response to significant game events.
     */
    public void showMessage() {
        Rectangle flash = new Rectangle(0, 0, main.getPrimaryStage().getWidth(), main.getPrimaryStage().getHeight());
        flash.setFill(Color.WHITE); // Set color of the flash
        Platform.runLater(() -> main.getRoot().getChildren().add(flash));

        // Fade out effect for the flash
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), flash);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setOnFinished(event -> main.getRoot().getChildren().remove(flash));
        fadeTransition.play();
    }

}
