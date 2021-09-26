package service.sitter.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class User implements Serializable {
    private static final String TAG = User.class.getSimpleName();


    private String uuid;
    private String firstName;
    private String lastName;
    //TODO Change to email address class
    private String emailAddress;
    //TODO Change to phone number class
    private String phoneNumber;
    private UserCategory category;
    // TODO Location
    private String location;
    //TODO Change to image
    private String image;
    private List<Connection> connections;


    // User is default Ctor. needed for Firestore.
    public User() {
    }

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, UserCategory category, String location, String image) {
        uuid = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.location = location;
        this.image = image;
        this.connections = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", category=" + category +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", connections=" + connections +
                '}';
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserCategory getCategory() {
        return category;
    }

    public void setCategory(UserCategory category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void addConnection(Connection newConnection) {
        Log.d(TAG, String.format("Adding new connection <%s> to <%s> user.", newConnection, this));
        this.connections.add(newConnection);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }
}
