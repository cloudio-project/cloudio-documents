package ch.hevs.cloudio.example.iobox.ui;

import javax.swing.*;
import java.awt.*;

public class JLed extends JComponent {
    private static final int MARGIN = 10;
    private static final Color BASECOLOR = Color.green;

    private boolean state = false;

    public void setState(boolean state) {
        this.state = state;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calculate maximal possible radius.
        int radius = Math.min(getWidth() / 2, getHeight() / 2) - MARGIN;
        int x = getWidth() / 2 - radius;
        int y = getHeight() / 2 - radius;

        // Create and configure graphics object.
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // Set fill color depending the actual state.
        if (state) {
            g2d.setColor(BASECOLOR.brighter());
        } else {
            g2d.setColor(BASECOLOR.darker());
        }

        // Fill the oval.
        g2d.fillOval(x, y, 2 * radius, 2 * radius);

        // Draw line.
        g2d.setColor(BASECOLOR.darker().darker());
        g2d.setStroke(new BasicStroke((int)(radius/5.)));
        g2d.drawOval(x, y, 2 * radius, 2 * radius);
    }
}
