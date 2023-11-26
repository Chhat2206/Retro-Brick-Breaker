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
  - **Solution**: Investigate and resolve the visual glitches affecting the paddle, objects, and other elements in the game.


## Credits


### Sound Effects

### Images
#### Background Images
- [backgroundImage-4](https://wallpapercave.com/wp/wp1933991.jpg)
- [backgroundImage-5](https://wallpapercave.com/wp/wp7495828.jpg)
- [backgroundImage-6](https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwallpaperaccess.com%2Ffull%2F1249933.jpg&f=1&nofb=1&ipt=e2886b5a3ba670dac4164b8be29b02d4fc81650ca6bd4137412dbe209bfa5679&ipo=images)
- [backgroundImage-7](https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwallpaperaccess.com%2Ffull%2F1439591.jpg&f=1&nofb=1&ipt=1acbca1f4ba13fdefc9b62e9297e2a5ffd8ac63ca81308b7fc0f0c339e735c34&ipo=images)
- [backgroundImage-8](https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwallpaperaccess.com%2Ffull%2F1485290.jpg&f=1&nofb=1&ipt=f56910d13f6ae0a854e07282d049446d5b4408a69cdfbffb32e1b0affcb631bd&ipo=images)
- [backgroundImage-9](https://get.pxhere.com/photo/landscape-tree-water-nature-grass-outdoor-horizon-cloud-plant-sky-field-night-meadow-prairie-countryside-sunlight-lake-atmosphere-land-pond-environment-evening-reflection-crop-scenic-color-peaceful-natural-scenery-calm-serene-blue-rest-colorful-agriculture-moon-season-plain-relaxation-single-stars-nightsky-grassland-lone-beauty-inspiration-amazing-beautiful-scene-natur-reflex-exciting-breathtaking-incredible-grass-family-atmosphere-of-earth-computer-wallpaper-883277.jpg)

#### General Assets
- [Paddle & General Block Design](https://kenney.nl/assets/shape-characters)

### Sound Effects
#### Background Music
- [backgroundMusicSoftPiano](https://pixabay.com/sound-effects/soft-piano-100-bpm-121529/)
- [backgroundMusicNCS](http://ncs.lnk.to/karmaAT/youtube)
- [backgroundMusicSoftPiano](https://pixabay.com/sound-effects/8bit-music-for-game-68698/)

#### Menus
- [menuOpen](https://pixabay.com/sound-effects/game-start-6104/)

#### General Sounds
- [collectPowerup](https://pixabay.com/sound-effects/coin-collect-retro-8-bit-sound-effect-145251/)
- [buttonClickSound](https://pixabay.com/sound-effects/click-button-app-147358/)
- [changeBackgroundMusic](https://pixabay.com/sound-effects/tape-cassette-insert-172758/)
- [muteSoundPauseMenu](https://pixabay.com/sound-effects/click-21156/)
- [levelUp](https://pixabay.com/sound-effects/game-bonus-144751/)
- [gameOver](https://pixabay.com/sound-effects/videogame-death-sound-43894/)
- [winSound](https://pixabay.com/sound-effects/winsquare-6993/)
- [goldBallPowerUp](https://pixabay.com/sound-effects/collect-5930/)
- [collectBonus](https://pixabay.com/sound-effects/collectcoin-6075/)
- [ballHitFloor](https://pixabay.com/sound-effects/jazz-bass-open-e-39297/)
- [paddleBounce](https://pixabay.com/sound-effects/stop-13692/)

???
- [interface-124464](https://pixabay.com/sound-effects/interface-124464/)
- [interface-soft-click-131438](https://pixabay.com/sound-effects/interface-soft-click-131438/)
- [buttonClickSound](https://pixabay.com/sound-effects/click-for-game-menu-131903/)