package service.sitter.models;

public class Child {

    private final String name;
    private final int age;
    private final String image; //TODO Change image to image object

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
}
