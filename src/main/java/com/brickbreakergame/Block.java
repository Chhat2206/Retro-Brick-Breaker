package com.brickbreakergame;


import com.brickbreakergame.managers.SoundManager;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

/**
 * Represents a block in the brick-breaking game.
 * Blocks can have different colors and types, and they can be destroyed by the ball.
 */
public class Block implements Serializable {

    // Constants for block dimensions and padding
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99);
    private final int WIDTH = 80;
    private final int HEIGHT = 30;
    private final int PADDING_TOP = HEIGHT * 2;
    private final int PADDING_HEIGHT = 50;
    public Rectangle rect;

    // Constants for different types of block hits
    public static int NO_HIT = -1;
    public static final int HIT_RIGHT = 0;
    public static final int HIT_BOTTOM = 1;
    public static final int HIT_LEFT = 2;
    public static final int HIT_TOP = 3;

    // Constants for different types of blocks
    public static int NORMAL = 99;
    public static int RANDOM = 100;
    public static int GOLDEN_TIME = 101;
    public static int HEART = 102;

    // Block properties
    public int row;
    public int column;
    public boolean isDestroyed = false;
    Color color;
    public int type;
    public int x;
    public int y;

    // Images for different block types
    private static final Image IMAGE_RANDOM = new Image("/images/blocks/chocoBlock.png");
    private static final Image IMAGE_HEART = new Image("/images/blocks/heartBlock.png");
    private static final Image IMAGE_GOLDEN_TIME = new Image("/images/blocks/goldenBallBlock.png");

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;
        draw();
    }

    private void draw() {
        x = (column * WIDTH) + PADDING_HEIGHT;
        y = (row * HEIGHT) + PADDING_TOP;

        rect = new Rectangle();
        rect.setWidth(WIDTH);
        rect.setHeight(HEIGHT);
        rect.setX(x);
        rect.setY(y);

        if (type == RANDOM) {
            Image image = new Image("/images/blocks/chocoBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == HEART) {
            Image image = new Image("/images/blocks/heartBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == GOLDEN_TIME) {
            Image image = new Image("/images/blocks/goldenBallBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }
    }


    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) return NO_HIT;

        if (xBall >= x && xBall <= x + WIDTH) {
            if (yBall - ballRadius <= y + HEIGHT && yBall + ballRadius > y + HEIGHT) {
                SoundManager.blockHit();
                return HIT_BOTTOM;
            } else if (yBall + ballRadius >= y && yBall - ballRadius < y) {
                SoundManager.blockHit();
                return HIT_TOP;
            }
        }
        if (yBall >= y && yBall <= y + HEIGHT) {
            if (xBall - ballRadius <= x + WIDTH && xBall + ballRadius > x + WIDTH) {
                SoundManager.blockHit();
                return HIT_RIGHT;
            } else if (xBall + ballRadius >= x && xBall - ballRadius < x) {
                SoundManager.blockHit();
                return HIT_LEFT;
            }
        }
        return NO_HIT;
    }

    public static int getPaddingTop() {
        return block.PADDING_TOP;
    }

    public static int getPaddingHeight() {
        return block.PADDING_HEIGHT;
    }

    public static int getHeight() {
        return block.HEIGHT;
    }

    public static int getWidth() {
        return block.WIDTH;
    }
}
