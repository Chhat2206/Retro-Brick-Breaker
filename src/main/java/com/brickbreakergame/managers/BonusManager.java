package com.brickbreakergame.managers;

import com.brickbreakergame.Block;
import com.brickbreakergame.Main;
import com.brickbreakergame.Score;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

public class BonusManager implements Serializable {
    public Rectangle choco;

    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;

    private Main main; // Reference to Main class

    public BonusManager(int row, int column, Main main) {
        this.main = main;
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        draw();
    }

    private void draw() {
        choco = new Rectangle();
        choco.setWidth(20);
        choco.setHeight(20);
        choco.setX(x);
        choco.setY(y);

        String url;
        url = "/images/bonus.png";

        choco.setFill(new ImagePattern(new Image(url)));
    }

    public void applyBonusEffect() {
        SoundManager.collectBonus();

        this.taken = true;
        this.choco.setVisible(false);

        Random rand = new Random();
        int effect = rand.nextInt(3);

        switch (effect) {
            case 0:
                Platform.runLater(() -> applyPaddleSizeEffect(rand));
                break;
            case 1:
                Platform.runLater(() -> applyScoreEffect(rand));
                break;
            case 2:
                Platform.runLater(() -> applyBallSizeEffect(rand));
                break;
        }
        new Score().show(this.x, this.y, 3, main);
    }

    private void applyPaddleSizeEffect(Random rand) {
        // Store the original paddle width before the change
        main.setOriginalPaddleWidth(main.getPaddleWidth());

        // Determine whether to increase or decrease the paddle width
        boolean increaseWidth = rand.nextBoolean();
        int sizeChange = rand.nextInt(6) + 20; // Random size change between 20 and 25

        // If decreasing width, make sizeChange negative and ensure a minimum width
        if (!increaseWidth) {
            sizeChange = -sizeChange;
            int newWidth = Math.max(main.getPaddleWidth() + sizeChange, 20);
            main.setPaddleWidth(newWidth);
        } else {
            // Update paddle width and position
            int newWidth = main.getPaddleWidth() + sizeChange;
            main.setPaddleWidth(newWidth);
        }

        // Log the paddle size change and its duration
        main.setPaddleWidthChangeTime(System.currentTimeMillis());
        long paddleWidthChangeDuration = (rand.nextInt(6) + 5) * 1000; // Random duration between 5 and 10 seconds
        main.setPaddleWidthChangeDuration(paddleWidthChangeDuration);
        main.setPaddleWidthChanged(true);

        // Adjust paddle position to keep it within game boundaries
        double paddleMoveX = main.getPaddleMoveX() - (double) sizeChange / 2;
        paddleMoveX = Math.max(paddleMoveX, 0);
        paddleMoveX = Math.min(paddleMoveX, Main.SCENE_WIDTH - main.getPaddleWidth());
        main.setPaddleMoveX(paddleMoveX);

        // Logging for debugging
        System.out.println("\u001B[35m" + "Paddle width changed from " + main.getOriginalPaddleWidth() + " to " + main.getPaddleWidth() + " for " + (paddleWidthChangeDuration / 1000) + " seconds." + "\u001B[0m");
    }

    private void applyScoreEffect(Random rand) {
        // Determine the number of bonus points
        int bonusPoints = rand.nextInt(6) + 3; // Random bonus points between 3 and 8

        // Update the score with the bonus points
        main.setScore(main.getScore() + bonusPoints);

        // Log the effect of earning additional score
        System.out.println("\u001B[33m" + "Score increased by " + bonusPoints + " points. New score: " + main.getScore() + "\u001B[0m");
    }

    private void applyBallSizeEffect(Random rand) {
        // Store the original ball radius
        main.setOriginalBallRadius(main.getBallRadius());

        // Compute the new ball radius
        int sizeChange = rand.nextInt(11) - 5; // Random size change between -5 and +5
        double newBallRadius = main.getBallRadius() + sizeChange;

        // Ensure the ball stays within the scene boundaries
        double ballPosX = Math.max(main.getBallPosX(), newBallRadius);
        ballPosX = Math.min(ballPosX, Main.SCENE_WIDTH - newBallRadius);
        double ballPosY = Math.max(main.getBallPosY(), newBallRadius);
        ballPosY = Math.min(ballPosY, Main.SCENE_HEIGHT - newBallRadius);

        // Update ball's radius and position
        main.setBallRadius(newBallRadius);
        main.setBallPosX(ballPosX);
        main.setBallPosY(ballPosY);

        // Log the ball size change
        main.setBallSizeChangeTime(System.currentTimeMillis());
        long ballSizeChangeDuration = (rand.nextInt(6) + 5) * 1000; // Random duration between 5 and 10 seconds
        main.setBallSizeChangeDuration(ballSizeChangeDuration);
        main.setBallSizeChanged(true);

        System.out.println("\u001B[31m" + "Ball size changed from " + main.getOriginalBallRadius() + " to " + newBallRadius + " for " + (ballSizeChangeDuration / 1000) + " seconds." + "\u001B[0m");
    }

    public void resetTemporaryChanges() {
        long currentTime = System.currentTimeMillis();

        if (main.isPaddleWidthChanged() && (currentTime - main.getPaddleWidthChangeTime()) >= main.getPaddleWidthChangeDuration()) {
            main.setPaddleWidth(main.getOriginalPaddleWidth());
            main.setPaddleWidthChanged(false);
        }

        if (main.isBallSizeChanged() && (currentTime - main.getBallSizeChangeTime()) >= main.getBallSizeChangeDuration()) {
            main.setBallRadius(main.getOriginalBallRadius());
            main.setBallSizeChanged(false);
        }
    }
}

