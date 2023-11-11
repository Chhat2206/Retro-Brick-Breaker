# Block Breaker Game
## Compilation Instructions:
- Provide a clear, step-by-step guide on how to compile the code to produce the application. Include any dependencies or special settings required.

## Implemented and Working Properly:
- List the features that have been successfully implemented and are functioning as expected. Provide a brief description of each.

## Implemented but Not Working Properly:
- List any features that have been implemented but are not working correctly. Explain the issues you encountered, and if possible, the steps you took to address them.

## Features Not Implemented:
- Identify any features that you were unable to implement and provide a clear explanation for why they were left out.

## New Java Classes:
- Enumerate any new Java classes that you introduced for the assignment. Include a brief description of each class's purpose and its location in the code.

## Modified Java Classes:
- List the Java classes you modified from the provided code base. Describe the changes you made and explain why these modifications were necessary.

## Unexpected Problems:
Communicate any unexpected challenges or issues you encountered during the assignment. Describe how you addressed or attempted to
resolve them.
- Collision Detection is completely messed up in this game. We have currently fixed half the problem:

Adding the ball radius to the 'checkHitToBlock' function allows every block to detect and dissapear when the ball collides with the block.

The next issue to solve is making the ball bounce when it hits with the object

- Game engine runs after every life for no reason. This makes it so the pause menu is completely broken when the user loses a heart. 
- two threads accessing array at the same time so it gives a concurrent error, that is what breaks the game after lvl 1
