package brickGame;

import java.util.Random;

public class GameBoardManager {
    private Main mainInstance;
    private Random random;

    public GameBoardManager(Main mainInstance) {
        this.mainInstance = mainInstance;
        this.random = new Random();
    }

    protected void setupGameBoard() {
        mainInstance.blocks.clear();
        switch (mainInstance.level) {
            case 1:
                createLevel1Layout();
                break;
            case 2:
                createLevel2Layout();
                break;
            case 3:
                createLevel3Layout();
                break;
            case 4:
                createLevel4Layout();
                break;
            case 5:
                createLevel5Layout();
                break;
            case 6:
                createLevel6Layout();
                break;
            default:
                createDefaultLayout();
                break;
        }
    }

    private void createLevel1Layout() {
        // Simple row of blocks, with a chance of special blocks
        for (int i = 0; i < 5; i++) {
            int blockType = determineBlockType(random.nextInt(100));
            mainInstance.blocks.add(new Block(0, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
        }
    }

    private void createLevel2Layout() {
        // Pyramid Shape with random block types, up to 5 blocks horizontally and 12 vertically
        for (int j = 0; j < 12; j++) {
            for (int i = Math.max(0, j - 7); i < Math.min(5, 12 - j); i++) {
                int blockType = determineBlockType(random.nextInt(100));
                mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
            }
        }
    }

    private void createLevel3Layout() {
        // X shape with random block types, up to 5 blocks horizontally and 12 vertically
        for (int j = 0; j < 12; j++) {
            int blockTypeLeft = determineBlockType(random.nextInt(100));
            int blockTypeRight = determineBlockType(random.nextInt(100));
            if (j < 5) {
                mainInstance.blocks.add(new Block(j, j, mainInstance.colors[j % mainInstance.colors.length], blockTypeLeft));
                mainInstance.blocks.add(new Block(j, 4 - j, mainInstance.colors[(4 - j) % mainInstance.colors.length], blockTypeRight));
            }
        }
    }

    private void createLevel4Layout() {
        // Checkerboard pattern with random block types, up to 5 blocks horizontally and 12 vertically
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i < 5; i++) {
                if ((j + i) % 2 == 0) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel5Layout() {
        // Circle shape with random block types, up to 5 blocks horizontally and 12 vertically
        int centerX = 2;
        int centerY = 6; // Adjusted for 12 vertical blocks
        int radius = 3; // Adjusted radius
        for (int j = 0; j < 12; j++) {
            for (int i = 0; i < 5; i++) {
                if (Math.pow(i - centerX, 2) + Math.pow(j - centerY, 2) <= Math.pow(radius, 2)) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel6Layout() {
        // New layout for level 6 - Inverted Pyramid, up to 5 blocks horizontally and 12 vertically
        for (int j = 0; j < 12; j++) {
            for (int i = Math.max(0, j - 7); i < Math.min(5, 12 - j); i++) {
                int blockType = determineBlockType(random.nextInt(100));
                mainInstance.blocks.add(new Block(11 - j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
            }
        }
    }


    private void createDefaultLayout() {
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

    private int determineBlockType(int randomValue) {
        if (randomValue < 10) {
            return Block.BLOCK_RANDOM;
        } else if (randomValue < 15 && !mainInstance.isExistHeartBlock) {
            mainInstance.isExistHeartBlock = true;
            return Block.BLOCK_HEART;
        } else if (randomValue >= 15 && randomValue < 20) {
            return Block.BLOCK_GOLDEN_TIME;
        }
        return Block.BLOCK_NORMAL;
    }
}
