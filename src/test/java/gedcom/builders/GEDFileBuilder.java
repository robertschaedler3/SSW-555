package gedcom.builders;

import java.util.ArrayList;
import java.util.List;

import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class GEDFileBuilder {

    private List<Individual> individuals;
    private List<Family> families;

    public GEDFileBuilder() {
        this.individuals = new ArrayList<>();
        this.families = new ArrayList<>();
    }

    public GEDFileBuilder withIndividual(Individual individual) {
        this.individuals.add(individual);
        return this;
    }

    public GEDFileBuilder withIndividuals(List<Individual> individuals) {
        this.individuals.addAll(individuals);
        return this;
    }

    public GEDFileBuilder withIndividuals(Individual... individuals) {
        for (Individual individual : individuals) {
            this.individuals.add(individual);
        }
        return this;
    }

    public GEDFileBuilder withFamily(Family family) {
        this.families.add(family);
        return this;
    }

    public GEDFileBuilder withFamilies(List<Family> families) {
        this.families.addAll(families);
        return this;
    }

    public GEDFileBuilder withFamilies(Family... families) {
        for (Family family : families) {
            this.families.add(family);
        }
        return this;
    }

    public GEDFile build() {
        return new GEDFile(individuals, families);
    }

}
