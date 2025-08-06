import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;

public class MyPanel extends JPanel implements ActionListener, MouseListener {

    SearchSpace searchSpace;
    final int PANEL_WIDTH = 650;
    final int PANEL_HEIGHT = 650;
    Timer timer;

    // Hyperparameter configuration examples
    // 0.9, 2, 0.2;
    // 0.7, 2, 0.5;
    // 1, 4, 0
    // 0.5, 2, 2
    // 0.9, 0.2, 2
    // 0.9, 1.2, 0.2; Note to self: Use this one
    MyPanel() {
        searchSpace = new SearchSpace (0.9, 1.2, 0.2, PANEL_WIDTH, PANEL_HEIGHT, 2, false);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(Color.white);
        timer = new Timer(41, this); //24 fps - 41
        timer.start();
        this.addMouseListener(this);
    }

    public void paint (Graphics g) {

        super.paint(g);

        Graphics2D g2D = (Graphics2D) g;
        int particleSize = 7;

        // Draw walls
        if (searchSpace.getUseWalls()) {
            g2D.setColor(Color.black);
            Rectangle[] walls = searchSpace.getWalls();
            for (Rectangle wall: walls) {
                g2D.fillRect((int)wall.getX(), (int)wall.getY(), (int)wall.getWidth(), (int)wall.getHeight());
            }
        }

        // Draw goal state
        g2D.setColor(Color.red);
        int[] goalState = searchSpace.getGoalState();
        g2D.fillOval(-(particleSize - goalState[0]), -(particleSize - goalState[1]), particleSize, particleSize);

        // Draw particles
        g2D.setColor(Color.green);
        Particle[] particles = searchSpace.getParticles();
        for (Particle particle : particles) {
            double[] position = particle.getCurrentPosition();
            int xCoordinate = (int)Math.round(position[0]);
            int yCoordinate = (int)Math.round(position[1]);
            g2D.fillOval(-(particleSize - xCoordinate), -(particleSize - yCoordinate), particleSize, particleSize);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        searchSpace.pso();
//        double[] gBest = searchSpace.getGBest();
//        double[] currPos = searchSpace.getParticlePosition();
//        System.out.println("Best Global Position at: " + gBest[0] + ", " + gBest[1]);
//        System.out.println("Particle 1 at " + currPos[0] + ", " + currPos[1]);
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        searchSpace.setGoalState(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}