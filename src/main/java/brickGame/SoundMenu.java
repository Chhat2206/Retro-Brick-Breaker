package brickGame;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SoundMenu {

    private static boolean isMuted = false;
    private static Stage soundStage;
    private static VBox soundLayout;

    public static void display() {
        initializeSoundStage();
        configureSoundLayout();
        HBox volumeControls = new HBox(10);
        volumeControls.setAlignment(Pos.CENTER);
        Slider volumeSlider = createVolumeSlider();
        Button muteButton = createMuteButton();
        volumeControls.getChildren().addAll(volumeSlider, muteButton);

        soundLayout.getChildren().addAll(volumeSlider, muteButton);

        Scene scene = new Scene(soundLayout, 300, 400);
        soundStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().add("/css/defaultMenu.css");
        scene.getStylesheets().add("/css/soundMenu.css");
        soundStage.showAndWait();

    }

    private static void initializeSoundStage() {
        soundStage = new Stage();
        soundStage.initModality(Modality.APPLICATION_MODAL);
        soundStage.initStyle(StageStyle.TRANSPARENT);
    }

    private static void configureSoundLayout() {
        soundLayout = new VBox(10);
        soundLayout.setAlignment(Pos.CENTER);
        soundLayout.setSpacing(20);
    }

    public static Slider createVolumeSlider() {
        Slider volumeSlider = new Slider(0, 1, SoundManager.getVolume());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setVolume(newValue.doubleValue());
        });
        return volumeSlider;
    }

    public static Button createMuteButton() {
        ImageView muteIcon = new ImageView(new Image("/images/muteMusic.png"));
        muteIcon.setFitWidth(60);
        muteIcon.setFitHeight(60);
        muteIcon.setPreserveRatio(true);
        Button muteButton = new Button("", muteIcon);
        muteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        muteButton.setOnAction(e -> {
            isMuted = !isMuted;
            SoundManager.toggleMuteBackgroundMusic();
            SoundManager.muteSoundPauseMenu();
            muteIcon.setImage(isMuted ? new Image("/images/playMusic.png") : new Image("/images/muteMusic.png"));
        });
        return muteButton;
    }

    private static void positionSoundMenuOverGame(Stage primaryStage) {
        double centerXPosition = primaryStage.getX() + (primaryStage.getWidth() / 2) - (soundStage.getWidth() / 2);
        double centerYPosition = primaryStage.getY() + (primaryStage.getHeight() / 2) - (soundStage.getHeight() / 2);
        soundStage.setX(centerXPosition);
        soundStage.setY(centerYPosition);
    }

    public static void initializeSoundMenuBlur(Stage primaryStage) {
        GaussianBlur blur = new GaussianBlur(4);
        primaryStage.getScene().getRoot().setEffect(blur);
    }
}