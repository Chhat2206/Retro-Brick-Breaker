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
- Identify any features that you were unable to implement and provide a clear explanation for why they were left out.
a pause button. it was implemented but i wanted to remove mouse cursor to make the game run smoother


## New Java Classes:
- Enumerate any new Java classes that you introduced for the assignment. Include a brief description of each class's purpose and its location in the code.

## Modified Java Classes:
- List the Java classes you modified from the provided code base. Describe the changes you made and explain why these modifications were necessary.

## Unexpected Problems:
- Collision Detection is completely messed up in this game. We have currently fixed half the problem:

Adding the ball radius to the 'checkHitToBlock' function allows every block to detect and disappear when the ball collides with the block.

- Added pause menu

Main menu implementation required passing the main and engine functions. Access Modifier must be changed from private to protected to give access in the same package.
When loading game, menu should be automatically closed. 

- Loading game changed blocks, speeds up ball velocity & does not progress level 
When i minimize my pause menu and open the game back up, it only shows the pause menu and not the entire game

When running any commands to my buttons, they sometimes will fall into a feedback loop of esc working, then pressing esc again quickly closes it, then repeats over and over
when taking anything out of the main menu into functions, it creates a massive butterfly effect throughout the code
trying to limit window size broke the pause menu
sometimes sounds would not work

- pause menu would remain in place when game screen was moved. fixed with the below code
private static void positionPauseMenuOverGame(Stage primaryStage) {
  pauseStage.setX(primaryStage.getX() + primaryStage.getWidth() / 3.3 - pauseLayout.getPrefWidth() / 2);
  pauseStage.setY(primaryStage.getY() + primaryStage.getHeight() / 6 - pauseLayout.getPrefHeight() / 2);
  }

- Game engine runs after every life for no reason. This makes it so the pause menu is completely broken when the user loses a heart. 
- two threads accessing array at the same time so it gives a concurrent error, that is what breaks the game after lvl 1

- Falling objects gain extra physics (bounce upwards) when engine is stopped
# Credits:
Background Music:
background-music-soft-piano: https://pixabay.com/sound-effects/soft-piano-100-bpm-121529/

background-music-ncs: Song: Alaina Cross - Karma [NCS Release]
Music provided by NoCopyrightSounds
Free Download/Stream: http://ncs.io/karma
Watch: http://ncs.lnk.to/karmaAT/youtube 

Ball:
<a href="https://www.freepik.com/free-vector/variety-balls-with-unique-patterns_1164446.htm#query=ball%20game&position=0&from_view=keyword&track=ais">Image by brgfx</a> on Freepik

Interface: https://pixabay.com/sound-effects/interface-124464/

button: https://pixabay.com/sound-effects/button-124476/

block-hit: https://pixabay.com/sound-effects/stop-13692/

https://pixabay.com/sound-effects/interface-soft-click-131438/

https://pixabay.com/sound-effects/click-for-game-menu-131903/

paddle & block assets: https://kenney.nl/assets/shape-characters

Backgrounds:
https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwallpapercave.com%2Fwp%2Fwp1933991.jpg&f=1&nofb=1&ipt=fc0b8ee5c987bf5da44add35593287a16a29a3c3e22aff1eabc7c4d7aa071bb5&ipo=images
https://wallpapercave.com/dwp2x/wp1933955.png
https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwallpapercave.com%2Fwp%2Fwp7495828.jpg&f=1&nofb=1&ipt=1bd232da5f62157ec8678330150854cac87851b14658277ad6c770e645afd960&ipo=images

muteMusicPauseMenu: https://pixabay.com/sound-effects/click-21156/
win sound: https://pixabay.com/sound-effects/game-bonus-144751/
game over: https://pixabay.com/sound-effects/videogame-death-sound-43894/
level up:https://pixabay.com/sound-effects/level-up-47165/
ball hit the bottom: https://pixabay.com/sound-effects/jazz-bass-open-e-39297/