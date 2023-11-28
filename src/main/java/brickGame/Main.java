package brickGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.*;


import java.io.*;


public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    // Constants
    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
    private static int paddleWidth = 90;
    private static final int PADDLE_HEIGHT = 14;
    private static final int ballRadius = 10;
    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 700;

    // Game State Variables
    protected int level = 3;
    protected int score = 0;
    private int heart = 3;
    private int destroyedBlockCount = 0;

    // Paddle Variables
    private static final int PADDLE_SPEED = 3;
    private double paddleMoveX = 250.0;
    private double paddleMoveY = 680.0f;
    private final int halfPaddleWidth = paddleWidth / 2;
    private double centerBreakX;
    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;
    private AnimationTimer paddleMoveTimer;

    // Ball Variables
    private Circle ball;
    private double ballPosX;
    private double ballPosY;
    // Example renamed from vX

    // Game Mechanics Variables
    private boolean loadFromSave = false;
    private volatile long time = 0;
    private volatile long goldTime = 0;

    // UI Components
    Pane root;
    protected Stage primaryStage;

    // Game Engine, GameBoardManager and Media
    private GameEngine engine;

    // Ball Movement and Collision Flags
    private boolean goDownBall = true;
    private boolean goRightBall = true;
    private boolean collideToBreak = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall = false;
    private boolean collideToLeftWall = false;
    private boolean collideToRightBlock = false;
    private boolean collideToBottomBlock = false;
    private boolean collideToLeftBlock = false;
    private boolean collideToTopBlock = false;
    private double ballVelocityX = 1.000;
    double ballVelocityY = 1.000;
    private static final double MAX_VELOCITY_X = 3.0; // Maximum horizontal velocity of the ball
    private static final double MAX_VELOCITY = 4.0;   // Maximum overall velocity of the ball
    private static final double SPIN_EFFECT = 0.5;    // Effect of spin on ball's trajectory

    // RandomBlock Temporary Variables
    long paddleWidthChangeTime;
    private volatile boolean paddleWidthChanged;
    long ballSizeChangeTime;
    private volatile boolean ballSizeChanged;
    int originalPaddleWidth;
    int originalBALL_RADIUS;
    long paddleWidthChangeDuration = 0;
    long ballSizeChangeDuration = 0;

    // Game Objects
    private Rectangle rect;
    protected final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> chocos = new ArrayList<>();
    protected final Color[] colors = new Color[]{
            Color.rgb(0, 0, 128),          // Dark Blue
            Color.rgb(255, 255, 255),      // White
            Color.rgb(255, 223, 186),      // Peach
            Color.rgb(135, 206, 250),      // Light Sky Blue
            Color.rgb(255, 165, 0),        // Orange
            Color.rgb(147, 112, 219),      // Medium Purple
            Color.rgb(0, 128, 128),        // Teal
            Color.rgb(255, 69, 0),         // Red-Orange
            Color.rgb(128, 0, 128),        // Purple
            Color.rgb(176, 196, 222)       // Light Steel Blue
    };



    // File Paths for Saving and Loading
    public static final String SAVE_PATH = "./save/save.mdds";
    public static final String savePathDir = "./save/";


    // Other Instance Variables
    private boolean isGoldStatus = false;
    protected boolean isExistHeartBlock = false;

    protected GameEngine getGameEngine() {
        return engine;
    }

    private UIManager uiManager;

    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage, this);
        mainMenu.display();
    }

    private void initializeGameObjects() {
        initializeBall();
        createPaddle();
    }

    private void setUpGameBoard() {
        if (!loadFromSave) {
            GameBoardManager gameBoardManager = new GameBoardManager(this);
            gameBoardManager.setupGameBoard();
        }
        primaryStage.setResizable(false);
    }

    private void createUIComponents() {
        root = new Pane();
        this.uiManager = new UIManager(root);
        String backgroundImagePath = "/images/Background Images/backgroundImage-" + level + ".png";
        // Restart game has issues when utilizing this code, fix in the future
        this.uiManager.makeBackgroundImage(backgroundImagePath);
        this.uiManager.makeHeartScore(heart, score, level);
        root.getChildren().addAll(rect, ball);
    }

    private void setUpBlocks() {
        if (!loadFromSave) {
            for (Block block : blocks) {
                Platform.runLater(() -> {
                    if (block != null && block.rect != null) {
                        root.getChildren().add(block.rect);
                    }
                });
            }
        }
    }

    private void setUpScene() {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
        scene.getStylesheets().add("/css/main.css");
        scene.getStylesheets().add("/css/score.css");
        primaryStage.setTitle("The Incredible Block Breaker Game");
        primaryStage.getIcons().add(new Image("/images/favicon.png"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startGameEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
    }

    public void newGame(Stage primaryStage) {
        this.primaryStage = primaryStage;

        if (!loadFromSave) {
            level++;
            if (level > 1) {
                new Score().showMessage(this);
            }

            //11
            if (level == 11) {
                YouWinScreen.display(this, primaryStage);
                return;
            }
        }

        initializeGameObjects();
        setUpGameBoard();
        createUIComponents();
        setUpBlocks();
        setUpScene();
        startGameEngine();
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            handleKeyPressed(event);
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            handleKeyReleased(event);
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case A:
                leftKeyPressed = true;
                movePaddleX(LEFT);
                break;
            case D:
                rightKeyPressed = true;
                movePaddleX(RIGHT);
                break;
            case ESCAPE:
                PauseMenu.display(this, getGameEngine(), primaryStage);
                event.consume();
                break;
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case A:
                leftKeyPressed = false;
                break;
            case D:
                rightKeyPressed = false;
                break;
        }
    }

    private void movePaddleX(final int direction) {
        if (paddleMoveTimer != null) {
            paddleMoveTimer.stop();
        }
        paddleMoveTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if ((direction == LEFT && !leftKeyPressed) || (direction == RIGHT && !rightKeyPressed)) {
                    this.stop();
                    return;
                }
                if (paddleMoveX <= 0 && direction == LEFT || paddleMoveX >= (SCENE_WIDTH - paddleWidth) && direction == RIGHT) {
                    return;
                }
                paddleMoveX += (direction == RIGHT ? PADDLE_SPEED : -PADDLE_SPEED);
                centerBreakX = paddleMoveX + halfPaddleWidth;
                rect.setX(paddleMoveX);
            }
        };
        paddleMoveTimer.start();
    }


    private void initializeBall() {
        ballPosX = SCENE_WIDTH / 2.0;
        ballPosY = SCENE_HEIGHT * 0.7 ;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("/images/ball.png")));

    }

    private void createPaddle() {
        rect = new Rectangle();
        rect.setWidth(paddleWidth);
        rect.setHeight(PADDLE_HEIGHT);
        rect.setX(paddleMoveX);
        rect.setY(paddleMoveY);
        ImagePattern pattern = new ImagePattern(new Image("/images/paddle.png"));
        rect.setFill(pattern);
    }

    private void resetCollideFlags() {
        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;
        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }
    private void setPhysicsToBall() {
        updateBallPosition();
        checkCollisionWithWalls();

        if (checkContinuousCollisionWithPaddle()) {
            handlePaddleCollision(); // Fixes paddle randomly bugging out
        } else {
            checkCollisionWithPaddle(); // Fallback to original collision check
        }

        checkCollisionWithBlocks();
        handleBallDirection();
    }

    private void checkCollisionWithWalls() {
        if (ballPosY <= ballRadius || ballPosY + ballRadius >= SCENE_HEIGHT) {
            handleWallCollision();
        }

        if (ballPosX + ballRadius >= SCENE_WIDTH || ballPosX - ballRadius <= 0) {
            handleSideWallCollision();
        }
    }

    private void handleWallCollision() {
        if (ballPosY <= ballRadius) {
            bounceOffTopWall();
        } else {
            bounceOffBottomWall();
        }
    }

    private void bounceOffTopWall() {
        SoundManager.paddleBounceSound();
        resetCollideFlags();
        goDownBall = true;
    }

    private void bounceOffBottomWall() {
        resetCollideFlags();
        goDownBall = false;
        if (!isGoldStatus) {
            heart--;
            SoundManager.ballHitFloor();
            new Score().show((double) SCENE_WIDTH / 2, (double) SCENE_HEIGHT / 2, -1, this);

            if (heart <= 0) {
                new Score().showGameOver(this);
                engine.stop();
            }
        }
    }


    private void handleSideWallCollision() {
        SoundManager.paddleBounceSound();
        resetCollideFlags();
        if (ballPosX + ballRadius >= SCENE_WIDTH) {
            collideToRightWall = true;
        } else {
            collideToLeftWall = true;
        }
    }

    private void checkCollisionWithPaddle() {
        if (ballPosY + ballRadius >= paddleMoveY &&
                ballPosX + ballRadius >= paddleMoveX &&
                ballPosX - ballRadius <= paddleMoveX + paddleWidth) {
            handlePaddleCollision();
        }
    }

    private boolean checkContinuousCollisionWithPaddle() {
        double nextX = ballPosX + (goRightBall ? ballVelocityX : -ballVelocityX);
        double nextY = ballPosY + (goDownBall ? ballVelocityY : -ballVelocityY);

        // Check for collision in the path between current position and next position, to fix the bug where it phases through the paddle
        if (nextY + ballRadius >= paddleMoveY && nextY - ballRadius <= paddleMoveY + PADDLE_HEIGHT) {
            if (nextX + ballRadius >= paddleMoveX && nextX - ballRadius <= paddleMoveX + paddleWidth) {
                return true;
            }
        }
        return false;
    }

    private void handlePaddleCollision() {
        resetCollideFlags();
        calculateBallVelocity();
        goDownBall = false;
        collideToBreakAndMoveToRight = ballPosX - centerBreakX > 0;
        SoundManager.paddleBounceSound();
    }

    private void calculateBallVelocity() {
        double relation = (ballPosX - centerBreakX) / ((double) paddleWidth / 2);
        ballVelocityX = Math.abs(relation) * MAX_VELOCITY_X; // MAX_VELOCITY_X can be a constant defining max horizontal velocity
        ballVelocityY = Math.sqrt(Math.pow(MAX_VELOCITY, 2) - Math.pow(ballVelocityX, 2)); // MAX_VELOCITY is the maximum speed of the ball

        // Add spin effect based on paddle movement
        if (leftKeyPressed) {
            ballVelocityX -= SPIN_EFFECT; // SPIN_EFFECT is a constant defining how much spin affects the ball
        } else if (rightKeyPressed) {
            ballVelocityX += SPIN_EFFECT;
        }
    }

    private void checkCollisionWithBlocks() {
        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = false;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }
    }

    private void handleBallDirection() {
        // Logic to handle the direction of the ball after collision
        if (collideToBreak) {
            goRightBall = collideToBreakAndMoveToRight;
        }
        if (collideToRightWall) {
            goRightBall = false;
        }
        if (collideToLeftWall) {
            goRightBall = true;
        }
    }

    private void updateBallPosition() {
        ballPosX += goRightBall ? ballVelocityX : -ballVelocityX;
        ballPosY += goDownBall ? ballVelocityY : -ballVelocityY;
    }

    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            nextLevel();
        }
    }

    protected void saveGame() {
        new Thread(() -> {
            new File(savePathDir).mkdirs();
            File file = new File(SAVE_PATH);
            ObjectOutputStream outputStream = null;
            try {
                outputStream = new ObjectOutputStream(new FileOutputStream(file));

                outputStream.writeInt(level);
                outputStream.writeInt(score);
                outputStream.writeInt(heart);
                outputStream.writeInt(destroyedBlockCount);
                outputStream.writeDouble(ballPosX);
                outputStream.writeDouble(ballPosY);
                outputStream.writeDouble(paddleMoveX);
                outputStream.writeDouble(paddleMoveY);
                outputStream.writeDouble(centerBreakX);
                outputStream.writeLong(time);
                outputStream.writeLong(goldTime);
                outputStream.writeDouble(ballVelocityX);
                outputStream.writeBoolean(isExistHeartBlock);
                outputStream.writeBoolean(isGoldStatus);
                outputStream.writeBoolean(goDownBall);
                outputStream.writeBoolean(goRightBall);
                outputStream.writeBoolean(collideToBreak);
                outputStream.writeBoolean(collideToBreakAndMoveToRight);
                outputStream.writeBoolean(collideToRightWall);
                outputStream.writeBoolean(collideToLeftWall);
                outputStream.writeBoolean(collideToRightBlock);
                outputStream.writeBoolean(collideToBottomBlock);
                outputStream.writeBoolean(collideToLeftBlock);
                outputStream.writeBoolean(collideToTopBlock);

                ArrayList<BlockSerializable> blockSerializable = new ArrayList<>();
                for (Block block : blocks) {
                    if (block.isDestroyed) {
                        continue;
                    }
                    blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                    BlockSerializable serializedBlock = new BlockSerializable(block.row, block.column, block.type);
                    serializedBlock.colorIndex = Arrays.asList(colors).indexOf(block.color); // Save color index
                    blockSerializable.add(serializedBlock);
                }
                outputStream.writeObject(blockSerializable);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    assert outputStream != null;
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    protected void loadGame(Stage primaryStage) {
        this.primaryStage = primaryStage;

        File saveFile = new File(SAVE_PATH);
        if (!saveFile.exists()) {
            System.out.println("Save file not found. Loading a New Game!");
            newGame(primaryStage);
            return;
        }

        GameState gameState = new GameState();
        gameState.read();

        isExistHeartBlock = gameState.isExistHeartBlock;
        isGoldStatus = gameState.isGoldStatus;
        goDownBall = gameState.goDownBall;
        goRightBall = gameState.goRightBall;
        collideToBreak = gameState.collideToBreak;
        collideToBreakAndMoveToRight = gameState.collideToBreakAndMoveToRight;
        collideToRightWall = gameState.collideToRightWall;
        collideToLeftWall = gameState.collideToLeftWall;
        collideToRightBlock = gameState.collideToRightBlock;
        collideToBottomBlock = gameState.collideToBottomBlock;
        collideToLeftBlock = gameState.collideToLeftBlock;
        collideToTopBlock = gameState.collideToTopBlock;
        level = gameState.level;
        score = gameState.score;
        heart = gameState.heart;
        destroyedBlockCount = gameState.destroyedBlockCount;
        ballPosX = gameState.xBall;
        ballPosY = gameState.yBall;
        paddleMoveX = gameState.xBreak;
        paddleMoveY = gameState.yBreak;
        centerBreakX = gameState.centerBreakX;
        time = gameState.time;
        goldTime = gameState.goldTime;
        ballVelocityX = gameState.vX;

        blocks.clear();
        chocos.clear();

        if (this.root == null) {
            this.root = new Pane();
        }

        for (BlockSerializable ser : gameState.blocks) {
            Color blockColor = colors[ser.colorIndex];
            Block block = new Block(ser.row, ser.column, blockColor, ser.type);
            blocks.add(block);
            // Add block's rectangle to the UI if needed
            Platform.runLater(() -> {
                if (block.rect != null) {
                    root.getChildren().add(block.rect);
                }
            });
        }

        loadFromSave = true;
        newGame(primaryStage);
    }

    private void nextLevel() {
        Platform.runLater(() -> {
            try {
                ballVelocityX = 1.000;
                resetCollideFlags();
                goDownBall = true;
                isGoldStatus = false;
                isExistHeartBlock = false;
                time = 0;
                goldTime = 0;
                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;
                newGame(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void restartGame() {

        try {
            level = 0;
            heart = 3;
            score = 0;
            ballVelocityX = 1.000;
            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;
            isGoldStatus = false;
            isExistHeartBlock = false;
            time = 0;
            goldTime = 0;
            blocks.clear();
            chocos.clear();

            newGame(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        resetTemporaryChanges();
        updateUIComponents();
        updateGameObjects();
        handleBlockCollisions();
        handleBonusCollection();
    }

    private void updateUIComponents() {
        Platform.runLater(() -> {
            uiManager.setScore(score);
            Label heartLabelFromUIManager = uiManager.getHeartLabel();
            heartLabelFromUIManager.setText("Hearts: " + heart);
        });
    }

    private synchronized void updateGameObjects() {
        Platform.runLater(() -> {
            rect.setX(paddleMoveX);
            rect.setY(paddleMoveY);
            ball.setCenterX(ballPosX);
            ball.setCenterY(ballPosY);
            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
        });
    }

    private void handleBlockCollisions() {
        for (final Block block : blocks) {
            if (!block.isDestroyed) {
                int hitCode = block.checkHitToBlock(ballPosX, ballPosY, ballRadius);
                if (hitCode != Block.NO_HIT) {
                    handleBlockHit(block, hitCode);
                    repositionBallAfterCollision(block, hitCode);
                    // Break after handling one collision to prevent multiple collisions in a frame
                    break;
                }
            }
        }
    }

    private void repositionBallAfterCollision(Block block, int hitCode) {
        switch (hitCode) {
            case Block.HIT_BOTTOM:
                // Reposition ball below the block
                ballPosY = block.y + block.getHeight() + ballRadius;
                break;
            case Block.HIT_TOP:
                // Reposition ball above the block
                ballPosY = block.y - ballRadius;
                break;
            case Block.HIT_LEFT:
                // Reposition ball to the left of the block
                ballPosX = block.x - ballRadius;
                break;
            case Block.HIT_RIGHT:
                // Reposition ball to the right of the block
                ballPosX = block.x + block.getWidth() + ballRadius;
                break;
        }
    }


    private void handleBlockHit(Block block, int hitCode) {
        score += 1;
        new Score().show(block.x, block.y, 1, this);

        block.rect.setVisible(false);
        block.isDestroyed = true;
        destroyedBlockCount++;
        resetCollideFlags();

        checkBlockTypeActions(block);

        switch (hitCode) {
            case Block.HIT_RIGHT:
                collideToRightBlock = true;
                break;
            case Block.HIT_BOTTOM:
                collideToBottomBlock = true;
                break;
            case Block.HIT_LEFT:
                collideToLeftBlock = true;
                break;
            case Block.HIT_TOP:
                collideToTopBlock = true;
                break;
        }
    }

    private void checkBlockTypeActions(Block block) {
        if (block.type == Block.BLOCK_RANDOM) {
            handleRandomBlock(block);
        } else if (block.type == Block.BLOCK_GOLDEN_TIME) {
            handleGoldenTimeBlock();
        } else if (block.type == Block.BLOCK_HEART) {
            heart++;
        }
    }

    private void handleRandomBlock(Block block) {
        final Bonus choco = new Bonus(block.row, block.column);
        choco.timeCreated = time;
        Platform.runLater(() -> root.getChildren().add(choco.choco));
        chocos.add(choco);
    }

    private void handleGoldenTimeBlock() {
        goldTime = time;
        ball.setFill(new ImagePattern(new Image("/images/goldBall.png")));
        SoundManager.goldBallPowerUp();
        isGoldStatus = true;
    }


    private void handleBonusCollection() {
        Iterator<Bonus> iterator = chocos.iterator();
        while (iterator.hasNext()) {
            Bonus choco = iterator.next();

            if (choco.y > SCENE_HEIGHT || choco.taken) {
                iterator.remove();
                continue;
            }

            if (choco.y >= paddleMoveY && choco.y <= paddleMoveY + PADDLE_HEIGHT && choco.x >= paddleMoveX && choco.x <= paddleMoveX + paddleWidth) {
                applyBonusEffect(choco);
                iterator.remove();
            } else {
                choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
            }
        }
    }

    private void applyBonusEffect(Bonus choco) {
        SoundManager.collectBonus();
        choco.taken = true;
        choco.choco.setVisible(false);

        Random rand = new Random();
        int effect = rand.nextInt(3); // Assuming 3 different types of bonuses

        switch (effect) {
            case 0:
                applyPaddleSizeEffect(rand);
                break;
            case 1:
                applyScoreEffect(rand);
                break;
            case 2:
                applyBallSizeEffect(rand);
                break;
        }
        new Score().show(choco.x, choco.y, 3, this);
    }

    private void applyPaddleSizeEffect(Random rand) {
        originalPaddleWidth = paddleWidth;
        // Randomly decide whether to increase or decrease the paddle width
        boolean increaseWidth = rand.nextBoolean();
        int sizeChange = rand.nextInt(6) + 20; // Random size change between 20 and 25
        if (increaseWidth) {
            paddleWidth += sizeChange; // Increase width
        } else {
            paddleWidth -= sizeChange; // Decrease width
            paddleWidth = Math.max(paddleWidth, 20); // Ensure a minimum width
        }

        // Adjust paddle position to maintain its center alignment
        paddleMoveX -= sizeChange / 2;
        // Ensure the paddle stays within the game boundaries
        paddleMoveX = Math.max(paddleMoveX, 0);
        paddleMoveX = Math.min(paddleMoveX, SCENE_WIDTH - paddleWidth);

        rect.setWidth(paddleWidth);
        rect.setX(paddleMoveX); // Update the paddle's position

        paddleWidthChangeTime = System.currentTimeMillis();
        paddleWidthChangeDuration = (rand.nextInt(6) + 5) * 1000; // Duration for the size change effect
        paddleWidthChanged = true;
        System.out.println("Paddle width changed from " + originalPaddleWidth + " to " + paddleWidth + " for " + (paddleWidthChangeDuration / 1000) + " seconds.");
    }



    private synchronized void applyBallSizeEffect(Random rand) {
        originalBALL_RADIUS = ballRadius;
        ballPosX = Math.max(ballPosX, ballRadius); // Prevent moving off left edge
        ballPosX = Math.min(ballPosX, SCENE_WIDTH - ballRadius); // Prevent moving off right edge
        ballPosY = Math.max(ballPosY, ballRadius); // Prevent moving off top edge
        ballPosY = Math.min(ballPosY, SCENE_HEIGHT - ballRadius); // Prevent moving off bottom edge
        double newBallRadius = ballRadius + rand.nextInt(11) - 5;
        // Recompute the ball's center position to account for the change in radius
        ballPosX += (newBallRadius - ballRadius);
        ballPosY += (newBallRadius - ballRadius);
        ball.setRadius(newBallRadius);
        ballSizeChangeTime = System.currentTimeMillis();
        ballSizeChangeDuration = (rand.nextInt(6) + 5) * 1000;
        ballSizeChanged = true;
        System.out.println("Ball size changed from " + originalBALL_RADIUS + " to " + newBallRadius + " for " + (ballSizeChangeDuration / 1000) + " seconds.");
    }

    private void applyScoreEffect(Random rand) {
        int bonusPoints = rand.nextInt(6) + 3; // Random bonus points between 3 to 8
        score += bonusPoints;
        System.out.println("Bonus " + bonusPoints + " points!");
    }


    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();
        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("/images/ball.png")));
            isGoldStatus = false;
        }
    }


    private void resetTemporaryChanges() {
        long currentTime = System.currentTimeMillis();
        if (paddleWidthChanged && (currentTime - paddleWidthChangeTime) >= paddleWidthChangeDuration) {
            paddleWidth = originalPaddleWidth;
            rect.setWidth(paddleWidth);
            paddleWidthChanged = false;
        }
        if (ballSizeChanged && (currentTime - ballSizeChangeTime) >= ballSizeChangeDuration) {
            ball.setRadius(ballRadius);
            ballSizeChanged = false;
        }
    }
    @Override
    public void onTime(long time) {
        this.time = time;
    }

}
