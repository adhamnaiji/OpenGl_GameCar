package car;

import com.jogamp.opengl.GL2;
import java.awt.event.KeyEvent;
import static java.lang.Math.*;

public class Car {
    private float x, y, z;
    private float speed;
    private boolean moveLeft, moveRight;
    private final float roadLimit = 4.5f;
    private float wheelRotation = 0.0f;
    private float bodyTilt = 0.0f; // For tilt when turning
    private Monkey monkey; // Create an instance of Monkey

    public Car() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        speed = 0.15f; // Faster than regular car
        monkey = new Monkey(0.0f, 1.0f, 0.0f); // Initialize the monkey's position
    }

    public void update() {
        handleMovement();
        adjustTiltAndRotation();
        keepCarWithinBounds();
    }

    private void handleMovement() {
        if (moveLeft) {
            x -= speed;
            wheelRotation += 8.0f;
        } else if (moveRight) {
            x += speed;
            wheelRotation -= 8.0f;
        }
    }

    private void adjustTiltAndRotation() {
        if (moveLeft) {
            bodyTilt = max(bodyTilt - 0.5f, -5.0f); // Tilt when turning left
        } else if (moveRight) {
            bodyTilt = min(bodyTilt + 0.5f, 5.0f); // Tilt when turning right
        } else {
            // Return to neutral position when no movement
            bodyTilt = (bodyTilt > 0) ? max(0, bodyTilt - 0.5f) : min(0, bodyTilt + 0.5f);
        }
    }

    private void keepCarWithinBounds() {
        x = max(-roadLimit, min(roadLimit, x)); // Keep the car within the road limits
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(bodyTilt, 0, 0, 1); // Apply body tilt based on the movement

        drawChassis(gl);
        drawCockpit(gl);
        drawWheels(gl);
        drawAerodynamicFeatures(gl);
        drawLightsAndDetails(gl);

        monkey.draw(gl); // Draw the monkey inside the car

        gl.glPopMatrix();
    }

    private void drawChassis(GL2 gl) {
        // Main body color (metallic blue)
        gl.glColor3f(0.2f, 0.4f, 0.8f);

        // Low, elongated body
        gl.glBegin(GL2.GL_QUADS);
        drawFrontHood(gl);
        drawBodySides(gl);
        drawRear(gl);
        gl.glEnd();

        // Roof scoop
        drawRoofScoop(gl);
    }

    private void drawFrontHood(GL2 gl) {
        gl.glVertex3f(-1.0f, 0.2f, 2.5f);
        gl.glVertex3f(1.0f, 0.2f, 2.5f);
        gl.glVertex3f(0.8f, 0.4f, 1.5f);
        gl.glVertex3f(-0.8f, 0.4f, 1.5f);
    }

    private void drawBodySides(GL2 gl) {
        for (float z = 1.5f; z >= -2.0f; z -= 0.5f) {
            float width = (z > 0) ? 0.8f : 0.9f;
            float height = 0.4f + (z < -1.0f ? 0.1f * (z + 1.0f) : 0);

            // Left side
            gl.glVertex3f(-width, height, z);
            gl.glVertex3f(-width, height, z - 0.5f);
            gl.glVertex3f(-width, 0.2f, z - 0.5f);
            gl.glVertex3f(-width, 0.2f, z);

            // Right side
            gl.glVertex3f(width, height, z);
            gl.glVertex3f(width, height, z - 0.5f);
            gl.glVertex3f(width, 0.2f, z - 0.5f);
            gl.glVertex3f(width, 0.2f, z);
        }
    }

    private void drawRear(GL2 gl) {
        gl.glVertex3f(-0.9f, 0.2f, -2.5f);
        gl.glVertex3f(0.9f, 0.2f, -2.5f);
        gl.glVertex3f(0.9f, 0.5f, -2.0f);
        gl.glVertex3f(-0.9f, 0.5f, -2.0f);
    }

    private void drawRoofScoop(GL2 gl) {
        gl.glColor3f(0.1f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.3f, 0.7f, 0.0f);
        gl.glVertex3f(0.3f, 0.7f, 0.0f);
        gl.glVertex3f(0.3f, 0.7f, -0.5f);
        gl.glVertex3f(-0.3f, 0.7f, -0.5f);
        gl.glEnd();
    }

    private void drawCockpit(GL2 gl) {
        // Windshield
        gl.glColor4f(0.7f, 0.8f, 1.0f, 0.3f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-0.7f, 0.4f, 1.5f);
        gl.glVertex3f(0.7f, 0.4f, 1.5f);
        gl.glVertex3f(0.6f, 0.7f, 0.5f);
        gl.glVertex3f(-0.6f, 0.7f, 0.5f);
        gl.glEnd();

        // Side windows
        drawSideWindows(gl);
    }

    private void drawSideWindows(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS);
        // Left window
        gl.glVertex3f(-0.8f, 0.4f, 1.5f);
        gl.glVertex3f(-0.8f, 0.4f, -1.0f);
        gl.glVertex3f(-0.8f, 0.7f, -1.0f);
        gl.glVertex3f(-0.8f, 0.7f, 1.0f);

        // Right window
        gl.glVertex3f(0.8f, 0.4f, 1.5f);
        gl.glVertex3f(0.8f, 0.4f, -1.0f);
        gl.glVertex3f(0.8f, 0.7f, -1.0f);
        gl.glVertex3f(0.8f, 0.7f, 1.0f);
        gl.glEnd();
    }

    private void drawWheels(GL2 gl) {
        gl.glColor3f(0.1f, 0.1f, 0.1f); // Black tires

        // Front wheels
        gl.glPushMatrix();
        gl.glTranslatef(0.8f, 0.2f, 1.5f);
        drawTire(gl);
        gl.glPopMatrix();

        // Rear wheels
        gl.glPushMatrix();
        gl.glTranslatef(-0.8f, 0.2f, 1.5f);
        drawTire(gl);
        gl.glPopMatrix();
    }

    private void drawTire(GL2 gl) {
        com.jogamp.opengl.util.gl2.GLUT glut = new com.jogamp.opengl.util.gl2.GLUT();
        glut.glutSolidTorus(0.1, 0.2, 16, 16);
    }

    private void drawAerodynamicFeatures(GL2 gl) {
        // Spoiler and other features can be added here
    }

    private void drawLightsAndDetails(GL2 gl) {
        // Headlights, tail lights, etc.
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight = false;
        }
    }

    // Getters for car position
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    // Getter for wheel rotation
    public float getWheelRotation() {
        return wheelRotation;
    }

    // Getter for body tilt
    public float getBodyTilt() {
        return bodyTilt;
    }
}
