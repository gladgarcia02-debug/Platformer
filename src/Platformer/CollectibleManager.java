package Platformer;

import java.awt.Point;
import java.awt.Rectangle;

/** Owns collectible instances and tracks the player's collection progress. */
final class CollectibleManager {
    private final Crystal[] crystals;
    private int collectedCount;

    CollectibleManager(Point[] spawnPositions) {
        crystals = new Crystal[spawnPositions.length];
        for (int i = 0; i < spawnPositions.length; i++) {
            Point spawn = spawnPositions[i];
            crystals[i] = new Crystal(spawn.x, spawn.y);
        }
    }

    /** Collects any uncollected crystals that overlap the supplied player bounds. */
    void collectOverlapping(Rectangle playerBounds) {
        for (Crystal crystal : crystals) {
            if (!crystal.isCollected() && playerBounds.intersects(crystal.bounds())) {
                if (crystal.collect()) {
                    collectedCount++;
                }
            }
        }
    }

    Crystal[] crystals() {
        return crystals.clone();
    }

    int collectedCount() {
        return collectedCount;
    }

    int totalCount() {
        return crystals.length;
    }

    void reset() {
        for (Crystal crystal : crystals) {
            crystal.reset();
        }
        collectedCount = 0;
    }
}
