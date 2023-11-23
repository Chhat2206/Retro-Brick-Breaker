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

public class YouWinScreen {

    private static Stage youWinStage;
    private static VBox vbox;

    public static void display(Main main, Stage primaryStage) {
        initializeYouWinStage();
        configureLayout();
        addElementsToLayout(main, primaryStage);

        Scene winScene = new Scene(vbox, 400, 200);
        winScene.getStylesheets().add("/css/youWinScreen.css");
        youWinStage.setScene(winScene);

        initializeBlur(primaryStage);

        fadeInScreen();

        youWinStage.showAndWait();
    }

    private static void initializeYouWinStage() {
        youWinStage = new Stage();
        youWinStage.initModality(Modality.APPLICATION_MODAL);
        youWinStage.initStyle(StageStyle.TRANSPARENT);
        youWinStage.setTitle("You Win!");
    }

    private static void configureLayout() {
        vbox = new VBox(20);
        vbox.getStyleClass().add("you-win-layout");
    }

    private static void addElementsToLayout(Main main, Stage primaryStage) {
        Label winLabel = new Label("Congratulations! You Win!");
        winLabel.getStyleClass().add("win-label");

        Button restartButton = createButton("Restart", e -> {
            fadeOutScreen();
            main.restartGame();
            youWinStage.close();
        });

        Button returnButton = createButton("Return to Main Menu", e -> {
            fadeOutScreen();
            MainMenu mainMenu = new MainMenu(primaryStage, main);
            mainMenu.display();
            youWinStage.close();
        });

        vbox.getChildren().addAll(winLabel, restartButton, returnButton);
    }

    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        return button;
    }

    private static void initializeBlur(Stage primaryStage) {
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
