package car;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import java.awt.Font;

public class ScoreManager {
    private int score;
    private float elapsedTime;  // The time that has passed since the game started.
    private TextRenderer textRenderer;
    private long lastTime;      // Used for computing elapsed time.

    public ScoreManager() {
        score = 0;
        elapsedTime = 0.0f; // Start at 0 seconds.
        lastTime = System.currentTimeMillis();
        textRenderer = null; // Lazy initialization inside render.
    }
    
    public void update() {
        // Compute real elapsed time.
        long currentTime = System.currentTimeMillis();
        float deltaSeconds = (currentTime - lastTime) / 1000.0f;
        lastTime = currentTime;
        elapsedTime += deltaSeconds;
    }
    
    /**
     * Call this method when an enemy car is passed successfully.
     */
    public void incrementScore() {
        score += 1;
    }
    
    /**
     * Renders the HUD (score and elapsed time) on the screen.
     *
     * @param gl the GL2 context.
     * @param width current drawable width.
     * @param height current drawable height.
     */
    public void render(GL2 gl, int width, int height) {
        if (textRenderer == null) {
            textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 18));
        }
        textRenderer.beginRendering(width, height);
        textRenderer.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        textRenderer.draw("Score: " + score, 10, height - 30);
        textRenderer.draw("Time: " + String.format("%.2f", elapsedTime), 10, height - 60);
        textRenderer.endRendering();
    }
    
    /**
     * Renders a game over message on the screen.
     *
     * @param gl the GL2 context.
     * @param width current drawable width.
     * @param height current drawable height.
     */
    public void renderGameOver(GL2 gl, int width, int height) {
        if (textRenderer == null) {
            textRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 36));
        }
        textRenderer.beginRendering(width, height);
        textRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        String msg = "GAME OVER";
        // A rough centering (approx. 20 pixels per character).
        int textWidth = msg.length() * 20;
        int xPos = (width - textWidth) / 2;
        int yPos = height / 2;
        textRenderer.draw(msg, xPos, yPos);
        textRenderer.endRendering();
    }
    
    public int getScore() { return score; }
    public float getElapsedTime() { return elapsedTime; }
}