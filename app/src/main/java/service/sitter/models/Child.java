package service.sitter.models;

import java.util.Date;
import java.io.Serializable;

public class Child implements Serializable {

    private String name;
    private String birthday;
    private String image; //TODO Change image to image object

    // Child is default Ctor. needed for Firestore.
    public Child() {

    }

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

    public int getAge() {
        Date now = new Date();
        long timeBetween = now.getTime() - new Date(birthday).getTime();
        double yearsBetween = timeBetween / 3.15576e+10;
        return (int) Math.floor(yearsBetween);
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
