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

    private List<Family> childFamilies; // Individual is a child in these families
    private List<Family> spouseFamilies; // Individual is a spouse in these families

    public Individual(String ID) {
        this.ID = ID;
        this.name = "";
        this.gender = Gender.NOT_SPECIFIED;
        this.childFamilies = new ArrayList<>();
        this.spouseFamilies = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
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
        if (birthday == null) {
            throw new IllegalArgumentException();
        }

        if (this.death != null) {
            if (this.death.equals(birthday) || this.death.after(birthday)) {
                this.birthday = birthday;
            } else {
                throw new IllegalStateException("Birthday cannot occur after death.");
            }
        } else {
            this.birthday = birthday;
        }
    }

    public Date getDeath() {
        return death;
    }

    public void setDeath(Date death) {
        if (death == null) {
            throw new IllegalArgumentException();
        }

        if (this.birthday != null) {
            if (this.birthday.equals(death) || this.birthday.before(death)) {
                this.death = death;
            } else {
                throw new IllegalStateException("Death cannot occur before birth.");
            }
        } else {
            this.death = death;
        }
    }

    public List<Family> getChildFamilies() {
        return childFamilies;
    }

    public boolean addChildFamily(Family family) {
        return (family != null) ? this.childFamilies.add(family) : false;
    }

    public List<Family> getSpouseFamily() {
        return spouseFamilies;
    }

    public boolean addSpouseFamily(Family family) {
        return (family != null) ? this.spouseFamilies.add(family) : false;
    }

    public long age() {
        if (birthday == null) {
            throw new IllegalStateException("Cannot determine age without a birth date.");
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
            throw new IllegalStateException("Cannot determine living status without a birth date.");
        }
        return (death == null) ? true : false;
    }

    public List<Individual> getChildren() {
        List<Individual> children = new ArrayList<Individual>();
        for (Family family : this.spouseFamilies) {
            children.addAll(family.getChildren());
        }
        return children;
    }

    public List<Individual> getSpouses() {
        List<Individual> spouses = new ArrayList<Individual>();
        for (Family family : this.spouseFamilies) {
            if (family.getHusband().equals(this)) {
                spouses.add(family.getWife());
            } else {
                spouses.add(family.getHusband());
            }
        }
        return spouses;
    }

    @Override
    public String toString() {
        return this.ID;
    }
}
