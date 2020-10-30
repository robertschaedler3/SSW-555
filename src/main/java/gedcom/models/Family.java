package gedcom.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gedcom.interfaces.Gender;
import gedcom.logging.Error;

public class Family extends GEDObject {

    public static final int MAX_CHILDREN = 15;
    public static final int MAX_MULTIPLE_BIRTH = 5;
    public static final int MIN_MARRIAGE_AGE = 14;
    public static final int FATHER_AGE_THRESHOLD = 80;
    public static final int MOTHER_AGE_THRESHOLD = 60;

    private Individual husband;
    private Individual wife;

    private Date marriage;
    private Date divorce;

    private Set<Individual> children;

    public Family(String ID) {
        super(ID);
        this.children = new HashSet<>();
    }

    public Individual getHusband() {
        return husband;
    }

    public Individual getWife() {
        return wife;
    }

    public Date getMarriage() {
        return marriage;
    }

    public Date getDivorce() {
        return divorce;
    }

    public void setHusband(Individual husband) {
        if (husband == null) {
            throw new IllegalArgumentException();
        }
        
        if (husband.getGender() == Gender.F) { // Allow NOT_SPECIFIED gender
            LOGGER.error(Error.INCORRECT_GENDER_ROLE, husband);
        }

        this.husband = husband;
    }

    public void setWife(Individual wife) {
        if (wife == null) {
            throw new IllegalArgumentException();
        }

        if (wife.getGender() == Gender.M) { // Allow NOT_SPECIFIED gender
            LOGGER.error(Error.INCORRECT_GENDER_ROLE, wife);
        }

        this.wife = wife;
    }

    public void setMarriage(Date marriage) {
        if (marriage == null) {
            throw new IllegalArgumentException();
        }

        if (marriage.after(new Date())) {
            LOGGER.error(Error.DATE_BEFORE_CURRENT_DATE, "marriage occurs before current time");
        }

        if (this.divorce != null) {
            if (this.divorce.equals(marriage) || this.divorce.after(marriage)) {
                this.marriage = marriage;
            } else {
                LOGGER.error(Error.DIVORCE_BEFORE_MARRIAGE, "marriage occurs after divorce.");
            }
        } else {
            this.marriage = marriage;
        }
    }

    public void setDivorce(Date divorce) {
        if (divorce == null) {
            throw new IllegalArgumentException();
        }

        if (divorce.after(new Date())) {
            LOGGER.error(Error.DATE_BEFORE_CURRENT_DATE, "divorce occurs before current time");
        }

        if (this.marriage != null) {
            if (this.marriage.equals(divorce) || this.marriage.before(divorce)) {
                this.divorce = divorce;
            } else {
                LOGGER.error(Error.DIVORCE_BEFORE_MARRIAGE, "divorce occurs before marriage");
            }
        } else {
            this.divorce = divorce;
        }
    }

    public List<Individual> getChildren() {
        List<Individual> children = new ArrayList<>(this.children);
        children.sort(Comparator.comparing(Individual::getBirthday, Comparator.nullsFirst(Comparator.naturalOrder())).reversed());
        return children;
    }

    public boolean addChild(Individual child) {
        if (child == null) {
            throw new IllegalArgumentException("Child cannot be null.");
        }

        if (children.size() >= MAX_CHILDREN) {
            LOGGER.anomaly(Error.MAX_FAMILY_CHILDREN_EXCEEDED);
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

}
