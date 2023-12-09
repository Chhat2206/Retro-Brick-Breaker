package com.brickbreakergame;

import javafx.scene.paint.Color;
import java.io.Serializable;

/**
 * Represents a serializable block used in a brick breaker game.
 * This class encapsulates the position, type, and color information of a block.
 */
public class BlockSerializable implements Serializable {

    public final int row;
    public final int column;
    public final int type;
    public int colorIndex;

    /**
     * Constructs a new BlockSerializable with specified position and type.
     *
     * @param row    The row position of the block in the grid.
     * @param column The column position of the block in the grid.
     * @param type   The type of the block.
     */
    public BlockSerializable(int row, int column, int type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }

    /**
     * Sets the color index for this block by matching the block's color with a given array of colors.
     * The index is set to the position of the matching color in the array.
     *
     * @param colors     An array of Color objects used in the game to represent different block colors.
     * @param blockColor The actual color of this block, which will be matched against the colors array.
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
