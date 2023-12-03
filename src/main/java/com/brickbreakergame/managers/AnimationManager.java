package com.brickbreakergame.managers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
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
}
