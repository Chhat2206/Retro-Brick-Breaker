package brickGame;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;
import java.util.function.Consumer;

public class MainMenu {

    private Stage primaryStage;
    private Main mainGame; // Reference to Main

    public MainMenu(Stage primaryStage, Main mainGame) {
        this.primaryStage = primaryStage;
        this.mainGame = mainGame; // Store the reference
    }

    public void display() {
        Pane root = new Pane();
        VBox menuOptions = new VBox(10);
        menuOptions.setTranslateX(135);
        menuOptions.setTranslateY(250);

        root.setStyle("-fx-background-image: url('/images/Main%20Menu/backgroundImage.png');" +
                "-fx-background-size: cover;");

//        SoundManager.mainMenuMusic();


        // Load and add the logo
        ImageView logoView = createLogoView("/images/Main Menu/logo.png");
        logoView.setTranslateX(155);
        logoView.setTranslateY(50);
        root.getChildren().add(logoView);

        // Create buttons with increased size
        Button startNewGameButton = createButton("/images/Main Menu/newGame.png", e -> startNewGame(), 230, 90);
        Button loadGameButton = createButton("/images/Main Menu/loadGame.png", e -> mainGame.loadGame(primaryStage), 230, 90);
        Button exitButton = createButton("/images/Main Menu/quitGame.png", e -> Platform.exit(), 230, 90);

        menuOptions.getChildren().addAll(startNewGameButton, loadGameButton, exitButton);
        root.getChildren().add(menuOptions);

        Scene scene = new Scene(root, 500, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/mainMenu.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private ImageView createLogoView(String imagePath) {
        Image logo = loadImage(imagePath);
        ImageView imageView = new ImageView(logo);
        imageView.setFitWidth(200); // Set the logo size
        imageView.setFitHeight(150);
        return imageView;
    }

    private Button createButton(String imagePath, Consumer<Void> action, int width, int height) {
        Image image = loadImage(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        button.setOnAction(e -> action.accept(null));
        return button;
    }

    private Image loadImage(String path) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (NullPointerException e) {
            System.err.println("Error loading image: " + path);
            return new Image("path/to/placeholder.png");
        }
    }

    private void startNewGame() {
        // Create a translate transition to move the current scene out
        TranslateTransition translateOut = new TranslateTransition(Duration.seconds(.2), primaryStage.getScene().getRoot());
        translateOut.setFromX(0);
        translateOut.setToX(-primaryStage.getWidth()); // Move to the left

        // When the translation out is complete, start the new game
        translateOut.setOnFinished(e -> {
            Main game = new Main();

            // Assuming newGame creates and sets up the scene for the game
            game.newGame(primaryStage);

            // The scene should now be set up in the newGame method, so we can retrieve it
            Scene newGameScene = primaryStage.getScene();

            // Create a translate transition for the new game scene to slide in
            TranslateTransition translateIn = new TranslateTransition(Duration.seconds(.1), newGameScene.getRoot());
            translateIn.setFromX(primaryStage.getWidth()); // Start from the right
            translateIn.setToX(0); // Move to the original position
            translateIn.play();
        });

        // Start the translate out transition
        translateOut.play();
    }





}
