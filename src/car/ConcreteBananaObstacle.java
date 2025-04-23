package car;

import com.jogamp.opengl.GL2;

public class ConcreteBananaObstacle extends BananaObstacle {

    @Override
    public void update() {
        // Implement update logic for this specific banana obstacle
        // Example: Move the banana obstacle across the screen or check for other game logic.
    }

    @Override
    public void draw(GL2 gl) {
        // Implement drawing logic for this specific banana obstacle
        // Here, we inherit the drawing logic from the abstract class
        super.draw(gl);
    }
}