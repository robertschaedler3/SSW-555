package gedcom.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
            LOGGER.error(Error.DATE_AFTER_CURRENT_DATE, this);
        }

        if (this.divorce != null) {
            if (this.divorce.equals(marriage) || this.divorce.after(marriage)) {
                this.marriage = marriage;
            } else {
                LOGGER.error(Error.DIVORCE_BEFORE_MARRIAGE, this);
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
            LOGGER.error(Error.DATE_AFTER_CURRENT_DATE, this);
        }

        if (this.marriage != null) {
            if (this.marriage.equals(divorce) || this.marriage.before(divorce)) {
                this.divorce = divorce;
            } else {
                LOGGER.error(Error.DIVORCE_BEFORE_MARRIAGE, this);
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
            LOGGER.anomaly(Error.MAX_FAMILY_CHILDREN_EXCEEDED, this);
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

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends GEDObjectBuilder {

        private static int id = 1;

        private String ID;

        private Individual husband;
        private Individual wife;

        private Date marriage;
        private Date divorce;

        private List<Individual> children;

        // A record of the families this builder has created
        private List<Family> families;

        private Builder() {
            this.children = new ArrayList<>();
            this.families = new ArrayList<>();
        }

        public Builder ID(String ID) {
            this.ID = ID;
            return this;
        }

        public Builder husband(Individual husband) {
            this.husband = husband;
            return this;
        }

        public Builder wife(Individual wife) {
            this.wife = wife;
            return this;
        }

        public Builder spouses(Individual husband, Individual wife) {
            this.husband = husband;
            this.wife = wife;
            return this;
        }

        public Builder child(Individual child) {
            this.children.add(child);
            return this;
        }

        public Builder children(List<Individual> children) {
            this.children.addAll(children);
            return this;
        }

        public Builder children(Individual... children) {
            this.children.addAll(Arrays.asList(children));
            return this;
        }

        public Builder children(int n) {
            for (int i = 0; i < n; i++) {
                this.children.add(Individual.builder().build());
            }
            return this;
        }

        public Builder marriage(Date marriage) {
            this.marriage = marriage;
            return this;
        }

        public Builder marriage(int date, int month, int year) {
            this.marriage = buildDate(date, month, year);
            return this;
        }

        public Builder marriage(int year) {
            this.marriage = buildDate(1, Calendar.JANUARY, year);
            return this;
        }

        public Builder divorce(Date divorce) {
            this.divorce = divorce;
            return this;
        }

        public Builder divorce(int date, int month, int year) {
            this.divorce = buildDate(date, month, year);
            return this;
        }

        public Builder divorce(int year) {
            this.divorce = buildDate(1, Calendar.JANUARY, year);
            return this;
        }

        public Family build() {

            Family family;

            if (ID != null) {
                family = new Family(ID);
            } else {
                family = new Family(String.format("@F%d@", id++));
            }

            if (husband != null) {
                family.setHusband(husband);
                husband.addSpouseFamily(family);
            }

            if (wife != null) {
                family.setWife(wife);
                wife.addSpouseFamily(family);
            }

            for (Individual child : children) {
                child.addChildFamily(family);
                family.addChild(child);
            }

            if (marriage != null) {
                family.setMarriage(marriage);
            }

            if (divorce != null) {
                family.setDivorce(divorce);
            }

            this.families.add(family);

            clear();

            return family;
        }

        private void clear() {
            this.ID = null;
            this.husband = null;
            this.wife = null;
            this.marriage = null;
            this.divorce = null;
            this.children = new ArrayList<>();
        }

        /**
         * Returns the list of all Families built by this builder.
         * 
         * @return
         */
        public List<Family> getFamilies() {
            return this.families;
        }

    }

}
