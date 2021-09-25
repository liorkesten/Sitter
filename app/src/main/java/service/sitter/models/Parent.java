package service.sitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Parent extends User implements Serializable {
    // Array of the children.
    private List<Child> children;
    // The default price per hour that the parent would pay for babysitter.
    private int defaultPricePerHour;

    // Parent is default Ctor. needed for Firestore.
    public Parent() {
        super();
    }

    @Override
    public String toString() {
        return "Parent{" +
                "children=" + children +
                ", defaultPricePerHour=" + defaultPricePerHour +
                '}';
    }

    public Parent(String firstName, String lastName, String emailAddress, String phoneNumber,
                  String location, String image, List<Child> children, int defaultPricePerHour) {
        super(firstName, lastName, emailAddress, phoneNumber, UserCategory.Parent, location, image);

        this.children = new ArrayList<>(children);
        this.defaultPricePerHour = defaultPricePerHour;
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
