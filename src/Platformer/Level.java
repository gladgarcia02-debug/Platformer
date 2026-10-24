package Platformer;

import java.awt.Rectangle;

/** Immutable geometry for the current level. */
final class Level {
    private final Rectangle[] platforms = {
        new Rectangle(0, 520, GameConfig.WORLD_WIDTH, 80),
        new Rectangle(150, 430, 120, 20),
        new Rectangle(320, 350, 120, 20),
        new Rectangle(490, 280, 120, 20),
        new Rectangle(700, 420, 150, 20),
        new Rectangle(900, 350, 140, 20),
        new Rectangle(1100, 290, 160, 20),
        new Rectangle(1320, 380, 140, 20),
        new Rectangle(1530, 320, 150, 20),
        new Rectangle(1750, 410, 150, 20),
        new Rectangle(1980, 340, 160, 20),
        new Rectangle(2200, 430, 120, 20)
    };
    private final Rectangle goal = new Rectangle(2300, 450, 30, 70);

    Rectangle[] platforms() {
        Rectangle[] copy = new Rectangle[platforms.length];
        for (int i = 0; i < platforms.length; i++) {
            copy[i] = new Rectangle(platforms[i]);
        }
        return copy;
    }

    Rectangle goal() {
        return new Rectangle(goal);
    }
}
