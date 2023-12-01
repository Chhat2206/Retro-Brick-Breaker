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
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    public int row;
    public int column;
    public boolean isDestroyed = false;
    Color color;
    public int type;

    public int x;
    public int y;

    private final int width = 80;
    private final int height = 30;
    private final int paddingTop = height * 2;
    private final int paddingHeight = 50;
    public Rectangle rect;

    // Constants for different types of block hits
    public static int NO_HIT = -1;
    public static final int HIT_RIGHT = 0;
    public static final int HIT_BOTTOM = 1;
    public static final int HIT_LEFT = 2;
    public static final int HIT_TOP = 3;

    // Constants for different types of blocks
    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_RANDOM = 100;
    public static int BLOCK_GOLDEN_TIME = 101;
    public static int BLOCK_HEART = 102;


    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    private void draw() {
        x = (column * width) + paddingHeight;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_RANDOM) {
            Image image = new Image("/images/blocks/chocoBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("/images/blocks/heartBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_GOLDEN_TIME) {
            Image image = new Image("/images/blocks/goldenBallBlock.png");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else {
            rect.setFill(color);
        }

    }


    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {

        if (isDestroyed) {
            return NO_HIT;
        }

        // Check collision with the bottom of the block
        if (xBall >= x && xBall <= x + width && yBall - ballRadius <= y + height && yBall + ballRadius > y + height) {
            SoundManager.blockHit();
            return HIT_BOTTOM;
        }

        // Check collision with the top of the block
        if (xBall >= x && xBall <= x + width && yBall + ballRadius >= y && yBall - ballRadius < y) {
            SoundManager.blockHit();
            return HIT_TOP;
        }

        // Check collision with the right side of the block
        if (yBall >= y && yBall <= y + height && xBall - ballRadius <= x + width && xBall + ballRadius > x + width) {
            SoundManager.blockHit();
            return HIT_RIGHT;
        }

        // Check collision with the left side of the block
        if (yBall >= y && yBall <= y + height && xBall + ballRadius >= x && xBall - ballRadius < x) {
            SoundManager.blockHit();
            return HIT_LEFT;
        }

        return NO_HIT;
    }

    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingHeight;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }
}
