package car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class Camera {
    private Car car;
    private float eyeX, eyeY, eyeZ;
    private float centerX, centerY, centerZ;
    
    public Camera(Car car) {
        this.car = car;
        // Set default camera parameters relative to the car.
        eyeX = car.getX();
        eyeY = car.getY() + 2.0f;
        eyeZ = car.getZ() + 5.0f;
    }
    
    public void update() {
        // Update camera position based on the car’s current position.
        eyeX = car.getX();
        eyeY = car.getY() + 2.0f;
        eyeZ = car.getZ() + 5.0f;
        
        // Set the point to look at (ahead of the car).
        centerX = car.getX();
        centerY = car.getY();
        centerZ = car.getZ() - 10.0f;
    }
    
    public void applyView(GL2 gl) {
        GLU glu = new GLU();
        gl.glLoadIdentity();
        glu.gluLookAt(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, 0, 1, 0);
    }
}