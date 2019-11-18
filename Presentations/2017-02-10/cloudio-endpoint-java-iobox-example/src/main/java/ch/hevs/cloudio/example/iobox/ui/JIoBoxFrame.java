package ch.hevs.cloudio.example.iobox.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class JIoBoxFrame extends JFrame {
    public JIoBoxFrame(final int ioCount) {
        setMinimumSize(new Dimension(ioCount * 75, 200));
        setLayout(new GridLayout(2,4));
        leds = new JLed[ioCount];
        for (int i = 0; i < ioCount; ++i) {
            leds[i] = new JLed();
            add(leds[i]);
        }
        switches= new JSwitch[ioCount];
        for (int i = 0; i < ioCount; ++i) {
            switches[i] = new JSwitch();
            add(switches[i]);
        }
    }

    public JLed getLed(int index) {
        return leds[index];
    }

    public JSwitch getSwitch(int index) {
        return switches[index];
    }

    public static void main(String... args) {
        final JIoBoxFrame box = new JIoBoxFrame(4);
        for (int i = 0; i < 4; ++i) {
            final int j = i;
            box.getSwitch(j).setListener(new JSwitchListener() {
                @Override
                public void stateChanged(JSwitch jswitch) {
                    box.getLed(j).setState(jswitch.getState());
                }
            });
        }

        box.setVisible(true);
    }

    private final JLed leds[];
    private final JSwitch switches[];
}
