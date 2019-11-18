package ch.hevs.cloudio.example.iobox.model;

import ch.hevs.cloudio.endpoint.Implements;
import ch.hevs.cloudio.endpoint.CloudioNode;

@Implements(value = {"Toto"})
public class IoBox extends CloudioNode {
    public Inputs inputs;
    public Outputs outputs;
}
