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
    private static final Color BACKGROUND_COLOR = new Color(20, 20, 20);
    private static final Color PRIMARY_COLOR = new Color(255, 60, 60);
    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 48);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.PLAIN, 24);

    private GLJPanel canvas;
    private FPSAnimator animator;
    private JButton startButton, exitButton;
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;

        // Setup OpenGL
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);
        canvas = new GLJPanel(capabilities);
        canvas.addGLEventListener(this);

        animator = new FPSAnimator(canvas, 60);

        // Setup styled buttons
        startButton = createStyledButton("START GAME");
        exitButton = createStyledButton("EXIT GAME");

        startButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Create menu panel with centered content
        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 20, 50, 20));

        JLabel titleLabel = new JLabel("STEAL BANANAS", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 50, 0));

        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(titleLabel);
        menuPanel.add(startButton);
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(exitButton);
        menuPanel.add(Box.createVerticalGlue());

        // Wrapper to center the menu panel
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.add(menuPanel);

        // Layered panel that takes the full frame size
        JPanel layeredPanel = new JPanel(new BorderLayout());
        layeredPanel.setOpaque(false);
        layeredPanel.add(centerWrapper, BorderLayout.CENTER);

        // Set frame layout
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(canvas, BorderLayout.CENTER);       // OpenGL background
        frame.getContentPane().add(layeredPanel, BorderLayout.CENTER); // Centered menu overlay

        animator.start();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
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
        button.setForeground(TEXT_COLOR);
        button.setFont(BUTTON_FONT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 60));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        return button;
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
        gl.glClearColor(0f, 0f, 0f, 1f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        renderMenuText(gl);
    }

    private void renderMenuText(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, -5f);
        gl.glBegin(GL2.GL_TRIANGLES);
        gl.glColor3f(1f, 0f, 0f);
        gl.glVertex3f(-1f, -1f, 0f);
        gl.glColor3f(0f, 1f, 0f);
        gl.glVertex3f(1f, -1f, 0f);
        gl.glColor3f(0f, 0f, 1f);
        gl.glVertex3f(0f, 1f, 0f);
        gl.glEnd();
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
            System.out.println("Start button clicked");
            animator.stop();
            frame.getContentPane().removeAll();
            Game game = new Game(frame);
            frame.getContentPane().add(game.getCanvas(), BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
            game.start();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}
