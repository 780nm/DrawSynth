package ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;
import java.util.TreeMap;

// Drawable parametric graph UI component
public class GraphDrawer extends JPanel {

    private static final int RESOLUTION = 500;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int padding = 25;
    
    private static final Color POINT_COLOR = new Color(90, 90, 90, 255);
    private static final Color GRID_COLOR = new Color(35, 35, 35, 255);

    protected Map<Integer, Double> frames;

    private double min;
    private double max;
    private double baseline;

    // EFFECTS: Creates a new graph element with associated max, min and baseline
    public GraphDrawer(double min, double max, double baseline) {
        super();
        this.min = min;
        this.max = max;
        this.baseline = baseline;
        frames = new TreeMap<>();
        setupMouseClick();
        setupMouseDrag();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    // MODIFIES: this
    // EFFECTS: Resets the graph
    public void reset() {
        frames.clear();
        repaint();
    }

    // EFFECTS: Returns the collection of points displayed
    public Map<Integer, Double> getFrames() {
        return frames;
    }

    // MODIFIES: this
    // EFFECTS: Adds mouse handler for drawing functionality to panel
    private void setupMouseClick() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                frames.clear();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (frames.size() >= 2) {
                    packLine();
                }
                repaint();
            }
        });
    }


    // MODIFIES: this
    // EFFECTS: Adds mouse handler for drawing functionality to panel
    private void setupMouseDrag() {
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
            }

            public void mouseDragged(MouseEvent e) {
                int positionX = e.getX();
                int positionY = e.getY();

                if (positionX >= padding && positionX <= WIDTH && positionY >= 0 && positionY <= HEIGHT) {
                    frames.put((int)((e.getX() - padding) * (double)(RESOLUTION) / (WIDTH - padding)),
                            max - (max - min) * (positionY / (double)HEIGHT));
                }
                repaint();
            }
        });
    }


    // MODIFIES: this
    // EFFECTS: Given set of frames, interpolates intermediate frames and fills line with a linear approximation
    private void packLine() {
        Integer[] keys = new Integer[frames.size()];
        keys = frames.keySet().toArray(keys);

        double previousValue = frames.get(keys[0]);
        double nextValue;

        for (int i = 1; i < keys.length; i++) {
            nextValue = frames.get(keys[i]);
            for (int j = keys[i - 1] + 1; j < keys[i]; j++) {
                frames.put(j,
                        previousValue + (j - keys[i - 1]) * (nextValue - previousValue) / (keys[i] - keys[i - 1]));
            }
            previousValue = nextValue;
        }

    }

    // EFFECTS: Draws the graph and set of point to the frame
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.WHITE);
        g2.fillRect(padding, 0, WIDTH - padding, HEIGHT);

        for (Map.Entry<Integer,Double> frame : frames.entrySet()) {
            g2.setColor(POINT_COLOR);
            g2.fillOval((int)(frame.getKey() * (WIDTH - padding) / (double)RESOLUTION) + padding,
                    (int)((max - frame.getValue()) / (max - min) * HEIGHT), 4, 4);
        }

        g2.setColor(GRID_COLOR);
        g2.drawLine(padding, HEIGHT, padding, 0);
        g2.drawLine(padding, (int)((max - baseline) / (max - min) * HEIGHT), WIDTH,
                (int)((max - baseline) / (max - min) * HEIGHT));

        g2.drawString(String.format("%.1g", max), 0,padding);
        g2.drawString(String.format("%.1g", min), 0,HEIGHT - padding);
        if (baseline != max && baseline != min) {
            g2.drawString(String.format("%.1g", baseline), 0, (int) ((max - baseline) / (max - min) * HEIGHT));
        }

    }

}
