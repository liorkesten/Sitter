package service.sitter.models;

import java.util.UUID;

public abstract class User {
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

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, UserCategory category, String location, String image) {
        uuid = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.category = category;
        this.location = location;
        this.image = image;
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
}
