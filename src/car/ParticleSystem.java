package car;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jogamp.opengl.GL2;

public class ParticleSystem {
    private List<Particle> particles;
    private float x, y, z;

    public ParticleSystem(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        particles = new ArrayList<>();
    }
    public boolean isDead() {
        return particles.isEmpty();
    }
    public void spawn(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;

        System.out.println("Spawning particles at: (" + x + ", " + y + ", " + z + ")");
        for (int i = 0; i < 100; i++) {  // Spawn 100 particles for example
            particles.add(new Particle(x, y, z));
        }

        System.out.println("Particles spawned: " + particles.size());
    }

    public void update(float deltaTime) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update(deltaTime);
            System.out.println("Updating particle at: (" + p.getX() + ", " + p.getY() + ", " + p.getZ() + "), life: " + p.getLife());

            if (p.isDead()) {
                it.remove();
            }
        }
    }

    public void draw(GL2 gl) {
        for (Particle p : particles) {
            p.draw(gl);
        }
    }
}
