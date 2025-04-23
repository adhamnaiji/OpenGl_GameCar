package car;

public class CollisionDetector {
    public static boolean checkCollision(Car car, GameObject obj, ScoreManager scoreManager) {
        float dx = car.getX() - obj.getX();
        float dz = car.getZ() - obj.getZ();
        float distance = (float) Math.sqrt(dx * dx + dz * dz);

        if (distance < 1.0f) {
            if (obj instanceof BananaObstacle) {
                if (!obj.isPassed()) {
                    scoreManager.incrementScore();  // Only increment score once
                    obj.setPassed(true);            // Avoid scoring multiple times
                }
                return false; // Banana does not cause game over
            } else if (obj instanceof BoxObstacle) {
                return true; // Box causes game over
            }
        }
        return false;
    }
}
