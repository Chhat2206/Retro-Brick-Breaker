package com.brickbreakergame.managers;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class manages various animations within a Brick Breaker game application.
 * It includes methods for transitioning between scenes, animating UI elements, and applying visual effects.
 */
public class AnimationManager {

    /**
     * Initiates a sliding transition animation for scene switching.
     * The current scene slides out to the left, and then the new scene slides in from the right.
     * A specified action is executed after the transition completes.
     *
     * @param stage The stage on which the transition animation is to be performed. It should not be null and should contain a valid scene.
     * @param afterTransition A {@link Runnable} that encapsulates the actions to be performed after the transition animation completes.
     *                        It is executed once the incoming scene has finished sliding into view.
     * @throws IllegalArgumentException if the stage or its scene or root node is null.
     */
    public void startTransition(Stage stage, Runnable afterTransition) {
        if (stage == null || stage.getScene() == null || stage.getScene().getRoot() == null) {
            System.err.println("Stage, Scene, or Root is null. Cannot proceed with transition.");
            return;
        }

        TranslateTransition translateOut = new TranslateTransition(Duration.seconds(.2), stage.getScene().getRoot());
        translateOut.setFromX(0);
        translateOut.setToX(-stage.getWidth());

        translateOut.setOnFinished(e -> {
            afterTransition.run();

            Scene newScene = stage.getScene();
            TranslateTransition translateIn = new TranslateTransition(Duration.seconds(.1), newScene.getRoot());
            translateIn.setFromX(stage.getWidth()); // Start from the right
            translateIn.setToX(0); // Move to the original position
            translateIn.play();
        });

        translateOut.play();
    }

    /**
     * Applies a fade-in effect to a VBox layout. The opacity of the layout gradually increases from a lower value to full opacity.
     *
     * @param layout The {@link VBox} layout to be faded in. The effect makes the layout transition from semi-transparent to fully opaque.
     */
    public void fadeInMenu(VBox layout) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), layout);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Applies a fade-out effect to a VBox layout and closes the associated stage.
     * The layout's opacity transitions from full opacity to fully transparent, after which the stage is closed.
     *
     * @param layout The {@link VBox} layout to be faded out.
     * @param stage The {@link Stage} to be closed after the fade-out effect completes.
     */
    public void fadeOutMenu(VBox layout, Stage stage) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), layout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> stage.close());
        fadeOut.play();
    }

    /**
     * Applies a partial fade-out effect to a VBox layout. The opacity of the layout transitions to a specified lower value.
     *
     * @param layout The {@link VBox} layout to be partially faded out.
     * @param toValue The target opacity value. Must be between 0.0 (fully transparent) and 1.0 (fully opaque).
     * @throws IllegalArgumentException if the toValue is not within the range of 0.0 to 1.0.
     */
    public void fadeOutPartially(VBox layout, double toValue) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), layout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(toValue);
        fadeOut.play();
    }

    /**
     * Animates a label to indicate the loss of a heart in the game.
     * The label's text color alternates between red and white to create a flashing effect.
     *
     * @param heartLabel The {@link Label} representing the heart count. Its text color is animated to indicate the loss.
     */
    public void animateHeartLoss(Label heartLabel) {
        Timeline timeline = new Timeline();

        // Define the color key frames
        KeyValue kv1 = new KeyValue(heartLabel.textFillProperty(), Color.RED);
        KeyFrame kf1 = new KeyFrame(Duration.millis(250), kv1);
        KeyValue kv2 = new KeyValue(heartLabel.textFillProperty(), Color.WHITE);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        KeyValue kv3 = new KeyValue(heartLabel.textFillProperty(), Color.RED);
        KeyFrame kf3 = new KeyFrame(Duration.millis(750), kv3);
        KeyValue kv4 = new KeyValue(heartLabel.textFillProperty(), Color.WHITE);
        KeyFrame kf4 = new KeyFrame(Duration.millis(1000), kv4);

        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.play();
    }

    /**
     * Animates a label to indicate the gain of a heart in the game.
     * The label's text color alternates between green and white to create a flashing effect.
     *
     * @param heartLabel The {@link Label} representing the heart count. Its text color is animated to indicate the gain.
     */
    public void animateHeartIncrease(Label heartLabel) {
        Timeline timeline = new Timeline();

        // Define the color key frames
        KeyValue kv1 = new KeyValue(heartLabel.textFillProperty(), Color.GREEN);
        KeyFrame kf1 = new KeyFrame(Duration.millis(250), kv1);
        KeyValue kv2 = new KeyValue(heartLabel.textFillProperty(), Color.WHITE);
        KeyFrame kf2 = new KeyFrame(Duration.millis(500), kv2);
        KeyValue kv3 = new KeyValue(heartLabel.textFillProperty(), Color.GREEN);
        KeyFrame kf3 = new KeyFrame(Duration.millis(750), kv3);
        KeyValue kv4 = new KeyValue(heartLabel.textFillProperty(), Color.WHITE);
        KeyFrame kf4 = new KeyFrame(Duration.millis(1000), kv4);

        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4);
        timeline.play();
    }


    /**
     * Creates an animation for a button that makes it shrink and fade out.
     * The button first scales down and then fades out completely. An action is executed after the animation completes.
     *
     * @param button The {@link Button} to be animated with the shrinking and fading effect.
     * @param afterAnimation A {@link Runnable} that is executed after the animation completes. It can contain any subsequent actions.
     */
    public void coolButtonAnimation(Button button, Runnable afterAnimation) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), button);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(0.8);
        scaleTransition.setFromY(1);
        scaleTransition.setToY(0.8);

        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        SequentialTransition sequentialTransition = new SequentialTransition(button, scaleTransition, fadeTransition);
        sequentialTransition.setOnFinished(event -> afterAnimation.run());

        sequentialTransition.play();
    }

    /**
     * Applies a Gaussian blur effect to the entire stage, creating a blur overlay.
     * This is typically used to create a visual distinction between different UI layers.
     *
     * @param stage The {@link Stage} on which the blur effect is to be applied.
     * @throws IllegalArgumentException if the stage or its scene is null.
     */
    public void initializeBlur(Stage stage) {
        GaussianBlur blur = new GaussianBlur(4); // Blur intensity
        if (stage != null && stage.getScene() != null) {
            stage.getScene().getRoot().setEffect(blur);
        }
    }

    /**
     * Animates a rectangle representing a bonus item by shrinking and fading it out.
     * The rectangle scales down to nothing and fades out to full transparency, then becomes invisible.
     *
     * @param rectangle The {@link Rectangle} representing a bonus item in the game. It is animated and then made invisible.
     */
    public static void shrinkAndFadeOutBonus(Rectangle rectangle) {
        // Create a scale transition to shrink the rectangle
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), rectangle);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0.0);
        scaleTransition.setToY(0.0);

        // Create a fade transition to fade out the rectangle
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), rectangle);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        // Combine both transitions using a ParallelTransition
        ParallelTransition parallelTransition = new ParallelTransition(rectangle, scaleTransition, fadeOut);
        parallelTransition.setOnFinished(event -> rectangle.setVisible(false));

        // Play the combined transition
        parallelTransition.play();
    }
}
