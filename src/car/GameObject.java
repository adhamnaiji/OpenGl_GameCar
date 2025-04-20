package car;

import com.jogamp.opengl.GL2;

public abstract class GameObject {
    protected float x, y, z;
    protected boolean passed;

    public abstract void update();
    public abstract void draw(GL2 gl);
    
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public void onCollision(Car car) {
        // Méthode par défaut : peut être vide
    }
    
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

}