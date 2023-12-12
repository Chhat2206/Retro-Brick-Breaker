package com.brickbreakergame;

/**
 * The GameEngine class orchestrates the main game loop, physics computations,
 * and time tracking for a brick breaker game. This modular engine allows for the implementation
 * of customized game logic and physics through a set of defined callback interfaces.
 * It is crafted to ensure efficient and consistent execution of game dynamics.
 */
public class GameEngine {
    /**
     * The OnAction interface defines callbacks for key game engine actions.
     * Implementations of this interface provide specific behaviors for updating game logic,
     * managing physics, and tracking game time. These methods are invoked at regular intervals
     * by the GameEngine to maintain a smooth and responsive game experience.
     */
    private OnAction onAction;
    private int frameTimeMillis = 1000 / 120;
    private Thread updateThread, physicsThread, timeThread;
    private volatile boolean running = false;
    private long time = 0;
    public interface OnAction {

        /**
         * Executes game logic updates, facilitating real-time interactions and events within the game.
         */
        void onUpdate();

        /**
         * Manages physics updates, responsible for handling dynamics and kinematics of game elements.
         */
        void onPhysicsUpdate();

        /**
         * Monitors and updates game time, essential for time-dependent game mechanics.
         *
         * @param time The current time in milliseconds.
         */
        void onTime(long time);
    }

    /**
     * Assigns the implementation of the OnAction interface to handle game actions.
     *
     * @param onAction The concrete implementation of the OnAction interface.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Configures the desired frame rate of the game, impacting game fluidity and responsiveness.
     *
     * @param fps The target frames per second for the game.
     * @throws IllegalArgumentException If {@code fps} is non-positive.
     */
    public void setFps(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be greater than 0");
        }
        this.frameTimeMillis = 1000 / fps;
    }

    /**
     * Initiates the game engine's operational cycle. It launches separate threads for game updates,
     * physics, and time management. This method ensures that the engine starts only if it is not already active.
     */
    public void start() {
        if (running) {
            return;
        }

        running = true;
        time = 0;

        updateThread = new Thread(this::runUpdateLoop);
        physicsThread = new Thread(this::runPhysicsLoop);
        timeThread = new Thread(this::runTimeLoop);

        updateThread.start();
        physicsThread.start();
        timeThread.start();
    }

    /**
     * Halts the operation of the game engine. It safely terminates the update, physics, and time tracking threads.
     * The shutdown process is engaged only if the engine is currently active.
     */
    public void stop() {
        if (!running) {
            return;
        }

        running = false;
        interruptAndJoin(updateThread);
        interruptAndJoin(physicsThread);
        interruptAndJoin(timeThread);
    }

    /**
     * Executes the main game update loop. This method is pivotal in maintaining the continuity
     * of the game's state. It repeatedly invokes the onUpdate method of the OnAction interface,
     * ensuring that game logic is consistently processed at each frame.
     */
    private void runUpdateLoop() {
        while (running) {
            onAction.onUpdate();
            sleep(frameTimeMillis);
        }
    }

    /**
     * Manages the physics update loop. This method plays a critical role in the real-time simulation
     * of physical interactions within the game. It continuously calls the onPhysicsUpdate method
     * of the OnAction interface, providing regular updates to the game's physics.
     */
    private void runPhysicsLoop() {
        while (running) {
            onAction.onPhysicsUpdate();
            sleep(frameTimeMillis);
        }
    }

    /**
     * Conducts the time tracking loop. This method is integral to the tracking of elapsed game time.
     * It methodically increments the game time and invokes the onTime method of the OnAction interface.
     * These actions are performed repeatedly, ensuring accurate and up-to-date time tracking
     * for the duration of the game engine's operation.
     */
    private void runTimeLoop() {
        while (running) {
            time++;
            onAction.onTime(time);
            sleep(1);
        }
    }

    /**
     * Temporarily suspends the execution of the current thread for a specified duration.
     * This method is used to control the timing of the game loop and other operations, allowing
     * for consistent and controlled execution pacing.
     *
     * @param millis The duration, in milliseconds, for which the thread is to be suspended.
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Ensures the safe termination of a specified thread. This method interrupts and then joins
     * the given thread, facilitating a controlled and orderly shutdown of thread-based operations.
     * It is a critical method for ensuring that the game engine's threads terminate correctly.
     *
     * @param thread The thread that is to be interrupted and joined.
     */
    private void interruptAndJoin(Thread thread) {
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
