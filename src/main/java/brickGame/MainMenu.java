package brickGame;

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

        SoundManager.mainMenuMusic();


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
        // Logic to start a new game
        Main game = new Main();
        game.newGame(primaryStage);
    }

}
