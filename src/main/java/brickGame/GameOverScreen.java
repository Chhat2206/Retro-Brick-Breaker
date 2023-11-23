package brickGame;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class GameOverScreen {

    private static Stage gameOverStage;
    private static VBox vbox;

    public static void display(Main main, Stage primaryStage) {
        initializeGameOverStage();
        configureLayout();
        addElementsToLayout(main, primaryStage);

        Scene scene = new Scene(vbox, 400, 200);
        scene.getStylesheets().add("/css/gameOverScreen.css"); // Assuming a separate CSS file
        gameOverStage.setScene(scene);

        applyBlurEffect(primaryStage);

        fadeInScreen();

        gameOverStage.showAndWait();
    }

    private static void initializeGameOverStage() {
        gameOverStage = new Stage();
        gameOverStage.initModality(Modality.APPLICATION_MODAL);
        gameOverStage.initStyle(StageStyle.TRANSPARENT);
        gameOverStage.setTitle("Game Over");
    }

    private static void configureLayout() {
        vbox = new VBox(20);
        vbox.getStyleClass().add("game-over-layout");
    }

    private static void addElementsToLayout(Main main, Stage primaryStage) {
        Label gameOverLabel = new Label("Game Over");
        gameOverLabel.getStyleClass().add("game-over-label");

        Button restartButton = createButton("Restart", e -> {
            fadeOutScreen();
            main.restartGame();
            gameOverStage.close();
        });

        Button mainMenuButton = createButton("Main Menu", e -> {
            fadeOutScreen();
            MainMenu mainMenu = new MainMenu(primaryStage, main);
            mainMenu.display();
            gameOverStage.close();
        });

        vbox.getChildren().addAll(gameOverLabel, restartButton, mainMenuButton);
    }

    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    private static void applyBlurEffect(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

    private static void fadeInScreen() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), vbox);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static void fadeOutScreen() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), vbox);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.play();
    }
}
