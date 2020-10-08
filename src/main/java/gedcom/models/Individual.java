package gedcom.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gedcom.interfaces.Gender;

public class Individual {

    private String ID;
    private String name;

    private Gender gender;

    private Date birthday;
    private Date death;

    private List<Family> childrenFamilies;
    private List<Family> spouseFamilies;

    public Individual(String ID) {
        this.ID = ID;
        this.name = "";
        this.gender = Gender.NOT_SPECIFIED;
        this.childrenFamilies = new ArrayList<>();
        this.spouseFamilies = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        if (birthday != null) {
            if (this.getDeath() == null) { // death not set
                this.birthday = birthday;
            } else if (this.getDeath().after(birthday)) {
                this.birthday = birthday;
            } else {
                System.out.println("Birthday cannot be set because death occurred before birthday");
            }
        }

    }

    public Date getDeath() {
        return death;
    }

    public void setDeath(Date death) {
        if (death != null) {
            if (this.getBirthday() == null) {
                this.death = death;
            } else if (this.getBirthday().before(death)) {
                this.death = death;
            } else {
                System.out.println("Death cannot be set because birthday occurred after death");
            }
        }

    }

    public List<Family> getChildrenFamily() {
        return childrenFamilies;
    }

    public boolean addChildFamily(Family family) {
        return this.childrenFamilies.add(family);
    }

    public List<Family> getSpouseFamily() {
        return spouseFamilies;
    }

    public boolean addSpouseFamily(Family family) {
        return this.spouseFamilies.add(family);
    }

    public long age() {
        if (birthday == null) {
            throw new IllegalStateException();
        }

        long diff;
        if (death != null) {
            diff = death.getTime() - birthday.getTime();
        } else {
            diff = (new Date()).getTime() - birthday.getTime();
        }
        return diff / (1000l * 60 * 60 * 24 * 365);
    }

    public boolean alive() {
        if (birthday == null) {
            throw new IllegalStateException();
        }
        return (death == null) ? true : false;
    }

    public List<Individual> getChildren() {
        List<Individual> children = new ArrayList<Individual>();
        for (Family family : this.childrenFamilies) {
            children.addAll(family.getChildren());
        }
        return children;
    }

    @Override
    public String toString() {
        return this.ID;
    }
}
