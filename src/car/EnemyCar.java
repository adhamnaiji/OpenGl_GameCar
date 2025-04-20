package car;

import com.jogamp.opengl.GL2;
import java.util.Random;

public class EnemyCar extends GameObject {
    private float speed;
    private Random random;
    
    public EnemyCar() {
        random = new Random();
        // Spawn at a random x-position within road boundaries (with a margin).
        x = (random.nextFloat() - 0.5f) * 8; // roughly between -4 and 4
        y = 0.0f;
        z = -50.0f;
        speed = 0.2f;
        passed = false;
    }
    
    @Override
    public void update() {
        // Move the enemy car toward the player.
        z += speed;
        if (z > 5) {
            reset();
        }
    }
    
    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        
        // Draw the body of the enemy car.
        gl.glColor3f(0.0f, 0.0f, 1.0f); // blue car
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-0.8f, 0.0f, 1.0f);
            gl.glVertex3f(0.8f, 0.0f, 1.0f);
            gl.glVertex3f(0.8f, 0.0f, -1.0f);
            gl.glVertex3f(-0.8f, 0.0f, -1.0f);
        gl.glEnd();
        
        // Draw the roof as a smaller rectangle.
        gl.glColor3f(0.0f, 0.0f, 0.8f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-0.5f, 0.5f, 0.5f);
            gl.glVertex3f(0.5f, 0.5f, 0.5f);
            gl.glVertex3f(0.5f, 0.5f, -0.5f);
            gl.glVertex3f(-0.5f, 0.5f, -0.5f);
        gl.glEnd();
        
        gl.glPopMatrix();
    }
    
    private void reset() {
        // Reset the enemy car to a new random x-position and far back.
        x = (random.nextFloat() - 0.5f) * 8;
        z = -50.0f;
        passed = false;
    }
}