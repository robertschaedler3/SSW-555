package gedcom.builders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gedcom.interfaces.Gender;
import gedcom.models.Family;
import gedcom.models.Individual;

public class IndividualBuilder {

    private final String DATE_FORMAT = "dd MMM yyyy";

    private static int id = 0;

    private Gender gender;

    private Date birth;
    private Date death;

    private List<Family> childrenFamilies;
    private List<Family> spouseFamilies;

    private SimpleDateFormat dateFmt;

    public IndividualBuilder() {
        this.childrenFamilies = new ArrayList<>();
        this.spouseFamilies = new ArrayList<>();
        this.dateFmt = new SimpleDateFormat(DATE_FORMAT);
    }

    public IndividualBuilder withGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public IndividualBuilder withBirth(Date birth) {
        this.birth = birth;
        return this;
    }

    public IndividualBuilder withBirth(String birth) throws ParseException {
        this.birth = this.dateFmt.parse(birth);
        return this;
    }

    public IndividualBuilder withDeath(Date death) {
        this.death = death;
        return this;
    }

    public IndividualBuilder withDeath(String death) throws ParseException {
        this.death = this.dateFmt.parse(death);
        return this;
    }

    public Individual build() {
        Individual individual = new Individual(String.format("I_%d", id++));

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

}
