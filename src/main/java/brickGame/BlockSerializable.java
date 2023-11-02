package brickGame;

import java.io.Serializable;

public class BlockSerializable implements Serializable {
    public final int row;
    public final int j;
    // It is called column in block
    public final int type;

    public BlockSerializable(int row , int j , int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
