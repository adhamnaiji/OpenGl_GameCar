package car;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Enhanced restart menu with modern UI design and improved functionality.
 * Displays when the game ends and provides options to restart the game.
 */
public class RestartMenu {
    private static final Color BACKGROUND_COLOR = new Color(20, 20, 20);
    private static final Color PRIMARY_COLOR = new Color(255, 60, 60);
    private static final Color SECONDARY_COLOR = new Color(220, 220, 220);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 42);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 24);

    private final JFrame parentFrame;
    private final FPSAnimator animator;
    private final JPanel panel;

    public RestartMenu(JFrame frame, FPSAnimator animator) {
        this.parentFrame = frame;
        this.animator = animator;
        this.panel = createPanel();
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        JLabel gameOverLabel = new JLabel("GAME OVER");
        styleLabel(gameOverLabel);
        panel.add(Box.createVerticalGlue());
        panel.add(gameOverLabel);
        panel.add(Box.createVerticalStrut(40));

        // Button wrapper panel for centering
        JPanel buttonWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(createRestartButton());

        panel.add(buttonWrapper);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private void styleLabel(JLabel label) {
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(TITLE_FONT);
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
    }

    private JButton createRestartButton() {
        JButton button = new JButton("RESTART GAME") {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(PRIMARY_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g.setColor(PRIMARY_COLOR);
                } else {
                    g.setColor(new Color(60, 60, 60));
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(PRIMARY_COLOR);
                g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
            }
        };

        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addActionListener(this::handleRestart);
        return button;
    }

    private void handleRestart(ActionEvent e) {
        // Stop current animation if running
        if (animator != null && animator.isStarted()) {
            animator.stop();
        }

        // Clear the frame
        parentFrame.getContentPane().removeAll();

        // Create and start new game
        Game newGame = new Game(parentFrame);
        GLJPanel canvas = newGame.getCanvas();
        parentFrame.getContentPane().add(canvas);

        // Refresh frame
        parentFrame.revalidate();
        parentFrame.repaint();
        newGame.start();
    }

    public JPanel getPanel() {
        return panel;
    }
}
