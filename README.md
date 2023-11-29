# Block Breaker Game
## Compilation Instructions:
- Provide a clear, step-by-step guide on how to compile the code to produce the application. Include any dependencies or special settings required.

## Implemented and Working Properly:
- List the features that have been successfully implemented and are functioning as expected. Provide a brief description of each.

## Implemented but Not Working Properly:
Everything is working properly.

## Features Not Implemented:
- **Feature**: Pause button
  - **Issue**: Main menu did pause very smoothly, no need for a dedicated button

- **Feature**: Custom sound menu to tweak every sound in the game
  - **Issue**: Seems unnecessary when I modified each sound to be pleasing. I all-ready demonstrated the knowledge of modifying sound with one slider. I also could not make the menu look good.

- **Feature**: Pause Menu Image, fade out of the pause menu, fixing score, custom paddle and ball chosen by the user, custom level difficulty
  - **Issue**: The features above (and those unmentioned) were implemented but did not fit the theme and style of the game, as such were removed.

- **Feature**:
  - **Issue**:

## New Java Classes:
- Enumerate any new Java classes that you introduced for the assignment. Include a brief description of each class's purpose and its location in the code.

## Modified Java Classes:
- List the Java classes you modified from the provided code base. Describe the changes you made and explain why these modifications were necessary.

## Unexpected Problems:
All issues not mentioned here were uninteresting to describe. These are the major issues and fixes with unique solutions.
### Collision Detection
- **Issue**: Collision detection is problematic.
  - **Solution**: Partially resolved by incorporating the ball radius into the 'checkHitToBlock' function, allowing blocks to detect and disappear upon collision with the ball. From there, add many collision checks and redundancy to account for any change in ball movement. ballRadius is added to all relevant items that collide with the ball to ensure smooth gameplay.

### Movement Game Issues
- **Issue**: The ball, paddle, and all gui items with velocity would randomly disappear or have strange properties.
  - **Solution**: Rewrite every function to connect it to threads instead of being interdependent to other non-related states.

### Persistent Pause Menu
- **Issue**: The pause menu remained in place when the game screen was moved.
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
  
###
- **Issue**:
  - **Solution**:


## Credits
The items that are not listed below were hand created by me.

### Images
#### General Assets
- [Paddle Design](https://kenney.nl/assets/shape-characters)

### Sound Effects
#### Background Music
- [backgroundMusicSoftPiano](https://pixabay.com/sound-effects/soft-piano-100-bpm-121529/)
- [backgroundMusicCosmic](https://pixabay.com/sound-effects/spaceship-ambience-with-effects-21420/)
- [backgroundMusicAnimePiano](https://pixabay.com/sound-effects/the-last-piano-112677/)
- [backgroundMusicMorningVibes](https://pixabay.com/sound-effects/good-morning-heaven-no-drums-55690/)

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