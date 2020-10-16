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

    public String getFirstName() {
        String[] parts = this.name.split("/");
        return (parts.length > 0) ? parts[0].trim() : "";
    }

    public String getLastName() {
        String[] parts = this.name.split("/");
        return (parts.length > 1) ? parts[1].trim() : "";
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
                throw new IllegalStateException("US03: Birth cannot occur after death.");
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
                throw new IllegalStateException("US03: Death cannot occur before birth.");
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

    public List<Family> getSpouseFamilies() {
        return spouseFamilies;
    }

    public boolean addSpouseFamily(Family family) {
        return (family != null) ? this.spouseFamilies.add(family) : false;
    }

    public int age() {
        if (birthday == null) {
            throw new IllegalStateException("Cannot determine age without a birth date.");
        }

        long diff;
        if (death != null) {
            diff = death.getTime() - birthday.getTime();
        } else {
            diff = (new Date()).getTime() - birthday.getTime();
        }
        return (int) (diff / (1000l * 60 * 60 * 24 * 365));
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

    /**
     * Creates and returns a list of all the individual's spouses from all
     * marriages.
     */
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

    /**
     * Creates and returns a list of all descendants. Returns an emtpy list if there
     * are no descendants.
     */
    public List<Individual> getDescendants() {
        List<Individual> descendants = new ArrayList<>();
        List<Individual> children = this.getChildren();

        descendants.addAll(children);

        for (Individual child : children) {
            descendants.addAll(child.getDescendants());
        }

        return descendants;
    }

    /**
     * Creates a list of all the descendants at distance i from the current
     * individual.
     * 
     * @param i
     * @return list of descendants
     */
    public List<Individual> getDescendantsAt(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }

        List<Individual> descendants = new ArrayList<>();
        if (i == 0) {
            descendants.add(this);
            return descendants;
        } else {
            for (Individual child : this.getChildren()) {
                descendants.addAll(child.getDescendantsAt(i - 1));
            }
            return descendants;
        }
    }

    /**
     * Checks is the individual is a descendant of the given individual.
     * 
     * @param individual
     * @return true if the individual is a descendant, false otherwise
     */
    public boolean isDescendant(Individual individual) {
        List<Individual> children = individual.getChildren();
        if (children.isEmpty()) {
            return false;
        } else if (children.contains(this)) {
            return true;
        } else {
            for (Individual child : children) {
                if (this.isDescendant(child)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Checks is the given individual is a descendant of the current individual.
     * 
     * @param individual
     * @return true if the individual is a descendant, false otherwise
     */
    public boolean hasDescendant(Individual individual) {
        List<Individual> children = this.getChildren();
        if (children.isEmpty()) {
            return false;
        } else if (children.contains(individual)) {
            return true;
        } else {
            for (Individual child : children) {
                if (child.hasDescendant(individual)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Creates and returns a list of all known parents of an individual.
     */
    public List<Individual> getParents() {
        List<Individual> parents = new ArrayList<Individual>();
        Individual father;
        Individual mother;
        for (Family family : this.childFamilies) {
            if ((father = family.getHusband()) != null) {
                parents.add(father);
            }
            if ((mother = family.getWife()) != null) {
                parents.add(mother);
            }
        }
        return parents;
    }

    /**
     * Creates and returns a list of all ancestors. Returns an emtpy list if there
     * are no ancestors.
     */
    public List<Individual> getAncestors() {
        List<Individual> ancestors = new ArrayList<>();
        List<Individual> parents = this.getParents();

        ancestors.addAll(parents);

        for (Individual parent : parents) {
            ancestors.addAll(parent.getAncestors());
        }

        return ancestors;
    }

    /**
     * Creates a list of all the ancestors at distance i from the current
     * individual.
     * 
     * @param i
     * @return list of ancestors
     */
    public List<Individual> getAncestorsAt(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }

        List<Individual> ancestors = new ArrayList<>();
        if (i == 0) {
            ancestors.add(this);
            return ancestors;
        } else {
            for (Individual parent : this.getParents()) {
                ancestors.addAll(parent.getAncestorsAt(i - 1));
            }
        }
        return ancestors;
    }

    /**
     * Checks is the current individual is an ancestor of the given individual.
     * 
     * @param individual
     * @return true if the given individual is an ancestor, false otherwise
     */
    public boolean isAncestor(Individual individual) {
        List<Individual> parents = individual.getParents();
        if (parents.isEmpty()) {
            return false;
        } else if (parents.contains(this)) {
            return true;
        } else {
            for (Individual parent : parents) {
                if (this.isAncestor(parent)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Checks is the given individual is an ancestor of the current individual.
     * 
     * @param individual
     * @return true if the individual is an ancestor, false otherwise
     */
    public boolean hasAncestor(Individual individual) {
        List<Individual> parents = this.getParents();
        if (parents.isEmpty()) {
            return false;
        } else if (parents.contains(individual)) {
            return true;
        } else {
            for (Individual parent : parents) {
                if (parent.hasAncestor(individual)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Makes a list of an individuals 1st cousins. Same as getCousins(1).
     */
    public List<Individual> getCousins() {
        return getCousins(1);
    }

    /**
     * Makes a list of an individuals nth cousins.
     */
    public List<Individual> getCousins(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }

        List<Individual> cousins = new ArrayList<>();
        List<Individual> descendants = new ArrayList<>();

        for (Individual individual : this.getAncestorsAt(n)) {
            descendants.addAll(individual.getDescendantsAt(n));
        }

        for (Individual individual : this.getAncestorsAt(n + 1)) {
            cousins.addAll(individual.getDescendantsAt(n + 1));
        }

        cousins.removeAll(descendants);
        return cousins;
    }

    @Override
    public String toString() {
        return this.ID;
    }
}
