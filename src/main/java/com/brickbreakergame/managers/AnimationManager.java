package com.brickbreakergame.managers;

import javafx.animation.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AnimationManager {

    /**
     * Initiates a transition animation when switching between the main menu and game scene.
     *
     * @param stage          The stage where the transition occurs.
     * @param afterTransition A runnable to be executed after the transition completes.
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

    public void fadeInMenu(VBox layout) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), layout);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    /**
     * Fades out the provided layout with a smooth animation and closes the given stage when finished.
     *
     * @param layout The layout to be faded out.
     * @param stage  The stage to be closed after fade out.
     */
    public void fadeOutMenu(VBox layout, Stage stage) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), layout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> stage.close());
        fadeOut.play();
    }

    public void fadeOutPartially(VBox layout, double toValue) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), layout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(toValue);
        fadeOut.play();
    }

    /**
     * Animates the heart label to indicate a lost heart.
     *
     * @param heartLabel The label that displays the heart count.
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
     * Animates the heart label to indicate an increase in heart count.
     *
     * @param heartLabel The label that displays the heart count.
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
     * Cool animation for buttons involving scaling and fading.
     *
     * @param button The button to be animated.
     * @param afterAnimation A runnable to be executed after the animation completes.
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
}
