# Brick Breaker Game
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

### Main Menu
- [x] Custom main menu favicon, custom title, custom brick breaker logo
- [x] Three custom buttons with save game, load game, and quit
- [x] Custom sound effect when opening the game
- [x] Clean transition effect when starting or loading the game
- [x] Blue highlight when hovering over a button

### Game
- [x] Many custom hand-selected random themes for the game
- [x] Custom block colors
- [x] 10 custom levels
- [x] 10 custom backgrounds
- [x] Custom sound effects for every action in the game.
- [x] Custom paddle creation
- [x] Coin icon, level icon and heart icon
- [x] New favicon and title
- [x] Blur effect behind the level coins and heart menu
- [x] Custom ball icon and custom heart blocks
- [x] Modified the random block and gold block to be very clean
- [x] Custom level-up animation and sound effect for leveling up
- [x] Custom paddle movement for smoother gameplay
- [x] Custom ball movement mechanics for improved flow
- [x] Randomization with 3 options: changing paddle width, changing ball width, and giving a random amount of bonus
- [x] Unique space border effect for the ball hovering over the paddle
- [x] Custom bonus icon dropped by the question block
- [x] Modified bonus icon behavior when the pause menu is open
- [x] Custom colors when showcasing a power-up bonus for cuter ui
- [x] Cool animation when player loses and gains a heart

### Pause and Sound Menu
- [x] Both menus follow the screen when it's moved
- [x] Custom gradients and transparent grey hue surrounding buttons
- [x] Clean animations for opening and closing menus with sound effects

#### Pause menu
- [x] Custom blue effect when the pause menu is open
- [x] Custom sound effect for opening the pause menu
- [x] Custom buttons with highlighting
- [x] Smooth resume game functionality
- [x] Save game turns white into 'Game Saved' for around 1 second
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
- [x] Centered on the screen and follows the primary stage
- [x] Custom score display with images and sound effects for win/lose scenarios
- [x] Restart and main menu buttons with custom UI and clean design

## Implemented but Not Working Properly:
- ⚠️ **Block Visual Glitch**: Occasionally, block visuals may persist on the screen after a collision, despite the block being non-existent. This causes no physical obstruction, and the game progresses normally once all actual blocks are removed. Temporarily pausing the game (stopping and restarting the game engine) resolves this issue. The occurrence is rare. It has only been observed in level 4 in my testing.
  - **Steps to counter**: I implemented threads to every block and attempted to stop and start back the engine before every level to prevent the issue. That caused the game to look frozen while generating new levels, so the idea was scrapped. I rewrote the ball movement function, paddle function which helped reduce the frequency but did not work. I created a new thread each time a powerup occured, which caused the game to lag immensely.
  
## Features Not Implemented:
- **Feature**: Pause button
  - **Issue**: Main menu did pause very smoothly, no need for a dedicated button

- **Feature**: Custom sound menu to tweak every sound in the game
  - **Issue**: Seems unnecessary when I modified each sound to be pleasing. I all-ready demonstrated the knowledge of modifying sound with one slider. I also could not make the menu look good.

- **Feature**: Pause Menu Image, fade out of the pause menu, fixing score, custom paddle and ball chosen by the user, custom level difficulty, original game assets such as unique forest and under water theme, many music pieces such as 8bit pop and looping piano.
  - **Issue**: The features above (and those unmentioned) were implemented but did not fit the theme and style of the game, as such were removed. The music in particular was removed if the loop got annoying after level 3. Many pieces were removed as a result. The ones left were modified and recreated to fit the game's theme.

# New Java Classes

## Managers
### SoundManager
- **Purpose:** Manages all music and sound effects in the game. It allows other classes to access and control sound effects throughout the game.
- **Location:** `src/main/java/brickGame/managers/SoundManager.java`

### UIManager
- **Purpose:** Manages the user interface elements, coordinating between different screens and UI components.
- **Location:** `src/main/java/brickGame/managers/UIManager.java`

## Menus
### MainMenu
- **Purpose:** Handles the main menu interface, including the display of menu options and navigation.
- **Location:** `src/main/java/brickGame/menus/MainMenu.java`

### PauseMenu
- **Purpose:** Manages the pause menu, providing options like resume, settings, or exit when the game is paused.
- **Location:** `src/main/java/brickGame/menus/PauseMenu.java`

### SoundMenu
- **Location:** `src/main/java/brickGame/menus/SoundMenu.java`

## Screens
### GameOverScreen
- **Location:** `src/main/java/brickGame/screens/GameOverScreen.java`

### YouWinScreen
- **Purpose:** Displays the victory screen when a player wins, showing scores and options for next steps.
- **Location:** `src/main/java/brickGame/screens/YouWinScreen.java`

## Default
### Block
- **Purpose:** Represents a basic building unit in the game, potentially used for constructing levels, obstacles, or other elements.
- **Location:** `src/main/java/brickGame/Block.java`

### BlockSerializable
- **Purpose:** Extends the `Block` class with serialization capabilities, facilitating saving/loading of block states for game persistence.
- **Location:** `src/main/java/brickGame/BlockSerializable.java`

### Bonus
- **Purpose:** Manages bonus items or power-ups in the game, potentially affecting gameplay by providing advantages or special abilities.
- **Location:** `src/main/java/brickGame/Bonus.java`

### GameBoard
- **Purpose:** Controls the game board logic, including the layout, piece movements, and game state management.
- **Location:** `src/main/java/brickGame/GameBoardManager.java`

### GameEngine
- **Purpose:** Core of the game's mechanics, handling the main game loop, state updates, and integration of various game components.
- **Location:** `src/main/java/brickGame/GameEngine.java`

### GameState
- **Purpose:** Represents and manages the current state of the game, such as playing, paused, or game over, including state transitions.
- **Location:** `src/main/java/brickGame/GameState.java`

### Main
- **Purpose:** The entry point of the game application, initializing and starting the game.
- **Location:** `src/main/java/brickGame/Main.java`

### Score
- **Purpose:** Manages the scoring system of the game, tracking and updating player scores based on game events.
- **Location:** `src/main/java/brickGame/Score.java`

## Modified Java Classes:
- List the Java classes you modified from the provided code base. Describe the changes you made and explain why these modifications were necessary.

## Unexpected Problems:
All issues not mentioned here were uninteresting to describe. These are the major issues and fixes with unique solutions.
### Balancing Game
- **Issue**: Balancing the game was a lot more tough than expected.
    - **Solution**: A lot of playtesting and tweaking to make everything perfect.
  
### Collision Detection
- **Issue**: Collision detection is problematic.
  - **Solution**: Partially resolved by incorporating the ball radius into the 'checkHitToBlock' function, allowing blocks to detect and disappear upon collision with the ball. From there, add many collision checks and redundancy to account for any change in ball movement. ballRadius is added to all relevant items that collide with the ball to ensure smooth gameplay.

### Movement Game Issues
- **Issue**: The ball, paddle, and all gui items with velocity would randomly disappear or have strange properties.
  - **Solution**: Rewrite every function to connect it to threads instead of being interdependent to other non-related states.

### Persistent Pause Menu
- **Issue**: The pause menu remained in place when the game screen was moved and did not show the game menu when minimized and reopened.
  - **Solution**: Resolved by adjusting the position of the pause menu based on the game screen's position, and utilizing the game scene as the primary scene.

### Score Freeze
- **Issue**: The score sometimes froze in place, and falling objects gained unexpected physics.
  - **Solution**: Rewrite all physics related to score, and added multiple checks in order to account for all variables.

### Fading Out
- **Issue**: Fading out didn't work as expected while in the pause and sound menu.
  - **Solution**: The animation for opening the sound menu was colliding with the new animation for closing the sound menu. This issue was fixed by setting a fixed duration on the animation time.

### Difficulty Launching Main Menu
- **Issue**: The main menu would open after every level up in game
  - **Solution**: A new thread is being created for every element continuously. Removing new threads from being constantly created and only utilizing the start function for running the Main Menu once fixed the issue.

### Index Out Of Bounds
- **Issue**: Randomly got index out of bounds error
  - **Solution**: Related to the blocks and arraylist, the board setup has been customized with new levels and the function for block detection was completely rewritten.

### Phasing Through Blocks
- **Issue**: Creating custom levels meant all blocks past the row level increment equal to the row of blocks are visible but have no collision detection.
  - **Solution**: 
  ```java
    if (ballPosY + BALL_RADIUS >= Block.getPaddingTop() && ballPosY - BALL_RADIUS <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
    ``` 
  was included in the code, meaning that all blocks past that level would be counted as blocks but only be visible with no borders. Removing and rewriting this line broke the collision detection, requiring a complete rewrite of the collision detection functions.

### Invalid URL
- **Issue**: Java URL syntax varies from one function to another without consistency.
  - **Solution**: Added redundancy links in case the primary syntax fails.


## Credits
The items that are not listed below were hand created by me.

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