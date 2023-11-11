package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class Block implements Serializable {
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    public int row;
    public int column;


    public boolean isDestroyed = false;

    private Color color;
    public int type;

    public int x;
    public int y;

    private final int width = 100;
    private final int height = 30;
    private final int paddingTop = height * 2;
    private final int paddingHeight = 50;
    public Rectangle rect;


    public static int NO_HIT = -1;
    public static int HIT_RIGHT = 0;
    public static int HIT_BOTTOM = 1;
    public static int HIT_LEFT = 2;
    public static int HIT_TOP = 3;

    public static int BLOCK_NORMAL = 99;
    public static int BLOCK_CHOCO = 100;
    public static int BLOCK_STAR = 101;
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

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
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

        // Check if the bottom edge of the ball hits the top edge of the block
        if (xBall >= x && xBall <= x + width && yBall + ballRadius >= y && yBall + ballRadius <= y + height) {
            return HIT_BOTTOM;
        }

        // Check if the top edge of the ball hits the bottom edge of the block
        if (xBall >= x && xBall <= x + width && yBall <= y + height && yBall >= y) {
            return HIT_TOP;
        }

        // Check if the right edge of the ball hits the left edge of the block
        if (yBall >= y && yBall <= y + height && xBall + ballRadius >= x && xBall + ballRadius <= x + width) {
            return HIT_RIGHT;
        }

        // Check if the left edge of the ball hits the right edge of the block
        if (yBall >= y && yBall <= y + height && xBall <= x + width && xBall >= x) {
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
