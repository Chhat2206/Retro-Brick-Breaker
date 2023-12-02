package com.brickbreakergame.managers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The SoundManager class manages sound effects and background music for the game.
 */
public class SoundManager {
    private static MediaPlayer backgroundMediaPlayer;
    private static final List<String> musicFiles = new ArrayList<>();

    static {
        Collections.addAll(musicFiles,
                "src/main/resources/Sound Effects/Background Music/backgroundMusicCosmic.mp3",
                "src/main/resources/Sound Effects/Background Music/backgroundMusicSoftPiano.mp3");
    }

    /**
     * Starts playing a random background music track.
     * If there are no music files available, an error message is printed, and no action is taken.
     */
    public static void startRandomBackgroundMusic() {
        if (musicFiles.isEmpty()) {
            System.err.println("No music files available.");
            return;
        }

        // Stop the current playing music, if any
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }

        // Shuffle and select a new song from the list
        Collections.shuffle(musicFiles);
        String musicFilePath = musicFiles.get(0); // Get the first song in the shuffled list
        startBackgroundMusic(musicFilePath);
    }

    /**
     * Plays a sound effect from the specified sound file.
     *
     * @param soundFile The file path of the sound effect to play.
     */
    private static void playSound(String soundFile) {
        try {
            Media sound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            setVolume(1);
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    /**
     * Plays a button click sound effect.
     */
    public static void buttonClickSound() {
        playSound("src/main/resources/Sound Effects/buttonClickSound.mp3");
    }

    /**
     * Plays a heart bonus sound effect.
     */
    public static void heartBonus() {
        playSound("src/main/resources/Sound Effects/heartBonus.mp3");
    }

    /**
     * Plays a paddle bounce sound effect.
     */
    public static void paddleBounceSound() {
        playSound("src/main/resources/Sound Effects/paddleBounce.mp3");
    }

    /**
     * Plays a pause menu music sound effect.
     */
    public static void pauseMenuMusic() {
        playSound("src/main/resources/Sound Effects/Menus/pauseMenu.mp3");
    }

    /**
     * Plays a sound effect when the ball hits the floor.
     */
    public static void ballHitFloor() {
        playSound("src/main/resources/Sound Effects/ballHitFloor.mp3");
    }

    /**
     * Plays a sound effect when the player levels up.
     */
    public static void levelUp() {
        playSound("src/main/resources/Sound Effects/levelUp.mp3");
    }

    /**
     * Plays a game over sound effect.
     */
    public static void gameOver() {
        playSound("src/main/resources/Sound Effects/gameOver.mp3");
    }

    /**
     * Plays a win sound effect.
     */
    public static void winSound() {
        playSound("src/main/resources/Sound Effects/winSound.mp3");
    }

    /**
     * Plays a sound effect when the player collects a gold ball power-up.
     */
    public static void goldBallPowerUp() {
        playSound("src/main/resources/Sound Effects/goldBallPowerUp.mp3");
    }

    /**
     * Plays a sound effect to mute sound in the pause menu.
     */
    public static void muteSoundPauseMenu() {
        playSound("src/main/resources/Sound Effects/muteSoundPauseMenu.mp3");
    }

    /**
     * Plays a sound effect when a block is hit.
     */
    public static void blockHit() {
        playSound("src/main/resources/Sound Effects/blockHit.mp3");
    }

    /**
     * Plays a sound effect when a bonus is collected.
     */
    // To modify the volume, the function must be re-initialized. It can't have a global variable to control every minor sound.
    public static void collectBonus() {
        try {
            Media sound = new Media(new File("src/main/resources/Sound Effects/collectBonus.mp3").toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(0.4);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound effect: " + e.getMessage());
        }
    }

    /**
     * Plays a sound effect for opening a menu and sets the volume.
     */
    public static void soundMenu() {
        playSound("src/main/resources/Sound Effects/Menus/menuOpen.mp3");
        setVolume(0.6);
    }

    /**
     * Starts playing background music from the specified music file.
     * @param musicFilePath The file path of the background music to start.
     */
    public static void startBackgroundMusic(String musicFilePath) {
        try {
            Media sound = new Media(new File(musicFilePath).toURI().toString());
            backgroundMediaPlayer = new MediaPlayer(sound);
            backgroundMediaPlayer.setVolume(0.37); // Set initial volume
            backgroundMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
        }
    }

    /**
     * Toggles the mute state of the background music.
     */
    public static void toggleMuteBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setMute(!backgroundMediaPlayer.isMute());
        }
    }

    /**
     * Sets the volume level for the background music.
     * @param volume The volume level (0.0 to 1.0) to set.
     */
    public static void setVolume(double volume) {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setVolume(volume);
        }
    }

    /**
     * Gets the current volume level of the background music.
     * @return The current volume level (0.0 to 1.0).
     */
    public static double getVolume() {
        if (backgroundMediaPlayer != null) {
            return backgroundMediaPlayer.getVolume();
        }
        return 0; // Default volume if the player is null
    }

}