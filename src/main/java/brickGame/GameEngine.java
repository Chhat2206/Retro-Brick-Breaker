package brickGame;

public class GameEngine {

    private OnAction onAction;
    private int frameTimeMillis = 1000 / 120; // Default FPS is 120
    private Thread updateThread, physicsThread, timeThread;
    private volatile boolean running = false;
    private long time = 0;

    public interface OnAction {
        void onUpdate();
        void onPhysicsUpdate();
        void onTime(long time);
    }

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    public void setFps(int fps) {
        if (fps > 0) {
            this.frameTimeMillis = 1000 / fps;
        } else {
            throw new IllegalArgumentException("FPS must be greater than 0");
        }
    }

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

    public void stop() {
        if (!running) return;

        running = false;
        interruptAndJoin(updateThread);
        interruptAndJoin(physicsThread);
        interruptAndJoin(timeThread);
    }

    private void runUpdateLoop() {
        while (running) {
            onAction.onUpdate();
            sleep(frameTimeMillis);
        }
    }

    private void runPhysicsLoop() {
        while (running) {
            onAction.onPhysicsUpdate();
            sleep(frameTimeMillis);
        }
    }

    private void runTimeLoop() {
        while (running) {
            time++;
            onAction.onTime(time);
            sleep(1);
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

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
