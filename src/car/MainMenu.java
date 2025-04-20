package car;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu implements GLEventListener, ActionListener {
    private GLJPanel canvas;
    private FPSAnimator animator;
    private JButton startButton;
    private JFrame frame;

    public MainMenu(JFrame frame) {
        this.frame = frame;

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        canvas = new GLJPanel(capabilities);
        canvas.addGLEventListener(this);

        animator = new FPSAnimator(canvas, 60);

        startButton = new JButton("Start Game");
        startButton.addActionListener(this);

        JPanel panel = new JPanel();
        panel.add(startButton);

        frame.getContentPane().add(canvas);
        frame.getContentPane().add(panel, "South");

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
        System.out.println("Main Menu - Press the 'Start Game' button to begin.");
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
            animator.stop();
            frame.getContentPane().removeAll();
            Game game = new Game(frame);
            frame.getContentPane().add(game.getCanvas());
            frame.revalidate();
            frame.repaint();
            game.start();
        }
    }
}
