package brickGame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private static int PADDLE_WIDTH = 90;
    private static final int PADDLE_HEIGHT = 14;
    private static final int BALL_RADIUS = 10;
    private static final int SCENE_WIDTH = 500;
    private static final int SCENE_HEIGHT = 700;

    // Game State Variables
    protected int level = 0;
    private int score = 0;
    private int heart = 3;
    private int destroyedBlockCount = 0;

    // Paddle Variables
    private static final int PADDLE_SPEED = 3;
    private double paddleMoveX = 250.0;
    private double paddleMoveY = 680.0f;
    private final int halfPaddleWidth = PADDLE_WIDTH / 2;
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
    private long time = 0;
    private long goldTime = 0;

    // UI Components
    Pane root;
    private Label scoreLabel;
    private Label heartLabel;
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

    // Game Objects
    private Rectangle rect;
    protected final ArrayList<Block> blocks = new ArrayList<>();
    private final ArrayList<Bonus> chocos = new ArrayList<>();
    protected final Color[] colors = new Color[]{
            Color.MAGENTA, Color.RED, Color.GOLD, Color.CORAL,
            Color.AQUA, Color.VIOLET, Color.GREENYELLOW,
            Color.ORANGE, Color.PINK,
            Color.YELLOW, Color.TOMATO,
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

    public void newGame(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Increment level and show messages if appropriate
        if (!loadFromSave) {
            level++;
            if (level > 1) {
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 10) {
                new Score().showWin(this);
                return;
            }
        }

        // Initialize ball and paddle
        initializeBall();
        createPaddle();

        // Set up the game board
        if (!loadFromSave) {
            GameBoardManager gameBoardManager = new GameBoardManager(this);
            gameBoardManager.setupGameBoard();
        }
        primaryStage.setResizable(false);

        // Create and set up UI components
        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        Label levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);

        this.uiManager = new UIManager(root);
        this.uiManager.makeBackgroundImage();
        this.uiManager.makeHeartScore(heart);

        root.getChildren().addAll(rect, ball, scoreLabel, levelLabel);

        // Set up the blocks
        if (!loadFromSave) {
            for (Block block : blocks) {
                Platform.runLater(() -> {
                    if (block != null && block.rect != null) {
                        root.getChildren().add(block.rect);
                    }
                });
            }
        }

        // Set up the scene
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setOnKeyPressed(this);
        scene.setOnKeyReleased(this);
        scene.getStylesheets().add("/css/main.css");
        primaryStage.setTitle("The Incredible Block Breaker Game");
        primaryStage.getIcons().add(new Image("/images/favicon.png"));
        SoundManager.startBackgroundMusic("src/main/resources/Sound Effects/Background Music/backgroundMusic8Bit.mp3");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the game engine
        engine = new GameEngine();
        engine.setOnAction(this);
        engine.setFps(120);
        engine.start();
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
                if (paddleMoveX <= 0 && direction == LEFT || paddleMoveX >= (SCENE_WIDTH - PADDLE_WIDTH) && direction == RIGHT) {
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
        ballPosY = SCENE_HEIGHT / 2.0;
        ball = new Circle();
        ball.setRadius(BALL_RADIUS);
        ball.setFill(new ImagePattern(new Image("/images/ball.png")));

    }

    private void createPaddle() {
        rect = new Rectangle();
        rect.setWidth(PADDLE_WIDTH);
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
        checkCollisionWithPaddle();
        checkCollisionWithBlocks();
        handleBallDirection();
    }

    private void checkCollisionWithWalls() {
        if (ballPosY <= BALL_RADIUS || ballPosY + BALL_RADIUS >= SCENE_HEIGHT) {
            handleWallCollision();
        }

        if (ballPosX + BALL_RADIUS >= SCENE_WIDTH || ballPosX - BALL_RADIUS <= 0) {
            handleSideWallCollision();
        }
    }

    private void handleWallCollision() {
        if (ballPosY <= BALL_RADIUS) {
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
        if (ballPosX + BALL_RADIUS >= SCENE_WIDTH) {
            collideToRightWall = true;
        } else {
            collideToLeftWall = true;
        }
    }

    private void checkCollisionWithPaddle() {
        if (ballPosY + BALL_RADIUS >= paddleMoveY &&
                ballPosX + BALL_RADIUS >= paddleMoveX &&
                ballPosX - BALL_RADIUS <= paddleMoveX + PADDLE_WIDTH) {
            handlePaddleCollision();
        }
    }


    private void handlePaddleCollision() {
        resetCollideFlags();
        calculateBallVelocity();
        goDownBall = false;
        collideToBreakAndMoveToRight = ballPosX - centerBreakX > 0;
        SoundManager.paddleBounceSound();
    }

    private void calculateBallVelocity() {
        double relation = (ballPosX - centerBreakX) / ((double) PADDLE_WIDTH / 2);
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
        Platform.runLater(() -> {
            scoreLabel.setText("Score: " + score);

            // Update this line to get the heartLabel from UIManager
            Label heartLabelFromUIManager = uiManager.getHeartLabel();
            heartLabelFromUIManager.setText("Heart : " + heart);

            rect.setX(paddleMoveX);
            rect.setY(paddleMoveY);
            ball.setCenterX(ballPosX);
            ball.setCenterY(ballPosY);
            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
        });


        if (ballPosY + BALL_RADIUS >= Block.getPaddingTop() && ballPosY - BALL_RADIUS <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            for (final Block block : blocks) {
                int hitCode = block.checkHitToBlock(ballPosX, ballPosY, BALL_RADIUS);
                if (hitCode != Block.NO_HIT) {
                    score += 1;

                    new Score().show(block.x, block.y, 1, this);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    resetCollideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(() -> root.getChildren().add(choco.choco));
                        chocos.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("/images/goldBall.png")));
                        System.out.println("gold ball");
                        isGoldStatus = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        collideToRightBlock = true;
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        collideToBottomBlock = true;
                    } else if (hitCode == Block.HIT_LEFT) {
                        collideToLeftBlock = true;
                    } else if (hitCode == Block.HIT_TOP) {
                        collideToTopBlock = true;
                    }
                }
            }
        }
    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();
        if (time - goldTime > 5000) {
            ball.setFill(new ImagePattern(new Image("/images/ball.png")));
            isGoldStatus = false;
        }

        for (Bonus choco : chocos) {
            if (choco.y > SCENE_HEIGHT || choco.taken) {
                continue;
            }
            if (choco.y >= paddleMoveY && choco.y <= paddleMoveY + PADDLE_HEIGHT && choco.x >= paddleMoveX && choco.x <= paddleMoveX + PADDLE_WIDTH) {
                SoundManager.collectBonus();
                choco.taken = true;
                choco.choco.setVisible(false);

                Random rand = new Random();
                int effect = rand.nextInt(3); // Randomly choose an effect
                switch (effect) {
                    case 0: // Change paddle width
                        PADDLE_WIDTH = rand.nextBoolean() ? PADDLE_WIDTH + 10 : Math.max(PADDLE_WIDTH + 10, 30);
                        System.out.println("Paddle width changed!");
                        break;
                    case 1: // Bonus 3 points
                        score += 3;
                        System.out.println("Bonus 3 points!");
                        break;
                    case 2: // Gold ball
                        goldTime = time;
                        ball.setFill(new ImagePattern(new Image("/images/goldBall.png")));
                        isGoldStatus = true;
                        System.out.println("Gold ball activated!");
                        break;
                }

                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }
    }
    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
