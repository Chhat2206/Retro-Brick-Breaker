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

## Features Not Implemented:
- **Feature**: Pause button
  - **Issue**: Main menu did pause very smoothly, no need for a dedicated button

- **Feature**: Custom sound menu to tweak every sound in the game
  - **Issue**: Seems unnecessary when I modified each sound to be pleasing. I all-ready demonstrated the knowledge of modifying sound with one slider. I also could not make the menu look good.

- **Feature**: Pause Menu Image, fade out of the pause menu, fixing score, custom paddle and ball chosen by the user, custom level difficulty, original game assets such as unique forest and under water theme, many music pieces such as 8bit pop and looping piano.
  - **Issue**: The features above (and those unmentioned) were implemented but did not fit the theme and style of the game, as such were removed. The music in particular was removed if the loop got annoying after level 3. Many pieces were removed as a result. The ones left were modified and recreated to fit the game's theme.

- **Feature**: Custom animation while changing the ball and paddle size
  - **Issue**: I couldn't find animations that looked smooth.

# New Java Classes

## Managers

### AnimationManager
- **Purpose:** Manages various animations within the Brick Breaker game, enhancing the user experience with dynamic visual effects.
- **Key Features:**
    - Scene transitions using sliding effects for a smooth flow between game stages.
    - Fade-in and fade-out effects for UI elements like menus and layouts.
    - Animations for heart count indicators, highlighting gains or losses during gameplay.
    - Enhanced button animations, providing a polished look for interactive elements.
    - Application of visual effects like Gaussian blur for UI layer distinction.
- **Usage:** This class is invoked in different parts of the game where animations are required, such as during scene changes, menu interactions, and game events like losing or gaining hearts.
- **Location:** Located within the `com.brickbreakergame.managers` package of the game's codebase.

### GameBoardManager
- **Purpose:** Manages the layout of the game board for different levels in the Brick Breaker game. This class is responsible for creating and arranging blocks in various patterns, adding variability and uniqueness to each level.
- **Key Features:**
    - Generation of unique level layouts, including shapes like space ships, patterns, and other designs.
    - Randomized block type determination, enhancing gameplay variety.
- **Methods:**
    - `setupGameBoard()`: Sets up the game board layout based on the current level.
    - `createLevel1Layout()` to `createLevel10Layout()`: Specific methods for generating layouts for each level, such as space ships, patterns, or shapes.
    - `createDefaultLayout()`: Generates a default layout as a fallback for unrecognized levels.
    - `determineBlockType(int randomValue)`: Determines the type of block to be generated based on a random value, ensuring variety and rarity of certain block types.
- **Usage:** This class is integral to the game for dynamically generating the game board layout for each level, ensuring a fresh and engaging experience every time a new level is started.
- **Location:** Part of the `com.brickbreakergame.managers` package.

### SoundManager
- **Purpose:** Manages sound effects and background music for the Brick Breaker game. Provides methods to control the playback of various sound effects and background music tracks, catering to different game events like button clicks, collecting bonuses, and more.
- **Key Features:**
    - Ability to play, stop, and control volume of different sound effects and background music.
    - Specific methods to manage sound effects for game events such as level up, game over, block hit, and bonus collection.
    - Random selection and looping of background music tracks from a predefined list.
- **Methods:**
    - `startRandomBackgroundMusic()`: Starts playing a random background music track.
    - `playSound(String soundFile)`: Plays a specified sound effect.
    - `toggleMuteBackgroundMusic()`: Toggles the mute state of background music.
    - `setVolume(double volume)`: Sets the volume for background music.
    - `getVolume()`: Returns the current volume level of the background music.
    - Individual methods like `buttonClickSound()`, `heartBonus()`, `paddleBounceSound()`, etc., for specific sound effects.
- **Usage:** Enhances the game's auditory experience by providing appropriate sound effects and background music for various game situations and actions.
- **Location:** Part of the `com.brickbreakergame.managers` package.

### UIManager
- **Purpose:** Manages the user interface elements for the Brick Breaker game, handling the dynamic creation and arrangement of UI components like labels for game-related information (level, score, hearts) and setting background images.
- **Key Features:**
    - Creation and display of labels for heart count, score, and level, with accompanying icons.
    - Updating the game UI dynamically to reflect changes in game state such as score or level.
    - Setting and updating background images based on the current level of the game.
- **Methods:**
    - `makeHeartScore(int heart, int score, int level)`: Creates and displays UI elements for hearts, score, and level.
    - `getHeartLabel()`: Retrieves the label displaying the heart count.
    - `setScore(int score)`: Updates the score display on the UI.
    - `updateBackgroundImage(int level)`: Changes the background image according to the game level.
    - Private methods like `createLevelLabel`, `createScoreLabel`, and `createHeartLabel` for individual UI components.
    - `arrangeLabelsOnUI()`: Arranges the labels in an HBox container for a cohesive UI layout.
- **Usage:** Essential for providing an interactive and informative UI, enhancing the player's experience by keeping them updated on game progress and adding visual appeal to the game.
- **Location:** Part of the `com.brickbreakergame.managers` package.

## Menus

### MainMenu
- **Purpose:** Represents the main menu of the Brick Breaker game, providing options to start a new game, load a saved game, or exit the application. It sets up UI elements like buttons and logos and configures their actions.
- **Key Features:**
    - Display of the main menu with buttons for starting a new game, loading a game, or exiting.
    - Configuration of UI elements, including their styles and layout.
    - Handling of button actions with corresponding sound effects.
- **Methods:**
    - `display()`: Displays the main menu and sets up the UI elements.
    - `createLogoView()`: Creates an ImageView for the game's logo.
    - `createButton(String imagePath, Consumer<Void> action, int width, int height)`: Creates a customized button with an image and specified action.
    - `loadImage(String path)`: Loads an image from the given file path.
    - `startNewGame()`: Initiates the process of starting a new game from the main menu.
- **Usage:** Acts as the entry point for players to navigate to different parts of the game, such as starting a new game or loading an existing one.
- **Location:** Part of the `com.brickbreakergame.menus` package.

### PauseMenu
- **Purpose:** Manages the in-game pause menu interface for the Brick Breaker game, providing various options during gameplay interruption like resuming the game, managing sound settings, saving or loading progress, restarting the current game, or quitting.
- **Key Features:**
    - Display and handling of the pause menu with multiple interactive options.
    - Configuration and styling of menu elements, such as buttons for different actions.
    - Integration with game's main functionality to perform actions like save, load, restart, and exit.
- **Methods:**
    - `display(Main main, GameEngine engine, Stage primaryStage)`: Displays the pause menu and sets up its functionalities.
    - `initializePauseStage()`: Initializes the pause menu stage with specific settings.
    - `configurePauseLayout()`: Configures the layout for the pause menu.
    - `addButtonsToLayout(Main main, GameEngine engine, LevelManager levelManager)`: Adds buttons for various functionalities to the pause menu's layout.
    - `createButton(String text, EventHandler<ActionEvent> action)`: Creates a button with specified text and action.
    - `positionPauseMenuOverGame(Stage primaryStage)`: Positions the pause menu over the game's primary stage.
    - `initializePauseMenuBlur(Stage primaryStage)`: Applies a Gaussian blur effect to the game window when the pause menu is displayed.
    - `disablePauseMenuBlur(Stage primaryStage)`: Removes the blur effect when the pause menu is closed.
    - `soundMenuOpen()`: Opens the sound settings menu.
    - `highlightSoundSystemButton()`, `resetSoundButtonStyle()`: Manages styling of the sound system button.
    - `getPauseLayout()`: Retrieves the VBox layout used in the pause menu.
- **Usage:** Essential for providing players with control over the game during interruptions, allowing them to manage game settings, save progress, or exit the game.
- **Location:** Part of the `com.brickbreakergame.menus` package.

### SoundMenu
- **Purpose:** Represents the in-game sound settings menu of the Brick Breaker game. This class manages the interface for adjusting the game's audio volume and mute/unmute settings, presented as a modal dialog over the pause menu.
- **Key Features:**
    - Display and configuration of sound settings options such as volume adjustment and mute toggle.
    - Integration with the game's audio system to reflect changes in sound settings.
    - User-friendly interface with sliders and buttons for audio control.
- **Methods:**
    - `display()`: Sets up and shows the sound settings menu.
    - `initializeSoundStage()`: Initializes the sound stage with specific settings for modality and style.
    - `configureSoundLayout()`: Configures the layout for the sound settings menu.
    - `createVolumeSlider()`: Creates a volume slider control for audio adjustment.
    - `createMuteButton()`: Creates a mute/unmute button.
    - `addVolumeControls()`: Adds volume control elements to the sound menu layout.
    - `addCloseButton()`: Adds a button to close the sound settings menu.
    - `positionSoundMenuNextToPauseMenu()`: Positions the sound menu next to the pause menu for a cohesive interface layout.
- **Usage:** Provides players with an accessible and intuitive interface for managing audio settings during gameplay, enhancing user experience.
- **Location:** Part of the `com.brickbreakergame.menus` package.

## Screens

### GameOverScreen
- **Purpose:** Manages and displays the game over interface for the Brick Breaker game when the player loses. This class provides a user interface with options to restart the game or return to the main menu, facilitating a smooth transition from the end of one game to the beginning of another or to the main menu.
- **Key Features:**
    - Display of the game over screen as a modal dialog over the primary game window.
    - Configuration and arrangement of UI elements such as the score display, restart button, and main menu button.
    - Integration with the game's main functionality for actions like restarting the game or returning to the main menu.
- **Methods:**
    - `display(Main main, Stage primaryStage)`: Sets up and positions the game over screen.
    - `initializeGameOverStage()`: Initializes the game over stage with appropriate settings.
    - `configureGameOverLayout()`: Configures the layout for the game over screen.
    - `addElementsToLayout(Main main, Stage primaryStage, LevelManager levelManager)`: Adds essential elements to the game over screen layout.
    - `createButton(String text, EventHandler<ActionEvent> action)`: Creates a styled button with specified text and action.
    - `positionGameOverMenuOverGame(Stage primaryStage)`: Positions the game over menu over the primary game window.
- **Usage:** Essential for providing players with end-game options, allowing them to easily restart the game or navigate to the main menu after losing.
- **Location:** Part of the `com.brickbreakergame.screens` package.

### YouWinScreen
- **Purpose:** Manages and displays the "You Win" screen in the Brick Breaker game. This class is responsible for presenting the victory interface when a player successfully completes the game, offering options to restart the game or return to the main menu.
- **Key Features:**
    - Display of the "You Win" screen as a modal dialog over the primary game window.
    - Configuration and arrangement of UI elements like the victory image, score display, restart button, and main menu button.
    - Integration with the game's main functionality for actions like restarting the game or returning to the main menu.
- **Methods:**
    - `display(Main main, Stage primaryStage)`: Sets up and positions the "You Win" screen.
    - `initializeYouWinStage()`: Initializes the "You Win" stage with specific settings.
    - `configureYouWinLayout()`: Configures the layout for the "You Win" screen.
    - `addElementsToLayout(Main main, Stage primaryStage, LevelManager levelManager)`: Adds essential elements to the "You Win" screen layout.
    - `createButton(String text, EventHandler<ActionEvent> action)`: Creates a styled button with specified text and action.
    - `positionYouWinMenuOverGame(Stage primaryStage)`: Positions the "You Win" menu over the primary game window.
- **Usage:** Essential for providing players with celebratory end-game options, allowing them to easily restart the game or navigate to the main menu after winning.
- **Location:** Part of the `com.brickbreakergame.screens` package.


## Modified Java Classes:

### Block Class
- **Modifications:**
    - Block dimensions (`width`, `height`, `paddingTop`, `paddingH`) are now constants and have been made private and final, standardizing block sizes across the game.
    - Block type names have been updated from `BLOCK_NORMAL`, `BLOCK_CHOCO`, `BLOCK_STAR`, `BLOCK_HEART` to `NORMAL`, `RANDOM`, `GOLDEN_TIME`, `HEART` for improved clarity.
    - The `draw()` method has been revised to use new image paths for different block types, aligning with the updated naming convention and directory structure.
    - Integration with `SoundManager` has been added in the `checkHitToBlock` method to play sound effects upon collision, enhancing the auditory feedback in the game.
    - Collision detection logic in `checkHitToBlock` has been improved to consider the ball's radius, resulting in more accurate collision handling.

- **Reason for Changes:**
    - These changes were necessary to improve the maintainability and readability of the code, ensuring consistent block dimensions throughout the game. Renaming block types and updating the image paths made the block characteristics clearer and more intuitive. The addition of sound effects in collision detection enriched the gameplay experience, providing better player feedback. Finally, enhancing the collision detection accuracy was crucial for a fair and enjoyable gaming experience.

### BlockSerializable Class
- **Modifications:**
  - Updated the package structure: changed from `brickGame` to `com.brickbreakergame` to adhere to standard Java package naming conventions.
  - Refactored the variable `j` to `column`, providing a clearer and more descriptive naming for the column position of the block.
  - Introduced a new field `colorIndex` to store the index of the block's color. This additional attribute facilitates more efficient serialization and deserialization of block colors.
  - Added a new method, `setColorIndex(Color[] colors, Color blockColor)`, to determine the color index of the block. This method allows for efficient matching of block colors against a predefined array of colors during the serialization process.
  - Enhanced documentation with JavaDoc comments, providing a clear description of the class's purpose and each method.

- **Reason for Changes:**
  - The changes were necessary to improve code readability and maintainability. The package name adjustment aligns with standard Java practices, enhancing project organization. Renaming `j` to `column` improves the clarity of the code, making it more intuitive to understand the block's grid position. The addition of `colorIndex` and the corresponding method streamlines the serialization process, allowing for a more compact and efficient representation of block colors. The added documentation enhances understanding and maintenance of the code, especially for future modifications or debugging.

### Bonus Class
- **Modifications:**
  - The class has been moved from the `brickGame` package to `com.brickbreakergame.managers`, indicating a shift in its structural organization within the project.
    Block type names have been updated from `choco` to `bonusImage` for improved clarity.
  - Renamed from `Bonus` to `BonusManager`, reflecting its enhanced role in managing bonuses in the game.
  - Added a reference to the `Main` class (`private Main main;`), integrating the bonus management more closely with the main game logic.
  - Changed the bonus drawing logic in the `draw()` method, standardizing the bonus appearance with a single image URL instead of random selection.
  - Introduced new methods to apply various bonus effects (`applyPaddleSizeEffect`, `applyScoreEffect`, `applyBallSizeEffect`) and a method to reset temporary changes (`resetTemporaryChanges`). These additions bring more diverse and dynamic interactions to the game's bonuses.
  - Incorporated `SoundManager` and `AnimationManager` for auditory feedback and visual effects, enriching the game experience.

- **Reason for Changes:**
  - The relocation and renaming of the class to `BonusManager` under a new package structure align with best practices in software design, providing clearer organization and purpose. The integration with the `Main` class allows for direct manipulation of game states based on bonus effects, enhancing gameplay interactivity. The standardization of the bonus appearance ensures a consistent visual style. The addition of new methods for applying and resetting bonus effects broadens the gameplay dynamics, making the bonuses more impactful. The integration of sound and animation managers adds depth to the player's experience, making the game more engaging and immersive.

### GameEngine Class
- **Modifications:**
  - The class has been updated to provide a more structured and robust game loop with dedicated threads for updates, physics, and time tracking.
  - Removed the separate `Initialize` method and integrated its functionality within the `start` method for streamlined execution.
  - Improved exception handling and thread interruption logic to ensure smooth operation and safe termination of the game engine.
  - Introduced input validation in `setFps`, throwing an `IllegalArgumentException` if a non-positive fps value is provided, ensuring the game runs at a valid frame rate.
  - Changed the names of constants and variables for clarity, such as renaming `fps` to `frameTimeMillis`, making the code more readable and maintainable.
  - Updated method names to be more descriptive (`runUpdateLoop`, `runPhysicsLoop`, `runTimeLoop`), providing clearer insight into their responsibilities.
  - Removed the `OnAction` nested interface and its implementation from within the class, suggesting a separation of concerns and better encapsulation of the game engine's functionality.

- **Reason for Changes:**
  - The updates to the `GameEngine` class are aimed at enhancing the performance, maintainability, and readability of the game's core engine. The restructuring of the game loop into separate threads for game updates, physics calculations, and time management leads to more efficient and reliable game execution. The introduction of input validation and improved exception handling ensures the stability and robustness of the game engine. Renaming variables and methods, and removing the nested `OnAction` interface, aligns with best coding practices, providing a clean, modular, and easily understandable codebase. These changes collectively contribute to a more effective and streamlined game engine, essential for a smooth and engaging gaming experience.

### LevelManager Class
- **Modifications:**
  - Renamed from `LoadSave` to `LevelManager` to better reflect its purpose and functionality.
  - Integrated with `Main` and `Stage` objects to directly manipulate the game's state and UI.
  - Added `restartGame` method to reset the game state to initial conditions and reinitialize the game.
  - Introduced `nextLevel` method to handle transitions between levels, updating game state and reinitializing as needed.
  - Improved exception handling and platform compatibility by using `Platform.runLater` for UI updates.

- **Reason for Changes:**
  - Renaming to `LevelManager` and integrating with `Main` and `Stage` represents a more focused approach to managing game levels and progression. The new `restartGame` and `nextLevel` methods provide a clear and direct way to reset and advance the game, aligning with the class's revised purpose. Improved exception handling and platform compatibility ensure that the game runs smoothly across different environments. The restructured class now serves as a central point for managing game levels and state transitions, providing a cleaner and more maintainable codebase.


### Main Class
- **Modifications:**
  - Refactored code for enhanced readability and maintainability, including separation of concerns and addition of inline comments.
  - Improved integration with `GameEngine` for efficient game loop management, including frame rate control and smoother updates.
  - Enhanced User Interface (UI), with updated visuals for gameplay elements and improved responsiveness.
  - Introduced new game mechanics and power-ups, such as variable paddle and ball sizes, 'Gold' mode, and advanced collision detection.
  - Refined paddle and ball movement logic for smoother control and more realistic physics.
  - Overhauled collision detection system for more accurate interactions between game elements.
  - Implemented a more comprehensive scoring system and heart (life) management, enhancing the game's challenge and player engagement.
  - Introduced a more structured level progression system with the ability to handle transitions between levels, restart the game, and load saved games.
  - Integrated sound effects and animations to enhance the gaming experience, making the game more immersive and enjoyable.
  - Added a system to manage bonuses (bonus.png), including their creation, effects, and collection.
  - Improved game state management to keep track of various aspects like game level, score, heart count, block count, and paddle and ball properties.

- **Reason for Changes:**
  - The refactoring and enhancements were necessary to improve the game's performance, scalability, and player experience.
  - The UI enhancements and new game mechanics were implemented to make the game more engaging, challenging, and enjoyable for players.
  - The improved paddle and ball movement, along with the refined collision system, offer a more realistic and responsive gameplay experience. 
  - The structured level management and game state tracking allow for smoother transitions between game states and levels, providing a more cohesive gaming experience.
  - The addition of sound and animation enriches the overall game atmosphere, making it more immersive.
  - The bonus management system adds an extra layer of strategy and excitement to the gameplay.

### Score Class
- **Modifications:**
  - **Refactored Method Structures:** Reorganized class methods for clearer logic and better readability, focusing on specific tasks like creating labels, displaying score, and showing messages.
  - **Enhanced Animations:** Advanced animation techniques using JavaFX's `ScaleTransition` and `FadeTransition` for a dynamic and visually appealing representation of score changes and game messages.
  - **Game Engine Integration:** Improved interaction with `GameEngine` for synchronized score updates and game event notifications.
  - **Improved Game Event Handling:** More sophisticated methods for displaying game-over and win scenarios, using dedicated screens and sound effects.
  - **Styling and Theming:** Enhanced visual presentation of score labels and messages with CSS styling.
  - **New Flash Message Effect:** Introduction of a flash message effect to highlight significant game events, adding dramatic impact.
  - **Level Check Method:** `checkLevels` method to handle actions based on the game's current level, important for game progression and stage transitions.

- **Reason for Changes:**
  - **Code Modularity and Maintainability:** To improve the modularity and maintainability of the code.
  - **Player Engagement:** To make the game more engaging and visually appealing with advanced animations and enhanced styling.
  - **Accurate Score Tracking:** To ensure accurate and prompt updates of the game's score system, reflecting player progress.
  - **Enhanced Player Experience:** To provide thematic elements like specific screens for game-over and winning scenarios, enriching the gaming experience.
  - **Dramatic Gameplay Impact:** To utilize the new flash message effect as a powerful tool for highlighting crucial game moments.

## Unexpected Problems:
These are the major issues and fixes with unique solutions.

### Refactor Trouble
- **Issue**: I did not realise how much trouble moving my own refactored creation would give me. Following the correct principles would have saved both time and effort.
      - **Solution**: Spending days manually fixing what I could in the code perfectly
  
### Visual Errors
- **Issue**: The game kept producing visual errors where the ball and paddle disappeared but still interacted with the game screen as normal 
    - **Solution**: Adding threads to gui seemed to fix a majority of the issues faced. Moving the ui to a different class fixed the remaining errors.
    
### Load Game 
- **Issue**: Moving the loadgame to a different class kept producing errors.
    - **Solution**: Initializing the loadgame ball in the gamecontroller class itself fixed the first half of the issue. Making getroot a getter fixed the other half
  
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

### Main Menu being broken
- **Issue**: Main menu stopped working randomly one day
    - **Solution**: Moving the function to a different class and moving around the variable flags

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