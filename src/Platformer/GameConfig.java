package Platformer;

/** Shared dimensions and tuning values for the game. */
final class GameConfig {
    static final int VIEW_WIDTH = 800;
    static final int VIEW_HEIGHT = 600;
    static final int WORLD_WIDTH = 2400;
    static final int PLAYER_WIDTH = 30;
    static final int PLAYER_HEIGHT = 40;
    static final int MOVE_SPEED = 5;
    static final int GRAVITY = 1;
    static final int JUMP_STRENGTH = -15;
    static final int TIMER_DELAY_MS = 16;

    private GameConfig() { }
}
