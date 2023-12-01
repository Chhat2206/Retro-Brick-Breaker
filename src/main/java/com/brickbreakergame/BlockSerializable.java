package com.brickbreakergame;

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
}
