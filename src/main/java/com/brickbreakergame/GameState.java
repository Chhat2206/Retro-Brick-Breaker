package com.brickbreakergame;

import java.io.*;
import java.util.ArrayList;

public class GameState {
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
     * Reads the game state from a saved file, populating the fields of this object for the loadGame function to use.
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
     * This method is now part of the GameState class.
     */
    public void saveGame(Main mainInstance) {
        new Thread(() -> {
            new File(Main.savePathDir);
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


}
