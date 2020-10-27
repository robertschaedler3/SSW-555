package gedcom.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import gedcom.interfaces.Gender;

public class Family {

    public static final int MAX_SIBLINGS = 15;
    public static final int MAX_MULTIPLE_BIRTH = 5;
    public static final int MIN_MARRIAGE_AGE = 14;
    public static final int FATHER_AGE_THRESHOLD = 80;
    public static final int MOTHER_AGE_THRESHOLD = 60;

    private String ID;
    private Individual husband;
    private Individual wife;

    private Date marriage;
    private Date divorce;

    private List<Individual> children;

    public Family(String ID) {
        this.ID = ID;
        this.children = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public Individual getHusband() {
        return husband;
    }

    public Individual getWife() {
        return wife;
    }

    public void setHusband(Individual husband) {
        if (husband == null) {
            throw new IllegalArgumentException();
        }

        if (husband.getGender() == Gender.F) {
            throw new IllegalStateException(String.format("Error US21: Individual %s gender is not MALE.", husband.getID()));
        }
        
        this.husband = husband;
    }
    
    public void setWife(Individual wife) {
        if (wife == null) {
            throw new IllegalArgumentException();
        }
        
        if (wife.getGender() == Gender.M) {
            throw new IllegalStateException(String.format("Error US21: Individual %s gender is not FEMALE.", wife.getID()));
        }
        
        this.wife = wife;
    }

    public Date getMarriage() {
        return marriage;
    }

    public void setMarriage(Date marriage) {
        if (marriage == null) {
            throw new IllegalArgumentException();
        }

        if (marriage.after(new Date())) {
            throw new IllegalStateException("Error US01: Marriage must occur before the current time.");
        }

        if (this.divorce != null) {
            if (this.divorce.equals(marriage) || this.divorce.after(marriage)) {
                this.marriage = marriage;
            } else {
                throw new IllegalStateException("Error US04: Marriage cannot occur after divorce.");
            }
        } else {
            this.marriage = marriage;
        }
    }

    public Date getDivorce() {
        return divorce;
    }

    public void setDivorce(Date divorce) {
        if (divorce == null) {
            throw new IllegalArgumentException();
        }

        if (divorce.after(new Date())) {
            throw new IllegalStateException("Error US01: Divorce must occur before the current time.");
        }

        if (this.marriage != null) {
            if (this.marriage.equals(divorce) || this.marriage.before(divorce)) {
                this.divorce = divorce;
            } else {
                throw new IllegalStateException("Error US04: Divorce cannot occur before marriage.");
            }
        } else {
            this.divorce = divorce;
        }
    }

    public List<Individual> getChildren() {
        children.sort(Comparator.comparing(Individual::getBirthday, Comparator.nullsFirst(Comparator.naturalOrder())).reversed());
        return children;
    }

    public boolean addChild(Individual child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null.");
        }
        if (children.contains(child)) {
            throw new IllegalArgumentException("Child already exists in family.");
        }

        if (children.size() >= MAX_SIBLINGS) {
            throw new IllegalStateException(String.format("Error US22: A family can only have a max of %d children.", MAX_SIBLINGS));
        }

        return this.children.add(child);
    }

    public List<Family> getChildFamilies() {
        List<Family> childFamilies = new ArrayList<Family>();

        for (Individual individual : children) {
            childFamilies.addAll(individual.getChildFamilies());
        }

        return childFamilies;
    }

    public boolean isChild(Individual individual) {
        return children.contains(individual);
    }

    @Override
    public String toString() {
        return this.ID;
    }

}
