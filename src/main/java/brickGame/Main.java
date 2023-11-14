    package brickGame;

    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.event.ActionEvent;
    import javafx.event.EventHandler;
    import javafx.scene.Cursor;
    import javafx.scene.Scene;
    import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.image.Image;
    import javafx.scene.image.ImageView;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.Pane;
    import javafx.scene.media.Media;
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
        private MediaPlayer mediaPlayer;
        private int level = 0;
        private double Paddle_Move_X = 0.0f;
        private double centerBreakX;
        private double Paddle_Move_Y = 640.0f;
        private final int paddleWidth = 130;
        private final int paddleHeight = 30;
        private final int halfBreakWidth = paddleWidth / 2;
        private final int sceneWidth = 500;
        private final int sceneHeight = 700;
        private static final int LEFT  = 1;
        private static final int RIGHT = 2;
        private Circle ball;
        private double xBall;
        private double yBall;
        private boolean isGoldStatus = false;
        private boolean isExistHeartBlock = false;
        private Rectangle rect;
        private final int ballRadius = 10;
        private int destroyedBlockCount = 0;
        private double v = 1.000;
        private int  heart    = 3;
        private int  score    = 0;
        private long time     = 0;
        private long hitTime  = 0;
        private long goldTime = 0;
        private GameEngine engine;
        public static String savePath    = "./save/save.mdds";
        public static String savePathDir = "./save/";
        private final ArrayList<Block> blocks = new ArrayList<Block>();
        private final ArrayList<Bonus> chocos = new ArrayList<Bonus>();
        private final Color[] colors = new Color[]{
                Color.MAGENTA,
                Color.RED,
                Color.GOLD,
                Color.CORAL,
                Color.AQUA,
                Color.VIOLET,
                Color.GREENYELLOW,
                Color.ORANGE,
                Color.PINK,
                Color.SLATEGREY,
                Color.YELLOW,
                Color.TOMATO,
                Color.TAN,
        };
        public  Pane root;
        private Label scoreLabel;
        private Label heartLabel;
        private boolean loadFromSave = false;
        Stage  primaryStage;
        Button load = null;
        Button newGame = null;
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
                if (level == 10) {
                    new Score().showWin(this);
                    return;
                }

                initializeBall();
                createPaddle();
                setupGameBoard();

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


            if (!loadFromSave) {
                root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame);
                //root.getChildren().add(ball);

            } else {
                root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
            }

            // Error around here, creates index out of bound exception
            for (Block block : blocks) {
                root.getChildren().add(block.rect);
    //            for (int i = 0; i < blocks.size(); i++) {
    //                Block currentblock = blocks.get(i);
    //                root.getChildren().add(currentblock.rect);
            }
            Scene scene = new Scene(root, sceneWidth, sceneHeight);
            scene.getStylesheets().add("style.css");
            scene.setOnKeyPressed(this);
            // When in full screen, the main-menu stage takes you out of full screen.
    //       primaryStage.setFullScreen(true);

            primaryStage.setTitle("The Incredible Block Breaker Game");
            primaryStage.getIcons().add(new Image("/images/favicon.png"));
            primaryStage.setScene(scene);
            primaryStage.show();

            // Works on displaying the temp 'main menu' created
            // MainMenu.display(primaryStage);

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
                        buttonClickSound();
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
                        startBackgroundMusic();

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
            for (int i = 0; i < 4; i++) {
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
                    move(LEFT);
                    break;
                case D:
                case RIGHT:
                    move(RIGHT);
                    break;
                case DOWN:
                case S:
                    setPhysicsToBall();
                    break;
                case K:
                    saveGame();
                    break;
                case L:
                    loadGame();
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
        private void move(final int direction) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int INITIAL_SLEEP_TIME = 4;
                    for (int i = 0; i < 30; i++) {
                        if (Paddle_Move_X == (sceneWidth - paddleWidth) && direction == RIGHT) {
                            return;
                        }
                        if (Paddle_Move_X == 0 && direction == LEFT) {
                            return;
                        }
                        if (direction == RIGHT) {
                            Paddle_Move_X++;
                        } else {
                            Paddle_Move_X--;
                        }
                        centerBreakX = Paddle_Move_X + halfBreakWidth;

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
            xBall = sceneWidth / 2.0;
            yBall = sceneHeight / 2.0;
            ball = new Circle();
            ball.setRadius(ballRadius);
            ball.setFill(new ImagePattern(new Image("/images/Ball.png")));

    //        ball.setVisible(false);
        }

        private void createPaddle() {
            rect = new Rectangle();
            rect.setWidth(paddleWidth);
            rect.setHeight(paddleHeight);
            rect.setX(Paddle_Move_X);
            rect.setY(Paddle_Move_Y);
            ImagePattern pattern = new ImagePattern(new Image("block.jpg"));
            rect.setFill(pattern);
        }


        private boolean goDownBall                  = true;
        private boolean goRightBall                 = true;
        private boolean collideToBreak = false;
        private boolean collideToBreakAndMoveToRight = true;
        private boolean collideToRightWall = false;
        private boolean collideToLeftWall = false;
        private boolean collideToRightBlock = false;
        private boolean collideToBottomBlock = false;
        private boolean collideToLeftBlock = false;
        private boolean collideToTopBlock = false;
        private double vX = 1.000;


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
                xBall += vX;
            } else {
                xBall -= vX;
            }

            if (yBall <= ballRadius) {
                //vX = 1.000;
                resetCollideFlags();
                goDownBall = true;
                return;
            }
            if (yBall + ballRadius >= sceneHeight) {
                goDownBall = false;
                if (!isGoldStatus) {
                    //TODO gameover
                    heart--;
                    new Score().show((double) sceneWidth / 2, (double) sceneHeight / 2, -1, this);

                    if (heart == 0) {
                        new Score().showGameOver(this);
                        engine.stop();
                    }

                }
                //return;
            }

            if (yBall >= Paddle_Move_Y - ballRadius) {
                //System.out.println("Colide1");
                if (xBall >= Paddle_Move_X && xBall <= Paddle_Move_X + paddleWidth) {
                    hitTime = time;
                    resetCollideFlags();
                    collideToBreak = true;
                    goDownBall = false;

                    double relation = (xBall - centerBreakX) / ((double) paddleWidth / 2);

                    if (Math.abs(relation) <= 0.3) {
                        //vX = 0;
                        vX = Math.abs(relation);
                    } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                        vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                        //System.out.println("vX " + vX);
                    } else {
                        vX = (Math.abs(relation) * 2) + (level / 3.500);
                        //System.out.println("vX " + vX);
                    }

                    collideToBreakAndMoveToRight = xBall - centerBreakX > 0;
                    //System.out.println("Colide2");
                }
            }

            // Collision with right wall
            if (xBall + ballRadius >= sceneWidth) {
                resetCollideFlags();
                //vX = 1.000;
                collideToRightWall = true;
            }

            // Collision with left wall
            if (xBall - ballRadius <= 0) {
                resetCollideFlags();
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
            if (yBall + ballRadius >= Paddle_Move_Y && yBall - ballRadius <= Paddle_Move_Y + paddleHeight) {
                if (xBall + ballRadius >= Paddle_Move_X && xBall - ballRadius <= Paddle_Move_X + paddleWidth) {
                    paddleBounceSound();
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
                    File file = new File(savePath);
                    ObjectOutputStream outputStream = null;
                    try {
                        outputStream = new ObjectOutputStream(new FileOutputStream(file));

                        outputStream.writeInt(level);
                        outputStream.writeInt(score);
                        outputStream.writeInt(heart);
                        outputStream.writeInt(destroyedBlockCount);


                        outputStream.writeDouble(xBall);
                        outputStream.writeDouble(yBall);
                        outputStream.writeDouble(Paddle_Move_X);
                        outputStream.writeDouble(Paddle_Move_Y);
                        outputStream.writeDouble(centerBreakX);
                        outputStream.writeLong(time);
                        outputStream.writeLong(goldTime);
                        outputStream.writeDouble(vX);


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

                        new Score().showMessage("Game Saved", Main.this);


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
        private void startBackgroundMusic() {
            // Playing the background music
            String musicFile = "src/main/resources/Sound Effects/background-music-soft-piano.mp3";
            Media sound = new Media(new File(musicFile).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);

            // Set initial volume to 50%
            mediaPlayer.setVolume(0.37);

            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            mediaPlayer.play();
        }

        protected void pauseBackgroundMusic() {
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
            } }

        public void resumeBackgroundMusic() {
            if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play();
            }}

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
            Paddle_Move_X = gameState.xBreak;
            Paddle_Move_Y = gameState.yBreak;
            centerBreakX = gameState.centerBreakX;
            time = gameState.time;
            goldTime = gameState.goldTime;
            vX = gameState.vX;

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
                        vX = 1.000;
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
                vX = 1.000;
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
                    rect.setX(Paddle_Move_X);
                    rect.setY(Paddle_Move_Y);
                    ball.setCenterX(xBall);
                    ball.setCenterY(yBall);

                    for (Bonus choco : chocos) {
                        choco.choco.setY(choco.y);
                    }
                }
            });


            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
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
                            ball.setFill(new ImagePattern(new Image("goldball.png")));
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
                ball.setFill(new ImagePattern(new Image("/images/Ball.png")));
                isGoldStatus = false;
            }

            for (Bonus choco : chocos) {
                if (choco.y > sceneHeight || choco.taken) {
                    continue;
                }
                if (choco.y >= Paddle_Move_Y && choco.y <= Paddle_Move_Y + paddleHeight && choco.x >= Paddle_Move_X && choco.x <= Paddle_Move_X + paddleWidth) {
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

        private static void paddleBounceSound() {
            // Playing the background music
            String musicFile = "src/main/resources/Sound Effects/paddle-bounce.mp3";
            Media sound = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(1);
            mediaPlayer.setCycleCount(1);
            mediaPlayer.play();
        }

        private static void buttonClickSound() {
            String musicFile = "src/main/resources/Sound Effects/buttonClickSound.mp3"; // Path to your sound file
            Media sound = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(1); // Set the volume as needed
            mediaPlayer.setCycleCount(1); // Play once
            mediaPlayer.play();
        }

        public void makeHeartScore() {
            Image heartImage = new Image("/images/heart.png");
            ImageView heartImageView = new ImageView(heartImage);
            heartImageView.setFitHeight(20);
            heartImageView.setFitWidth(20);

            heartLabel = new Label("Heart: " + heart, heartImageView);
            heartLabel.getStyleClass().add("heart-label-gradient");
            heartLabel.setTranslateX(sceneWidth - 90);
        }

        public void makeBackgroundImage() {
            Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background-image-2.png")));
            ImageView backgroundView = new ImageView(backgroundImage);
            backgroundView.setFitWidth(sceneWidth);
            backgroundView.setFitHeight(sceneHeight);
            root.getChildren().add(backgroundView);

        }
    }
