    package brickGame;

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
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.Pane;
    import javafx.scene.media.MediaPlayer;
    import javafx.scene.paint.Color;
    import javafx.scene.paint.ImagePattern;
    import javafx.scene.shape.Circle;
    import javafx.scene.shape.Rectangle;
    import javafx.stage.Stage;


    import java.io.*;
    import java.util.ArrayList;
    import java.util.Objects;
    import java.util.Random;

    public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {
        // Constants
        private static final int LEFT  = 1;
        private static final int RIGHT = 2;
        private static final int PADDLE_WIDTH = 110;
        private static final int PADDLE_HEIGHT = 20;
        private static final int BALL_RADIUS = 10;
        private static final int SCENE_WIDTH = 500;
        private static final int SCENE_HEIGHT = 700;

        // Game State Variables
        private int level = 0;
        private int score = 0;
        private int heart = 3;
        private int destroyedBlockCount = 0;

        // Paddle Variables
        private double paddleMoveX = 0.0;
        private double paddleMoveY = 640.0f;
        private final int halfPaddleWidth = PADDLE_WIDTH / 2;
        private double centerBreakX;

        // Ball Variables
        private Circle ball;
        private double xBall;
        private double yBall;
         // Example renamed from vX

        // Game Mechanics Variables
        private boolean loadFromSave = false;
        private long time = 0;
        private long hitTime = 0;
        private long goldTime = 0;

        // UI Components
        Pane root;
        private Label scoreLabel;
        private Label heartLabel;
        private Button load;
        private Button newGame;
        protected Stage primaryStage;

        // Game Engine and Media
        private GameEngine engine;
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

        // Game Objects
        private Rectangle rect;
        private final ArrayList<Block> blocks = new ArrayList<>();
        private final ArrayList<Bonus> chocos = new ArrayList<>();
        private final Color[] colors = new Color[]{
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
        private boolean isExistHeartBlock = false;

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
                setupGameBoard();
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
            scene.getStylesheets().add("/css/main.css");
            scene.setOnKeyPressed(this);
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

        private void setupGameBoard() {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < level + 1; j++) {
                    int r = new Random().nextInt(500);
                    if (r % 5 == 0) {
                        continue;
                    }
                    int type;
                    if (r % 10 == 1) {
                        type = Block.BLOCK_CHOCO;
                    } else if (r % 10 == 2) {
                        if (!isExistHeartBlock) {
                            type = Block.BLOCK_HEART;
                            isExistHeartBlock = true;
                        } else {
                            type = Block.BLOCK_NORMAL;
                        }
                    } else if (r % 10 == 3) {
                        type = Block.BLOCK_STAR;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                    blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                    //System.out.println("colors " + r % (colors.length));
                }
            }
        }

        @Override
        public void handle(KeyEvent event) {
            switch (event.getCode()) {
                case LEFT:
                case A:
                    movePaddleX(LEFT);
                    break;
                case D:
                case RIGHT:
                    movePaddleX(RIGHT);
                    break;
                case ESCAPE:
                    PauseMenu.display(this, getGameEngine(), primaryStage);
                    event.consume();
                    break;
                case M:
                    toggleMute();
                    break;
            }
        }

        // Not properly synchronizing to variable
        private void movePaddleX(final int direction) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int INITIAL_SLEEP_TIME = 4;
                    for (int i = 0; i < 30; i++) {
                        if (paddleMoveX == (SCENE_WIDTH - PADDLE_WIDTH) && direction == RIGHT) {
                            return;
                        }
                        if (paddleMoveX == 0 && direction == LEFT) {
                            return;
                        }
                        if (direction == RIGHT) {
                            paddleMoveX++;
                        } else {
                            paddleMoveX--;
                        }
                        centerBreakX = paddleMoveX + halfPaddleWidth;

                        // Controlling frame rate. Code looks awful so will fix
                        try {
                            Thread.sleep(INITIAL_SLEEP_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (i >= 20) {
                            INITIAL_SLEEP_TIME = i;
                        }
                    }
                }
            }).start();
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
            //v = ((time - hitTime) / 1000.000) + 1.000;

            double vY = 1.000;
            if (goDownBall) {
                yBall += vY;
            } else {
                yBall -= vY;
            }

            if (goRightBall) {
                xBall += ballVelocityX;
            } else {
                xBall -= ballVelocityX;
            }

            if (yBall <= BALL_RADIUS) {
                //vX = 1.000;
                resetCollideFlags();
                SoundManager.paddleBounceSound();
                goDownBall = true;
                return;
            }
            if (yBall + BALL_RADIUS >= SCENE_HEIGHT) {
                goDownBall = false;
                SoundManager.ballHitFloor();
                if (!isGoldStatus) {
                    //TODO gameover
                    heart--;
                    new Score().show((double) SCENE_WIDTH / 2, (double) SCENE_HEIGHT / 2, -1, this);

                    if (heart == 0) {
                        new Score().showGameOver(this);
                        engine.stop();
                    }

                }
                //return;
            }

            if (yBall >= paddleMoveY - BALL_RADIUS) {
                //System.out.println("Colide1");
                if (xBall >= paddleMoveX && xBall <= paddleMoveX + PADDLE_WIDTH) {
                    hitTime = time;
                    resetCollideFlags();
                    collideToBreak = true;
                    goDownBall = false;

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
                    //System.out.println("Colide2");
                }
            }

            // Collision with right wall
            if (xBall + BALL_RADIUS >= SCENE_WIDTH) {
                resetCollideFlags();
                SoundManager.paddleBounceSound();
                //vX = 1.000;
                collideToRightWall = true;
            }

            // Collision with left wall
            if (xBall - BALL_RADIUS <= 0) {
                resetCollideFlags();
                SoundManager.paddleBounceSound();
                //vX = 1.000;
                collideToLeftWall = true;
            }

            if (collideToBreak) {
                if (collideToBreakAndMoveToRight) {
                    goRightBall = true;
                } else {
                    goRightBall = false;
                }
            }

            //Wall Collide

            if (collideToRightWall) {
                goRightBall = false;
            }

            if (collideToLeftWall) {
                goRightBall = true;
            }

            //Block Collide

            if (collideToRightBlock) {
                goRightBall = true;
            }

            if (collideToLeftBlock) {
                goRightBall = true;
            }

            if (collideToTopBlock) {
                goDownBall = false;
            }

            if (collideToBottomBlock) {
                goDownBall = true;
            }

            // Collision with paddle
            if (yBall + BALL_RADIUS >= paddleMoveY && yBall - BALL_RADIUS <= paddleMoveY + PADDLE_HEIGHT) {
                if (xBall + BALL_RADIUS >= paddleMoveX && xBall - BALL_RADIUS <= paddleMoveX + PADDLE_WIDTH) {
                    SoundManager.paddleBounceSound();
                }
            }
        }


        private void checkDestroyedCount() {
            if (destroyedBlockCount == blocks.size()) {
                //TODO win level todo...
                //System.out.println("You Win");

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
                        hitTime = 0;
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
                hitTime = 0;
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
                        //System.out.println("size is " + blocks.size());
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
    //                        root.getStyleClass().add("goldRoot");
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

                    //TODO hit to break and some work here....
                    //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
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
                    choco.taken = true;
                    choco.choco.setVisible(false);
                    score += 3;
                    new Score().show(choco.x, choco.y, 3, this);
                }
                choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
            }

            //System.out.println("time is:" + time + " goldTime is " + goldTime);

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

        public DropShadow dropShadow() {
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(1.0);
            dropShadow.setOffsetY(1.0);
            dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
            return dropShadow;
        }
    }
