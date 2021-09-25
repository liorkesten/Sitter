package service.sitter.models;

import java.io.Serializable;

public class Child implements Serializable {

    private String name;
    private int age;
    private String image; //TODO Change image to image object

    // Child is default Ctor. needed for Firestore.
    public Child() {

    }

    public Child(String name, int age, String image) {
        this.name = name;
        this.age = age;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getImage() {
        return image;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
