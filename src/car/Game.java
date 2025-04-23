package car;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import java.awt.BorderLayout;

public class Game implements GLEventListener, KeyListener {
    private BackgroundMusicPlayer musicPlayer;

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
    private final float MIN_Z_DISTANCE = 2.0f;

    private List<float[]> activeObstaclePositions = new ArrayList<>();
    private List<ParticleSystem> particleSystems = new ArrayList<>();  // Particle systems list

    public Game(JFrame frame) {
        this.frame = frame;

        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities capabilities = new GLCapabilities(profile);

        canvas = new GLJPanel(capabilities);
        canvas.addGLEventListener(this);
        canvas.addKeyListener(this);

        animator = new FPSAnimator(canvas, 60);

        car = new Car();
        road = new Road();
        camera = new Camera(car);
        scoreManager = new ScoreManager();
        forestBackground = new ForestBackground();
        gameObjects = new ArrayList<>();
        File musicFile = new File("bg_car.wav");
        if (!musicFile.exists()) {
            System.out.println("Error: Music file not found at path " + musicFile.getAbsolutePath());
        } else {
            musicPlayer = new BackgroundMusicPlayer(musicFile.getAbsolutePath());
        }

        gameObjects.add(new BoxObstacle());
        gameObjects.add(new BananaObstacle());
    }

    public GLJPanel getCanvas() {
        return canvas;
    }

    public void start() {
        animator.start();
        canvas.requestFocusInWindow();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_DEPTH_TEST);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {}

    private void updateSpawnInterval() {
        Random rand = new Random();
        spawnInterval = 4.0f + rand.nextFloat(); // 4.0 to 5.0
    }

    private boolean isPositionOccupied(float x, float z) {
        for (float[] pos : activeObstaclePositions) {
            float dz = Math.abs(pos[1] - z);
            if (dz < MIN_Z_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    private void spawnNewObstacle() {
        if (gameObjects.size() < MAX_OBSTACLES) {
            Random rand = new Random();
            float x = rand.nextInt(3) - 1; // Lane: -1, 0, 1
            float z = car.getZ() - 40.0f;
            float y = 0.35f;

            if (!isPositionOccupied(x, z)) {
                GameObject newObstacle = getRandomObstacle(x, y, z);
                gameObjects.add(newObstacle);
                activeObstaclePositions.add(new float[]{x, z});
            }
        }
    }

    private GameObject getRandomObstacle(float x, float y, float z) {
        Random rand = new Random();
        int choice = rand.nextInt(2); // 0 or 1

        GameObject obj = (choice == 0) ? new BoxObstacle() : new BananaObstacle();
        obj.setX(x);
        obj.setY(y);
        obj.setZ(z);
        return obj;
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        if (!gameOver) {
            // Start background music if not already playing
            if (!musicPlayer.isPlaying()) {
                musicPlayer.play();
            }

            // Update world
            road.update();
            car.update();
            forestBackground.update();

            // Spawn new obstacles
            long currentTime = System.currentTimeMillis();
            float deltaTime = (currentTime - lastSpawnTime) / 1000f;

            if (deltaTime >= spawnInterval) {
                spawnNewObstacle();
                lastSpawnTime = currentTime;
                updateSpawnInterval();
            }

            // Remove old obstacles
            activeObstaclePositions.removeIf(pos -> pos[1] > car.getZ() + 5.0f);

            // Update and handle game objects
            Iterator<GameObject> objIterator = gameObjects.iterator();
            while (objIterator.hasNext()) {
                GameObject obj = objIterator.next();
                obj.update();

                // Check collision with the car
                if (CollisionDetector.checkCollision(car, obj, scoreManager)) {
                    if (obj instanceof BoxObstacle) {
                        // Game over when colliding with a BoxObstacle
                        gameOver = true;
                        animator.stop();
                        showRestartMenu();
                        break;
                    } else if (obj instanceof BananaObstacle) {
                        // Trigger particles at the banana's location
                        ParticleSystem particleSystem = new ParticleSystem(obj.getX(), obj.getY(), obj.getZ());
                        particleSystems.add(particleSystem);

                        // Remove the banana obstacle immediately after collision
                        objIterator.remove();
                        continue; // Skip further checks for this obstacle
                    }
                } else if (!obj.isPassed() && obj.getZ() > car.getZ() + 1.0f) {
                    obj.setPassed(true);
                }
            }

            // Update particle systems
            Iterator<ParticleSystem> psIterator = particleSystems.iterator();
            while (psIterator.hasNext()) {
                ParticleSystem ps = psIterator.next();
                ps.update(deltaTime); // Pass deltaTime
                if (ps.isDead()) {
                    psIterator.remove();
                }
            }
        } else {
            // Stop music if game is over
            musicPlayer.stop();
        }

        // Update camera
        camera.update();
        camera.applyView(gl);

        // Draw background and objects
        forestBackground.draw(gl);
        road.draw(gl);
        car.draw(gl);

        for (GameObject obj : gameObjects) {
            obj.draw(gl);
        }

        // Draw active particles
        for (ParticleSystem ps : particleSystems) {
            ps.draw(gl);
        }

        // Draw UI
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();

        scoreManager.render(gl, width, height);

        if (gameOver) {
            scoreManager.renderGameOver(gl, width, height);
        }
    }

    private void showRestartMenu() {
        frame.getContentPane().removeAll();
        RestartMenu restartMenu = new RestartMenu(frame, animator);
        frame.getContentPane().add(restartMenu.getPanel(), BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private void restartGame() {
        frame.getContentPane().removeAll();
        Game newGame = new Game(frame);
        GLJPanel canvas = newGame.getCanvas();
        frame.getContentPane().add(canvas);
        frame.revalidate();
        frame.repaint();
        newGame.start();
        canvas.requestFocusInWindow();
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
        car.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        car.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}
