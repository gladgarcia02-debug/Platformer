package Platformer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

/** Draws game state without changing it. */
final class GameRenderer {
    private int animationFrame;

    void advanceAnimation() { animationFrame++; }

    void render(Graphics2D g, GameModel model, boolean moving) {
        int cameraX = model.cameraX();
        Player player = model.player();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setPaint(new GradientPaint(0, 0, new Color(15, 25, 65),
                0, GameConfig.VIEW_HEIGHT, new Color(75, 155, 190)));
        g.fillRect(0, 0, GameConfig.VIEW_WIDTH, GameConfig.VIEW_HEIGHT);

        Graphics2D background = (Graphics2D) g.create();
        background.translate(-cameraX / 3, 0);
        drawMountain(background, -80, 330, 250, 190, new Color(48, 65, 115));
        drawMountain(background, 130, 290, 300, 230, new Color(39, 80, 120));
        drawMountain(background, 400, 340, 270, 180, new Color(35, 75, 105));
        drawMountain(background, 600, 300, 300, 220, new Color(29, 62, 95));
        background.dispose();

        g.setColor(new Color(220, 240, 255, 180));
        int[][] stars = {{80,65},{180,115},{330,55},{510,100},{690,70},{750,155},{420,175},{250,210}};
        for (int[] star : stars) g.fillOval(star[0], star[1], 3, 3);

        Graphics2D world = (Graphics2D) g.create();
        world.translate(-cameraX, 0);
        for (Rectangle platform : model.platforms()) drawPlatform(world, platform);
        drawPortal(world, model.goal());
        drawPlayer(world, player, moving);
        world.dispose();

        drawHud(g);
        if (model.hasWon()) drawVictory(g);
    }

    private void drawMountain(Graphics2D g, int x, int baseY, int width, int height, Color color) {
        g.setColor(color);
        g.fillPolygon(new int[]{x, x + width / 2, x + width},
                new int[]{baseY, baseY - height, baseY}, 3);
    }

    private void drawPlatform(Graphics2D g, Rectangle r) {
        g.setColor(new Color(5, 15, 30, 100));
        g.fillRoundRect(r.x, r.y + 6, r.width, r.height, 12, 12);
        g.setPaint(new GradientPaint(r.x, r.y, new Color(65, 100, 125),
                r.x, r.y + r.height, new Color(25, 43, 68)));
        g.fillRoundRect(r.x, r.y, r.width, r.height, 12, 12);
        g.setColor(new Color(100, 235, 205));
        g.fillRoundRect(r.x, r.y, r.width, 4, 4, 4);
    }

    private void drawPortal(Graphics2D g, Rectangle goal) {
        g.setColor(new Color(40, 255, 220, 35));
        g.fillOval(goal.x - 18, goal.y - 18, goal.width + 36, goal.height + 36);
        g.setColor(new Color(30, 210, 200, 100));
        g.fillRoundRect(goal.x, goal.y, goal.width, goal.height, 30, 30);
        g.setStroke(new BasicStroke(4));
        g.setColor(new Color(130, 255, 230));
        g.drawRoundRect(goal.x, goal.y, goal.width, goal.height, 30, 30);
        g.setStroke(new BasicStroke(1));
    }

    private void drawPlayer(Graphics2D g, Player p, boolean moving) {
        boolean jumping = !p.onGround && p.velocityY < 0;
        boolean falling = !p.onGround && p.velocityY >= 0;
        int frame = (animationFrame / 5) % 2;
        int leftLeg = moving && p.onGround ? (frame == 0 ? -5 : 4) : 0;
        int rightLeg = moving && p.onGround ? (frame == 0 ? 4 : -5) : 0;
        int leftArm = moving && p.onGround ? (frame == 0 ? 4 : -3) : 0;
        int rightArm = moving && p.onGround ? (frame == 0 ? -3 : 4) : 0;
        if (jumping) { leftLeg = rightLeg = -4; leftArm = rightArm = -7; }
        if (falling) { leftArm = rightArm = -10; leftLeg = rightLeg = 3; }
        int breathe = !moving && p.onGround ? (animationFrame / 12) % 2 : 0;

        g.setColor(new Color(0, 0, 0, 65));
        g.fillOval(p.x - 4, p.y + GameConfig.PLAYER_HEIGHT - 2, GameConfig.PLAYER_WIDTH + 8, 8);
        g.setColor(new Color(22, 28, 50));
        g.fillRoundRect(p.x + 4, p.y + 29 + leftLeg, 10, 10, 5, 5);
        g.fillRoundRect(p.x + 17, p.y + 29 + rightLeg, 10, 10, 5, 5);
        g.setColor(new Color(255, 195, 150));
        g.fillRoundRect(p.x - 1, p.y + 14 + leftArm, 7, 15, 6, 6);
        g.fillRoundRect(p.x + 24, p.y + 14 + rightArm, 7, 15, 6, 6);
        g.setPaint(new GradientPaint(p.x, p.y + 10, new Color(80, 225, 235),
                p.x + GameConfig.PLAYER_WIDTH, p.y + 34, new Color(35, 75, 185)));
        g.fillRoundRect(p.x + 3, p.y + 11 + breathe, 24, 23, 8, 8);
        g.setColor(new Color(245, 190, 75));
        g.fillRoundRect(p.x + 4, p.y + 27 + breathe, 22, 4, 3, 3);
        int headY = p.y - breathe;
        g.setColor(new Color(255, 211, 170));
        g.fillOval(p.x + 5, headY, 21, 21);
        g.setColor(new Color(35, 29, 55));
        g.fillArc(p.x + 4, headY - 2, 23, 15, 0, 180);
        g.setColor(new Color(25, 30, 45));
        g.fillOval(p.facingRight ? p.x + 20 : p.x + 9, headY + 9, 3, 3);
    }

    private void drawHud(Graphics2D g) {
        g.setColor(new Color(8, 15, 40, 190));
        g.fillRoundRect(15, 15, 770, 55, 16, 16);
        g.setFont(new Font("SansSerif", Font.BOLD, 19));
        g.setColor(new Color(110, 255, 220));
        g.drawString("ECHOES OF THE LOST", 30, 39);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(new Color(225, 235, 255));
        g.drawString("THE FORGOTTEN VALLEY", 31, 55);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("ARROWS  MOVE     SPACE  JUMP", 510, 47);
    }

    private void drawVictory(Graphics2D g) {
        g.setColor(new Color(5, 10, 30, 190));
        g.fillRoundRect(180, 210, 440, 150, 24, 24);
        g.setColor(new Color(120, 255, 220));
        g.setFont(new Font("SansSerif", Font.BOLD, 34));
        g.drawString("ADVENTURE COMPLETE", 220, 270);
        g.setColor(Color.WHITE);
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        g.drawString("Press R to play again", 310, 315);
    }
}
