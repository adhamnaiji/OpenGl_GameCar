package car;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class Game implements GLEventListener, KeyListener {
    private GLJPanel canvas;
    private FPSAnimator animator;
    private Car car;
    private Road road;
    private Camera camera;
    private List<GameObject> gameObjects;
    private ScoreManager scoreManager;
    private ForestBackground forestBackground;
    private boolean gameOver = false;
    private JFrame frame;

    private long lastSpawnTime = System.currentTimeMillis();
    private float spawnInterval = 4.0f;
    private final int MAX_OBSTACLES = 4;
    private List<float[]> activeObstaclePositions = new ArrayList<>();

    public Game(JFrame frame) {
        this.frame = frame;

        // Set up OpenGL profile and capabilities
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        // Initialize the OpenGL canvas (GLJPanel)
        canvas = new GLJPanel(capabilities);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);

        // Set up the FPS animator (60 frames per second)
        animator = new FPSAnimator(canvas, 60);

        // Initialize game components
        car = new Car();
        road = new Road();
        camera = new Camera(car);
        scoreManager = new ScoreManager();
        forestBackground = new ForestBackground();
        gameObjects = new ArrayList<>();

        // Add obstacles to game objects
        gameObjects.add(new BoxObstacle());
        gameObjects.add(new TomatoObstacle());
        gameObjects.add(new BananaObstacle());
    }

    // Getter for the canvas (OpenGL panel)
    public GLJPanel getCanvas() {
        return canvas;
    }

    // Start the animation loop
    public void start() {
        animator.start();
        canvas.requestFocusInWindow();
    }

    // Getter for the animator
    public FPSAnimator getAnimator() {
        return animator;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);  // Enable depth test for 3D rendering
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    // Update spawn interval for obstacles
    private void updateSpawnInterval() {
        Random rand = new Random();
        spawnInterval = 4.0f + rand.nextFloat(); // Random spawn time between 4.0 and 5.0 seconds
    }

    // Check if a position is already occupied by an obstacle
    private boolean isPositionOccupied(float x, float z) {
        final float MIN_DISTANCE = 0.5f;
        for (float[] pos : activeObstaclePositions) {
            float dx = pos[0] - x;
            float dz = pos[1] - z;
            if (Math.sqrt(dx * dx + dz * dz) < MIN_DISTANCE) {
                return true;  // The position is occupied
            }
        }
        return false;  // The position is free
    }

    // Spawn a new obstacle at a random position
    private void spawnNewObstacle() {
        if (gameObjects.size() < MAX_OBSTACLES) {
            Random rand = new Random();
            float x = rand.nextInt(3) - 1;  // Random position on x-axis (-1 to 1)
            float z = car.getZ() - 40.0f;   // Spawn behind the car on the z-axis
            float y = 0.35f;  // Fixed height for obstacles

            if (!isPositionOccupied(x, z)) {
                GameObject newObstacle = getRandomObstacle(x, y, z);
                gameObjects.add(newObstacle);
                activeObstaclePositions.add(new float[]{x, z});  // Add position to active list
            }
        }
    }

    // Get a random obstacle type to spawn
    private GameObject getRandomObstacle(float x, float y, float z) {
        Random rand = new Random();
        int choice = rand.nextInt(3);  // Randomly choose an obstacle type

        GameObject obj;
        switch (choice) {
            case 0: obj = new TomatoObstacle(); break;
            case 1: obj = new BoxObstacle(); break;
            default: obj = new BananaObstacle(); break;
        }

        obj.setX(x);
        obj.setY(y);
        obj.setZ(z);
        return obj;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);  // Clear the screen

        if (!gameOver) {
            road.update();
            car.update();
            forestBackground.update();

            // Spawn obstacles at regular intervals
            long currentTime = System.currentTimeMillis();
            float deltaTime = (currentTime - lastSpawnTime) / 1000f;

            if (deltaTime >= spawnInterval) {
                spawnNewObstacle();
                lastSpawnTime = currentTime;
                updateSpawnInterval();
            }

            // Update and check collisions for all obstacles
            for (int i = 0; i < gameObjects.size(); i++) {
                GameObject obj = gameObjects.get(i);
                obj.update();

                // Check if car collides with any obstacle
                if (CollisionDetector.checkCollision(car, obj)) {
                    gameOver = true;
                    animator.stop();  // Stop the animation loop

                    // Trigger restart menu when the game is over
                    showRestartMenu();
                    break;
                } else if (!obj.isPassed() && obj.getZ() > car.getZ() + 1.0f) {
                    scoreManager.incrementScore();  // Increment score if the obstacle is passed
                    obj.setPassed(true);
                }
            }

            scoreManager.update();  // Update score display
        }

        camera.update();  // Update camera
        camera.applyView(gl);  // Apply the camera's view to OpenGL

        // Draw background, road, car, and obstacles
        forestBackground.draw(gl);
        road.draw(gl);
        car.draw(gl);

        for (GameObject obj : gameObjects) {
            obj.draw(gl);  // Draw each obstacle
        }

        // Render the score
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();
        scoreManager.render(gl, width, height);

        // Display the game-over screen if the game is over
        if (gameOver) {
            scoreManager.renderGameOver(gl, width, height);
        }
    }

    // Show the restart menu
    private void showRestartMenu() {
        // Remove game content and show restart menu
        frame.getContentPane().removeAll();
        RestartMenu restartMenu = new RestartMenu(frame, getAnimator());  // Pass animator to RestartMenu
        frame.getContentPane().add(restartMenu.getPanel());  // Use getPanel() from RestartMenu
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(60.0, (double) width / height, 0.1, 1000);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        car.handleKeyPress(e);  // Handle key press for car movement
    }

    @Override
    public void keyReleased(KeyEvent e) {
        car.handleKeyRelease(e);  // Handle key release for car movement
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
