package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class SoundManager {
    private static MediaPlayer backgroundMediaPlayer;

    private static void playSound(String soundFile) {
        try {
            Media sound = new Media(new File(soundFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setVolume(1);
            mediaPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void buttonClickSound() {
        playSound("src/main/resources/Sound Effects/buttonClickSound.mp3");
    }
    public static void paddleBounceSound() {
        playSound("src/main/resources/Sound Effects/paddleBounce.mp3");
    }
    public static void pauseMenuSound() {
        playSound("src/main/resources/Sound Effects/Menus/pauseMenu.mp3");
    }
    public static void ballHitFloor() {
        playSound("src/main/resources/Sound Effects/ballHitFloor.mp3");
    }
    public static void levelUp() {
        playSound("src/main/resources/Sound Effects/levelUp.mp3");
    }
    public static void gameOver() {
        playSound("src/main/resources/Sound Effects/gameOver.mp3");
    }
    public static void winSound() { playSound("src/main/resources/Sound Effects/winSound.mp3"); }
    public static void muteSoundPauseMenu() { playSound("src/main/resources/Sound Effects/muteSoundPauseMenu.mp3"); }
    public static void blockHit() {playSound("src/main/resources/Sound Effects/blockHit.mp3"); setVolume(0.57);}
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

    public static void stopBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.stop();
        }
    }

    public static void pauseBackgroundMusic() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            backgroundMediaPlayer.pause();
        }
    }

    public static void resumeBackgroundMusic() {
        if (backgroundMediaPlayer != null && backgroundMediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
            backgroundMediaPlayer.play();
        }
    }

    public static void toggleMuteBackgroundMusic() {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setMute(!backgroundMediaPlayer.isMute());
        }
    }

    public static void setVolume(double volume) {
        if (backgroundMediaPlayer != null) {
            backgroundMediaPlayer.setVolume(volume);
        }
    }

    public static double getVolume() {
        if (backgroundMediaPlayer != null) {
            return backgroundMediaPlayer.getVolume();
        }
        return 0; // Default volume if the player is null
    }

}
