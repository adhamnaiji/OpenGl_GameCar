package car;

import com.jogamp.opengl.GL2;
import java.util.Random;

public class Road {
    private float speed = 0.1f;
    private final float segmentLength = 100.0f;
    private float[] segmentZ = {0f, -segmentLength, -2*segmentLength};
    private float dashOffset = 0f;
    private int currentIndex = 0;
    private Random random = new Random();
    private float[] crackPositions = new float[30];
    private float[] stainPositions = new float[20];

    public Road() {
        // Initialize random road features
        for (int i = 0; i < crackPositions.length; i++) {
            crackPositions[i] = random.nextFloat() * segmentLength * 3;
        }
        for (int i = 0; i < stainPositions.length; i++) {
            stainPositions[i] = random.nextFloat() * segmentLength * 3;
        }
    }

    public void update() {
        dashOffset = (dashOffset + speed) % 10f;
        
        for (int i = 0; i < segmentZ.length; i++) {
            segmentZ[i] += speed;
        }
        
        if (segmentZ[currentIndex] > segmentLength) {
            segmentZ[currentIndex] = getPreviousSegmentZ() - segmentLength;
            currentIndex = (currentIndex + 1) % segmentZ.length;
        }
    }

    private float getPreviousSegmentZ() {
        int prevIndex = (currentIndex - 1 + segmentZ.length) % segmentZ.length;
        return segmentZ[prevIndex];
    }

    public void draw(GL2 gl) {
        for (int i = 0; i < segmentZ.length; i++) {
            int index = (currentIndex + i) % segmentZ.length;
            drawRoadSegment(gl, segmentZ[index], index == currentIndex);
        }
    }

    private void drawRoadSegment(GL2 gl, float z, boolean isCurrent) {
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, z);

        drawAsphaltSurface(gl);
        drawRoadMarkings(gl, isCurrent);
        drawRoadDetails(gl);

        gl.glPopMatrix();
    }

    private void drawAsphaltSurface(GL2 gl) {
        // Main road surface with subtle gradient
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor3f(0.18f, 0.18f, 0.20f); // Darker at far end
        gl.glVertex3f(-5f, 0.001f, segmentLength);
        gl.glVertex3f(5f, 0.001f, segmentLength);
        gl.glColor3f(0.22f, 0.22f, 0.24f); // Lighter at near end
        gl.glVertex3f(5f, 0.001f, 0f);
        gl.glVertex3f(-5f, 0.001f, 0f);
        gl.glEnd();

        // Asphalt texture details
        drawRoadImperfections(gl);
    }

    private void drawRoadImperfections(GL2 gl) {
        // Cracks
        gl.glColor3f(0.12f, 0.12f, 0.12f);
        gl.glLineWidth(0.8f);
        gl.glBegin(GL2.GL_LINES);
        for (float crackZ : crackPositions) {
            if (crackZ >= 0 && crackZ <= segmentLength) {
                float x = -4.5f + random.nextFloat() * 9f;
                float length = 0.3f + random.nextFloat() * 1.5f;
                gl.glVertex3f(x, 0.011f, crackZ);
                gl.glVertex3f(x + (random.nextFloat() - 0.5f)*0.5f, 0.011f, crackZ + length);
            }
        }
        gl.glEnd();

        // Stains/oil spots
        gl.glBegin(GL2.GL_QUADS);
        for (float stainZ : stainPositions) {
            if (stainZ >= 0 && stainZ <= segmentLength) {
                float x = -4.5f + random.nextFloat() * 9f;
                float size = 0.3f + random.nextFloat() * 0.7f;
                gl.glColor3f(0.08f, 0.08f, 0.10f);
                gl.glVertex3f(x-size, 0.011f, stainZ-size);
                gl.glVertex3f(x+size, 0.011f, stainZ-size);
                gl.glColor3f(0.14f, 0.14f, 0.16f);
                gl.glVertex3f(x+size, 0.011f, stainZ+size);
                gl.glVertex3f(x-size, 0.011f, stainZ+size);
            }
        }
        gl.glEnd();
    }

    private void drawRoadMarkings(GL2 gl, boolean isCurrent) {
        // Solid side lines with reflective quality
        gl.glColor3f(0.95f, 0.95f, 0.90f); // Off-white
        gl.glLineWidth(0.25f);
        gl.glBegin(GL2.GL_QUADS);
        // Left line
        gl.glVertex3f(-4.75f, 0.02f, segmentLength);
        gl.glVertex3f(-4.5f, 0.02f, segmentLength);
        gl.glVertex3f(-4.5f, 0.02f, 0f);
        gl.glVertex3f(-4.75f, 0.02f, 0f);
        // Right line
        gl.glVertex3f(4.5f, 0.02f, segmentLength);
        gl.glVertex3f(4.75f, 0.02f, segmentLength);
        gl.glVertex3f(4.75f, 0.02f, 0f);
        gl.glVertex3f(4.5f, 0.02f, 0f);
        gl.glEnd();

        // Dashed center line with reflective markers
        float patternStart = isCurrent ? -dashOffset : -(dashOffset + (segmentLength % 10f));
        gl.glBegin(GL2.GL_QUADS);
        for (float z = patternStart; z < segmentLength; z += 10f) {
            if (z + 4f > 0) { // 4m dashes with 6m gaps
                float start = Math.max(0f, z);
                float end = Math.min(z + 4f, segmentLength);
                // Main line
                gl.glVertex3f(-0.15f, 0.02f, start);
                gl.glVertex3f(0.15f, 0.02f, start);
                gl.glVertex3f(0.15f, 0.02f, end);
                gl.glVertex3f(-0.15f, 0.02f, end);
            }
        }
        gl.glEnd();

        // Reflective road studs
        gl.glColor3f(0.9f, 0.9f, 0.8f);
        gl.glPointSize(2.5f);
        gl.glBegin(GL2.GL_POINTS);
        for (float z = patternStart + 2f; z < segmentLength; z += 10f) {
            if (z > 0) {
                gl.glVertex3f(0f, 0.03f, z);
            }
        }
        gl.glEnd();
    }

    private void drawRoadDetails(GL2 gl) {
        // Road shoulders/curbs
        gl.glBegin(GL2.GL_QUADS);
        // Left shoulder
        gl.glColor3f(0.35f, 0.30f, 0.25f);
        gl.glVertex3f(-5.5f, -0.05f, segmentLength);
        gl.glVertex3f(-5.0f, -0.05f, segmentLength);
        gl.glVertex3f(-5.0f, -0.05f, 0f);
        gl.glVertex3f(-5.5f, -0.05f, 0f);
        // Right shoulder
        gl.glVertex3f(5.0f, -0.05f, segmentLength);
        gl.glVertex3f(5.5f, -0.05f, segmentLength);
        gl.glVertex3f(5.5f, -0.05f, 0f);
        gl.glVertex3f(5.0f, -0.05f, 0f);
        gl.glEnd();

        // Curb edges
        gl.glColor3f(0.25f, 0.20f, 0.15f);
        gl.glLineWidth(1.5f);
        gl.glBegin(GL2.GL_LINES);
        // Left curb
        gl.glVertex3f(-5.5f, -0.05f, segmentLength);
        gl.glVertex3f(-5.5f, -0.05f, 0f);
        // Right curb
        gl.glVertex3f(5.5f, -0.05f, segmentLength);
        gl.glVertex3f(5.5f, -0.05f, 0f);
        gl.glEnd();
    }

    public void setSpeed(float newSpeed) {
        speed = newSpeed;
    }
}