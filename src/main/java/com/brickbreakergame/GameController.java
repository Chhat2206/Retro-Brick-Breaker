package com.brickbreakergame;

import javafx.stage.Stage;

public class GameController {
    private final Main mainApp;
    private final Stage primaryStage;

    public GameController(Main mainApp, Stage primaryStage) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
    }

    /**
     * Restarts the game, resetting the game state to the initial conditions.
     */
    public void restartGame() {
        try {
            mainApp.setLevel(1);
            mainApp.setHeart(3);
            mainApp.setScore(0);
            mainApp.setBallVelocityX(1.000);
            mainApp.setDestroyedBlockCount(0);
            mainApp.resetCollideFlags();
            mainApp.setGoDownBall(true);
            mainApp.setIsGoldStatus(false);
            mainApp.setIsExistHeartBlock(false);
            mainApp.setTime(0);
            mainApp.setGoldTime(0);
            mainApp.getBlocks().clear();
            mainApp.getChocos().clear();

            mainApp.newGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
