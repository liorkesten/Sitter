package service.sitter.models;

import android.provider.ContactsContract;

import java.util.UUID;

public abstract class User {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String emailAddress; //TODO Change to email address class
    private String phoneNumber; //TODO Change to phone number class
    private UserCategory category;
    private String location; // TODO Location
    private String image; //TODO Change to image
}
