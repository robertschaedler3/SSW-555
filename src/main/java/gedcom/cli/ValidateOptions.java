package gedcom.cli;

import picocli.CommandLine.Option;
import gedcom.validators.*;


public class ValidateOptions {

    @Option(names = { "-A", "--all" }, description = "Perform all validation checks.")
    protected boolean all;

    @Option(names = { "-ce", "--corr-entries" }, description = "Checks that the information in the individual and family records is consistent")
    protected boolean correspondingEntries;

    @Option(names = { "-pt", "--patriarchy" }, description = "Checks that all male members of a family have the same last name")
    protected boolean patriarchy;

    @Option(names = { "-mb", "--mult-births" }, description = "Checks that no more then 5 siblings are born at a time")
    protected boolean multBirths;

    @Option(names = { "-bi", "--bigamy" }, description = "Checks for overlapping marriages")
    protected boolean bigamy;

    @Option(names = { "-in", "--incest" }, description = "Checks for occurances of incest")
    protected boolean incest;

    @Option(names = { "-pa", "--parent-age" }, description = "Checks that parents are not too old when a child is born")
    protected boolean parentAge;

    @Option(names = { "-uf", "--unique-families" }, description = "Checks that no more than one family has the same spouse names/marriage date")
    protected boolean uniqueFamilies;

    @Option(names = { "-ui", "--unique-individuals" }, description = "Checks that no more than one individual has the same name/birth date")
    protected boolean uniqueIndividuals;

    @Option(names = { "-ub", "--unique-birth" }, description = "Checks that all children of a family with the same birth date have different names")
    protected boolean uniqueBirth;

    @Option(names = { "-vb", "--valid-birth" }, description = "Checks that all births are valid")
    protected boolean validBirth;

    @Option(names = { "-vm", "--valid-marriage" }, description = "Checks that all marriages are valid")
    protected boolean validMarriage;

    private boolean addValidator(boolean option) {
        return all || option;
    }

    protected Validator buildValidator() {
        
        Validator validator = new DefaultValidator();

        if (addValidator(correspondingEntries)) {
            validator = new CorrespondingEntries(validator);
        }

        if (addValidator(patriarchy)) {
            validator = new MaleLastNames(validator);
        }

        if (addValidator(multBirths)) {
            validator = new MultipleBirths(validator);
        }

        if (addValidator(bigamy)) {
            validator = new NoBigamy(validator);
        }

        if (addValidator(incest)) {
            validator = new NoIncest(validator);
        }

        if (addValidator(parentAge)) {
            validator = new ParentsNotTooOld(validator);
        }

        if (addValidator(uniqueFamilies)) {
            validator = new UniqueFamiliesBySpouses(validator);
        }

        if (addValidator(uniqueIndividuals)) {
            validator = new UniqueIndividuals(validator);
        }

        if (addValidator(uniqueBirth)) {
            validator = new UniqueNameBirthdays(validator);
        }

        if (addValidator(validBirth)) {
            validator = new ValidBirth(validator);
        }

        if (addValidator(validMarriage)) {
            validator = new ValidMarriage(validator);
        }

        return validator;
    }
    
}
