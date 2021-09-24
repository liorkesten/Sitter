package service.sitter.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class User {
    private static final String TAG = User.class.getSimpleName();


    private final String uuid;
    private final String firstName;
    private final String lastName;
    //TODO Change to email address class
    private final String emailAddress;
    //TODO Change to phone number class
    private final String phoneNumber;
    private final UserCategory category;
    // TODO Location
    private final String location;
    //TODO Change to image
    private final String image;
    private final List<Connection> connections;


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

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserCategory getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
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
}
