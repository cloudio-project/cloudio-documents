package ch.hevs.cloudio.example.iobox;

import ch.hevs.cloudio.endpoint.*;
import ch.hevs.cloudio.example.iobox.model.IoBox;
import ch.hevs.cloudio.example.iobox.ui.JIoBoxFrame;
import ch.hevs.cloudio.example.iobox.ui.JSwitch;
import ch.hevs.cloudio.example.iobox.ui.JSwitchListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class IoBoxExample {
    public static void main(String... args) {
        try {
            final CloudioEndpoint endpoint = new CloudioEndpoint("test_client");
            final IoBox box = endpoint.addNode("io", IoBox.class);
            final JIoBoxFrame frame = new JIoBoxFrame(1);

            frame.getSwitch(0).setListener(new JSwitchListener() {
                @Override
                public void stateChanged(JSwitch jswitch) {
                    try {
                        box.inputs.switch0.setValue(jswitch.getState());
                    } catch (CloudioAttributeConstraintException e) {
                        e.printStackTrace();
                    }
                }
            });

            box.outputs.led0.addListener(new CloudioAttributeListener<Boolean>() {
                @Override
                public void attributeHasChanged(CloudioAttribute<Boolean> attribute) {
                    frame.getLed(0).setState(attribute.getValue());
                }
            });

            frame.setVisible(true);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    endpoint.close();
                    //Runtime.getRuntime().exit(0);
                }
            });

        } catch (InvalidUuidException e) {
            e.printStackTrace();
        } catch (InvalidPropertyException e) {
            e.printStackTrace();
        } catch (CloudioEndpointInitializationException e) {
            e.printStackTrace();
        } catch (DuplicateNamedItemException e) {
            e.printStackTrace();
        }
    }
}
