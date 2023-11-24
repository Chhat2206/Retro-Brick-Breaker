package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoundManager {
    private static MediaPlayer backgroundMediaPlayer;
    private static final List<String> musicFiles = new ArrayList<>();

    static {
        // Initialize the list with music files
        Collections.addAll(musicFiles,
                "src/main/resources/Sound Effects/Background Music/backgroundMusic8Bit.mp3",
                "src/main/resources/Sound Effects/Background Music/backgroundMusicNCS.mp3",
                "src/main/resources/Sound Effects/Background Music/backgroundMusicSoftPiano.mp3");
    }

    public static void startRandomBackgroundMusic() {
        if (musicFiles.isEmpty()) {
            System.err.println("No music files available.");
            return;
        }

        // Shuffle and select the first song
        Collections.shuffle(musicFiles);
        String musicFilePath = musicFiles.remove(0); // Remove the played song from the list
        startBackgroundMusic(musicFilePath);
    }

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
    public static void pauseMenuMusic() {
        playSound("src/main/resources/Sound Effects/Menus/pauseMenu.mp3");
        setVolume(0.7);
    }
    public static void ballHitFloor() {
        playSound("src/main/resources/Sound Effects/ballHitFloor.mp3");
        setVolume(1.5);
    }
    public static void levelUp() {
        playSound("src/main/resources/Sound Effects/levelUp.mp3");
    }
    public static void gameOver() {
        playSound("src/main/resources/Sound Effects/gameOver.mp3");
    }
    public static void winSound() {
        playSound("src/main/resources/Sound Effects/winSound.mp3");
        setVolume(0.6);
    }
    public static void goldBallPowerUp() {
        playSound("src/main/resources/Sound Effects/goldBallPowerUp.mp3");}
    public static void muteSoundPauseMenu() {
        playSound("src/main/resources/Sound Effects/muteSoundPauseMenu.mp3");
    }
    public static void blockHit() {
        playSound("src/main/resources/Sound Effects/blockHit.mp3");
        setVolume(0.57);
    }
    public static void collectBonus() {
        playSound("src/main/resources/Sound Effects/collectBonus.mp3");
        setVolume(1);
    }

    public static void soundMenu() {
        playSound("src/main/resources/Sound Effects/Menus/menuOpen.mp3");
        setVolume(0.6);
    }


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
