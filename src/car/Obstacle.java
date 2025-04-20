package car;

import com.jogamp.opengl.GL2;
import java.util.Random;

public abstract class Obstacle extends GameObject {
    protected float speed;
    protected Random random;
    protected float size;

    public Obstacle() {
        random = new Random();
        x = (random.nextFloat() - 0.5f) * 5;
        y = 0.0f;
        z = -50.0f;
        speed = 0.2f;
        size = 1.0f;
        passed = false;
    }

    @Override
    public void update() {
        z += speed;
        if (z > 5) {
            reset();
        }
    }

    protected void reset() {
        x = (random.nextFloat() - 0.5f) * 5;
        z = -50.0f;
        passed = false;
    }

    public abstract void draw(GL2 gl);
}
