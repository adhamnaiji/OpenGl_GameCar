package car;

public class CollisionDetector {
    public static boolean checkCollision(Car car, GameObject obj) {
        // A simple collision check using distance (or bounding boxes).
        float dx = car.getX() - obj.getX();
        float dz = car.getZ() - obj.getZ();
        float distance = (float) Math.sqrt(dx * dx + dz * dz);
        // Assume a collision if within a threshold.
        return distance < 1.0f;
    }
}