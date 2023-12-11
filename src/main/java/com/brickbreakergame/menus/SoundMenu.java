package com.brickbreakergame.menus;
import com.brickbreakergame.managers.AnimationManager;
import com.brickbreakergame.menus.PauseMenu;

import com.brickbreakergame.managers.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The SoundMenu class represents the in-game sound settings menu that provides options to adjust volume and mute/unmute audio.
 * It is displayed as a modal dialog over the pause menu.
 */
public class SoundMenu {

    private static boolean isMuted = false;
    private static Stage soundStage;
    private static VBox soundLayout;
    private static AnimationManager animationManager = new AnimationManager();

    /**
     * Displays the sound settings menu.
     * This method sets up and shows the sound settings menu, allowing users to adjust audio volume and mute/unmute audio.
     * It is displayed as a modal dialog over the pause menu, providing an intuitive and user-friendly interface for sound settings.
     */
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

    /**
     * Initializes the sound stage with specific settings for modality and style.
     * This stage is used for displaying the sound settings menu as a modal dialog over the game.
     */
    private static void initializeSoundStage() {
        soundStage = new Stage();
        soundStage.initModality(Modality.APPLICATION_MODAL);
        soundStage.initStyle(StageStyle.TRANSPARENT);
    }

    /**
     * Configures the layout for the sound settings menu.
     * Sets the alignment, spacing, and style of the VBox layout that contains the sound menu's UI elements.
     */
    private static void configureSoundLayout() {
        soundLayout = new VBox(10);
        soundLayout.setAlignment(Pos.CENTER);
        soundLayout.setSpacing(10);
        soundLayout.getStyleClass().add("sound-menu-gradient");
    }

    /**
     * Creates and returns a volume slider control for the sound settings menu.
     * This slider allows users to adjust the audio volume dynamically during the game.
     *
     * @return The Slider control for adjusting audio volume.
     */
    public static Slider createVolumeSlider() {
        Slider volumeSlider = new Slider(0, 1, SoundManager.getVolume());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> SoundManager.setVolume(newValue.doubleValue()));
        return volumeSlider;
    }

    /**
     * Creates and returns a mute/unmute button for the sound settings menu.
     * This button allows users to toggle the mute status of the game's audio.
     *
     * @return The Button control for muting/unmuting audio.
     */
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

    /**
     * Adds volume controls, including a slider and a mute button, to the sound settings menu layout.
     * These controls enable users to manage audio settings conveniently within the game.
     */
    private static void addVolumeControls() {
        VBox volumeControls = new VBox(10); // Changed from HBox to VBox
        volumeControls.setAlignment(Pos.CENTER);

        Slider volumeSlider = createVolumeSlider();
        Button muteButton = createMuteButton();

        volumeControls.getChildren().addAll(volumeSlider, muteButton);
        soundLayout.getChildren().add(volumeControls);
    }

    /**
     * Adds a close button to the sound settings menu.
     * This button allows users to close the sound settings menu and return to the game or pause menu.
     */
    private static void addCloseButton() {
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            SoundManager.buttonClickSound();
            PauseMenu.resetSoundButtonStyle(); // Assuming this resets the sound button style in PauseMenu
            soundStage.close();
            // Use AnimationManager to fade in the PauseMenu
            animationManager.fadeInMenu(PauseMenu.getPauseLayout()); // Replace with correct layout
        });

        soundLayout.getChildren().add(closeButton);
    }

    /**
     * Positions the sound settings menu adjacent to the pause menu.
     * Aligns the sound settings menu in relation to the pause menu's position and dimensions for a cohesive interface layout.
     */
    private static void positionSoundMenuNextToPauseMenu() {
        // Position the Sound Menu to the right of the Pause Menu
        soundStage.setX(PauseMenu.getPauseStageX() + PauseMenu.getPauseStageWidth());
        soundStage.setY(PauseMenu.getPauseStageY());
    }

}