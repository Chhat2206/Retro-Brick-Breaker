# Block Breaker Game
## Compilation Instructions:
- Provide a clear, step-by-step guide on how to compile the code to produce the application. Include any dependencies or special settings required.
  
- src, pom, gitignore
- 
## Implemented and Working Properly:
- List the features that have been successfully implemented and are functioning as expected. Provide a brief description of each.

## Implemented but Not Working Properly:
- List any features that have been implemented but are not working correctly. Explain the issues you encountered, and if possible, the steps you took to address them.

## Features Not Implemented:
- 


## New Java Classes:
- Enumerate any new Java classes that you introduced for the assignment. Include a brief description of each class's purpose and its location in the code.

## Modified Java Classes:
- List the Java classes you modified from the provided code base. Describe the changes you made and explain why these modifications were necessary.

## Unexpected Problems:
### Collision Detection
- **Issue**: Collision detection is problematic.
  - **Solution**: Partially resolved by incorporating the ball radius into the 'checkHitToBlock' function, allowing blocks to detect and disappear upon collision with the ball.

### Pause Menu Addition
- **Issue**: Adding a pause menu led to access modifier changes and problems with automatic closing when loading the game.
  - **Solution**: Changed access modifiers from private to protected for access in the same package. Menu should automatically close when loading the game.

### Loading Game Issues
- **Issue**: Loading the game caused various issues like block changes, increased ball velocity, and level progression problems. Reopening the game after minimizing the pause menu displayed only the pause menu.
  - **Solution**: Investigate and fix the issues related to loading game and pause menu behavior.

### Button Commands Loop
- **Issue**: Running commands on buttons sometimes triggered a feedback loop with repeated 'esc' key actions. Moving code from the main menu to functions caused cascading issues.
  - **Solution**: Analyze and address the feedback loop issue and refactor code to prevent cascading problems.

### Window Size Limitation
- **Issue**: Attempting to limit the window size caused problems with the pause menu and sound effects.
  - **Solution**: Investigate the issue with window size limitation and resolve the problems with the pause menu and sound effects.

### Persistent Pause Menu
- **Issue**: The pause menu remained in place when the game screen was moved.
  - **Solution**: Resolved by adjusting the position of the pause menu based on the game screen's position.

### Game Engine After Life
- **Issue**: The game engine ran after every life, causing problems with the pause menu.
  - **Solution**: Investigate and fix the issue related to the game engine running after a life is lost, potentially due to concurrent access to an array.

### Block Collision
- **Issue**: Block collision was completely broken and required a rewrite of the handleBlockCollisions function.
  - **Solution**: Analyze the block collision issue and rewrite the function to ensure proper collision handling.

### Score Freeze
- **Issue**: The score sometimes froze in place, and falling objects gained unexpected physics.
  - **Solution**: Investigate and resolve the issues related to score freezing and unexpected physics in falling objects.

### Ball Disappearance
- **Issue**: The ball occasionally disappeared during gameplay.
  - **Solution**: Implement additional checks and fixes to prevent the ball from disappearing unexpectedly.

### Fading Out
- **Issue**: Fading out didn't work as expected, and there were issues with the sound menu.
  - **Solution**: Review and address the problems related to fading out and sound menu behavior.

### Background Function Bug
- **Issue**: Loading the game caused issues with the background function.
  - **Solution**: Investigate and fix the background function issue, which may involve checking why it increases by 1.

### Visual Glitches
- **Issue**: Visual glitches like afterimages for the paddle and objects stuck in the air were observed.
  - **Solution**: Multiple problems present themselves. The threads clog up the memory in this game very quickly because they are created every time a level is created and an object is initialized. 
  - Large image files seems to have a hard time rendering in the game. Clear up the memory and lower the image quality.


## Credits
### Images
#### General Assets
- [Paddle Design](https://kenney.nl/assets/shape-characters)

### Sound Effects
#### Background Music
- [backgroundMusicSoftPiano](https://pixabay.com/sound-effects/soft-piano-100-bpm-121529/)
- [backgroundMusicSoftPiano](https://pixabay.com/sound-effects/8bit-music-for-game-68698/)
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