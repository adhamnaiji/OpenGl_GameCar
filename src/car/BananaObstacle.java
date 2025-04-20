package car;

import com.jogamp.opengl.GL2;
import static java.lang.Math.*;

public class BananaObstacle extends Obstacle {

    public BananaObstacle() {
        super();
        size = 1.0f;
        y = size / 2;
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(90, 0, 1, 0); // Rotate for banana orientation

        gl.glColor3f(1.0f, 1.0f, 0.0f); // Yellow banana

        drawBanana(gl, size, 0.1f);

        gl.glPopMatrix();
    }

    private void drawBanana(GL2 gl, float length, float radius) {
        int slices = 20;
        int segments = 10;
        float bend = 0.5f; // You can adjust for more curve

        for (int i = 0; i < segments; i++) {
            float angle1 = i * bend / segments;
            float angle2 = (i + 1) * bend / segments;

            float x1 = length * (i / (float)segments);
            float x2 = length * ((i + 1) / (float)segments);

            float y1 = (float)(sin(angle1) * length / 4);
            float y2 = (float)(sin(angle2) * length / 4);

            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int j = 0; j <= slices; j++) {
                float theta = (float)(2 * PI * j / slices);
                float dx = (float)(cos(theta) * radius);
                float dz = (float)(sin(theta) * radius);

                gl.glNormal3f(dx, 0, dz);
                gl.glVertex3f(x1, y1 + dx, dz);
                gl.glVertex3f(x2, y2 + dx, dz);
            }
            gl.glEnd();
        }
    }
}