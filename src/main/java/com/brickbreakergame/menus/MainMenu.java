package com.brickbreakergame.menus;

import com.brickbreakergame.GameState;
import com.brickbreakergame.Main;
import com.brickbreakergame.managers.AnimationManager;
import com.brickbreakergame.managers.SoundManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * The MainMenu class represents the main menu of the game.
 * It displays options to start a new game, load a saved game, or exit the application.
 */
public class MainMenu {

    /**
     * The primary stage where the main menu is displayed.
     */
    private final Stage primaryStage;
    /**
     * Reference to the main game application.
     */
    private final Main mainGame;

    private final AnimationManager animationManager = new AnimationManager();

    /**
     * Creates a new MainMenu instance.
     *
     * @param primaryStage The primary stage of the application.
     * @param mainGame     A reference to the main game application.
     */
    public MainMenu(Stage primaryStage, Main mainGame) {
        this.primaryStage = primaryStage;
        this.mainGame = mainGame;
    }

    /**
     * Displays the main menu including options to start a new game, load a saved game, or exit the application.
     */
    public void display() {
        Pane root = new Pane();
        VBox menuOptions = new VBox(10);
        menuOptions.setTranslateX(135);
        menuOptions.setTranslateY(250);
        primaryStage.setTitle("The Incredible Block Breaker Menu");
        primaryStage.getIcons().add(new Image("/images/Main Menu/favicon.png"));
        primaryStage.setResizable(false);
        root.getStyleClass().add("background-pane");
       SoundManager.soundMenu();

        // Load and add the logo
        ImageView logoView = createLogoView();
        logoView.setTranslateX(155);
        logoView.setTranslateY(50);
        root.getChildren().add(logoView);

        Button startNewGameButton = createButton("/images/Main Menu/newGame.png", e -> {
            System.out.println("\u001B[32m" + "Starting New Game" + "\u001B[0m"); // Green text
            startNewGame();
        }, 230, 90);

        Button loadGameButton = createButton("/images/Main Menu/loadGame.png", e -> {
            System.out.println("\u001B[34m" + "Loading Game" + "\u001B[0m"); // Blue text
            SoundManager.buttonClickSound();
            GameState gameState = new GameState();
            gameState.loadGame(mainGame, primaryStage);
        }, 230, 90);

        Button exitButton = createButton("/images/Main Menu/quitGame.png", e -> Platform.exit(), 230, 90);

        menuOptions.getChildren().addAll(startNewGameButton, loadGameButton, exitButton);
        root.getChildren().add(menuOptions);

        Scene scene = new Scene(root, 500, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/mainMenu.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private ImageView createLogoView() {
        Image logo = loadImage("/images/Main Menu/logo.png");
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(200); // Set the logo size
        imageView.setFitHeight(150);
        return imageView;
    }

    /**
     * Creates a button with an ImageView, allowing customization of the button's appearance.
     *
     * @param imagePath The path to the button's image.
     * @param action    The action to be executed when the button is clicked.
     * @param width     The width of the button.
     * @param height    The height of the button.
     * @return A customized button with the specified image and action.
     */
    private Button createButton(String imagePath, Consumer<Void> action, int width, int height) {
        Image image = loadImage(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        button.setOnAction(e -> {
            SoundManager.startRandomBackgroundMusic();

            action.accept(null);
        });
        return button;
    }

    /**
     * Loads an image from the specified path.
     *
     * @param path The path to the image to be loaded.
     * @return The loaded image, or a placeholder image if loading fails.
     */
    private Image loadImage(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            System.err.println("Error loading image: " + path);
            return new Image("path/to/placeholder.png");
        }
    }

    /**
     * Initiates the transition to a new game by creating a new Main game instance.
     * This method is called when the "Start New Game" button is clicked.
     */
    private void startNewGame() {
        if (primaryStage.getScene() == null) {
            System.err.println("Primary stage's scene is null. Cannot start new game.");
            return;
        }

        animationManager.startTransition(primaryStage, () -> {
            Main game = new Main();
            game.newGame(primaryStage);
        });
    }

}
