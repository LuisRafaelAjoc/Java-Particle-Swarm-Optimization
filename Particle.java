public class Particle {
    private double[] currentPosition = new double[2];
    private double[] pBest = new double[2]; // personal best position
    private double[] velocity = {1, 1};

    Particle (double[] initialPosition) {
        this.currentPosition = initialPosition;
        this.pBest = initialPosition;
    }

    public void updatePBest (int[] goalState) {
        double scorePBest = Math.pow(this.pBest[0] - goalState[0], 2) + Math.pow(this.pBest[1] - goalState[1], 2);
        double scoreCurrentPosition = Math.pow(this.currentPosition[0] - goalState[0], 2) + Math.pow(this.currentPosition[1] - goalState[1], 2);
        if (scoreCurrentPosition < scorePBest) {
            this.pBest = currentPosition;
        }
    }

    public void setCurrentPosition (double[] position) {
        this.currentPosition = position;
    }

    public double[] getCurrentPosition () {
        return this.currentPosition;
    }

    public double[] getBestPosition () {
        return this.pBest;
    }

    public void setVelocity (double[] velocity) {
        this.velocity = velocity;
    }

    public double[] getVelocity () {
        return this.velocity;
    }
}
