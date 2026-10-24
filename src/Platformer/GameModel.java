package Platformer;

import java.awt.Rectangle;

/** Owns game state and applies the rules of play. */
final class GameModel {
    private final Level level = new Level();
    private final Player player = new Player();
    private int cameraX;
    private boolean won;

    Player player() { return player; }
    Rectangle[] platforms() { return level.platforms(); }
    Rectangle goal() { return level.goal(); }
    int cameraX() { return cameraX; }
    boolean hasWon() { return won; }

    void update(boolean leftPressed, boolean rightPressed) {
        if (won) return;

        if (leftPressed != rightPressed) {
            player.x += rightPressed ? GameConfig.MOVE_SPEED : -GameConfig.MOVE_SPEED;
            player.facingRight = rightPressed;
        }
        player.x = Math.max(0, Math.min(player.x,
                GameConfig.WORLD_WIDTH - GameConfig.PLAYER_WIDTH));

        int oldBottom = player.y + GameConfig.PLAYER_HEIGHT;
        player.velocityY += GameConfig.GRAVITY;
        player.y += player.velocityY;
        player.onGround = false;

        for (Rectangle platform : level.platforms()) {
            boolean overlapsHorizontally = player.x + GameConfig.PLAYER_WIDTH > platform.x
                    && player.x < platform.x + platform.width;
            int newBottom = player.y + GameConfig.PLAYER_HEIGHT;
            if (player.velocityY >= 0 && overlapsHorizontally
                    && oldBottom <= platform.y && newBottom >= platform.y) {
                player.y = platform.y - GameConfig.PLAYER_HEIGHT;
                player.velocityY = 0;
                player.onGround = true;
            }
        }

        Rectangle bounds = new Rectangle(player.x, player.y,
                GameConfig.PLAYER_WIDTH, GameConfig.PLAYER_HEIGHT);
        if (bounds.intersects(level.goal())) won = true;
        if (player.y > GameConfig.VIEW_HEIGHT) reset();
        updateCamera();
    }

    void jump() {
        if (player.onGround && !won) {
            player.velocityY = GameConfig.JUMP_STRENGTH;
            player.onGround = false;
        }
    }

    void reset() {
        player.reset();
        cameraX = 0;
        won = false;
    }

    private void updateCamera() {
        cameraX = player.x + GameConfig.PLAYER_WIDTH / 2 - GameConfig.VIEW_WIDTH / 2;
        cameraX = Math.max(0, Math.min(cameraX,
                GameConfig.WORLD_WIDTH - GameConfig.VIEW_WIDTH));
    }
}
