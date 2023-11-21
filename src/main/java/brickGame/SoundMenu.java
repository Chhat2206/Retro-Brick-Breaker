package brickGame;

import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SoundMenu {

    private static boolean isMuted = false;
    private static Stage soundStage;
    private static VBox soundLayout;

    public static void display() {
        PauseMenu.soundMenuOpen();
        initializeSoundStage();
        configureSoundLayout();
        addVolumeControls();
        addCloseButton();

        Scene scene = new Scene(soundLayout, 150, 220);
        soundStage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        scene.getStylesheets().addAll("/css/defaultMenu.css", "/css/soundMenu.css");

        positionSoundMenuNextToPauseMenu();

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
        soundLayout.setSpacing(10);
        soundLayout.getStyleClass().add("sound-menu-gradient");
    }

    public static Slider createVolumeSlider() {
        Slider volumeSlider = new Slider(0, 1, SoundManager.getVolume());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            SoundManager.setVolume(newValue.doubleValue());
        });
        return volumeSlider;
    }

    public static Button createMuteButton() {
        ImageView muteIcon = new ImageView(new Image("/images/sound menu/muteMusic.png"));
        muteIcon.setFitWidth(60);
        muteIcon.setFitHeight(60);
        muteIcon.setPreserveRatio(true);
        Button muteButton = new Button("", muteIcon);
        muteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; -fx-padding: 0;");
        muteButton.setOnAction(e -> {
            isMuted = !isMuted;
            SoundManager.toggleMuteBackgroundMusic();
            SoundManager.muteSoundPauseMenu();
            muteIcon.setImage(isMuted ? new Image("/images/sound menu/playMusic.png") : new Image("/images/sound menu/muteMusic.png"));
        });
        return muteButton;
    }

    private static void fadeInMenu() {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), soundLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static void addVolumeControls() {
        VBox volumeControls = new VBox(10); // Changed from HBox to VBox
        volumeControls.setAlignment(Pos.CENTER);

        Slider volumeSlider = createVolumeSlider();
        Button muteButton = createMuteButton();

        volumeControls.getChildren().addAll(volumeSlider, muteButton);
        soundLayout.getChildren().add(volumeControls);
    }

    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            PauseMenu.resetSoundButtonStyle();
            soundStage.close();
            PauseMenu.fadeInMenu();
        });

        soundLayout.getChildren().add(closeButton);
    }

    private static void positionSoundMenuNextToPauseMenu() {
        // Position the Sound Menu to the right of the Pause Menu
        soundStage.setX(PauseMenu.getPauseStageX() + PauseMenu.getPauseStageWidth());
        soundStage.setY(PauseMenu.getPauseStageY());
    }

}