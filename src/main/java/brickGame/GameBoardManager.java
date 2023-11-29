package brickGame;

import javafx.scene.paint.Color;

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
            case 7:
                createLevel7Layout();
                break;
            case 8:
                createLevel8Layout();
                break;
            case 9:
                createLevel9Layout();
                break;
            case 10:
                createLevel10Layout();
                break;
            default:
                createDefaultLayout();
                break;
        }
    }

    private void createLevel1Layout() {
        // Create a space ship to take you on your journey
        int[][] spaceShip = {
                {0, 0, 1, 1, 1, 1, 1, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 1, 0},
                {1, 0, 0, 1, 0, 1, 0, 0, 1},
                {1, 0, 0, 1, 0, 1, 0, 0, 1},
                {1, 0, 1, 1, 1, 1, 1, 0, 1}
        };

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 9; i++) {
                if (spaceShip[j][i] == 1) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(i, j, mainInstance.colors[j % mainInstance.colors.length], blockType));
                }
            }
        }
    }
//        int row = 6;
//        int column = 2;
//        int blockType = determineBlockType(random.nextInt(100));
//        mainInstance.blocks.add(new Block(row, column, mainInstance.colors[column % mainInstance.colors.length], blockType));
//    }


    private void createLevel2Layout() {
        // X shape
        for (int j = 0; j < 12; j++) {
            int blockTypeLeft = determineBlockType(random.nextInt(100));
            int blockTypeRight = determineBlockType(random.nextInt(100));
            if (j < 5) {
                mainInstance.blocks.add(new Block(j, j, mainInstance.colors[j % mainInstance.colors.length], blockTypeLeft));
                mainInstance.blocks.add(new Block(j, 4 - j, mainInstance.colors[(4 - j) % mainInstance.colors.length], blockTypeRight));
            }
        }
    }


    private void createLevel3Layout() {
        // Random Dots
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if (random.nextBoolean() && random.nextBoolean()) { // Double randomness for sparser dots
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel4Layout() {
        // Checkerboard pattern
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
        // Vertical Lines
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if (i % 2 == 0) { // Every alternate column
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel6Layout() {
        // Horizontal Zigzag
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if ((j / 2 + i) % 2 == 0) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel7Layout() {
        // Verticle Zip Zag
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if ((i / 2 + j) % 2 == 0) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel8Layout() {
        // Space Ship!
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if (j % 3 == 0 || i % 2 == 0) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel9Layout() {
        // Zig Zag
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if ((j / 2 + i) % 2 == 0) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }

    private void createLevel10Layout() {
        // Diamond Shape
        int midRow = 6;
        int midColumn = 2;
        for (int j = 0; j < 13; j++) {
            for (int i = 0; i < 5; i++) {
                if (Math.abs(j - midRow) + Math.abs(i - midColumn) <= midColumn) {
                    int blockType = determineBlockType(random.nextInt(100));
                    mainInstance.blocks.add(new Block(j, i, mainInstance.colors[i % mainInstance.colors.length], blockType));
                }
            }
        }
    }


//        int row = 6;
//        int column = 2;
//        int blockType = determineBlockType(random.nextInt(100));
//        mainInstance.blocks.add(new Block(row, column, mainInstance.colors[column % mainInstance.colors.length], blockType));
//    }


    private void createDefaultLayout() { // Worst case if program fails. Best case, it will never be utilized.
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
