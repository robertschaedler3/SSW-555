package gedcom.builders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import gedcom.interfaces.Gender;
import gedcom.interfaces.Tag;
import gedcom.models.Family;
import gedcom.models.GEDFile;
import gedcom.models.Individual;

public class GEDCOMBuilder {

    private StringBuilder sb;

    public GEDCOMBuilder() {
        this.sb = new StringBuilder();
        sb.append(this.getHead());
    }

    private String getHead() {
        StringBuilder head = new StringBuilder();
        head.append(GEDLineBuilder.build(0, Tag.NOTE, "GEDCOM BUILDER"));
        head.append(GEDLineBuilder.build(0, Tag.HEAD));
        return head.toString();
    }

    private String getTail() {
        return GEDLineBuilder.build(0, Tag.TRLR);
    }

    private void addDateLine(Date date) {
        SimpleDateFormat dateFmt = new SimpleDateFormat(GEDFile.FULL_DATE_FORMAT);
        sb.append(GEDLineBuilder.build(2, Tag.DATE, dateFmt.format(date)));
    }

    public void addIndividual(Individual individual) {
        sb.append(GEDLineBuilder.build(0, individual.getID(), Tag.INDI));

        if (individual.getName() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.NAME, individual.getName()));
        }

        if (individual.getGender() != Gender.NOT_SPECIFIED) {
            sb.append(GEDLineBuilder.build(1, Tag.SEX, individual.getGender().toString()));
        }

        if (individual.getBirthday() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.BIRT));
            addDateLine(individual.getBirthday());
        }

        if (individual.getDeath() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.DEAT));
            addDateLine(individual.getDeath());
        }

        for (Family famc : individual.getChildFamilies()) {
            sb.append(GEDLineBuilder.build(1, Tag.FAMC, famc.getID()));
        }

        for (Family fams : individual.getSpouseFamilies()) {
            sb.append(GEDLineBuilder.build(1, Tag.FAMS, fams.getID()));
        }
    }

    public GEDCOMBuilder withIndividual(Individual individual) {
        addIndividual(individual);
        return this;
    }

    public GEDCOMBuilder withIndividuals(Individual... individuals) {
        for (Individual individual : individuals) {
            addIndividual(individual);
        }
        return this;
    }

    public GEDCOMBuilder withIndividuals(Iterable<Individual> individuals) {
        for (Individual individual : individuals) {
            addIndividual(individual);
        }
        return this;
    }

    public void addFamily(Family family) {
        sb.append(GEDLineBuilder.build(0, family.getID(), Tag.FAM));

        if (family.getHusband() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.HUSB, family.getHusband().getID()));
        }

        if (family.getWife() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.WIFE, family.getWife().getID()));
        }

        if (family.getMarriage() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.MARR));
            addDateLine(family.getMarriage());
        }

        if (family.getDivorce() != null) {
            sb.append(GEDLineBuilder.build(1, Tag.DIV));
            addDateLine(family.getDivorce());
        }

        for (Individual child : family.getChildren()) {
            sb.append(GEDLineBuilder.build(1, Tag.CHIL, child.getID()));
        }
    }

    public GEDCOMBuilder withFamily(Family family) {
        addFamily(family);
        return this;
    }

    public GEDCOMBuilder withFamilies(Family... families) {
        for (Family family : families) {
            addFamily(family);
        }
        return this;
    }

    public GEDCOMBuilder withFamilies(Iterable<Family> families) {
        for (Family family : families) {
            addFamily(family);
        }
        return this;
    }

    public String build() {
        sb.append(this.getTail());
        return sb.toString();
    }

    public static File getTempFile(String GEDCOM) {
        try {
            Path tempFile = Files.createTempFile(null, null);
            Files.write(tempFile, GEDCOM.getBytes(StandardCharsets.UTF_8));
            return tempFile.toFile();
        } catch (IOException e) {
            return null;
        }
    }

}
