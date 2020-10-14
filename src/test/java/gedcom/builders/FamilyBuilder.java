package gedcom.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gedcom.models.Family;
import gedcom.models.Individual;

public class FamilyBuilder {

    private static int id = 0;

    private Individual husband;
    private Individual wife;

    private Date marriage;
    private Date divorce;

    private List<Individual> children;

    public FamilyBuilder() {
        this.children = new ArrayList<>();
    }

    public FamilyBuilder withHusband(Individual husband) {
        this.husband = husband;
        return this;
    }

    public FamilyBuilder withWife(Individual wife) {
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

    public FamilyBuilder withMarriage(Date marriage) {
        this.marriage = marriage;
        return this;
    }

    public FamilyBuilder withMarriage(int date, int month, int year) {
        this.marriage = DateBuilder.build(date, month, year);
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

    public Family build() {
        Family family = new Family(String.format("F_%d", id++));

        family.setHusband(husband);
        family.setWife(wife);

        for (Individual child : children) {
            family.addChild(child);
        }

        if (marriage != null) {
            family.setMarriage(marriage);
        }

        if (divorce != null) {
            family.setDivorce(divorce);
        }

        return family;
    }

}
