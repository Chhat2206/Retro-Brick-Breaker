package brickGame;

import java.util.Random;

public class GameBoardManager {
    private Main mainInstance;
    private Random random; // Single Random instance

    public GameBoardManager(Main mainInstance) {
        this.mainInstance = mainInstance;
        this.random = new Random(); // Initialize the Random object here
    }

    protected void setupGameBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < mainInstance.level + 1; j++) {
                int r = random.nextInt(500); // Use the existing Random instance

                if (r % 5 == 0) {
                    continue;
                }

                int type = determineBlockType(r);

                mainInstance.blocks.add(new Block(j, i, mainInstance.colors[r % (mainInstance.colors.length)], type));
            }
        }
    }

    private int determineBlockType(int r) {
        if (r % 10 == 1) {
            return Block.BLOCK_CHOCO;
        } else if (r % 10 == 2) {
            if (!mainInstance.isExistHeartBlock) {
                mainInstance.isExistHeartBlock = true;
                return Block.BLOCK_HEART;
            }
            return Block.BLOCK_NORMAL;
        } else if (r % 10 == 3) {
            return Block.BLOCK_GOLDEN_TIME;
        }
        return Block.BLOCK_NORMAL;
    }
}
