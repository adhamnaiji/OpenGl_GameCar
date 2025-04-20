package car;

import com.jogamp.opengl.GL2;

public class ForestBackground {
    private float speed = 0.1f;
    private final float segmentLength = 100f;
    private float[] segmentZ = {0f, -segmentLength, -2*segmentLength};
    private int currentIndex = 0;
    private float totalDistance = 0f;
    private final int treesPerSegment = 20;

    public void update() {
        totalDistance += speed;
        
        for (int i = 0; i < segmentZ.length; i++) {
            segmentZ[i] += speed;
        }
        
        if (segmentZ[currentIndex] > segmentLength) {
            segmentZ[currentIndex] = getPreviousSegmentZ() - segmentLength;
            currentIndex = (currentIndex + 1) % segmentZ.length;
            
            if (totalDistance > 1000f) {
                resetPositions();
                totalDistance = 0f;
            }
        }
    }

    private float getPreviousSegmentZ() {
        int prevIndex = (currentIndex - 1 + segmentZ.length) % segmentZ.length;
        return segmentZ[prevIndex];
    }

    private void resetPositions() {
        float baseZ = segmentZ[currentIndex];
        segmentZ[currentIndex] = baseZ;
        segmentZ[(currentIndex + 1) % segmentZ.length] = baseZ - segmentLength;
        segmentZ[(currentIndex + 2) % segmentZ.length] = baseZ - 2*segmentLength;
    }

    public void draw(GL2 gl) {
        for (int i = 0; i < segmentZ.length; i++) {
            int index = (currentIndex + i) % segmentZ.length;
            drawSegment(gl, segmentZ[index]);
        }
    }

    private void drawSegment(GL2 gl, float z) {
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, z);

        // Forest floor with 5 units overlap
        gl.glColor3f(0f, 0.5f, 0f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-10f, -0.1f, segmentLength + 5f);
            gl.glVertex3f(-5f, -0.1f, segmentLength + 5f);
            gl.glVertex3f(-5f, -0.1f, -5f);
            gl.glVertex3f(-10f, -0.1f, -5f);
            
            gl.glVertex3f(5f, -0.1f, segmentLength + 5f);
            gl.glVertex3f(10f, -0.1f, segmentLength + 5f);
            gl.glVertex3f(10f, -0.1f, -5f);
            gl.glVertex3f(5f, -0.1f, -5f);
        gl.glEnd();

        // Trees with consistent patterns
        float treeSpacing = segmentLength / treesPerSegment;
        int patternSeed = (int)(z / segmentLength);
        
        for (int i = -3; i <= treesPerSegment + 3; i++) {
            float treeZ = i * treeSpacing;
            if (treeZ >= -5f && treeZ <= segmentLength + 5f) {
                drawTree(gl, -8f, 0f, treeZ, patternSeed + i);
                drawTree(gl, 8f, 0f, treeZ, patternSeed + i);
                
                if (i % 2 == 0) {
                    drawTree(gl, -7.3f, 0f, treeZ + treeSpacing/2f, patternSeed + i + 100);
                    drawTree(gl, 7.3f, 0f, treeZ + treeSpacing/2f, patternSeed + i + 100);
                }
            }
        }

        gl.glPopMatrix();
    }

    private void drawTree(GL2 gl, float x, float y, float z, int seed) {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, z);
        
        // Seed-based variations
        float width = 0.2f + (seed % 100)/400f;
        float height = 1.5f + (seed % 100)/150f;
        float green = 0.6f + (seed % 100)/250f;
        
        // Trunk
        gl.glColor3f(0.5f, 0.25f, 0.1f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex3f(-width, 0f, 0f);
            gl.glVertex3f(width, 0f, 0f);
            gl.glVertex3f(width, height, 0f);
            gl.glVertex3f(-width, height, 0f);
        gl.glEnd();
        
        // Foliage
        gl.glColor3f(0f, green, 0.2f);
        drawCone(gl, 0f, height, 0f, height * 0.8f, height * 1.2f, 8);
        
        gl.glPopMatrix();
    }

    private void drawCone(GL2 gl, float x, float y, float z, float radius, float height, int sides) {
        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glVertex3f(x, y + height, z);
        for (int i = 0; i <= sides; i++) {
            float angle = (float)(i * 2 * Math.PI / sides);
            gl.glVertex3f(x + (float)Math.cos(angle)*radius, y, z + (float)Math.sin(angle)*radius);
        }
        gl.glEnd();
    }
    
    public void setSpeed(float newSpeed) {
        speed = newSpeed;
    }
}