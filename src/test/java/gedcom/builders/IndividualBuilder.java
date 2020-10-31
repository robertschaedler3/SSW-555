package gedcom.builders;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import gedcom.interfaces.Gender;
import gedcom.models.Individual;

public class IndividualBuilder {

    private static int id = 1;

    private String name;

    private Gender gender;

    private Date birth;
    private Date death;

    private List<Individual> individuals;

    public IndividualBuilder() {
        this.gender = Gender.NOT_SPECIFIED;
        this.individuals = new ArrayList<>();
    }

    public IndividualBuilder withName(String name) {
        this.name = name;
        return this;
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

    public IndividualBuilder withBirth(int year) {
        this.birth = DateBuilder.build(1, Calendar.JANUARY, year);
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

    public IndividualBuilder withDeath(int year) {
        this.death = DateBuilder.build(1, Calendar.JANUARY, year);
        return this;
    }

    public IndividualBuilder withLife(Date birth, Date death) {
        this.birth = birth;
        this.death = death;
        return this;
    }

    /**
     * Build or update an Individual from an existing Individual.
     */
    public Individual buildFrom(Individual individual) {

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

        flush();
        return individual;
    }

    public List<Individual> getIndividuals() {
        return this.individuals;
    }

    public void flush() {
        this.name = null;
        this.gender = Gender.NOT_SPECIFIED;
        this.birth = null;
        this.death = null;
    }

    public Individual build() {
        return buildFrom(new Individual(String.format("@I%d@", id++)));
    }

}
