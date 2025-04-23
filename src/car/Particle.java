package car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

public class Particle {
    private float x, y, z;
    private float life;
    private float speedX, speedY, speedZ;

    public Particle(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.life = 2.0f;  // Set initial life to 2.0 for debugging
        this.speedX = (float) (Math.random() * 0.1 - 0.05);  // Random speed
        this.speedY = (float) (Math.random() * 0.1 - 0.05);
        this.speedZ = (float) (Math.random() * 0.1 - 0.05);
    }

    public void update(float deltaTime) {
        x += speedX * deltaTime;
        y += speedY * deltaTime;
        z += speedZ * deltaTime;

        life -= deltaTime * 0.05f;  // Slower life decay for debugging
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void draw(GL2 gl) {
        if (life > 0) {
            gl.glPushMatrix();
            gl.glTranslatef(x, y, z);
            gl.glPointSize(10.0f);  // Make particles bigger for debugging
            gl.glBegin(GL2.GL_POINTS);
            gl.glVertex3f(0.0f, 0.0f, 0.0f);
            gl.glEnd();
            gl.glPopMatrix();
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getLife() {
        return life;
    }
}