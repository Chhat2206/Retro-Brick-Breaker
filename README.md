# JavaFX Retro Pixel Art Brick Breaker Game
![Github Logo.png](documents%2FGithub%20Logo.png)
## Compilation Instructions with IntelliJ IDEA, JavaFX 21.01, and Amazon Corretto JDK 19.0.2

Follow these steps to compile and run your Java application using IntelliJ IDEA, JavaFX 21.01, and Amazon Corretto JDK 19.0.2:

1. **Install IntelliJ IDEA:**
  - If not already installed, download and install IntelliJ IDEA from the [official website](https://www.jetbrains.com/idea/download/).
  - Follow the installation instructions for your operating system.
  - This project was created in Intellij Student Edition, but any Intellij version should work.

2. **Clone Your GitHub Repository in IntelliJ IDEA:**
  - Open IntelliJ IDEA.
  - Click on "Check out from Version Control" on the welcome screen, or go to "File" > "New" > "Project from Version Control" > "Git."
  - Enter ```java https://github.com/Chhat2206/COMP2042_CW_hfycs2.git``` and follow the prompts to clone it.

3. **Set Up Project Structure:**
  - Ensure that your Java source code is organized according to the standard Java project structure within your IntelliJ project.

4. **Configure SDK and Libraries:**
  - Configure Amazon Corretto JDK 19.0.2 as your project's JDK:
    - Go to "File" > "Project Structure."
    - Under "Project," select your "Project SDK" and set it to Amazon Corretto JDK 19.0.2.
    - Ensure that the "Project language level" is set appropriately (e.g., Java 8 or higher).

5. **Add JavaFX Library:**
  - Add the JavaFX library to your project:
    - Go to "File" > "Project Structure."
    - Under "Project Settings," select "Libraries" and click the "+" icon to add a new library.
    - Choose "Java" and navigate to the `lib` directory within your JavaFX SDK 21.01 installation directory.
    - Click "OK" to add the JavaFX library.

5.5. (Optional) **Create a Run Configuration:**
  - Set up a run configuration for your Java application:
    - Right-click on your main application class (e.g., `Main.java`) in the project explorer.
    - Select "Create 'Main'" from the context menu (or use the name of your main class).
    - In the Run Configuration dialog, ensure that the main class is selected correctly.
    - Under "VM options," add the following to specify the JavaFX module path and modules:
    ```plaintext
    --module-path /path/to/javafx-sdk-21.01/lib --add-modules javafx.controls,javafx.fxml
    ```
    - Replace `/path/to/javafx-sdk-21.01` with the actual path to your JavaFX SDK 21.01 installation directory.

7. **Compile and Run Your Application:**
  - Click the "Run" button in IntelliJ IDEA or use the keyboard shortcut (typically Shift + F10) to compile and run your Java application.

Your Java application should now compile and run seamlessly using JavaFX 21.01 and Amazon Corretto JDK 19.0.2 within IntelliJ IDEA. Make any necessary adjustments to match your project structure and requirements.

## Implemented and Working Properly:

### General
- [x] Removing the ability to resize the window
- [x] Custom sound effects for every action and animation in the game, found inside the SoundManager class.

### Main Menu
- [x] Custom main menu favicon, custom title, custom brick breaker logo
- [x] Three custom buttons with save game, load game, and quit
- [x] Clean hover effect while hovering over the button to click
- [x] Clean transition effect when starting or loading the game

### Game
- [x] New favicon and title
- [x] 10 custom levels designs, including backgrounds and block layout
- [x] Smooth paddle movement and consistant ball movement for improved flow
- [x] Custom Coin icon, level icon and heart icon. 
- [x] Custom box and blur effect behind the level coins and heart menu to make it look stylish
- [x] Custom ball icon and custom heart blocks, coin blocks, and randomized block graphics
- [x] Custom animation for when the player gains points
- [x] Custom heart blinking animation for when the player both loses and gains a heart
- [x] Custom level-up animation
- [x] Custom random drop ui and custom animation when dropped by the question block
- [x] Randomization with 3 options: changing paddle width, changing ball width, and giving a random amount of bonus
- [x] Unique space border effect for the ball hovering over the paddle
- [x] Modified bonus icon behavior when the pause menu is open
- [x] Custom colors when showcasing a power-up bonus for cuter ui
- [x] Set paddle to the middle in the new levels and when loading game

### Pause and Sound Menu
- [x] Pause menu follows the screen when it's moved, and pulls up the screen when minimized
- [x] Custom gradients and transparent grey hue surrounding buttons
- [x] Clean animations for opening and closing menus with sound effects

#### Pause menu
- [x] Custom blue effect when the pause menu is open
- [x] Custom sound effect for opening the pause menu
- [x] Custom buttons with highlighting
- [x] Smooth resume game functionality
- [x] Saving game showcases a custom white button with a "Game Saved" text for a few seconds for a cleaner look
- [x] Restarting game also has a custom animation
- [x] Load and restart game buttons work as expected
- [x] Sound Settings open the sound menu and turn white while the sound menu is opened

#### Sound Menu
- [x] Positioned perfectly next to the pause menu
- [x] Transparency of the pause menu when opened
- [x] Clean slider for controlling background music volume
- [x] Mute/unmute background music functionality
- [x] Clean close button to fade out the sound menu

### YouWinScreen and GameOverScreen
- [x] Custom gradients and dark background
- [x] Blurred game screen
- [x] Centered on the screen and follows the primary stage
- [x] Custom score display with images and sound effects for win/lose scenarios
- [x] Restart and main menu buttons with custom UI and clean design
- [x] Custom animation when clicking the restart button or main menu button, both fading the button in and transitioning the scene cleanly in two separate concurrent animations.

## Implemented but Not Working Properly:
- **Issue**: Visual Errors: Ball and Paddle very rarely glitch out. 1/20 times while playing (I thought it was fixed but during final checks it popped up). They work as expected but visually only half the paddle is stuck while the paddle I control is behind it. Other times, they cut and show up at intervals. Issue is usually solved when opening the pause menu (stopping and restarting the game engine) or progressing levels
    - **Potential Solution 1**: I added additional threads to the ball and paddle. It caused memory leaks and made the game unplayable very quickly.
    - **Potential Solution 2**: I added it to the current threads. The issue subsided but still showed up very rarely.
    - **Potential Solution 3**: I added many checks to make sure both the paddle and the ball stays in the scene after changing width and not out of the screen, set it to float to account for integer overflows and so on.
    - **Potential Solution 4**: I added the ball to a new class and rewrote the functions which controlled the changing of size of paddle and ball, which seemed to fix the issue. However, after 2 weeks of testing, it popped up once (today).

## Credits
The items that are not listed below were hand created by me with the help of Artificial Intelligence and hours of Photoshop.

### Images
#### General Assets
- [Paddle Design](https://kenney.nl/assets/shape-characters)

### Sound Effects
#### Background Music
- [backgroundMusicSoftPiano](https://pixabay.com/sound-effects/soft-piano-100-bpm-121529/)
- [backgroundMusicCosmic](https://pixabay.com/sound-effects/spaceship-ambience-with-effects-21420/)

#### Menus
- [mainMenuOpen](https://pixabay.com/sound-effects/game-start-6104/)
- [pauseMenuOpen](https://pixabay.com/sound-effects/interface-soft-click-131438/)

#### General Sounds
- [collectPowerup](https://pixabay.com/sound-effects/coin-collect-retro-8-bit-sound-effect-145251/)
- [buttonClickSound](https://pixabay.com/sound-effects/click-button-app-147358/)
- [changeBackgroundMusic](https://pixabay.com/sound-effects/tape-cassette-insert-172758/)
- [muteSoundPauseMenu](https://pixabay.com/sound-effects/click-21156/)
- [levelUp](https://pixabay.com/sound-effects/woosh-low-long01-98755/)
- [gameOver](https://pixabay.com/sound-effects/power-down-7103/)
- [winSound](https://pixabay.com/sound-effects/winsquare-6993/)
- [goldBallPowerUp](https://pixabay.com/sound-effects/collect-5930/)
- [collectBonus](https://pixabay.com/sound-effects/blaster-2-81267/)
- [ballHitFloor](https://pixabay.com/sound-effects/jazz-bass-open-e-39297/)
- [paddleBounce](https://pixabay.com/sound-effects/stop-13692/)
- [buttonClickSound](https://pixabay.com/sound-effects/click-for-game-menu-131903/)
- [heartBonus](https://pixabay.com/sound-effects/hospital-monitor-151929/)
