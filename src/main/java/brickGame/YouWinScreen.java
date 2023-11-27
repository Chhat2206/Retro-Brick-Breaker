package brickGame;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class YouWinScreen {

    private static Stage youWinStage;
    private static VBox youWinLayout;

    public static void display(Main main, Stage primaryStage) {
        initializeYouWinStage();
        configureYouWinLayout();
        addElementsToLayout(main, primaryStage);

        Scene youWinScene = new Scene(youWinLayout, 250, 400);
        youWinScene.setFill(Color.TRANSPARENT);
        youWinScene.getStylesheets().addAll("/css/youWinScreen.css");
        youWinStage.setScene(youWinScene);

        positionYouWinMenuOverGame(primaryStage);
        initializeBlur(primaryStage);

        fadeInMenu();

        youWinStage.showAndWait();
    }

    private static void initializeYouWinStage() {
        youWinStage = new Stage();
        youWinStage.initModality(Modality.APPLICATION_MODAL);
        youWinStage.initStyle(StageStyle.TRANSPARENT);
        youWinStage.setTitle("You Win");
    }

    private static void configureYouWinLayout() {
        youWinLayout = new VBox(20);
        youWinLayout.setAlignment(Pos.CENTER);
        youWinLayout.getStyleClass().add("you-win-layout");
    }

    private static void addElementsToLayout(Main main, Stage primaryStage) {
        // Load the "You Win" image
        Image youWinImage = new Image("/images/Screens/youWin.png");
        ImageView youWinImageView = new ImageView(youWinImage);
        youWinImageView.setFitWidth(150);
        youWinImageView.setFitHeight(140);

        int score = main.score;

        Label scoreLabel = new Label("Score: " + score);
        Button restartButton = createButton("Play Again", e -> {
            fadeOutMenu();
            main.restartGame();
            youWinStage.close();
        });

        Button returnButton = createButton("Return to Main Menu", e -> {
            fadeOutMenu();
            MainMenu mainMenu = new MainMenu(primaryStage, main);
            mainMenu.display();
            youWinStage.close();
        });

        youWinLayout.getChildren().addAll(youWinImageView, scoreLabel, restartButton, returnButton);
    }

    private static Button createButton(String text, EventHandler<ActionEvent> action) {
        Button button = new Button(text);
        button.setOnAction(action);
        button.getStyleClass().add("refined-button-effect");
        return button;
    }

    private static void positionYouWinMenuOverGame(Stage primaryStage) {
        youWinStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.8 - youWinLayout.getPrefWidth() / 2);
        youWinStage.setY(primaryStage.getY() + primaryStage.getHeight() / 4 - youWinLayout.getPrefHeight() / 2);
    }

    private static void initializeBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }

    private static void fadeInMenu() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), youWinLayout);
        fadeIn.setFromValue(0.3);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static void fadeOutMenu() {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), youWinLayout);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(event -> youWinStage.close());
        fadeOut.play();
    }
}
