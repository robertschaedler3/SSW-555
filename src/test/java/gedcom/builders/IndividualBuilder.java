package gedcom.builders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.Individual;

public class IndividualBuilder {

    private static int id = 0;

    private Gender gender;

    private Date birth;
    private Date death;

    private List<Family> childrenFamilies;
    private List<Family> spouseFamilies;

    public IndividualBuilder() {
        this.childrenFamilies = new ArrayList<>();
        this.spouseFamilies = new ArrayList<>();
    }

    public IndividualBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public IndividualBuilder male() {
        this.gender = Gender.M;
        return this;
    }

    public IndividualBuilder female() {
        this.gender = Gender.F;
        return this;
    }

    public IndividualBuilder withBirth(Date birth) {
        this.birth = birth;
        return this;
    }

    public IndividualBuilder withBirth(int date, int month, int year) {
        this.birth = DateBuilder.build(date, month, year);
        return this;
    }

    public IndividualBuilder withDeath(Date death) {
        this.death = death;
        return this;
    }

    public IndividualBuilder withDeath(int date, int month, int year) {
        this.death = DateBuilder.build(date, month, year);
        return this;
    }

    /**
     * Build or update an Individual from an existing Individual.
     */
    public Individual buildFrom(Individual individual) {
        individual.setGender(gender);

        if (birth != null) {
            individual.setBirthday(birth);
        }

        if (death != null) {
            individual.setDeath(death);
        }

        for (Family family : childrenFamilies) {
            individual.addChildFamily(family);
        }

        for (Family family : spouseFamilies) {
            individual.addSpouseFamily(family);
        }
        return individual;
    }

    public Individual build() {
        return buildFrom(new Individual(String.format("I_%d", id++)));
    }

}
