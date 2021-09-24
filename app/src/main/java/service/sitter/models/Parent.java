package service.sitter.models;

import java.util.List;

public class Parent extends User {
    // Parent connections.
    private List<Connection> connections;
    // Array of the children.
    private List<Child> children;
    // The default price per hour that the parent would pay for babysitter.
    private int defaultPricePerHour;

    public Parent(String firstName, String lastName, String emailAddress, String phoneNumber, String location, String image) {
        super(firstName, lastName, emailAddress, phoneNumber, UserCategory.Parent, location, image);
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public List<Child> getChildren() {
        return children;
    }

    public void setChildren(List<Child> children) {
        this.children = children;
    }

    public int getDefaultPricePerHour() {
        return defaultPricePerHour;
    }

    public void setDefaultPricePerHour(int defaultPricePerHour) {
        this.defaultPricePerHour = defaultPricePerHour;
    }
}
