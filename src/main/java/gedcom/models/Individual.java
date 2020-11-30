package gedcom.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import gedcom.interfaces.Gender;
import gedcom.logging.Error;

public class Individual extends GEDObject {

    public static final int MAX_AGE = 150;

    private String name;

    private Gender gender;

    private Date birthday;
    private Date death;

    private Set<Family> childFamilies;  // Individual is a child in these families
    private Set<Family> spouseFamilies; // Individual is a spouse in these families

    public Individual(String ID) {
        super(ID);
        this.name = "";
        this.gender = Gender.NOT_SPECIFIED;
        this.childFamilies = new HashSet<>();
        this.spouseFamilies = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        if (this.name == null) {
            return null;
        }
        String[] parts = this.name.split("/");
        return (parts.length > 0) ? parts[0].trim() : "";
    }

    public String getLastName() {
        if (this.name == null) {
            return null;
        }
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

    public Date getDeath() {
        return death;
    }

    public int age() {
        return age(this.birthday, this.death);
    }

    private int age(Date birth, Date death) {
        if (birth == null) {
            throw new IllegalStateException("Cannot determine age without a birth date.");
        }

        long diff;
        if (death != null) {
            diff = death.getTime() - birth.getTime();
        } else {
            diff = (new Date()).getTime() - birth.getTime();
        }
        return (int) (diff / (1000l * 60 * 60 * 24 * 365));
    }

    public void setBirthday(Date birth) {
        if (birth == null) {
            throw new IllegalArgumentException();
        }

        if (birth.after(new Date())) {
            LOGGER.error(Error.DATE_AFTER_CURRENT_DATE, this);
        }

        if (this.death != null) {
            if (this.death.equals(birth) || this.death.after(birth)) {
                if (this.age(birth, this.death) > MAX_AGE) {
                    LOGGER.anomaly(Error.MAX_AGE_EXCEEDED, this);
                }
                this.birthday = birth;
            } else {
                LOGGER.error(Error.DEATH_BEFORE_BIRTH, this);
            }
        } else {
            this.birthday = birth;
        }
    }

    public void setDeath(Date death) {
        if (death == null) {
            throw new IllegalArgumentException();
        }

        if (death.after(new Date())) {
            LOGGER.error(Error.DATE_AFTER_CURRENT_DATE, this);
        }

        if (this.birthday != null) {
            if (this.birthday.equals(death) || this.birthday.before(death)) {
                if (this.age(this.birthday, death) > MAX_AGE) {
                    LOGGER.anomaly(Error.MAX_AGE_EXCEEDED, this);
                }
                this.death = death;
            } else {
                LOGGER.error(Error.DEATH_BEFORE_BIRTH, this);
            }
        } else {
            this.death = death;
        }
    }

    public List<Family> getChildFamilies() {
        return new ArrayList<>(childFamilies);
    }

    public boolean addChildFamily(Family family) {
        if (family == null) {
            throw new IllegalArgumentException("Family cannot be null.");
        }
        return this.childFamilies.add(family);
    }

    public List<Family> getSpouseFamilies() {
        return new ArrayList<>(spouseFamilies);
    }

    public boolean addSpouseFamily(Family family) {
        if (family == null) {
            throw new IllegalArgumentException("Family cannot be null.");
        }
        return this.spouseFamilies.add(family);
    }

    public boolean alive() {
        if (birthday == null) {
            throw new IllegalStateException("Cannot determine living status without a birth date.");
        }
        return (death == null) ? true : false;
    }

    public boolean isSibling(Individual individual) {
        if (individual == null) {
            throw new IllegalArgumentException();
        }
        return this.childFamilies.stream().filter(individual.getChildFamilies()::contains).collect(Collectors.toList()).size() != 0;
    }

    public List<Individual> getSiblings() {
        List<Individual> siblings = new ArrayList<>();
        for (Family family : this.childFamilies) {
            siblings.addAll(family.getChildren());
            siblings.remove(this);
        }
        return siblings;
    }

    public List<Individual> getChildren() {
        List<Individual> children = new ArrayList<Individual>();
        for (Family family : this.spouseFamilies) {
            children.addAll(family.getChildren());
        }
        children.sort(Comparator.comparing(Individual::getBirthday, Comparator.nullsFirst(Comparator.naturalOrder())).reversed());
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
        List<Individual> children = this.getChildren();
        Set<Individual> descendants = new HashSet<>(children);
        Queue<Individual> q = new LinkedList<>(children);

        while (!q.isEmpty()) {
            for (Individual child : q.poll().getChildren()) {
                if (descendants.add(child)) {
                    q.add(child);
                }
            }
        }

        return new ArrayList<>(descendants);
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

        List<Individual> children = this.getChildren();
        Set<Individual> descendants = new HashSet<>(children);
        Queue<Individual> q = new LinkedList<>(children);
        Individual current;

        q.add(null);
        i--;

        while (!q.isEmpty() && i > 0) {
            current = q.poll();
            if (current == null) {
                q.add(null);
                i--;
                continue;
            }
            for (Individual child : current.getChildren()) {
                if (descendants.add(child)) {
                    q.add(child);
                }
            }
        }

        q.remove(null);
        return (i == 0) ? new ArrayList<>(q) : new ArrayList<>();
    }

    /**
     * Checks is the individual is a descendant of the given individual.
     * 
     * @param individual
     * @return true if the individual is a descendant, false otherwise
     */
    public boolean isDescendant(Individual individual) {
        return individual.getDescendants().contains(this);
    }

    /**
     * Checks is the given individual is a descendant of the current individual.
     * 
     * @param individual
     * @return true if the individual is a descendant, false otherwise
     */
    public boolean hasDescendant(Individual individual) {
        return this.getDescendants().contains(individual);
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
        List<Individual> parents = this.getParents();
        Set<Individual> ancestors = new HashSet<>(parents);
        Queue<Individual> q = new LinkedList<>(parents);

        while (!q.isEmpty()) {
            for (Individual parent : q.poll().getParents()) {
                if (ancestors.add(parent)) {
                    q.add(parent);
                }
            }
        }

        return new ArrayList<>(ancestors);
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
        
        List<Individual> parents = this.getParents();
        Set<Individual> ancestors = new HashSet<>(parents);
        Queue<Individual> q = new LinkedList<>(parents);
        Individual current;

        q.add(null);
        i--;
        
        while (!q.isEmpty() && i > 0) {
            current = q.poll();
            if (current == null) {
                q.add(null);
                i--;
                continue;
            }
            for (Individual parent : current.getParents()) {
                if (ancestors.add(parent)) {
                    q.add(parent);
                }
            }
        }

        q.remove(null);
        return (i == 0) ? new ArrayList<>(q) : new ArrayList<>();
    }

    /**
     * Checks is the current individual is an ancestor of the given individual.
     * 
     * @param individual
     * @return true if the given individual is an ancestor, false otherwise
     */
    public boolean isAncestor(Individual individual) {
        return individual.getAncestors().contains(this);
    }

    /**
     * Checks is the given individual is an ancestor of the current individual.
     * 
     * @param individual
     * @return true if the individual is an ancestor, false otherwise
     */
    public boolean hasAncestor(Individual individual) {
        return this.getAncestors().contains(individual);
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
        List<Individual> linkingReliatives = new ArrayList<>();

        for (Individual ancestor : this.getAncestorsAt(n)) {
            linkingReliatives.addAll(ancestor.getDescendantsAt(n));
        }

        for (Individual ancestor : this.getAncestorsAt(n + 1)) {
            cousins.addAll(ancestor.getDescendantsAt(n + 1));
        }

        cousins.removeAll(linkingReliatives);
        return cousins;
    }
    
    public int getGeneration() {
    	int generation = 0;
    	
    	if(this.getParents().size() > 0) {
    		for(Individual indi : this.getParents()) {
    			generation = Math.max(generation, indi.getGeneration() + 1);
    		}
    	}
    	else {
    		return 1;
    	}
    	
    	return generation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder extends GEDObjectBuilder {

        // A count to keep track of auto generated IDs
        private static int id = 1;

        private String ID;

        private String name;

        private Gender gender;

        private Date birth;
        private Date death;

        // A list of all the Individuals created by this builder
        private List<Individual> individuals;

        private Builder() {
            this.gender = Gender.NOT_SPECIFIED;
            this.individuals = new ArrayList<>();
        }

        public Builder ID(String ID) {
            this.ID = ID;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder male() {
            return gender(Gender.M);
        }

        public Builder female() {
            return gender(Gender.F);
        }

        public Builder birth(Date birth) {
            this.birth = birth;
            return this;
        }

        public Builder birth(int date, int month, int year) {
            this.birth = buildDate(date, month, year);
            return this;
        }

        public Builder birth(int year) {
            this.birth = buildDate(1, Calendar.JANUARY, year);
            return this;
        }

        public Builder death(Date death) {
            this.death = death;
            return this;
        }

        public Builder death(int date, int month, int year) {
            this.death = buildDate(date, month, year);
            return this;
        }

        public Builder death(int year) {
            this.death = buildDate(1, Calendar.JANUARY, year);
            return this;
        }

        public Builder life(Date birth, Date death) {
            this.birth = birth;
            this.death = death;
            return this;
        }

        private void clear() {
            this.ID = null;
            this.name = null;
            this.gender = Gender.NOT_SPECIFIED;
            this.birth = null;
            this.death = null;
        }

        public Individual build() {

            Individual individual;

            if (ID != null) {
                individual = new Individual(ID);
            } else {
                individual = new Individual(String.format("@F%d@", id++));
            }

            individual.setGender(gender);

            if (name != null) {
                individual.setName(name);
            }

            if (birth != null) {
                individual.setBirthday(birth);
            }

            if (death != null) {
                individual.setDeath(death);
            }

            this.individuals.add(individual);

            clear();

            return individual;
        }

        /**
         * Returns a list of all the Individuals built by this builder.
         * 
         * @return
         */
        public List<Individual> getIndividuals() {
            return this.individuals;
        }

    }

}
