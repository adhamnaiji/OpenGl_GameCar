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

    public Car() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
        speed = 0.15f; // Faster than regular car
    }
    
    public void update() {
        if (moveLeft) {
            x -= speed;
            wheelRotation += 8.0f;
            bodyTilt = max(bodyTilt - 0.5f, -5.0f); // Tilt when turning
        } else if (moveRight) {
            x += speed;
            wheelRotation -= 8.0f;
            bodyTilt = min(bodyTilt + 0.5f, 5.0f); // Tilt when turning
        } else {
            // Return to neutral position
            if (bodyTilt > 0) bodyTilt = max(0, bodyTilt - 0.5f);
            else if (bodyTilt < 0) bodyTilt = min(0, bodyTilt + 0.5f);
        }
        
        x = max(-roadLimit, min(roadLimit, x));
    }
    
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        gl.glRotatef(bodyTilt, 0, 0, 1); // Apply body tilt

        drawChassis(gl);
        drawCockpit(gl);
        drawWheels(gl);
        drawAerodynamicFeatures(gl);
        drawLightsAndDetails(gl);

        gl.glPopMatrix();
    }
    
    private void drawChassis(GL2 gl) {
        // Main body color (metallic blue)
        gl.glColor3f(0.2f, 0.4f, 0.8f);
        
        // Low, elongated body
        gl.glBegin(GL2.GL_QUADS);
            // Front hood
            gl.glVertex3f(-1.0f, 0.2f, 2.5f);
            gl.glVertex3f(1.0f, 0.2f, 2.5f);
            gl.glVertex3f(0.8f, 0.4f, 1.5f);
            gl.glVertex3f(-0.8f, 0.4f, 1.5f);
            
            // Main body sides
            for (float z = 1.5f; z >= -2.0f; z -= 0.5f) {
                float width = (z > 0) ? 0.8f : 0.9f;
                float height = 0.4f + (z < -1.0f ? 0.1f*(z+1.0f) : 0);
                
                // Left side
                gl.glVertex3f(-width, height, z);
                gl.glVertex3f(-width, height, z-0.5f);
                gl.glVertex3f(-width, 0.2f, z-0.5f);
                gl.glVertex3f(-width, 0.2f, z);
                
                // Right side
                gl.glVertex3f(width, height, z);
                gl.glVertex3f(width, height, z-0.5f);
                gl.glVertex3f(width, 0.2f, z-0.5f);
                gl.glVertex3f(width, 0.2f, z);
            }
            
            // Rear
            gl.glVertex3f(-0.9f, 0.2f, -2.5f);
            gl.glVertex3f(0.9f, 0.2f, -2.5f);
            gl.glVertex3f(0.9f, 0.5f, -2.0f);
            gl.glVertex3f(-0.9f, 0.5f, -2.0f);
        gl.glEnd();
        
        // Roof scoop
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
        gl.glBegin(GL2.GL_QUADS);
            // Left
            gl.glVertex3f(-0.8f, 0.4f, 1.5f);
            gl.glVertex3f(-0.8f, 0.4f, -1.0f);
            gl.glVertex3f(-0.8f, 0.7f, -1.0f);
            gl.glVertex3f(-0.8f, 0.7f, 1.0f);
            
            // Right
            gl.glVertex3f(0.8f, 0.4f, 1.5f);
            gl.glVertex3f(0.8f, 0.4f, -1.0f);
            gl.glVertex3f(0.8f, 0.7f, -1.0f);
            gl.glVertex3f(0.8f, 0.7f, 1.0f);
        gl.glEnd();
        
        // Roof
        gl.glColor3f(0.15f, 0.3f, 0.6f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-0.6f, 0.7f, 0.5f);
            gl.glVertex3f(0.6f, 0.7f, 0.5f);
            gl.glVertex3f(0.6f, 0.7f, -1.0f);
            gl.glVertex3f(-0.6f, 0.7f, -1.0f);
        gl.glEnd();
    }
    
    private void drawWheels(GL2 gl) {
        float wheelRadius = 0.35f;
        float wheelWidth = 0.25f;
        int segments = 32;
        
        // Front wheels (larger)
        gl.glPushMatrix();
            gl.glTranslatef(-0.8f, 0.1f, 1.8f);
            gl.glRotatef(wheelRotation, 0, 0, 1);
            drawSportsWheel(gl, wheelRadius, wheelWidth, segments, true);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
            gl.glTranslatef(0.8f, 0.1f, 1.8f);
            gl.glRotatef(wheelRotation, 0, 0, 1);
            drawSportsWheel(gl, wheelRadius, wheelWidth, segments, true);
        gl.glPopMatrix();
        
        // Rear wheels (wider)
        gl.glPushMatrix();
            gl.glTranslatef(-0.9f, 0.1f, -1.8f);
            gl.glRotatef(wheelRotation, 0, 0, 1);
            drawSportsWheel(gl, wheelRadius, wheelWidth + 0.1f, segments, false);
        gl.glPopMatrix();
        
        gl.glPushMatrix();
            gl.glTranslatef(0.9f, 0.1f, -1.8f);
            gl.glRotatef(wheelRotation, 0, 0, 1);
            drawSportsWheel(gl, wheelRadius, wheelWidth + 0.1f, segments, false);
        gl.glPopMatrix();
    }
    
    private void drawSportsWheel(GL2 gl, float radius, float width, int segments, boolean isFront) {
        // Tire
        gl.glColor3f(0.1f, 0.1f, 0.1f);
        gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int i = 0; i <= segments; i++) {
                double angle = 2 * PI * i / segments;
                float x = (float) (cos(angle) * radius);
                float y = (float) (sin(angle) * radius);
                
                gl.glVertex3f(x, y, -width/2);
                gl.glVertex3f(x, y, width/2);
            }
        gl.glEnd();
        
        // Sport rim
        gl.glColor3f(0.8f, 0.8f, 0.9f); // Silver with bluish tint
        float rimRadius = radius * 0.7f;
        
        // Spokes
        int spokeCount = isFront ? 5 : 7; // Different spoke pattern for front/rear
        gl.glBegin(GL2.GL_TRIANGLES);
            for (int i = 0; i < spokeCount; i++) {
                double angle1 = 2 * PI * i / spokeCount;
                double angle2 = 2 * PI * (i + 0.35) / spokeCount;
                
                float x1 = (float) (cos(angle1) * rimRadius * 0.2f);
                float y1 = (float) (sin(angle1) * rimRadius * 0.2f);
                float x2 = (float) (cos(angle1) * rimRadius);
                float y2 = (float) (sin(angle1) * rimRadius);
                float x3 = (float) (cos(angle2) * rimRadius);
                float y3 = (float) (sin(angle2) * rimRadius);
                
                // Front face
                gl.glVertex3f(0, 0, width/2 - 0.02f);
                gl.glVertex3f(x2, y2, width/2 - 0.02f);
                gl.glVertex3f(x3, y3, width/2 - 0.02f);
                
                // Back face
                gl.glVertex3f(0, 0, -width/2 + 0.02f);
                gl.glVertex3f(x3, y3, -width/2 + 0.02f);
                gl.glVertex3f(x2, y2, -width/2 + 0.02f);
            }
        gl.glEnd();
    }
    
    private void drawAerodynamicFeatures(GL2 gl) {
        // Front splitter
        gl.glColor3f(0.05f, 0.05f, 0.05f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-0.8f, 0.1f, 2.6f);
            gl.glVertex3f(0.8f, 0.1f, 2.6f);
            gl.glVertex3f(0.8f, 0.0f, 2.4f);
            gl.glVertex3f(-0.8f, 0.0f, 2.4f);
        gl.glEnd();
        
        // Rear wing
        gl.glBegin(GL2.GL_QUADS);
            // Wing base
            gl.glVertex3f(-0.7f, 0.7f, -2.4f);
            gl.glVertex3f(0.7f, 0.7f, -2.4f);
            gl.glVertex3f(0.7f, 0.7f, -2.6f);
            gl.glVertex3f(-0.7f, 0.7f, -2.6f);
            
            // Wing supports
            gl.glVertex3f(-0.6f, 0.7f, -2.5f);
            gl.glVertex3f(-0.6f, 0.4f, -2.5f);
            gl.glVertex3f(-0.55f, 0.4f, -2.5f);
            gl.glVertex3f(-0.55f, 0.7f, -2.5f);
            
            gl.glVertex3f(0.6f, 0.7f, -2.5f);
            gl.glVertex3f(0.6f, 0.4f, -2.5f);
            gl.glVertex3f(0.55f, 0.4f, -2.5f);
            gl.glVertex3f(0.55f, 0.7f, -2.5f);
            
            // Wing main plane
            gl.glVertex3f(-0.8f, 0.9f, -2.7f);
            gl.glVertex3f(0.8f, 0.9f, -2.7f);
            gl.glVertex3f(0.8f, 0.9f, -2.3f);
            gl.glVertex3f(-0.8f, 0.9f, -2.3f);
        gl.glEnd();
        
        // Side skirts
        gl.glBegin(GL2.GL_QUADS);
            // Left
            gl.glVertex3f(-0.9f, 0.0f, 1.0f);
            gl.glVertex3f(-0.9f, 0.0f, -1.5f);
            gl.glVertex3f(-0.95f, -0.1f, -1.5f);
            gl.glVertex3f(-0.95f, -0.1f, 1.0f);
            
            // Right
            gl.glVertex3f(0.9f, 0.0f, 1.0f);
            gl.glVertex3f(0.9f, 0.0f, -1.5f);
            gl.glVertex3f(0.95f, -0.1f, -1.5f);
            gl.glVertex3f(0.95f, -0.1f, 1.0f);
        gl.glEnd();
    }
    
    private void drawLightsAndDetails(GL2 gl) {
        // Headlights
        gl.glColor3f(1.0f, 1.0f, 0.9f); // Bright white
        gl.glBegin(GL2.GL_QUADS);
            // Left
            gl.glVertex3f(-0.7f, 0.3f, 2.55f);
            gl.glVertex3f(-0.5f, 0.3f, 2.55f);
            gl.glVertex3f(-0.5f, 0.2f, 2.55f);
            gl.glVertex3f(-0.7f, 0.2f, 2.55f);
            
            // Right
            gl.glVertex3f(0.7f, 0.3f, 2.55f);
            gl.glVertex3f(0.5f, 0.3f, 2.55f);
            gl.glVertex3f(0.5f, 0.2f, 2.55f);
            gl.glVertex3f(0.7f, 0.2f, 2.55f);
        gl.glEnd();
        
        // Taillights
        gl.glColor3f(0.8f, 0.1f, 0.0f); // Bright red
        gl.glBegin(GL2.GL_QUADS);
            // Left
            gl.glVertex3f(-0.7f, 0.4f, -2.55f);
            gl.glVertex3f(-0.5f, 0.4f, -2.55f);
            gl.glVertex3f(-0.5f, 0.3f, -2.55f);
            gl.glVertex3f(-0.7f, 0.3f, -2.55f);
            
            // Right
            gl.glVertex3f(0.7f, 0.4f, -2.55f);
            gl.glVertex3f(0.5f, 0.4f, -2.55f);
            gl.glVertex3f(0.5f, 0.3f, -2.55f);
            gl.glVertex3f(0.7f, 0.3f, -2.55f);
        gl.glEnd();
        
        // Exhaust pipes
        gl.glColor3f(0.3f, 0.3f, 0.3f);
        gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int i = 0; i <= 10; i++) {
                double angle = PI * i / 10;
                float x1 = -0.6f;
                float x2 = -0.55f;
                float y = (float) (0.1 + 0.05 * sin(angle));
                float z = (float) (-2.6 + 0.05 * cos(angle));
                
                gl.glVertex3f(x1, y, z);
                gl.glVertex3f(x2, y, z);
            }
        gl.glEnd();
        
        gl.glBegin(GL2.GL_QUAD_STRIP);
            for (int i = 0; i <= 10; i++) {
                double angle = PI * i / 10;
                float x1 = 0.6f;
                float x2 = 0.55f;
                float y = (float) (0.1 + 0.05 * sin(angle));
                float z = (float) (-2.6 + 0.05 * cos(angle));
                
                gl.glVertex3f(x1, y, z);
                gl.glVertex3f(x2, y, z);
            }
        gl.glEnd();
    }
    
    // Getters and input handlers remain the same as previous example
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    
    public void handleKeyPress(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = true;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = true;
    }
    
    public void handleKeyRelease(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) moveLeft = false;
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) moveRight = false;
    }
}