package brickGame;

public class GameEngine {

    /**
     * The callback interface to be set by the user of the `GameEngine` to handle game-related actions.
     */
    private OnAction onAction;

    /**
     * The frame time in milliseconds, which determines the frame rate (FPS) of the game.
     */
    private int frameTimeMillis = 1000 / 120; // Default FPS is 120

    /**
     * Threads for running the game loop and time tracking.
     */
    private Thread updateThread, physicsThread, timeThread;

    /**
     * A flag to indicate whether the game engine is currently running.
     */
    private volatile boolean running = false;

    /**
     * The current time in milliseconds.
     */
    private long time = 0;

    /**
     * Interface for defining callback methods to handle game updates, physics updates, and time tracking.
     */
    public interface OnAction {

        /**
         * Callback method for handling game logic updates.
         */
        void onUpdate();

        /**
         * Callback method for handling physics updates.
         */
        void onPhysicsUpdate();

        /**
         * Callback method for tracking time.
         *
         * @param time The current time in milliseconds.
         */
        void onTime(long time);
    }


    /**
     * Sets the callback interface to handle game-related actions.
     *
     * @param onAction The callback interface.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the desired frame rate (FPS) for the game.
     *
     * @param fps The target frames per second.
     * @throws IllegalArgumentException if `fps` is less than or equal to 0.
     */
    public void setFps(int fps) {
        if (fps > 0) {
            this.frameTimeMillis = (int) (1000.0 / fps);
        } else {
            throw new IllegalArgumentException("FPS must be greater than 0");
        }
    }

    /**
     * Starts the game engine by creating and starting separate threads for game updates, physics updates,
     * and time tracking.
     */
    public void start() {
        if (running) return;

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
     * Stops the game engine by interrupting and joining the update, physics, and time tracking threads.
     */
    public void stop() {
        if (!running) return;

        running = false;
        interruptAndJoin(updateThread);
        interruptAndJoin(physicsThread);
        interruptAndJoin(timeThread);
    }

    /**
     * The main game update loop.
     */
    private void runUpdateLoop() {
        while (running) {
            onAction.onUpdate();
            sleep(frameTimeMillis);
        }
    }

    /**
     * The physics update loop.
     */
    private void runPhysicsLoop() {
        while (running) {
            onAction.onPhysicsUpdate();
            sleep(frameTimeMillis);
        }
    }

    /**
     * The time tracking loop, which increments the time in milliseconds and invokes the time callback.
     */
    private void runTimeLoop() {
        while (running) {
            time++;
            onAction.onTime(time);
            sleep(1);
        }
    }

    /**
     * Helper method to sleep the current thread for the specified number of milliseconds.
     *
     * @param millis The number of milliseconds to sleep.
     */
    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Helper method to interrupt and join a thread safely.
     *
     * @param thread The thread to interrupt and join.
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
