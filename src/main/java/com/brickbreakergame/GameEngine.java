package com.brickbreakergame;

/**
 * A game engine designed for a brick breaker game, managing game updates, physics, and time tracking.
 * It supports customization of game logic, physics, and time management through callbacks.
 */
public class GameEngine {

    /**
     * Callback interface for handling game-related actions.
     * Implementations of this interface will define specific actions for game updates, physics, and time tracking.
     */
    private OnAction onAction;

    /**
     * Frame time in milliseconds, which determines the game's frame rate (FPS).
     * Default FPS is set to 120.
     */
    private int frameTimeMillis = 1000 / 120;

    /**
     * Threads for running the game loop, physics updates, and time tracking.
     */
    private Thread updateThread, physicsThread, timeThread;

    /**
     * Flag indicating whether the game engine is currently running.
     */
    private volatile boolean running = false;

    /**
     * Current game time in milliseconds.
     */
    private long time = 0;

    /**
     * Interface for defining callback methods for game updates, physics, and time tracking.
     */
    public interface OnAction {

        /**
         * Handles game logic updates.
         */
        void onUpdate();

        /**
         * Handles physics updates.
         */
        void onPhysicsUpdate();

        /**
         * Tracks and handles time updates.
         *
         * @param time Current time in milliseconds.
         */
        void onTime(long time);
    }

    /**
     * Sets the callback interface for handling game-related actions.
     *
     * @param onAction Callback interface implementation.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the target frame rate (FPS) for the game.
     *
     * @param fps Target frames per second.
     * @throws IllegalArgumentException If {@code fps} is less than or equal to 0.
     */
    public void setFps(int fps) {
        if (fps <= 0) {
            throw new IllegalArgumentException("FPS must be greater than 0");
        }
        this.frameTimeMillis = 1000 / fps;
    }

    /**
     * Starts the game engine. Initializes and starts threads for game updates, physics, and time tracking.
     * Only starts the engine if it is not already running.
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
     * Stops the game engine. Interrupts and joins the update, physics, and time tracking threads.
     * Only attempts to stop the engine if it is currently running.
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
     * Runs the main game update loop. Continuously invokes the game update callback while the engine is running.
     */
    private void runUpdateLoop() {
        while (running) {
            onAction.onUpdate();
            sleep(frameTimeMillis);
        }
    }

    /**
     * Runs the physics update loop. Continuously invokes the physics update callback while the engine is running.
     */
    private void runPhysicsLoop() {
        while (running) {
            onAction.onPhysicsUpdate();
            sleep(frameTimeMillis);
        }
    }

    /**
     * Runs the time tracking loop. Continuously increments time and invokes the time update callback while the engine is running.
     */
    private void runTimeLoop() {
        while (running) {
            time++;
            onAction.onTime(time);
            sleep(1);
        }
    }

    /**
     * Sleeps the current thread for a specified duration.
     *
     * @param millis Duration in milliseconds for the thread to sleep.
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Safely interrupts and joins a thread.
     *
     * @param thread The thread to be interrupted and joined.
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
