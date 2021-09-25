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


    public Ranking getRank() {
        return rank;
    }

    public void setRank(Ranking rank) {
        this.rank = rank;
    }

    public boolean isHasCar() {
        return hasCar;
    }

    public void setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
