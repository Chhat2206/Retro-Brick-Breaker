package com.brickbreakergame;

import com.brickbreakergame.managers.*;
import com.brickbreakergame.menus.MainMenu;
import com.brickbreakergame.menus.PauseMenu;
import com.brickbreakergame.screens.YouWinScreen;
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

import java.util.ArrayList;
import java.util.Iterator;


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
 * @since 12 December 2023
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
    private int heart = 3;
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
    protected boolean loadFromSave = false;
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

    private GameController gameController = new GameController();

    /**
     * Initializes and displays the main game window.
     * This method sets up the primary stage of the game, including the main menu and game scenes.
     *
     * @param primaryStage The primary stage for this application, onto which the scene is set.
     */
    @Override
    public void start(Stage primaryStage) {
        MainMenu mainMenu = new MainMenu(primaryStage, this);
        mainMenu.display();
        gameController = new GameController();
    }

    public Pane getRoot() {
        return root;
    }

    /**
     * Initializes the ball object with default properties and position. The ball is a central game element
     * used in gameplay mechanics. This method sets its initial size, appearance, and starting position.
     */
    protected void initializeGameObjects() {
        initializeBall();
        createPaddle();
    }

    /**
     * Sets up the game board for the current level. This includes initializing blocks and other game elements
     * as per the current level's layout and difficulty. It resets the game state for a new level or game session.
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
     * This method sets up the visual elements that the player interacts with during gameplay.
     */
    private void createUIComponents() {
        root = new Pane();
        this.uiManager = new UIManager(root);
        this.uiManager.updateBackgroundImage(level);
        this.uiManager.makeHeartScore(heart, getScore(), level);
        root.getChildren().addAll(rect, ball);
    }

    /**
     * Adds the blocks to the game UI. This method is used when starting a new level or loading a game,
     * and is responsible for placing the blocks in their initial positions.
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
     * Sets up the scene for the game, including the layout and event handlers. This method is crucial for initializing
     * the visual and interactive components of the game window.
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

    /**
     * Starts the game's main engine. This method initiates the game loop,
     * handling updates and rendering of the game.
     */
    private void startGameEngine() {
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
        this.loadFromSave = false;
    }

    /**
     * Starts a new game session.
     * This method initializes the game objects, sets up the game board, UI components, and begins the game engine.
     *
     * @param primaryStage The primary stage used to display the game.
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
        Score score = new Score(this);
        score.checkLevels();
    }

    /**
     * Handles keyboard input events.
     * This method responds to paddle movement.
     *
     * @param event The KeyEvent representing the player's keyboard input.
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
     * Moves the paddle left or right. This method is responsible for controlling the paddle's
     * movement based on user input, ensuring that it stays within the game boundaries.
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
     * Resets flags that track ball collisions with various objects. This is essential for ensuring proper
     * response to new collisions after a collision has occurred and to prevent incorrect collision handling.
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
     * Updates the position of the ball based on its current velocity and direction. This method is crucial
     * for the movement mechanics of the ball as it calculates its new position every frame.
     */
    private void setPhysicsToBall() {
        updateBallPosition();
        checkCollisionWithWalls();

        // Enhanced collision detection
        boolean continuousCollision = checkContinuousCollisionWithPaddle();
        if (continuousCollision) {
            handlePaddleCollision();
        } else {
            checkCollisionWithPaddle();
        }

        checkCollisionWithBlocks();
        handleBallDirection();
    }

    /**
     * Checks and handles the ball's collision with the game walls. This method ensures that the ball reacts
     * appropriately when hitting the boundaries of the game area.
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
     * Handles the ball's collision with the top and bottom walls. This method determines the ball's response
     * when it comes into contact with either the top or bottom boundary of the game area.
     */
    private void handleWallCollision() {
        if (ballPosY <= ballRadius) {
            bounceOffTopWall();
        } else {
            bounceOffBottomWall();
        }
    }

    /**
     * Handles the ball's bounce off the top wall. This method reverses the ball's vertical direction
     * upon hitting the top boundary of the game area.
     */
    private void bounceOffTopWall() {
        SoundManager.paddleBounceSound();
        resetCollideFlags();
        goDownBall = true;
    }

    /**
     * Handles the ball's bounce off the bottom wall. This method deals with the consequences of the ball
     * hitting the bottom boundary, including life reduction and game over scenarios.
     */
    private void bounceOffBottomWall() {
        resetCollideFlags();
        goDownBall = false;
        if (!isGoldStatus) {
            heart--;
            SoundManager.ballHitFloor();
            new Score(this).show((double) SCENE_WIDTH / 2, (double) SCENE_HEIGHT / 2, -1, this);

            if (heart <= 0) {
                new Score(this).showGameOver(this);
                engine.stop();
            } else {
                animationManager.animateHeartLoss(uiManager.getHeartLabel());
            }
        }
    }

    /**
     * Handles the ball's collision with the left and right walls. This method reverses the ball's horizontal
     * direction when it hits the side boundaries of the game area.
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
     * Checks and handles the ball's collision with the paddle. This method determines if and how the ball
     * interacts with the paddle, affecting its trajectory.
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
     * Handles the ball's collision with the paddle, adjusting its velocity and direction. This method is critical
     * for reflecting the ball's movement when it interacts with the paddle.
     */
    private void handlePaddleCollision() {
        resetCollideFlags();
        calculateBallVelocity();
        goDownBall = false;
        collideToBreakAndMoveToRight = ballPosX - centerBreakX > 0;
        SoundManager.paddleBounceSound();
    }

    /**
     * Calculates the velocity of the ball after colliding with the paddle. This method adjusts the speed
     * and direction of the ball based on its point of contact with the paddle.
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
     * Checks for and handles the ball's collision with blocks. This method updates the game state
     * based on the interaction between the ball and individual blocks.
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
     * Determines the direction of the ball after it has collided with an object. This method is essential for
     * controlling the ball's movement logic post-collision.
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
     * Updates the position of the ball based on its current velocity and direction. This method is crucial
     * for the movement mechanics of the ball as it calculates its new position every frame.
     */
    private void updateBallPosition() {
        ballPosX += goRightBall ? ballVelocityX : -ballVelocityX;
        ballPosY += goDownBall ? ballVelocityY : -ballVelocityY;
    }

    /**
     * Checks if all blocks have been destroyed to progress to the next level. This is an essential part of the
     * game progression, triggering level advancement and difficulty increase.
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
     * Called on every frame update of the game. This method updates the game state, including UI components,
     * game object positions, and handles collisions.
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
     * Updates the user interface components like score and heart labels. This method keeps the UI elements
     * in sync with the game state.
     */
    private void updateUIComponents() {
        Platform.runLater(() -> {
            uiManager.setScore(getScore());
            Label heartLabelFromUIManager = uiManager.getHeartLabel();
            heartLabelFromUIManager.setText("Hearts: " + heart);
        });
    }

    /**
     * Updates the positions of game objects like the paddle and ball. This method is essential for ensuring
     * that game elements are displayed correctly according to their current state.
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
     * Manages the ball's collisions with the blocks.
     * This method is responsible for determining if the ball has collided with any block,
     * and it handles the consequences of such collisions, such as changing the ball's direction,
     * updating scores, or block destruction.
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
     * Repositions the ball after it has collided with a block. Makes the look very clean.
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
        new Score(this).show(block.x, block.y, 1, this);

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
        if (block.type == Block.RANDOM) {
            handleRandomBlock(block);
        } else if (block.type == Block.GOLDEN_TIME) {
            handleGoldenTimeBlock();
        } else if (block.type == Block.HEART) {
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
     * Handles the collection of bonuses and updates the game state accordingly. This method is responsible
     * for detecting and applying the effects of bonus items collected during gameplay.
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
     * Callback method for tracking game time. This method is used to update time-dependent aspects of the game,
     * such as power-ups and game timers.
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
     * Sets the horizontal velocity of the ball.
     * This method is crucial for controlling the ball's speed and direction along the X-axis,
     * typically in response to collisions or game events.
     *
     * @param ballVelocityX The new horizontal velocity for the ball. Positive values move the ball to the right, negative to the left.
     */
    public void setBallVelocityX(double ballVelocityX) {
        this.ballVelocityX = ballVelocityX;
    }

    /**
     * Updates the count of blocks destroyed in the game.
     * This count is used to track progress and can trigger level changes or score updates.
     *
     * @param destroyedBlockCount The total number of blocks destroyed so far in the game.
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

    /**
     * Sets the Y position of the paddle on the game screen.
     * This method updates the vertical position of the paddle, typically in response to game events or player input.
     *
     * @param paddleMoveY The new Y position for the paddle. Should be within the game screen bounds.
     */
    public void setPaddleMoveY(float paddleMoveY) {
        this.paddleMoveY = paddleMoveY;
    }

    /**
     * Retrieves the current width of the paddle.
     * This width may change during the game if the paddle encounters certain bonuses or penalties.
     *
     * @return The width of the paddle in pixels.
     */
    public int getPaddleWidth() {
        return paddleWidth;
    }

    /**
     * Sets the width of the paddle.
     *
     * @param width The width to set for the paddle.
     */
    public void setPaddleWidth(int width) {
        paddleWidth = width;
        rect.setWidth(width);
    }

    /**
     * Retrieves the X position of the paddle.
     *
     * @return The current X position of the paddle.
     */
    public double getPaddleMoveX() {
        return paddleMoveX;
    }

    /**
     * Sets the X position of the paddle.
     *
     * @param x The X position to set for the paddle.
     */
    public void setPaddleMoveX(double x) {
        paddleMoveX = x;
        if (rect != null) {
            rect.setX(x);
        }
    }

    /**
     * Retrieves the original width of the paddle.
     *
     * @return The original width of the paddle.
     */
    public int getOriginalPaddleWidth() {
        return originalPaddleWidth;
    }

    /**
     * Sets the original width of the paddle.
     *
     * @param width The original width of the paddle to set.
     */
    public void setOriginalPaddleWidth(int width) {
        originalPaddleWidth = width;
    }

    /**
     * Retrieves the time when the paddle width change occurred.
     *
     * @return The time of the last paddle width change.
     */
    public long getPaddleWidthChangeTime() {
        return paddleWidthChangeTime;
    }

    /**
     * Sets the time when the paddle width change occurred.
     *
     * @param time The time to set for the last paddle width change.
     */
    public void setPaddleWidthChangeTime(long time) {
        paddleWidthChangeTime = time;
    }

    /**
     * Retrieves the duration of the paddle width change.
     *
     * @return The duration of the paddle width change.
     */
    public long getPaddleWidthChangeDuration() {
        return paddleWidthChangeDuration;
    }

    /**
     * Sets the duration of the paddle width change.
     *
     * @param duration The duration to set for the paddle width change.
     */
    public void setPaddleWidthChangeDuration(long duration) {
        paddleWidthChangeDuration = duration;
    }

    /**
     * Checks if the paddle width has been changed.
     *
     * @return True if the paddle width has been changed, false otherwise.
     */
    public boolean isPaddleWidthChanged() {
        return paddleWidthChanged;
    }

    /**
     * Sets the status of the paddle width change.
     *
     * @param changed True if the paddle width has been changed, false otherwise.
     */
    public void setPaddleWidthChanged(boolean changed) {
        paddleWidthChanged = changed;
    }

    /**
     * Retrieves the radius of the ball.
     *
     * @return The current radius of the ball.
     */
    public double getBallRadius() {
        return ball.getRadius();
    }

    /**
     * Sets the radius of the ball.
     *
     * @param radius The radius to set for the ball.
     */
    public void setBallRadius(double radius) {
        ball.setRadius(radius);
    }

    /**
     * Retrieves the X position of the ball.
     *
     * @return The current X position of the ball.
     */
    public double getBallPosX() {
        return ball.getCenterX();
    }

    /**
     * Sets the X position of the ball.
     *
     * @param posX The X position to set for the ball.
     */
    public void setBallPosX(double posX) {
        if (ball != null) {
            ball.setCenterX(posX);
        }
    }

    /**
     * Sets the Y position of the ball.
     *
     * @param posY The Y position to set for the ball.
     */
    public void setBallPosY(double posY) {
        if (ball != null) {
            ball.setCenterY(posY);
        }
    }

    /**
     * Retrieves the Y position of the ball.
     *
     * @return The current Y position of the ball.
     */
    public double getBallPosY() {
        return ball.getCenterY();
    }

    /**
     * Retrieves the original radius of the ball.
     *
     * @return The original radius of the ball.
     */
    public double getOriginalBallRadius() {
        return originalBallRadius;
    }

    /**
     * Sets the original radius of the ball.
     *
     * @param radius The original radius to set for the ball.
     */
    public void setOriginalBallRadius(double radius) {
        originalBallRadius = (int) radius;
    }

    /**
     * Retrieves the time when the ball size change occurred.
     *
     * @return The time of the last ball size change.
     */
    public long getBallSizeChangeTime() {
        return ballSizeChangeTime;
    }

    /**
     * Sets the time when the ball size change occurred.
     *
     * @param time The time to set for the last ball size change.
     */
    public void setBallSizeChangeTime(long time) {
        ballSizeChangeTime = time;
    }

    /**
     * Retrieves the duration of the ball size change.
     *
     * @return The duration of the ball size change.
     */
    public long getBallSizeChangeDuration() {
        return ballSizeChangeDuration;
    }

    /**
     * Sets the duration of the ball size change.
     *
     * @param duration The duration to set for the ball size change.
     */
    public void setBallSizeChangeDuration(long duration) {
        ballSizeChangeDuration = duration;
    }

    /**
     * Checks if the ball size has been changed.
     *
     * @return True if the ball size has been changed, false otherwise.
     */
    public boolean isBallSizeChanged() {
        return ballSizeChanged;
    }

    /**
     * Sets the status of the ball size change.
     *
     * @param changed True if the ball size has been changed, false otherwise.
     */
    public void setBallSizeChanged(boolean changed) {
        ballSizeChanged = changed;
    }

    /**
     * Retrieves the number of hearts (lives) remaining in the game.
     *
     * @return The current number of hearts.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Retrieves the count of destroyed blocks in the game.
     *
     * @return The count of destroyed blocks.
     */
    public int getDestroyedBlockCount() {
        return destroyedBlockCount;
    }

    /**
     * Retrieves the Y position of the paddle.
     *
     * @return The current Y position of the paddle.
     */
    public double getPaddleMoveY() {
        return paddleMoveY;
    }

    /**
     * Retrieves the X position of the center of the paddle break.
     *
     * @return The X position of the center of the paddle break.
     */
    public double getCenterBreakX() {
        return centerBreakX;
    }

    /**
     * Retrieves the current game time.
     *
     * @return The current game time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Retrieves the time when the golden time block was hit.
     *
     * @return The time when the golden time block was hit.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Retrieves the velocity of the ball in the X direction.
     *
     * @return The X velocity of the ball.
     */
    public double getBallVelocityX() {
        return ballVelocityX;
    }

// Boolean state variable getters

    /**
     * Checks if there is an existing heart block in the game.
     *
     * @return True if a heart block exists, false otherwise.
     */
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Checks whether the game is currently in 'Gold' mode.
     * In 'Gold' mode, the ball will not lose hearts when bouncing off the ground.
     *
     * @return True if the game is in 'Gold' mode, false otherwise.
     */
    public boolean isGoldStatus() {
        return isGoldStatus;
    }

    /**
     * Checks if the ball is moving downward.
     *
     * @return True if the ball is moving down, false otherwise.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }

    /**
     * Checks if the ball is moving to the right.
     *
     * @return True if the ball is moving right, false otherwise.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }

    /**
     * Checks if there has been a collision leading to a break.
     *
     * @return True if there is a collision leading to a break, false otherwise.
     */
    public boolean isCollideToBreak() {
        return collideToBreak;
    }

    /**
     * Checks if there is a collision leading to a break and a move to the right.
     *
     * @return True if there is such a collision, false otherwise.
     */
    public boolean isCollideToBreakAndMoveToRight() {
        return collideToBreakAndMoveToRight;
    }

    /**
     * Checks if there has been a collision with the right wall.
     *
     * @return True if there is a collision with the right wall, false otherwise.
     */
    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    /**
     * Checks if there has been a collision with the left wall.
     *
     * @return True if there is a collision with the left wall, false otherwise.
     */
    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    /**
     * Checks if there has been a collision with a right block.
     *
     * @return True if there is a collision with a right block, false otherwise.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Checks if there has been a collision with a bottom block.
     *
     * @return True if there is a collision with a bottom block, false otherwise.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Checks if there has been a collision with a left block.
     *
     * @return True if there is a collision with a left block, false otherwise.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Checks if there has been a collision with the top block.
     *
     * @return True if there is a collision with a top block, false otherwise.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Retrieves the current state of the game.
     *
     * @return The current state of the game as a GameController object.
     */
    public GameController getGameState() {
        return this.gameController;
    }

    /**
     * Determines the horizontal movement direction of the ball.
     * Setting this to true makes the ball move towards the right side of the screen.
     *
     * @param goRightBall True to make the ball move to the right, false for moving it to the left.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    /**
     * Sets the collision status leading to a break.
     *
     * @param collideToBreak True if there is a collision leading to a break, false otherwise.
     */
    public void setCollideToBreak(boolean collideToBreak) {
        this.collideToBreak = collideToBreak;
    }

    /**
     * Sets the collision status leading to a break and a subsequent move to the right.
     *
     * @param collideToBreakAndMoveToRight True if there is such a collision, false otherwise.
     */
    public void setCollideToBreakAndMoveToRight(boolean collideToBreakAndMoveToRight) {
        this.collideToBreakAndMoveToRight = collideToBreakAndMoveToRight;
    }

    /**
     * Sets the collision status for the ball with the right wall.
     * When set to true, it indicates that the ball has collided with the right boundary of the game area.
     *
     * @param collideToRightWall True to indicate a collision with the right wall, false otherwise.
     */
    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    /**
     * Sets the collision status for the ball with the left wall.
     * When set to true, it indicates that the ball has collided with the left boundary of the game area.
     *
     * @param collideToLeftWall True to indicate a collision with the left wall, false otherwise.
     */
    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    /**
     * Sets the collision status for the ball with a block on the right.
     * This status is used in the game's logic to determine the ball's movement after hitting a block on its right side.
     *
     * @param collideToRightBlock True if the ball collides with a block on the right, false otherwise.
     */
    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    /**
     * Sets the collision status for the ball with a block at the bottom.
     * This is used for handling the ball's behavior after it collides with any block located below it.
     *
     * @param collideToBottomBlock True to indicate a collision with a block at the bottom, false otherwise.
     */
    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    /**
     * Sets the collision status for the ball with a block on the left.
     * It controls the game mechanics in terms of how the ball reacts after hitting a block to its left.
     *
     * @param collideToLeftBlock True if the ball collides with a block on the left, false otherwise.
     */
    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    /**
     * Sets the status of collision between the ball and a top block.
     * This method is crucial for determining the ball's rebound direction after hitting a block above it.
     *
     * @param collideToTopBlock True if there is a collision with a top block, false otherwise.
     */
    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

    /**
     * Sets the X-coordinate for the center point of the paddle's breaking action.
     * This is used to determine the impact point of the ball on the paddle and calculate the ball's rebound trajectory.
     *
     * @param centerBreakX The X-coordinate of the paddle's center breaking point.
     */
    public void setCenterBreakX(double centerBreakX) {
        this.centerBreakX = centerBreakX;
    }


    /**
     * Retrieves the ball object used in the game.
     * The ball is a central element in gameplay, and this method provides access to its properties.
     *
     * @return The Circle object representing the ball in the game.
     */
    public Circle getBall() {
        return ball;
    }
}