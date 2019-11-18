package ch.hevs.cloudio.example.iobox.model;

import ch.hevs.cloudio.endpoint.*;

public class Outputs extends CloudioObject{
    @SetPoint
    public CloudioAttribute<Boolean> led0;

    public Outputs() throws CloudioAttributeConstraintException, CloudioAttributeInitializationException {
        led0.setInitialValue(false);
    }
}
