package com.brickbreakergame;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * The GameController class is responsible for managing the game state in the BrickBreaker game.
 * It includes methods for saving, loading, and updating the game's state based on user interaction
 * and game events. This class is pivotal in managing the flow of the game, handling game data,
 * and ensuring the persistence of the game state across sessions.
 */
public class GameController {
    public boolean isExistHeartBlock;
    public boolean isGoldStatus;
    public boolean goDownBall;
    public boolean goRightBall;
    public boolean collideToBreak;
    public boolean collideToBreakAndMoveToRight;
    public boolean collideToRightWall;
    public boolean collideToLeftWall;
    public boolean collideToRightBlock;
    public boolean collideToBottomBlock;
    public boolean collideToLeftBlock;
    public boolean collideToTopBlock;
    public int level;
    public int score;
    public int heart;
    public int destroyedBlockCount;
    public double xBall;
    public double yBall;
    public double xBreak;
    public double yBreak;
    public double centerBreakX;
    public long time;
    public long goldTime;
    public double vX;
    public ArrayList<BlockSerializable> blocks = new ArrayList<>();


    /**
     * The {@code GameController} class is responsible for managing the game state in the BrickBreaker game.
     * It includes methods for saving, loading, and updating the game's state based on user interaction
     * and game events. This class is pivotal in managing the flow of the game, handling game data,
     * and ensuring the persistence of the game state across sessions.
     */
    public void read() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(Main.SAVE_PATH));
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = inputStream.readInt();
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStatus = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            collideToBreak = inputStream.readBoolean();
            collideToBreakAndMoveToRight = inputStream.readBoolean();
            collideToRightWall = inputStream.readBoolean();
            collideToLeftWall = inputStream.readBoolean();
            collideToRightBlock = inputStream.readBoolean();
            collideToBottomBlock = inputStream.readBoolean();
            collideToLeftBlock = inputStream.readBoolean();
            collideToTopBlock = inputStream.readBoolean();


            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Saves the current game state to a file for later retrieval.
     * This method serializes the current state of the game, including all relevant data
     * like the ball position, block states, and game scores, ensuring that the game can be resumed
     * at a later time from the same state.
     *
     * @param mainInstance The instance of the Main class representing the current game.
     */
    public void saveGame(Main mainInstance) {
        new Thread(() -> {
            new File(Main.SAVE_PATH_DIR);
            File file = new File(Main.SAVE_PATH);
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeInt(mainInstance.getLevel());
                outputStream.writeInt(mainInstance.getScore());
                outputStream.writeInt(mainInstance.getHeart());
                outputStream.writeInt(mainInstance.getDestroyedBlockCount());
                outputStream.writeDouble(mainInstance.getBallPosX());
                outputStream.writeDouble(mainInstance.getBallPosY());
                outputStream.writeDouble(mainInstance.getPaddleMoveX());
                outputStream.writeDouble(mainInstance.getPaddleMoveY());
                outputStream.writeDouble(mainInstance.getCenterBreakX());
                outputStream.writeLong(mainInstance.getTime());
                outputStream.writeLong(mainInstance.getGoldTime());
                outputStream.writeDouble(mainInstance.getBallVelocityX());
                outputStream.writeBoolean(mainInstance.isExistHeartBlock());
                outputStream.writeBoolean(mainInstance.isGoldStatus());
                outputStream.writeBoolean(mainInstance.isGoDownBall());
                outputStream.writeBoolean(mainInstance.isGoRightBall());
                outputStream.writeBoolean(mainInstance.isCollideToBreak());
                outputStream.writeBoolean(mainInstance.isCollideToBreakAndMoveToRight());
                outputStream.writeBoolean(mainInstance.isCollideToRightWall());
                outputStream.writeBoolean(mainInstance.isCollideToLeftWall());
                outputStream.writeBoolean(mainInstance.isCollideToRightBlock());
                outputStream.writeBoolean(mainInstance.isCollideToBottomBlock());
                outputStream.writeBoolean(mainInstance.isCollideToLeftBlock());
                outputStream.writeBoolean(mainInstance.isCollideToTopBlock());

                // Serialize blocks
                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : mainInstance.getBlocks()) {
                    if (block.isDestroyed) continue;
                    BlockSerializable serializedBlock = new BlockSerializable(block.row, block.column, block.type);
                    serializedBlock.setColorIndex(mainInstance.getColors(), block.color);
                    blockSerializable.add(serializedBlock);
                }
                outputStream.writeObject(blockSerializable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Loads a previously saved game state from the save file.
     * This method is used to resume a game from a saved state, reconstructing the game environment
     * and game objects based on the saved data. It ensures continuity in gameplay across sessions.
     *
     * @param mainInstance The instance of the Main class where the loaded game will be displayed.
     * @param primaryStage The primary stage where the game's scenes are displayed.
     */
    public void loadGame(Main mainInstance, Stage primaryStage) {
        File saveFile = new File(Main.SAVE_PATH);
        if (!saveFile.exists()) {
            System.out.println("Save file not found. Loading a New Game!");
            mainInstance.newGame(primaryStage);
            return;
        }

        read();

        mainInstance.setExistHeartBlock(isExistHeartBlock);
        mainInstance.setIsGoldStatus(isGoldStatus);
        mainInstance.setGoDownBall(goDownBall);
        mainInstance.setGoRightBall(goRightBall);
        mainInstance.setCollideToBreak(collideToBreak);
        mainInstance.setCollideToBreakAndMoveToRight(collideToBreakAndMoveToRight);
        mainInstance.setCollideToRightWall(collideToRightWall);
        mainInstance.setCollideToLeftWall(collideToLeftWall);
        mainInstance.setCollideToRightBlock(collideToRightBlock);
        mainInstance.setCollideToBottomBlock(collideToBottomBlock);
        mainInstance.setCollideToLeftBlock(collideToLeftBlock);
        mainInstance.setCollideToTopBlock(collideToTopBlock);
        mainInstance.setLevel(level);
        mainInstance.setScore(score);
        mainInstance.setHeart(heart);
        mainInstance.setDestroyedBlockCount(destroyedBlockCount);
        mainInstance.setBallPosX(xBall);
        mainInstance.setBallPosY(yBall);
        mainInstance.setPaddleMoveX(xBreak);
        mainInstance.setPaddleMoveY((float) yBreak);
        mainInstance.setCenterBreakX(centerBreakX);
        mainInstance.setTime(time);
        mainInstance.setGoldTime(goldTime);
        mainInstance.setBallVelocityX(vX);

        mainInstance.setDestroyedBlockCount(0);
        mainInstance.getBlocks().clear();
        mainInstance.getChocos().clear();

        if (mainInstance.root == null) {
            mainInstance.root = new Pane();
        }

        for (BlockSerializable ser : blocks) {
            Color blockColor = mainInstance.getColors()[ser.colorIndex];
            Block block = new Block(ser.row, ser.column, blockColor, ser.type);
            mainInstance.getBlocks().add(block);
            Platform.runLater(() -> {
                if (block.rect != null) {
                    mainInstance.root.getChildren().add(block.rect);
                }
            });
        }

        mainInstance.setPaddleMoveX(Main.SCENE_WIDTH / 2.0 - mainInstance.getPaddleWidth() / 2.0);
        mainInstance.initializeGameObjects();

        if (mainInstance.getBall() != null) {
            mainInstance.getBall().setCenterX(xBall);
            mainInstance.getBall().setCenterY(yBall);
        } else {
            System.err.println("Error: Ball object is not initialized.");
        }

        mainInstance.loadFromSave = true;
        mainInstance.newGame(primaryStage);
    }
}
