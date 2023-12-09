package com.brickbreakergame;

import com.brickbreakergame.managers.*;
import com.brickbreakergame.menus.MainMenu;
import com.brickbreakergame.menus.PauseMenu;
import com.brickbreakergame.screens.YouWinScreen;
import javafx.animation.*;
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


/**
 * The Main class represents the main entry point for the Brick Game application.
 * It handles the game's logic, UI components, and user input.
 * The game features a paddle, a ball, blocks to destroy, and various power-up bonuses.
 * Players aim to clear all blocks in each level while keeping the ball from falling to the floor
 * and managing their heart lives. The game progresses through levels of increasing difficulty.
 * This class extends the Application class and implements EventHandler<KeyEvent> and GameEngine.OnAction interfaces.
 *
 * @author Chhat
 * @version 1.0
 * @since 9 December 2023
 */
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
    // Constants
    private static final int LEFT  = 1;
    private static final int RIGHT = 2;
    private static int paddleWidth = 90;
    private static final int PADDLE_HEIGHT = 14;
    private static final int ballRadius = 10;
    public static final int SCENE_WIDTH = 500;
    public static final int SCENE_HEIGHT = 700;

    // Game State Variables
    protected int level = 1;
    private int score = 0;
    private int heart = 1;
    private int destroyedBlockCount = 0;

    // Paddle Variables
    private static final int PADDLE_SPEED = 3; //8
    private double paddleMoveX = 220.0;
    private double paddleMoveY = 683.0f;
    private final int halfPaddleWidth = paddleWidth / 2;
    private double centerBreakX;
    private boolean leftKeyPressed = false;
    private boolean rightKeyPressed = false;
    private AnimationTimer paddleMoveTimer;

    // Ball Variables
    private Circle ball;
    private double ballPosX;
    private double ballPosY;
    double fallSpeed = 2.0;

    // Game Mechanics Variables
    private boolean loadFromSave = false;
    private volatile long time = 0;
    private volatile long goldTime = 0;

    // UI Components
    Pane root;
    protected static Stage primaryStage;

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

    // Bonus Variables
    protected long paddleWidthChangeTime;
    protected volatile boolean paddleWidthChanged;
    protected long ballSizeChangeTime;
    protected volatile boolean ballSizeChanged;
    protected int originalPaddleWidth;
    protected int originalBallRadius;
    protected long paddleWidthChangeDuration = 0;
    protected long ballSizeChangeDuration = 0;

    // Game Objects
    private Rectangle rect;
    protected final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<BonusManager> chocos = new ArrayList<>();
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
    AnimationManager animationManager = new AnimationManager();
    BonusManager bonusManager = createBonus(0, 0);

    private GameState gameState = new GameState();

    /**
     * The start method is the main entry point for the JavaFX application.
     * It initializes the game by displaying the main menu.
     *
     * @param primaryStage The primary stage where the game's scenes will be displayed.
     */
    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage, this);
        mainMenu.display();
        gameState = new GameState();
    }

    /**
     * Initializes the ball object with default properties and position.
     */
    private void initializeGameObjects() {
        initializeBall();
        createPaddle();
    }

    /**
     * Sets up the game board for the current level. This method initializes blocks and other game elements.
     */
    private void setUpGameBoard() {
        if (!loadFromSave) {
            GameBoardManager gameBoardManager = new GameBoardManager(this);
            gameBoardManager.setupGameBoard();
        }
        primaryStage.setResizable(false);
    }

    /**
     * Creates the user interface components for the game, such as score labels and background images.
     */
    private void createUIComponents() {
        root = new Pane();
        this.uiManager = new UIManager(root);
        this.uiManager.updateBackgroundImage(level);
        this.uiManager.makeHeartScore(heart, getScore(), level);
        root.getChildren().addAll(rect, ball);
    }

    /**
     * Adds the blocks to the game UI. This method is used when starting a new level or loading a game.
     */
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

    /**
     * Sets up the scene for the game, including the layout and event handlers.
     */
    private void setUpScene() {
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
        scene.getStylesheets().addAll("/css/main.css", "/css/score.css");
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
        this.loadFromSave = false;
    }

    /**
     * Main method to start a new game session.
     *
     * @param primaryStage The primary stage for displaying the game.
     */
    public void newGame(Stage primaryStage) {
        Main.primaryStage = primaryStage;

        checkLevels();
        initializeGameObjects();
        setUpGameBoard();
        createUIComponents();
        setUpBlocks();
        setUpScene();
        startGameEngine();
    }


    private void checkLevels() {
        if (level > 1) {
            new Score().showMessage(this);
        }

        if (level == 11) {
            SoundManager.winSound();
            YouWinScreen.display(this, primaryStage);
        }
    }

    /**
     * Handles user keyboard input events such as moving the paddle and pausing the game.
     *
     * @param event The KeyEvent representing the user's keyboard input.
     */
    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            handleKeyPressed(event);
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            handleKeyReleased(event);
        }
    }

    /**
     * Handles key press events for the game.
     * This method is responsible for responding to keyboard inputs like moving the paddle left or right
     * and pausing the game.
     *
     * @param event The KeyEvent representing the user's keyboard input.
     */
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case A:
            case LEFT:
                leftKeyPressed = true;
                movePaddleX(LEFT);
                break;
            case D:
            case RIGHT:
                rightKeyPressed = true;
                movePaddleX(RIGHT);
                break;
            case ESCAPE:
                PauseMenu.display(this, getGameEngine(), primaryStage);
                event.consume();
                break;
        }
    }

    /**
     * Handles key release events for the game.
     * This method ensures smooth movement of the paddle by stopping it when the key is released.
     *
     * @param event The KeyEvent representing the user's keyboard input.
     */
    private void handleKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case A:
            case LEFT:
                leftKeyPressed = false;
                break;
            case D:
            case RIGHT:
                rightKeyPressed = false;
                break;
        }
    }

    /**
     * Moves the paddle in the specified direction.
     * This method controls the paddle's movement based on user input, ensuring that it stays within the game boundaries.
     *
     * @param direction The direction to move the paddle, either LEFT or RIGHT.
     */
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

    /**
     * Creates the ball and sets its initial properties and position.
     */
    private void initializeBall() {
        ballPosX = SCENE_WIDTH / 2.0;
        ballPosY = SCENE_HEIGHT * 0.9 ;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("/images/ball.png")));
    }

    /**
     * Creates the paddle and sets its initial properties and position.
     */
    private void createPaddle() {
        rect = new Rectangle();
        rect.setWidth(paddleWidth);
        rect.setHeight(PADDLE_HEIGHT);
        rect.setX(paddleMoveX);
        rect.setY(paddleMoveY);
        ImagePattern pattern = new ImagePattern(new Image("/images/paddle.png"));
        rect.setFill(pattern);
    }

    /**
     * Resets flags that track ball collisions with various objects.
     * Ensures proper response to new collisions after a collision has occurred.
     */
    public void resetCollideFlags() {
        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;
        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    /**
     * Sets the physics properties to the ball, including its movement and collision behavior.
     */
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

    /**
     * Checks and handles the ball's collision with the game walls.
     */
    private void checkCollisionWithWalls() {
        if (ballPosY <= ballRadius || ballPosY + ballRadius >= SCENE_HEIGHT) {
            handleWallCollision();
        }

        if (ballPosX + ballRadius >= SCENE_WIDTH || ballPosX - ballRadius <= 0) {
            handleSideWallCollision();
        }
    }

    /**
     * Handles the ball's collision with the top and bottom walls.
     */
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
            } else {
                animationManager.animateHeartLoss(uiManager.getHeartLabel());
            }
        }
    }

    /**
     * Handles the ball's collision with the left and right walls.
     */
    private void handleSideWallCollision() {
        SoundManager.paddleBounceSound();
        resetCollideFlags();
        if (ballPosX + ballRadius >= SCENE_WIDTH) {
            collideToRightWall = true;
        } else {
            collideToLeftWall = true;
        }
    }

    /**
     * Checks and handles the ball's collision with the paddle.
     */
    private void checkCollisionWithPaddle() {
        if (ballPosY + ballRadius >= paddleMoveY &&
                ballPosX + ballRadius >= paddleMoveX &&
                ballPosX - ballRadius <= paddleMoveX + paddleWidth) {
            handlePaddleCollision();
        }
    }

    /**
     * Checks for a continuous collision with the paddle.
     * This method is used to fix a bug where the ball could phase through the paddle.
     *
     * @return true if a continuous collision is detected, false otherwise.
     */
    private boolean checkContinuousCollisionWithPaddle() {
        double nextX = ballPosX + (goRightBall ? ballVelocityX : -ballVelocityX);
        double nextY = ballPosY + (goDownBall ? ballVelocityY : -ballVelocityY);


        // Check for collision in the path between current position and next position, to fix the bug where it phases through the paddle
        if (nextY + ballRadius >= paddleMoveY && nextY - ballRadius <= paddleMoveY + PADDLE_HEIGHT) {
            return nextX + ballRadius >= paddleMoveX && nextX - ballRadius <= paddleMoveX + paddleWidth;
        }
        return false;
    }

    /**
     * Handles the ball's collision with the paddle, adjusting its velocity and direction.
     */
    private void handlePaddleCollision() {
        resetCollideFlags();
        calculateBallVelocity();
        goDownBall = false;
        collideToBreakAndMoveToRight = ballPosX - centerBreakX > 0;
        SoundManager.paddleBounceSound();
    }

    /**
     * Calculates the velocity of the ball after colliding with the paddle.
     */
    private void calculateBallVelocity() {
        double relation = (ballPosX - centerBreakX) / ((double) paddleWidth / 2);

        // Ensure relation is not too small to avoid division by zero
        if (Math.abs(relation) < 0.001) {
            relation = 0.001 * Math.signum(relation);
        }

        ballVelocityX = Math.abs(relation) * MAX_VELOCITY_X; // Ensure this doesn't exceed MAX_VELOCITY_X
        ballVelocityX = Math.min(ballVelocityX, MAX_VELOCITY_X);

        // Calculate ballVelocityY ensuring it's not NaN
        double velocityYSquared = Math.pow(MAX_VELOCITY, 2) - Math.pow(ballVelocityX, 2);
        if (velocityYSquared < 0) {
            velocityYSquared = 0;
        }
        ballVelocityY = Math.sqrt(velocityYSquared);

        // Add spin effect based on paddle movement
        if (leftKeyPressed) {
            ballVelocityX -= SPIN_EFFECT; // SPIN_EFFECT is a constant defining how much spin affects the ball
        } else if (rightKeyPressed) {
            ballVelocityX += SPIN_EFFECT;
        }
    }

    /**
     * Checks and handles the ball's collision with blocks.
     */
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

    /**
     * Determines the direction of the ball after it has collided with an object.
     */
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

    /**
     * Updates the position of the ball based on its current velocity and direction.
     */
    private void updateBallPosition() {
        ballPosX += goRightBall ? ballVelocityX : -ballVelocityX;
        ballPosY += goDownBall ? ballVelocityY : -ballVelocityY;
    }

    /**
     * Checks if all blocks have been destroyed to progress to the next level.
     */
    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            LevelManager levelManager = new LevelManager(this, primaryStage);
            SoundManager.levelUp();
            level++;
            levelManager.nextLevel();
        }
    }


    /**
     * Loads a previously saved game state from the save file.
     *
     * @param primaryStage The primary stage where the loaded game will be displayed.
     */
    public void loadGame(Stage primaryStage) {
        Main.primaryStage = primaryStage;

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
        setScore(gameState.score);
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

        paddleMoveX = SCENE_WIDTH / 2.0 - paddleWidth / 2.0; // Moves paddle back to default location for very clean look

        loadFromSave = true;
        newGame(primaryStage);

    }

    /**
     * Updates the game objects' positions and handles collision and collision detection in each frame.
     */
    @Override
    public void onUpdate() {
        updateUIComponents();
        updateGameObjects();
        handleBlockCollisions();
        handleBonusCollection();
        bonusManager.resetTemporaryChanges();
    }

    /**
     * Updates the user interface components like score and heart labels.
     */
    private void updateUIComponents() {
        Platform.runLater(() -> {
            uiManager.setScore(getScore());
            Label heartLabelFromUIManager = uiManager.getHeartLabel();
            heartLabelFromUIManager.setText("Hearts: " + heart);
        });
    }

    /**
     * Updates the positions of game objects like the paddle and ball.
     */
    private synchronized void updateGameObjects() {
        Platform.runLater(() -> {
            rect.setX(paddleMoveX);
            rect.setY(paddleMoveY);
            ball.setCenterX(ballPosX);
            ball.setCenterY(ballPosY);
            for (BonusManager choco : chocos) {
                choco.choco.setY(choco.y);
            }
        });
    }

    /**
     * Handles the collisions between the ball and blocks, including block destruction.
     */
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

    /**
     * Repositions the ball after it has collided with a block. Makes the look very very clean.
     *
     * @param block   The block that the ball collided with.
     * @param hitCode The code indicating the side of the block hit by the ball.
     */
    private void repositionBallAfterCollision(Block block, int hitCode) {
        switch (hitCode) {
            case Block.HIT_BOTTOM:
                ballPosY = block.y + Block.getHeight() + ballRadius;
                break;
            case Block.HIT_TOP:
                ballPosY = block.y - ballRadius;
                break;
            case Block.HIT_LEFT:
                ballPosX = block.x - ballRadius;
                break;
            case Block.HIT_RIGHT:
                ballPosX = block.x + Block.getWidth() + ballRadius;
                break;
        }
    }

    /**
     * Handles the actions to be taken when a block is hit by the ball.
     *
     * @param block   The block that was hit.
     * @param hitCode The code indicating the side of the block hit by the ball.
     */
    private void handleBlockHit(Block block, int hitCode) {
        setScore(getScore() + 1);
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

    /**
     * Checks for and handles the ball's collision with the game blocks.
     * This method updates the game state based on the type of block hit.
     * @param block The block generated by the game board
     */
    private void checkBlockTypeActions(Block block) {
        if (block.type == Block.BLOCK_RANDOM) {
            handleRandomBlock(block);
        } else if (block.type == Block.BLOCK_GOLDEN_TIME) {
            handleGoldenTimeBlock();
        } else if (block.type == Block.BLOCK_HEART) {
            heart++;
            animationManager.animateHeartIncrease(uiManager.getHeartLabel());
            SoundManager.heartBonus();
        }
    }

    /**
     * Handles the actions when a random block is hit by the ball.
     * This method generates bonus items and updates the game state.
     *
     * @param block The block that was hit by the ball.
     */
    private void handleRandomBlock(Block block) {
        Platform.runLater(() -> {
            final BonusManager choco = new BonusManager(block.row, block.column, this);
            choco.timeCreated = time;
            root.getChildren().add(choco.choco);
            chocos.add(choco);
        });
    }

    /**
     * Handles the actions when a golden time block is hit.
     * This method activates a special mode where the ball becomes golden.
     */
    private void handleGoldenTimeBlock() {
        goldTime = System.currentTimeMillis();
        ball.setFill(new ImagePattern(new Image("/images/goldBall.png")));
        SoundManager.goldBallPowerUp();
        isGoldStatus = true;
    }

    /**
     * Handles the collection of bonuses and updates the game state accordingly.
     */
    private void handleBonusCollection() {
        Iterator<BonusManager> iterator = chocos.iterator();
        while (iterator.hasNext()) {
            BonusManager choco = iterator.next();

            if (choco.y > SCENE_HEIGHT || choco.taken) {
                iterator.remove();
                continue;
            }

            if (choco.y >= paddleMoveY && choco.y <= paddleMoveY + PADDLE_HEIGHT
                    && choco.x >= paddleMoveX && choco.x <= paddleMoveX + paddleWidth) {
//                applyBonusEffect(choco);
                choco.applyBonusEffect();
                iterator.remove();
            } else {
                // Update the Y position to simulate falling
                choco.y += fallSpeed;

                Platform.runLater(() -> choco.choco.setY(choco.y));
            }
        }
    }

    public BonusManager createBonus(int row, int column) {
        return new BonusManager(row, column, this);
    }

    /**
     * Callback method invoked by the game engine on every physics update.
     * It handles the core physics calculations for the game, such as ball movement and collision detection.
     */
    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();
        checkGoldStatus();
    }

    private void checkGoldStatus() {
        if (isGoldStatus && (System.currentTimeMillis() - goldTime) > 5000) {
            ball.setFill(new ImagePattern(new Image("/images/ball.png")));
            isGoldStatus = false;
        }
    }

    /**
     * Callback method for tracking game time.
     *
     * @param time The current game time in milliseconds.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }

    /**
     * Returns the current score of the game.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score of the game.
     *
     * @param score The score to set.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Returns the primary stage of the game.
     *
     * @return The primary stage used in the game.
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    /**
     * Sets the current game level.
     * @param level The level to be set for the game.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Sets the number of hearts (lives) for the player.
     * @param heart The number of hearts to be set.
     */
    public void setHeart(int heart) {
        this.heart = heart;
    }

    /**
     * Sets the velocity of the ball in the X direction.
     * @param ballVelocityX The X velocity to be set for the ball.
     */
    public void setBallVelocityX(double ballVelocityX) {
        this.ballVelocityX = ballVelocityX;
    }

    /**
     * Sets the number of blocks destroyed in the game.
     * @param destroyedBlockCount The count of destroyed blocks to be set.
     */
    public void setDestroyedBlockCount(int destroyedBlockCount) {
        this.destroyedBlockCount = destroyedBlockCount;
    }

    /**
     * Sets the flag indicating the direction of the ball's movement.
     * @param goDownBall True to set the ball to move downwards, false otherwise.
     */
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    /**
     * Sets the status of the 'Gold' mode in the game.
     * @param isGoldStatus True to activate 'Gold' mode, false to deactivate.
     */
    public void setIsGoldStatus(boolean isGoldStatus) {
        this.isGoldStatus = isGoldStatus;
    }

    /**
     * Sets the status of the existence of the heart block in the game.
     * @param isExistHeartBlock True to indicate the presence of a heart block, false otherwise.
     */
    public void setIsExistHeartBlock(boolean isExistHeartBlock) {
        this.isExistHeartBlock = isExistHeartBlock;
    }

    /**
     * Sets the current game time.
     * @param time The time to be set in the game.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Sets the time when the golden time block was hit.
     * @param goldTime The time to be set for when the golden time block was activated.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }

    /**
     * Gets the list of blocks currently in the game.
     * @return The list of Block objects.
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Gets the list of chocos (bonuses) currently in the game.
     * @return The list of Bonus objects.
     */
    public ArrayList<BonusManager> getChocos() {
        return chocos;
    }

    /**
     * Retrieves the instance of the GameEngine.
     * <p>
     * This method provides access to the GameEngine object, which is responsible for
     * managing the main loop of the game. It controls game timing, updates game states,
     * and orchestrates the rendering process. By accessing the GameEngine, other components
     * of the game can interact with the core game loop, such as starting, stopping, or
     * pausing the game.
     *
     * @return The current instance of GameEngine being used by the game.
     */
    public GameEngine getEngine() {
        return engine;
    }

    /**
     * Gets the current game level.
     *
     * @return The current level of the game.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the array of colors used in the game.
     *
     * @return The array of Color objects.
     */
    public Color[] getColors() {
        return colors;
    }

    /**
     * Checks if a heart block exists in the game.
     * <p>
     * The heart block is a special game element that affects the gameplay
     * by providing additional lives to the player when destroyed.
     *
     * @return True if a heart block exists in the current game level, false otherwise.
     */
    public boolean isHeartBlockExist() {
        return isExistHeartBlock;
    }

    /**
     * Sets the existence status of the heart block.
     *
     * @param existHeartBlock The new status of the heart block existence.
     */
    public void setExistHeartBlock(boolean existHeartBlock) {
        this.isExistHeartBlock = existHeartBlock;
    }

    public void setPaddleMoveY(float paddleMoveY) {
        this.paddleMoveY = paddleMoveY;
    }

    // Getter and setter for paddleWidth
    public int getPaddleWidth() {
        return paddleWidth;
    }

    public void setPaddleWidth(int width) {
        paddleWidth = width;
        rect.setWidth(width); // Assuming rect is the Rectangle representing the paddle
    }

    // Getter and setter for paddleMoveX
    public double getPaddleMoveX() {
        return paddleMoveX;
    }

    public void setPaddleMoveX(double x) {
        paddleMoveX = x;
        rect.setX(x); // Adjust the position of the paddle
    }

    // Getter and setter for originalPaddleWidth
    public int getOriginalPaddleWidth() {
        return originalPaddleWidth;
    }

    public void setOriginalPaddleWidth(int width) {
        originalPaddleWidth = width;
    }

    // Getter and setter for paddleWidthChangeTime
    public long getPaddleWidthChangeTime() {
        return paddleWidthChangeTime;
    }

    public void setPaddleWidthChangeTime(long time) {
        paddleWidthChangeTime = time;
    }

    // Getter and setter for paddleWidthChangeDuration
    public long getPaddleWidthChangeDuration() {
        return paddleWidthChangeDuration;
    }

    public void setPaddleWidthChangeDuration(long duration) {
        paddleWidthChangeDuration = duration;
    }

    // Getter and setter for paddleWidthChanged
    public boolean isPaddleWidthChanged() {
        return paddleWidthChanged;
    }

    public void setPaddleWidthChanged(boolean changed) {
        paddleWidthChanged = changed;
    }

    // Getter and setter for ballRadius
    public double getBallRadius() {
        return ball.getRadius();
    }

    public void setBallRadius(double radius) {
        ball.setRadius(radius);
    }

    // Getter and setter for ballPosX
    public double getBallPosX() {
        return ball.getCenterX();
    }

    public void setBallPosX(double posX) {
        ball.setCenterX(posX);
    }

    // Getter and setter for ballPosY
    public double getBallPosY() {
        return ball.getCenterY();
    }

    public void setBallPosY(double posY) {
        ball.setCenterY(posY);
    }

    // Getter and setter for originalBallRadius
    public double getOriginalBallRadius() {
        return originalBallRadius;
    }

    public void setOriginalBallRadius(double radius) {
        originalBallRadius = (int) radius;
    }

    // Getter and setter for ballSizeChangeTime
    public long getBallSizeChangeTime() {
        return ballSizeChangeTime;
    }

    public void setBallSizeChangeTime(long time) {
        ballSizeChangeTime = time;
    }

    // Getter and setter for ballSizeChangeDuration
    public long getBallSizeChangeDuration() {
        return ballSizeChangeDuration;
    }

    public void setBallSizeChangeDuration(long duration) {
        ballSizeChangeDuration = duration;
    }

    // Getter and setter for ballSizeChanged
    public boolean isBallSizeChanged() {
        return ballSizeChanged;
    }

    public void setBallSizeChanged(boolean changed) {
        ballSizeChanged = changed;
    }

    public int getHeart() {
        return heart;
    }

    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    public double getPaddleMoveY() {
        return paddleMoveY;
    }
    public double getCenterBreakX() {
        return centerBreakX;
    }


    public long getTime() {
        return time;
    }

    public long getGoldTime() {
        return goldTime;
    }

    public double getBallVelocityX() {
        return ballVelocityX;
    }

    // Boolean state variables
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public boolean isGoldStatus() {
        return isGoldStatus;
    }


    public boolean isGoDownBall() {
        return goDownBall;
    }

    public boolean isGoRightBall() {
        return goRightBall;
    }

    public boolean isCollideToBreak() {
        return collideToBreak;
    }


    public boolean isCollideToBreakAndMoveToRight() {
        return collideToBreakAndMoveToRight;
    }


    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }


    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    public GameState getGameState() {
        return this.gameState; // Assuming gameState is the variable name in Main class
    }
}