package com.brickbreakergame.managers;

import com.brickbreakergame.Main;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * Manages the levels and progression within the Brick Breaker game.
 * This class is responsible for handling the game's level transitions, including restarting the game
 * and advancing to the next level. It interacts closely with the Main class to reset or update
 * the game state accordingly.
 */
public class LevelManager {
    private final Main mainApp;
    private final Stage primaryStage;

    /**
     * Constructs a LevelManager with references to the main application and primary stage.
     *
     * @param mainApp       The main application instance, which manages the game's state and logic.
     * @param primaryStage  The primary stage of the application, used for rendering the game's UI.
     */
    public LevelManager(Main mainApp, Stage primaryStage) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
    }

    /**
     * Restarts the game, resetting the game state to the initial conditions.
     * This includes resetting the level, score, heart count, ball velocity, block count,
     * paddle position, and other game-related flags and parameters.
     * It also initializes a new game on the primary stage.
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
                mainApp.setPaddleMoveX(220.0);
                mainApp.setPaddleMoveY(683.0f);
                mainApp.setPaddleWidth(90);
                mainApp.setBallRadius(10);

                mainApp.newGame(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * Handles the progression to the next level in the game.
     * This method updates the game state for the new level, including resetting ball velocity,
     * collide flags, game status flags, and clearing existing blocks and bonuses.
     * It stops the current game engine, resets the block count, and initializes a new game instance.
     * All changes and the new game initialization are performed on the application's main thread.
     */
    public void nextLevel() {
        Platform.runLater(() -> {
            try {
                mainApp.setBallVelocityX(1.000);
                mainApp.resetCollideFlags();
                mainApp.setGoDownBall(true);
                mainApp.setIsGoldStatus(false);
                mainApp.setIsExistHeartBlock(false);
                mainApp.setTime(0);
                mainApp.setGoldTime(0);
                mainApp.getEngine().stop();
                mainApp.getBlocks().clear();
                mainApp.getChocos().clear();
                mainApp.setDestroyedBlockCount(0);
                mainApp.newGame(primaryStage);
                mainApp.setPaddleMoveX(220.0);
                mainApp.setPaddleMoveY(683.0f);
                mainApp.setPaddleWidth(90);
                mainApp.setBallRadius(10);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

