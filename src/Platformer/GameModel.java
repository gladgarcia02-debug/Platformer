package Platformer;

import java.awt.Rectangle;

/** Owns game state and applies the rules of play. */
final class GameModel {
    private final Level level = new Level();
    private final Player player = new Player();
    private final CollectibleManager collectibles =
            new CollectibleManager(level.crystalSpawns());
    private int cameraX;
    private boolean won;

    Player player() { return player; }
    Rectangle[] platforms() { return level.platforms(); }
    Rectangle goal() { return level.goal(); }
    Crystal[] crystals() { return collectibles.crystals(); }
    int collectedCrystalCount() { return collectibles.collectedCount(); }
    int totalCrystalCount() { return collectibles.totalCount(); }
    int cameraX() { return cameraX; }
    boolean hasWon() { return won; }

    void update(boolean leftPressed, boolean rightPressed) {
        if (won) return;

        updateHorizontalMovement(leftPressed, rightPressed);
        updateVerticalMovement();

        Rectangle bounds = playerBounds();
        collectibles.collectOverlapping(bounds);
        if (bounds.intersects(level.goal())) won = true;
        if (player.y > GameConfig.VIEW_HEIGHT) reset();

        updateCamera();
    }

    /**
     * Moves the player horizontally, then resolves any platform overlap.
     * Resolving horizontal movement separately prevents walking through platform sides.
     */
    private void updateHorizontalMovement(boolean leftPressed, boolean rightPressed) {
        if (leftPressed == rightPressed) return;

        int direction = rightPressed ? 1 : -1;
        player.facingRight = direction > 0;

        int oldX = player.x;
        int proposedX = oldX + direction * GameConfig.MOVE_SPEED;
        proposedX = Math.max(0, Math.min(proposedX,
                GameConfig.WORLD_WIDTH - GameConfig.PLAYER_WIDTH));
        player.x = proposedX;

        Rectangle playerBounds = playerBounds();
        for (Rectangle platform : level.platforms()) {
            if (!playerBounds.intersects(platform)) continue;

            if (direction > 0) {
                player.x = platform.x - GameConfig.PLAYER_WIDTH;
            } else {
                player.x = platform.x + platform.width;
            }
            playerBounds = playerBounds();
        }
    }

    /**
     * Applies gravity and resolves vertical collisions independently from horizontal movement.
     * Downward movement lands on platform tops; upward movement hits platform undersides.
     */
    private void updateVerticalMovement() {
        int oldY = player.y;
        int oldTop = oldY;
        int oldBottom = oldY + GameConfig.PLAYER_HEIGHT;

        player.velocityY += GameConfig.GRAVITY;
        int proposedY = oldY + player.velocityY;
        int proposedTop = proposedY;
        int proposedBottom = proposedY + GameConfig.PLAYER_HEIGHT;

        player.y = proposedY;
        player.onGround = false;

        for (Rectangle platform : level.platforms()) {
            boolean overlapsHorizontally =
                    player.x + GameConfig.PLAYER_WIDTH > platform.x
                    && player.x < platform.x + platform.width;

            if (!overlapsHorizontally) continue;

            // Falling: the player's feet crossed the platform's top surface.
            if (player.velocityY >= 0
                    && oldBottom <= platform.y
                    && proposedBottom >= platform.y) {
                player.y = platform.y - GameConfig.PLAYER_HEIGHT;
                player.velocityY = 0;
                player.onGround = true;
                proposedTop = player.y;
                proposedBottom = player.y + GameConfig.PLAYER_HEIGHT;
            }
            // Rising: the player's head crossed the platform's underside.
            else if (player.velocityY < 0
                    && oldTop >= platform.y + platform.height
                    && proposedTop <= platform.y + platform.height) {
                player.y = platform.y + platform.height;
                player.velocityY = 0;
                proposedTop = player.y;
                proposedBottom = player.y + GameConfig.PLAYER_HEIGHT;
            }
        }
    }

    private Rectangle playerBounds() {
        return new Rectangle(player.x, player.y,
                GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
    }

    void jump() {
        if (player.onGround && !won) {
            player.velocityY = GameConfig.JUMP_STRENGTH;
            player.onGround = false;
        }
    }

    void reset() {
        player.reset();
        collectibles.reset();
        cameraX = 0;
        won = false;
    }

    private void updateCamera() {
        cameraX = player.x + GameConfig.PLAYER_WIDTH / 2 - GameConfig.VIEW_WIDTH / 2;
        cameraX = Math.max(0, Math.min(cameraX,
                GameConfig.WORLD_WIDTH - GameConfig.VIEW_WIDTH));
    }
}
