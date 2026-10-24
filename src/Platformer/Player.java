package Platformer;

/** Mutable player state, kept separate from the Swing view. */
final class Player {
    int x = 50;
    int y = 450;
    int velocityY;
    boolean onGround;
    boolean facingRight = true;

    void reset() {
        x = 50;
        y = 450;
        velocityY = 0;
        onGround = false;
        facingRight = true;
    }
}
