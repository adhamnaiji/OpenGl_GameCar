package car;

import com.jogamp.opengl.GL2;

public class BoxObstacle extends Obstacle {

    public BoxObstacle() {
        super();
        size = 1.0f;
        y = size / 2;
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glColor3f(0.0f, 1.0f, 0.0f); // Green box

        gl.glBegin(GL2.GL_QUADS);
        // Front face
        gl.glVertex3f(-size/2, -size/2, size/2);
        gl.glVertex3f(size/2, -size/2, size/2);
        gl.glVertex3f(size/2, size/2, size/2);
        gl.glVertex3f(-size/2, size/2, size/2);
        gl.glEnd();

        gl.glPopMatrix();
    }
}
