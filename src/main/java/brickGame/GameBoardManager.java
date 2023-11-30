package brickGame;

import java.util.Random;

/**
 * The GameBoardManager class manages the setup of the game board layout for different levels
 * in the Brick Game.
 */
public class GameBoardManager {
    private final Main mainInstance;
    private final Random random;

    /**
     * Constructs a new GameBoardManager with a reference to the Main instance.
     *
     * @param mainInstance The Main instance of the game.
     */
    public GameBoardManager(Main mainInstance) {
        this.mainInstance = mainInstance;
        this.random = new Random();
    }

    /**
     * Sets up the game board layout based on the current level.
     * Clears existing blocks and generates a new layout.
     */
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

    /**
     * Creates the layout for Level 1, which represents a space ship.
     * This layout consists of a space ship shape.
     */
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


    /**
     * Creates the layout for Level 2, which represents an X shape pattern.
     * This layout forms an X shape with blocks.
     */
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

    /**
     * Creates the layout for Level 3, which consists of random dots scattered on the board.
     * This layout generates random dots with some sparsity.
     */
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

    /**
     * Creates the layout for Level 4, which represents a checkerboard pattern.
     * This layout forms a checkerboard pattern with blocks.
     */
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

    /**
     * Creates the layout for Level 5, which consists of vertical lines.
     * This layout generates vertical lines with blocks.
     */
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

    /**
     * Creates the layout for Level 6, which represents a horizontal zigzag pattern.
     * This layout forms a horizontal zigzag with blocks.
     */
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

    /**
     * Creates the layout for Level 7, which represents a vertical zip zag pattern.
     * This layout forms a vertical zip zag with blocks.
     */
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

    /**
     * Creates the layout for Level 8, which represents a space ship pattern.
     * This layout generates a space ship pattern with blocks.
     */
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

    /**
     * Creates the layout for Level 9, which represents a zigzag pattern.
     * This layout forms a zigzag pattern with blocks.
     */
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

    /**
     * Creates the layout for Level 10, which represents a diamond shape pattern.
     * This layout forms a diamond shape with blocks.
     */
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

    /**
     * Creates the default game board layout when the level is not recognized or specified.
     * This layout is used as a fallback if the level number is not recognized by the game.
     */
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

    /**
     * Determines the block type based on a randomized value.
     *
     * @param randomValue The random value used to determine the block type.
     * @return The type of block to be generated.
     */
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
