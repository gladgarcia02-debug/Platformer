package Platformer;

import java.awt.Point;
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
    private final Point[] crystalSpawns = {
        new Point(195, 400), new Point(365, 320), new Point(535, 250),
        new Point(765, 390), new Point(960, 320), new Point(1170, 260),
        new Point(1375, 350), new Point(1595, 290), new Point(1810, 380),
        new Point(2050, 310), new Point(2245, 400)
    };

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

    Point[] crystalSpawns() {
        Point[] copy = new Point[crystalSpawns.length];
        for (int i = 0; i < crystalSpawns.length; i++) {
            copy[i] = new Point(crystalSpawns[i]);
        }
        return copy;
    }
}
