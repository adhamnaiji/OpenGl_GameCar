package car;

import java.util.Random;
import com.jogamp.opengl.GL2;

public class Bonus extends GameObject {
    private float speed;
    private Random random;
    
    public Bonus() {
        random = new Random();
        // Start bonus at a random position.
        x = (random.nextFloat() - 0.5f) * 5;
        y = 0.0f;
        z = -70.0f;
        speed = 0.15f;
    }
    
    @Override
    public void update() {
        // Move bonus toward the car.
        z += speed;
        // Reset if passed the car.
        if (z > 5) {
            reset();
        }
    }
    
    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        // Draw a simple yellow object (using a cube as a placeholder).
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);
        // Front face.
        gl.glVertex3f(-0.3f, -0.3f, 0.3f);
        gl.glVertex3f(0.3f, -0.3f, 0.3f);
        gl.glVertex3f(0.3f, 0.3f, 0.3f);
        gl.glVertex3f(-0.3f, 0.3f, 0.3f);
        // Additional faces would be drawn here.
        gl.glEnd();
        gl.glPopMatrix();
    }
    
    private void reset() {
        // Reset to a new random position.
        x = (random.nextFloat() - 0.5f) * 5;
        z = -70.0f;
    }
}