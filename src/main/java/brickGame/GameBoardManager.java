package brickGame;

import java.util.Random;

public class GameBoardManager {
    private Main mainInstance;

    public GameBoardManager(Main mainInstance) {
        this.mainInstance = mainInstance;
    }

    protected void setupGameBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < mainInstance.level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!mainInstance.isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        mainInstance.isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                mainInstance.blocks.add(new Block(j, i, mainInstance.colors[r % (mainInstance.colors.length)], type));
            }
        }
    }
}
