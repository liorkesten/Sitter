package service.sitter.models;

import java.util.List;

public class Babysitter extends User {
    private Ranking rank;
    private boolean hasCar;
    private int age;
    private List<String> hobbies;
    private String notes;

    public Babysitter(String firstName, String lastName, String emailAddress, String phoneNumber, String location, String image) {
        super(firstName, lastName, emailAddress, phoneNumber, UserCategory.Babysitter, location, image);
    }
}
