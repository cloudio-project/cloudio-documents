package ch.hevs.cloudio.example.iobox.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class JSwitch extends JComponent {
    private static final int MARGIN = 5;

    private boolean state = false;
    private JSwitchListener listener = null;

    public JSwitch() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                state = !state;
                repaint();
                if (listener != null) {
                    listener.stateChanged(JSwitch.this);
                }
            }
        });
    }

    public void setListener(JSwitchListener listener) {
        this.listener = listener;
    }

    public boolean getState() {
        return state;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calculate target rectangle.
        double size = Math.min(getWidth() / 2., getHeight() / 3.) - MARGIN;
        Rectangle target = new Rectangle(0, 0, (int)(2. * size), (int)(3. * size));
        target.translate((getWidth() - target.width) / 2, (getHeight() - target.height) / 2);

        // Create and configure graphics object.
        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform orig = g2d.getTransform();
        if (state) {
            g2d.translate(target.getCenterX(), target.getCenterY());
            g2d.rotate(Math.PI);
            g2d.translate(-target.getCenterX(), -target.getCenterY());
        }

        g2d.setColor(Color.white);
        g2d.fillRoundRect(target.x, target.y, target.width, target.height, 8, 8);
        g2d.setColor(Color.gray);
        g2d.drawRoundRect(target.x, target.y, target.width, target.height, 8, 8);

        target.grow((int)(-size / 10.), (int)(-size / 10.));
        g2d.setPaint(new GradientPaint(0f, (float)target.y,
                new Color(200, 200, 220), 0f, (float)target.y + target.height, Color.white));
        g2d.fillRoundRect(target.x, target.y, target.width, target.height, 8, 8);

        g2d.setTransform(orig);
        if (state) {
            g2d.translate(0, -size * 0.05);
        } else {
            g2d.translate(0, size * 0.05);
        }

        g2d.setColor(Color.gray);
        g2d.setStroke(new BasicStroke((int)(size / 10.)));
        g2d.drawLine((int)target.getCenterX(), (int)(target.y + size / 3.), (int)target.getCenterX(),
                (int)(target.getCenterY() - size / 1.5));
        g2d.drawOval((int)(target.getCenterX() - size / 6.), (int)((target.y + target.height) - size / 1.5),
                (int)(size / 3.), (int)(size / 2.5));

    }
}
