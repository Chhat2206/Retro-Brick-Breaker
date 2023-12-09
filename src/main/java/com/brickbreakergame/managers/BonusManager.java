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

/**
 * Handles the management of bonuses in a Brick Breaker game.
 * This class is responsible for creating, displaying, and managing the effects of bonuses on the game elements,
 * such as the paddle and the ball. It integrates with the main game loop for updating the game state based on bonus interactions.
 */
public class BonusManager implements Serializable {
    // Fields with basic descriptions
    public Rectangle choco;
    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;
    private Main main; // Reference to Main class

    /**
     * Constructs a BonusManager object.
     * Initializes the position of the bonus based on the specified row and column and links it to the Main class.
     *
     * @param row   The row position of the bonus.
     * @param column The column position of the bonus.
     * @param main   Reference to the main game class which controls the game's logic and rendering.
     */
    public BonusManager(int row, int column, Main main) {
        this.main = main;
        x = (column * (Block.getWidth())) + Block.getPaddingHeight() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        draw();
    }

    /**
     * Initializes the graphical representation of the bonus as a rectangle.
     * Sets the dimensions and position of the bonus, and applies an image pattern as its fill.
     */
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

    /**
     * Activates the effect of the bonus when collected.
     * Randomly selects a bonus effect to apply, such as changing the paddle or ball size, or increasing the score.
     * Additionally, plays a sound effect and shows a score animation at the bonus's location.
     */
    public void applyBonusEffect() {
        SoundManager.collectBonus();

        this.taken = true;
        AnimationManager.shrinkAndFadeOutBonus(choco);

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

    /**
     * Applies an effect that alters the size of the paddle.
     * The size change can be an increase or decrease, and the effect lasts for a random, limited duration.
     *
     * @param rand An instance of {@link Random} used for generating the size change magnitude and effect duration.
     */
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

    /**
     * Increases the player's score by a random amount as a bonus effect.
     * The range of the score increase is determined randomly.
     *
     * @param rand An instance of {@link Random} used for generating the bonus score amount.
     */
    private void applyScoreEffect(Random rand) {
        // Determine the number of bonus points
        int bonusPoints = rand.nextInt(6) + 3; // Random bonus points between 3 and 8

        // Update the score with the bonus points
        main.setScore(main.getScore() + bonusPoints);

        // Log the effect of earning additional score
        System.out.println("\u001B[33m" + "Score increased by " + bonusPoints + " points. New score: " + main.getScore() + "\u001B[0m");
    }

    /**
     * Applies an effect that modifies the size of the ball.
     * The ball size can increase or decrease, and this change persists for a randomly determined duration.
     *
     * @param rand An instance of {@link Random} used for generating the size change magnitude and effect duration.
     */
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

    /**
     * Resets any temporary size changes applied to the paddle and the ball when their respective effect durations expire.
     * This method is invoked regularly to ensure timely reversion of the game elements to their original state.
     */
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
