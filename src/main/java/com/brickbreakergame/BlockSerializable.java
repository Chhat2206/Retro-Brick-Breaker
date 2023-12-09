package com.brickbreakergame;

import javafx.scene.paint.Color;

import java.io.Serializable;

public class BlockSerializable implements Serializable {
    public final int row;
    public final int column;
    public final int type;
    public int colorIndex;


    public BlockSerializable(int row , int j , int type) {
        this.row = row;
        this.column = j;
        this.type = type;
    }

    /**
     * Sets the index of the color in the colors array.
     *
     * @param colors Array of Color objects used in the game.
     * @param blockColor The color of the block.
     */
    public void setColorIndex(Color[] colors, Color blockColor) {
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(blockColor)) {
                this.colorIndex = i;
                break;
            }
        }
    }
}
