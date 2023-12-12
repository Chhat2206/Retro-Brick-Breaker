package com.brickbreakergame.menus;

import com.brickbreakergame.GameController;
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
    private final Stage primaryStage;
    private final Main mainGame;
    private final AnimationManager animationManager = new AnimationManager();

    /**
     * Constructs a MainMenu instance with a reference to the primary stage of the application and the main game.
     * Initializes the MainMenu with the necessary context to display menu options and handle interactions.
     *
     * @param primaryStage The primary stage of the application where the main menu will be displayed.
     * @param mainGame     A reference to the main game application, used to initiate new games or load existing ones.
     */
    public MainMenu(Stage primaryStage, Main mainGame) {
        this.primaryStage = primaryStage;
        this.mainGame = mainGame;
    }

    /**
     * Displays the main menu of the game, including options to start a new game, load a saved game, or exit.
     * Sets up the UI elements for the menu, such as buttons and the logo, and configures their actions.
     * Also applies the necessary styles and layout for the menu.
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
        });

        Button loadGameButton = createButton("/images/Main Menu/loadGame.png", e -> {
            System.out.println("\u001B[34m" + "Loading Game" + "\u001B[0m"); // Blue text
            SoundManager.buttonClickSound();
            GameController gameController = new GameController();
            gameController.loadGame(mainGame, primaryStage);
        });

        Button exitButton = createButton("/images/Main Menu/quitGame.png", e -> Platform.exit());

        menuOptions.getChildren().addAll(startNewGameButton, loadGameButton, exitButton);
        root.getChildren().add(menuOptions);

        Scene scene = new Scene(root, 500, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/mainMenu.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates and returns an ImageView for the game's logo.
     * Configures the dimensions and other properties of the logo image to be displayed in the main menu.
     *
     * @return An ImageView containing the game's logo.
     */
    private ImageView createLogoView() {
        Image logo = loadImage("/images/Main Menu/logo.png");
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        return imageView;
    }

    /**
     * Creates a customized start, load, and exit button with an image, specified dimensions, and an associated action.
     * Sets the style of the button and configures its on-action behavior, including playing a sound and executing the given action.
     *
     * @param imagePath The path to the image file for the button.
     * @param action    A Consumer<Void> action that defines what occurs when the button is clicked.
     * @return A Button object with the specified image and action behavior.
     */
    private Button createButton(String imagePath, Consumer<Void> action) {
        Image image = loadImage(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(230);
        imageView.setFitHeight(90);

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        button.setOnAction(e -> {
            SoundManager.startRandomBackgroundMusic();
            action.accept(null);
        });
        return button;
    }

    /**
     * Loads an image from a specified file path for the background if the initial fails.
     * Handles exceptions by returning a placeholder image if the specified image cannot be loaded.
     *
     * @param path The file path of the image to be loaded.
     * @return The loaded Image object, or a placeholder image in case of an error.
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
     * Initiates the process of starting a new game.
     * Handles the transition from the main menu to the game scene and creates a new instance of the Main game class.
     * Verifies the primary stage's scene is not null before proceeding to start the game.
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
