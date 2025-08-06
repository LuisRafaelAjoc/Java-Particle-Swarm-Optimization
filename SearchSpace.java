import java.util.*;
import java.awt.Rectangle;

public class SearchSpace {

    Random rd = new Random();

    final private Particle[] particles = new Particle[100]; // Array of particle objects

    private Rectangle[] walls = {
            new Rectangle(250, 50, 50, 100),
            new Rectangle(250, 200, 50, 100),
            new Rectangle(250, 400, 50, 200),
            new Rectangle(50, 250, 100, 50),
            new Rectangle(50, 500, 100, 50),
            new Rectangle(300, 50, 200, 50),
            new Rectangle(400, 150, 200, 50),
            new Rectangle(300, 250, 200, 50),
            new Rectangle(400, 350, 200, 50),
            new Rectangle(300, 500, 300, 50)
    };

    private int[] goalState = {450, 450};

    private double[] gBest = {0, 0}; // global best position

    private double w; // inertia, influences capacity to change direction
    private double c1; //cognitive coefficient, affects the influence of pBest on calculating velocity
    private double c2; //social coefficient, affects the influence of gBest on calculating velocity

    private int[] bounds = {0, 0}; // Limits movement to specific 2d area. Index 0 for x coordinates, index 1 for y coordinates
    private boolean useWalls;

    SearchSpace (double inertia, double cognitiveCoefficient, double socialCoefficient, int xBound, int yBound, int setConfiguration, boolean useWalls) {
        this.w = inertia;
        this.c1 = cognitiveCoefficient;
        this.c2 = socialCoefficient;
        this.bounds[0] = xBound;
        this.bounds[1] = yBound;
        this.useWalls = useWalls;

        if (setConfiguration == 1) {
//            Configuration 1: set initial positions of particles
            for (int i = 0; i < particles.length; i++) {
                double[] initialPosition = {rd.nextDouble(bounds[0]), rd.nextDouble(bounds[1])};
                particles[i] = new Particle (initialPosition);
            }
        } else if (setConfiguration == 2) {
//            Configuration 2: randomly initialize positions of particles in top left corner version 1
            for (int i = 0; i < particles.length; i++) {
                double[] initialPosition = {rd.nextDouble( 100), rd.nextDouble(100)};
                particles[i] = new Particle (initialPosition);
            }
        } else {
//            Configuration 3: randomly initialize positions of particles in top left corner version 2
            for (int i = 0; i < particles.length; i++) {
                double[] initialPosition = {rd.nextDouble( 200), rd.nextDouble(200)};
                particles[i] = new Particle (initialPosition);
            }
        }
    }

    public void updateGBest (double[] pBest) {
        double scoreGBest = Math.pow(gBest[0] - goalState[0], 2) + Math.pow(gBest[1] - goalState[1], 2);
        double scorePBest = Math.pow(pBest[0] - goalState[0], 2) + Math.pow(pBest[1] - goalState[1], 2);
        if (scorePBest < scoreGBest) {
            this.gBest = pBest;
        }
    }

    public double[] calculateNewVelocity(double[] currentPosition, double[] pBest, double[] velocity) {
        double[] newVelocity = {0, 0};

        // Random variables to introduce randomness in solutions
        double r1 = rd.nextDouble();
        double r2 = rd.nextDouble();

        for (int i = 0; i < 2; i++) {
            newVelocity[i] = (this.w * velocity[i]) + (this.c1 * r1 * (pBest[i] - currentPosition[i])) + (this.c2 * r2 * (this.gBest[i] - currentPosition[i]));
        }

        return newVelocity;
    }

    public double[] updatePosition (double[] currentPosition, double[] velocity) {
        double[] newPosition = new double[2];

        for (int i = 0; i < 2; i++) {
            newPosition[i] = currentPosition[i] + velocity[i];
        }

        if (useWalls) {
            for (Rectangle wall: walls) {
                if(wall.contains(newPosition[0], newPosition[1])) {
                    return currentPosition;
                }
            }
        }

        for (int i = 0; i < 2; i++) {
            if (newPosition[i] < 0) {
                newPosition[i] = 0;
            }
            if(newPosition[i] > this.bounds[i]) {
                newPosition[i] = this.bounds[i];
            }
        }

        return newPosition;
    }

    public double[] getGBest (){
        return this.gBest;
    }

    public int[] getGoalState () {
        return this.goalState;
    }

    public void setGoalState (int x, int y) {
        this.goalState[0] = x;
        this.goalState[1] = y;
    }

    public Rectangle[] getWalls () {
        return this.walls;
    }

    public Particle[] getParticles () {
        return this.particles;
    }

    public boolean getUseWalls () {
        return useWalls;
    }

    public void pso () {
        for (Particle particle: particles) {
            double[] newVelocity = calculateNewVelocity(particle.getCurrentPosition(), particle.getBestPosition(), particle.getVelocity());
            particle.setVelocity(newVelocity);
            particle.setCurrentPosition(updatePosition(particle.getCurrentPosition(), particle.getVelocity()));
            particle.updatePBest(goalState);
        }
        for(Particle particle: particles) {
            updateGBest(particle.getBestPosition());
        }
    }

    public double[] getParticlePosition () {
        return particles[1].getCurrentPosition();
    }
}
