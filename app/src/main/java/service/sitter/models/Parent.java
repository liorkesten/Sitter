package huji.postpc.exercises.sitter.models;

import java.util.List;

public class Parent extends User {
    // Parent connections.
    private List<Connection> connections;
    // Array of the children.
    private List<Child> children;
    // The default price per hour that the parent would pay for babysitter.
    private int defaultPricePerHour;
}
