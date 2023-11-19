    package brickGame;

    import javafx.animation.AnimationTimer;
    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.event.ActionEvent;
    import javafx.event.EventHandler;
    import javafx.scene.Cursor;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.effect.DropShadow;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyCode;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.Pane;
    import javafx.scene.media.MediaPlayer;
    import javafx.scene.paint.Color;
    import javafx.scene.paint.ImagePattern;
    import javafx.scene.shape.Circle;
    import javafx.scene.shape.Rectangle;
    import javafx.stage.Stage;
    import java.util.Random;


    import java.io.*;
    import java.util.ArrayList;
    import java.util.Objects;
    import java.util.Random;

    public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
        // Constants
        private static final int LEFT  = 1;
        private static final int RIGHT = 2;
        private static final int PADDLE_WIDTH = 90;
        private static final int PADDLE_HEIGHT = 10;
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
        private double paddleMoveX = 0.0;
        private double paddleMoveY = 640.0f;
        private final int halfPaddleWidth = PADDLE_WIDTH / 2;
        private double centerBreakX;
        private boolean leftKeyPressed = false;
        private boolean rightKeyPressed = false;
        private AnimationTimer paddleMoveTimer;

        // Ball Variables
        private Circle ball;
        private double xBall;
        private double yBall;
         // Example renamed from vX

        // Game Mechanics Variables
        private boolean loadFromSave = false;
        private long time = 0;
        private long goldTime = 0;

        // UI Components
        Pane root;
        private Label scoreLabel;
        private Label heartLabel;
        private Button load;
        private Button newGame;
        protected Stage primaryStage;

        // Game Engine, GameBoardManager and Media
        private GameEngine engine;
        private GameBoardManager gameBoardManager;
        private MediaPlayer mediaPlayer;

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
        private double ballVelocityY = 1.000;

        // Game Objects
        private Rectangle rect;
        protected final ArrayList<Block> blocks = new ArrayList<>();
        private final ArrayList<Bonus> chocos = new ArrayList<>();
        protected final Color[] colors = new Color[]{
                Color.MAGENTA, Color.RED, Color.GOLD, Color.CORAL,
                Color.AQUA, Color.VIOLET, Color.GREENYELLOW,
                Color.ORANGE, Color.PINK, Color.SLATEGREY,
                Color.YELLOW, Color.TOMATO, Color.TAN,
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

        @Override
        public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;

            if (!loadFromSave) {
                level++;
                if (level >1){
                    new Score().showMessage("Level Up :)", this);
                }
                if (level == 3) {
                    new Score().showWin(this);
                    return;
                }

                initializeBall();
                createPaddle();
                gameBoardManager = new GameBoardManager(this);
                gameBoardManager.setupGameBoard();
                primaryStage.setResizable(false);

                load = new Button("Load Game");
                newGame = new Button("Start New Game");
                load.setTranslateX(220);
                load.setTranslateY(300);
                newGame.setTranslateX(220);
                newGame.setTranslateY(340);
            }

            root = new Pane();
            scoreLabel = new Label("Score: " + score);
            Label levelLabel = new Label("Level: " + level);
            levelLabel.setTranslateY(20);

            makeHeartScore();
            makeBackgroundImage();

            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
            if (!loadFromSave) { root.getChildren().addAll(newGame); }

            // Error around here, creates index out of bound exception
            for (Block block : blocks) {
                root.getChildren().add(block.rect);
    //            for (int i = 0; i < blocks.size(); i++) {
    //                Block currentblock = blocks.get(i);
    //                root.getChildren().add(currentblock.rect);
            }
            Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
            scene.setOnKeyPressed(this); // Paddle Movement
            scene.setOnKeyReleased(this);
            scene.getStylesheets().add("/css/main.css");
            primaryStage.setTitle("The Incredible Block Breaker Game");
            primaryStage.getIcons().add(new Image("/images/favicon.png"));
            primaryStage.setScene(scene);
            primaryStage.show();

            if (!loadFromSave) {
                if (level > 1 && level < 18) {
                    load.setVisible(false);
                    newGame.setVisible(false);
                    engine = new GameEngine();
                    engine.setOnAction(this);
                    engine.setFps(120);
                    engine.start();
                }

                load.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        SoundManager.buttonClickSound();
                        loadGame();

                        load.setVisible(false);
                        newGame.setVisible(false);
                    }
                });

                newGame.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        primaryStage.getScene().setCursor(Cursor.NONE);
                        engine = new GameEngine();
                        engine.setOnAction(Main.this);
                        engine.setFps(120);
                        engine.start();
                        SoundManager.startBackgroundMusic("src/main/resources/Sound Effects/Background Music/backgroundMusicSoftPiano.mp3");

                        load.setVisible(false);
                        newGame.setVisible(false);
                    }
                });
            } else {
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
                loadFromSave = false;
            }
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
                case LEFT:
                case A:
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

        // Not properly synchronizing to variable
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
            xBall = SCENE_WIDTH / 2.0;
            yBall = SCENE_HEIGHT / 2.0;
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
            if (yBall <= BALL_RADIUS || yBall + BALL_RADIUS >= SCENE_HEIGHT) {
                handleWallCollision();
            }

            if (xBall + BALL_RADIUS >= SCENE_WIDTH || xBall - BALL_RADIUS <= 0) {
                handleSideWallCollision();
            }
        }

        private void handleWallCollision() {
            if (yBall <= BALL_RADIUS) {
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
            goDownBall = false;
            SoundManager.ballHitFloor();
            if (!isGoldStatus) {
                handleGameOver();
            }
        }

        private void handleGameOver() {
            // Game over logic
            heart--;
            new Score().show((double) SCENE_WIDTH / 2, (double) SCENE_HEIGHT / 2, -1, this);

            if (heart == 0) {
                new Score().showGameOver(this);
                engine.stop();
            }
        }

        private void handleSideWallCollision() {
            SoundManager.paddleBounceSound();
            resetCollideFlags();
            if (xBall + BALL_RADIUS >= SCENE_WIDTH) {
                collideToRightWall = true;
            } else {
                collideToLeftWall = true;
            }
        }

        private void checkCollisionWithPaddle() {
            // Collision logic with paddle
            if (yBall >= paddleMoveY - BALL_RADIUS &&
                    xBall >= paddleMoveX && xBall <= paddleMoveX + PADDLE_WIDTH) {
                handlePaddleCollision();

            }
        }

        private void handlePaddleCollision() {
            // Handle collision with paddle
            resetCollideFlags();
            calculateBallVelocity();
            goDownBall = false;
            collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
            SoundManager.paddleBounceSound();
        }

        private void calculateBallVelocity() {
            // Logic to calculate ball velocity
            double relation = (xBall - centerBreakX) / ((double) PADDLE_WIDTH / 2);
            if (Math.abs(relation) <= 0.3) {
                        //vX = 0;
                        ballVelocityX = Math.abs(relation);
                    } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                        ballVelocityX = (Math.abs(relation) * 1.5) + (level / 3.500);
                        //System.out.println("vX " + vX);
                    } else {
                        ballVelocityX = (Math.abs(relation) * 2) + (level / 3.500);
                        //System.out.println("vX " + vX);
                    }

                    collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
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
            xBall += goRightBall ? ballVelocityX : -ballVelocityX;
            yBall += goDownBall ? ballVelocityY : -ballVelocityY;
        }

        private void checkDestroyedCount() {
            if (destroyedBlockCount == blocks.size()) {
                nextLevel();
            }
        }

        protected void saveGame() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new File(savePathDir).mkdirs();
                    File file = new File(SAVE_PATH);
                    ObjectOutputStream outputStream = null;
                    try {
                        outputStream = new ObjectOutputStream(new FileOutputStream(file));

                        outputStream.writeInt(level);
                        outputStream.writeInt(score);
                        outputStream.writeInt(heart);
                        outputStream.writeInt(destroyedBlockCount);


                        outputStream.writeDouble(xBall);
                        outputStream.writeDouble(yBall);
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

                        ArrayList<BlockSerializable> blockSerializable = new ArrayList<BlockSerializable>();
                        for (Block block : blocks) {
                            if (block.isDestroyed) {
                                continue;
                            }
                            blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                        }
                        outputStream.writeObject(blockSerializable);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();

        }

        private void toggleMute() {
            if (mediaPlayer != null) {
                mediaPlayer.setMute(!mediaPlayer.isMute());
            }
        }

        protected void loadGame() {

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
            xBall = gameState.xBall;
            yBall = gameState.yBall;
            paddleMoveX = gameState.xBreak;
            paddleMoveY = gameState.yBreak;
            centerBreakX = gameState.centerBreakX;
            time = gameState.time;
            goldTime = gameState.goldTime;
            ballVelocityX = gameState.vX;

            blocks.clear();
            chocos.clear();

            for (BlockSerializable ser : gameState.blocks) {
                int r = new Random().nextInt(200);
                blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
            }


            try {
                loadFromSave = true;
                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        private void nextLevel() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
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
                        start(primaryStage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onUpdate() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    scoreLabel.setText("Score: " + score);
                    heartLabel.setText("Heart : " + heart);
                    rect.setX(paddleMoveX);
                    rect.setY(paddleMoveY);
                    ball.setCenterX(xBall);
                    ball.setCenterY(yBall);
                    for (Bonus choco : chocos) {
                        choco.choco.setY(choco.y);
                    }
                }
            });


            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, BALL_RADIUS);
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
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    root.getChildren().add(choco.choco);
                                }
                            });
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
                    System.out.println("You Got it and +3 score for you");
                    SoundManager.collectBonus();
                    choco.taken = true;
                    choco.choco.setVisible(false);
                    score += 3;
                    new Score().show(choco.x, choco.y, 3, this);
                }
                choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
            }
        }
        @Override
        public void onTime(long time) {
            this.time = time;
        }

        public void makeHeartScore() {
            Image heartImage = new Image("/images/heart.png");
            ImageView heartImageView = new ImageView(heartImage);
            heartImageView.setFitHeight(20);
            heartImageView.setFitWidth(20);

            heartLabel = new Label("Heart: " + heart, heartImageView);
            heartLabel.getStyleClass().add("heart-label-gradient");
            heartLabel.setTranslateX(SCENE_WIDTH - 90);
        }

        public void makeBackgroundImage() {
            Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Background Images/background-image-4.png")));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(SCENE_WIDTH);
            backgroundView.setFitHeight(SCENE_HEIGHT);
            root.getChildren().add(backgroundView);

        }
    }
