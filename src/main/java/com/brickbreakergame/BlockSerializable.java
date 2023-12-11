package com.brickbreakergame;

import javafx.scene.paint.Color;
import java.io.Serializable;

/**
 * Represents a block within a brick breaker game,
 * designed for serialization. It encapsulates essential attributes of a block such as
 * its position within a grid, type, and color information. The class is primarily used
 * to facilitate the saving and loading of game state by retaining key block attributes.
 */
public class BlockSerializable implements Serializable {

    public final int row;
    public final int column;
    public final int type;
    public int colorIndex;

    /**
     * Constructs a new BlockSerializable instance with specified position and type.
     * Initializes a block with its grid position and type, essential for recreating game state.
     *
     * @param row    The row position of the block in the grid.
     * @param column The column position of the block in the grid.
     * @param type   The type identifier of the block.
     */
    public BlockSerializable(int row, int column, int type) {
        this.row = row;
        this.column = column;
        this.type = type;
    }

    /**
     * Sets the color index for this block.
     * Determines the index of the block's color by comparing it against a predefined array of colors.
     * The color index is used for efficient serialization and deserialization of block colors.
     *
     * @param colors     An array of {@code Color} objects representing the possible colors of blocks.
     * @param blockColor The actual color of this block, to be matched against the provided colors array.
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
