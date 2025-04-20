package car;

import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A Restart Menu screen that appears when the game ends.
 * Allows the user to restart the game.
 */
public class RestartMenu {
    private JPanel panel;
    private JFrame frame;
    private FPSAnimator animator;

    public RestartMenu(JFrame frame, FPSAnimator animator) {
        this.frame = frame;
        this.animator = animator;
        initializeUI();
    }

    private void initializeUI() {
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        JLabel gameOverLabel = new JLabel("Game Over");
        gameOverLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.RED);
        panel.add(Box.createVerticalStrut(50));
        panel.add(gameOverLabel);

        JButton restartButton = new JButton("Restart Game");
        restartButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.setFocusPainted(false);
        restartButton.setBackground(Color.GRAY);
        restartButton.setForeground(Color.WHITE);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        panel.add(Box.createVerticalStrut(30));
        panel.add(restartButton);
    }

    private void restartGame() {
        // Remove current menu
        frame.getContentPane().removeAll();

        // Create a new Game instance and add its canvas to the frame
        Game newGame = new Game(frame);
        GLJPanel canvas = newGame.getCanvas();
        frame.getContentPane().add(canvas);

        // Revalidate and repaint the frame
        frame.revalidate();
        frame.repaint();

        // Start the game
        newGame.start();
    }

    public JPanel getPanel() {
        return panel;
    }
}
