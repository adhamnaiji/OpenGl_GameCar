package car;

import com.jogamp.opengl.GL2;
import static java.lang.Math.*;

public class TomatoObstacle extends Obstacle {

    public TomatoObstacle() {
        super();
        size = 0.7f;
        y = size / 2;
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);

        gl.glColor3f(1.0f, 0.0f, 0.0f); // Red tomato
        drawSphere(gl, size / 2, 16, 16);

        gl.glColor3f(0.0f, 0.5f, 0.0f); // Green stem
        gl.glPushMatrix();
        gl.glTranslatef(0, size / 2 + 0.1f, 0);
        gl.glRotatef(90, 1, 0, 0);
        drawCone(gl, 0.05f, 0.2f, 8);
        gl.glPopMatrix();

        gl.glPopMatrix();
    }

    private void drawSphere(GL2 gl, float radius, int slices, int stacks) {
        for (int i = 0; i < slices; i++) {
            float theta1 = (float) (i * PI / slices);
            float theta2 = (float) ((i + 1) * PI / slices);

            gl.glBegin(GL2.GL_TRIANGLE_STRIP);
            for (int j = 0; j <= stacks; j++) {
                float phi = (float) (j * 2 * PI / stacks);

                for (int k = 0; k <= 1; k++) {
                    float theta = k == 0 ? theta1 : theta2;
                    float x = (float) (radius * sin(theta) * cos(phi));
                    float y = (float) (radius * sin(theta) * sin(phi));
                    float z = (float) (radius * cos(theta));

                    gl.glNormal3f(x, y, z);
                    gl.glVertex3f(x, z, y); // Notice z and y are swapped for upright sphere
                }
            }
            gl.glEnd();
        }
    }

    private void drawCone(GL2 gl, float baseRadius, float height, int segments) {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex3f(0, height, 0);
        for (int i = 0; i <= segments; i++) {
            float angle = (float) (i * 2 * PI / segments);
            gl.glVertex3f((float) cos(angle) * baseRadius, 0, (float) sin(angle) * baseRadius);
        }
        gl.glEnd();
    }
}
