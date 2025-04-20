package car;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu implements GLEventListener, ActionListener {
    private GLJPanel canvas;
    private FPSAnimator animator;
    private JButton startButton, exitButton;
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;

        // Initialize OpenGL canvas
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        canvas = new GLJPanel(capabilities);
        canvas.addGLEventListener(this);

        animator = new FPSAnimator(canvas, 60);

        // Initialize buttons
        startButton = new JButton("Start Game");
        exitButton = new JButton("Exit Game");
        startButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Customize button size and font
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        exitButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setPreferredSize(new Dimension(200, 60));
        exitButton.setPreferredSize(new Dimension(200, 60));

        // Create a panel for buttons and title
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));  // FlowLayout to center buttons horizontally
        panel.add(startButton);
        panel.add(exitButton);

        // Title for the game
        JLabel titleLabel = new JLabel("Steal Bananas", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setPreferredSize(new Dimension(800, 100));  // Height for title space

        // Set up layout for the frame
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(canvas, BorderLayout.CENTER);  // Add OpenGL canvas in the center
        frame.getContentPane().add(titleLabel, BorderLayout.NORTH);    // Add title at the top
        frame.getContentPane().add(panel, BorderLayout.SOUTH);    // Add panel with buttons below title

        animator.start();
    }

    public GLJPanel getCanvas() {
        return canvas;
    }

    public void start() {
        canvas.requestFocusInWindow();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        renderMenuText(gl);
    }

    private void renderMenuText(GL2 gl) {
        gl.glPushMatrix();
        System.out.println("Main Menu - Press 'Start Game' to begin or 'Exit Game' to quit.");
        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(60.0, (double) width / height, 0.1, 1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            // Stop animator and transition to game screen
            animator.stop();
            frame.getContentPane().removeAll();
            Game game = new Game(frame);
            frame.getContentPane().add(game.getCanvas());
            frame.revalidate();
            frame.repaint();
            game.start();
        } else if (e.getSource() == exitButton) {
            // Close the game when exit button is clicked
            System.exit(0);  // Exit the application
        }
    }
}
