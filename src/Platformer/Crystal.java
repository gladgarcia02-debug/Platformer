package Platformer;

import java.awt.Rectangle;

/** Represents one collectible crystal in world coordinates. */
final class Crystal {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 26;

    private final int x;
    private final int y;
    private boolean collected;

    Crystal(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int x() { return x; }
    int y() { return y; }
    int width() { return WIDTH; }
    int height() { return HEIGHT; }
    boolean isCollected() { return collected; }

    Rectangle bounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /** Returns true only the first time this crystal is collected. */
    boolean collect() {
        if (collected) {
            return false;
        }
        collected = true;
        return true;
    }

    void reset() {
        collected = false;
    }
}
