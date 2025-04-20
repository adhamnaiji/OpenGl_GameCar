package car;

import com.jogamp.opengl.GL2;
import static java.lang.Math.*;

class Monkey {
    private float x, y, z; // Position of the monkey

    public Monkey(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Draw the monkey at its position
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z); // Translate to the monkey's position

        // Scale down the monkey's size
        gl.glScalef(0.5f, 0.5f, 0.5f); // Scale the monkey to 50% of its original size

        // Draw body (sphere)
        gl.glColor3f(0.8f, 0.5f, 0.2f); // Brown color
        drawSphere(gl, 0.5f); // Draw body (sphere)

        // Draw head (smaller sphere)
        gl.glTranslatef(0.0f, 0.6f, 0.0f); // Move to the head position
        gl.glColor3f(1.0f, 0.8f, 0.6f); // Lighter brown color
        drawSphere(gl, 0.3f); // Draw head (sphere)

        // Draw ears (two small cones)
        gl.glTranslatef(-0.4f, 0.0f, 0.0f); // Move to left ear position
        gl.glColor3f(0.8f, 0.5f, 0.2f); // Ear color
        drawCone(gl, 0.15f, 0.2f); // Left ear

        gl.glTranslatef(0.8f, 0.0f, 0.0f); // Move to right ear position
        drawCone(gl, 0.15f, 0.2f); // Right ear

        gl.glPopMatrix();
    }


    // Helper method to draw a sphere
    private void drawSphere(GL2 gl, float radius) {
        int slices = 16;
        int stacks = 8;
        com.jogamp.opengl.util.gl2.GLUT glut = new com.jogamp.opengl.util.gl2.GLUT();
        glut.glutSolidSphere(radius, slices, stacks);
    }

    // Helper method to draw a cone
    private void drawCone(GL2 gl, float radius, float height) {
        com.jogamp.opengl.util.gl2.GLUT glut = new com.jogamp.opengl.util.gl2.GLUT();
        glut.glutSolidCone(radius, height, 8, 1); // 8 slices, 1 stack
    }
}