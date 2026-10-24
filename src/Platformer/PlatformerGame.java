package Platformer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Swing view and input controller for the platformer. */
public final class PlatformerGame extends JPanel implements ActionListener {
    private final GameModel model = new GameModel();
    private final GameRenderer renderer = new GameRenderer();
    private final Timer timer = new Timer(GameConfig.TIMER_DELAY_MS, this);
    private boolean leftPressed;
    private boolean rightPressed;

    public PlatformerGame() {
        setPreferredSize(new Dimension(GameConfig.VIEW_WIDTH, GameConfig.VIEW_HEIGHT));
        setupControls();
        timer.start();
    }

    private void setupControls() {
        bindKey("LEFT", "left", () -> leftPressed = true);
        bindKey("released LEFT", "stopLeft", () -> leftPressed = false);
        bindKey("RIGHT", "right", () -> rightPressed = true);
        bindKey("released RIGHT", "stopRight", () -> rightPressed = false);
        bindKey("SPACE", "jump", model::jump);
        bindKey("R", "restart", this::restartGame);
    }

    private void bindKey(String key, String name, Runnable action) {
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), name);
        getActionMap().put(name, new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { action.run(); }
        });
    }

    private void restartGame() {
        model.reset();
        leftPressed = false;
        rightPressed = false;
    }

    @Override public void actionPerformed(ActionEvent e) {
        model.update(leftPressed, rightPressed);
        renderer.advanceAnimation();
        repaint();
    }

    @Override protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        renderer.render(g, model, leftPressed || rightPressed);
        g.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Java Platformer");
            frame.add(new PlatformerGame());
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
