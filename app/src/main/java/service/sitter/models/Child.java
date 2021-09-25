package service.sitter.models;

import java.util.Date;

public class Child {

    private final String name;
    private final String birthday;
    private final String image; //TODO Change image to image object

    public Child(String name, String birthday, String image) {
        this.name = name;
        this.birthday = birthday;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", birthday='" + birthday + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getAge(){
        Date now = new Date();
        long timeBetween = now.getTime() - new Date(birthday).getTime();
        double yearsBetween = timeBetween / 3.15576e+10;
        return (int) Math.floor(yearsBetween);
    }


    public String getBirthday() {
        return birthday;
    }

    public String getImage() {
        return image;
    }
}
