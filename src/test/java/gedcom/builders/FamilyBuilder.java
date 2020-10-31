package gedcom.builders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gedcom.models.Family;
import gedcom.models.Individual;

public class FamilyBuilder {

    private static int id = 1;

    private Individual husband;
    private Individual wife;

    private Date marriage;
    private Date divorce;

    private List<Individual> children;

    private List<Family> families;

    public FamilyBuilder() {
        this.children = new ArrayList<>();
        this.families = new ArrayList<>();
    }

    public FamilyBuilder withHusband(Individual husband) {
        this.husband = husband;
        return this;
    }

    public FamilyBuilder withWife(Individual wife) {
        this.wife = wife;
        return this;
    }

    public FamilyBuilder withSpouses(Individual husband, Individual wife) {
        this.husband = husband;
        this.wife = wife;
        return this;
    }

    public FamilyBuilder withChild(Individual child) {
        this.children.add(child);
        return this;
    }

    public FamilyBuilder withChildren(List<Individual> children) {
        this.children.addAll(children);
        return this;
    }

    public FamilyBuilder withChildren(Individual... children) {
        for (Individual child : children) {
            this.children.add(child);
        }
        return this;
    }

    public FamilyBuilder withChildren(int n) {
        for (int i = 0; i < n; i++) {
            this.children.add(new IndividualBuilder().build());
        }
        return this;
    }

    public FamilyBuilder withMarriage(Date marriage) {
        this.marriage = marriage;
        return this;
    }
    
    public FamilyBuilder withMarriage(int date, int month, int year) {
        this.marriage = DateBuilder.build(date, month, year);
        return this;
    }

    public FamilyBuilder withMarriage(int year) {
        this.marriage = DateBuilder.build(1, Calendar.JANUARY, year);
        return this;
    }

    public FamilyBuilder withMarriage(Date marriage, Date divorce) {
        this.marriage = marriage;
        this.divorce = divorce;
        return this;
    }

    public FamilyBuilder withDivorce(Date divorce) {
        this.divorce = divorce;
        return this;
    }

    public FamilyBuilder withDivorce(int date, int month, int year) {
        this.divorce = DateBuilder.build(date, month, year);
        return this;
    }

    public FamilyBuilder withDivorce(int year) {
        this.divorce = DateBuilder.build(1, Calendar.JANUARY, year);
        return this;
    }

    /**
     * Build a Family by editing or adding to an existing Family. This also adds the
     * Family as a spouse/child family to all individuals.
     */
    public Family buildFrom(Family family) {

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

        flush();
        return family;
    }

    public List<Family> getFamilies() {
        return this.families;
    }

    public void flush() {
        this.husband = null;
        this.wife = null;
        this.marriage = null;
        this.divorce = null;
        this.children = new ArrayList<>();
    }

    public Family build() {
        return buildFrom(new Family(String.format("@F%d@", id++)));
    }

}
