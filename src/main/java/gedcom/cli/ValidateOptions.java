package gedcom.cli;

import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Option;
import gedcom.validators.*;


public class ValidateOptions {

    @Option(names = { "-A", "--all" }, description = "Perform all validation checks.")
    protected boolean all;

    @ArgGroup(exclusive = false)
    private Validate validate = new Validate();

    private static class Validate {

        @Option(names = {"--corr-entries" }, description = "Checks that the information in the individual and family records is consistent")
        protected boolean correspondingEntries;
    
        @Option(names = {"--patriarchy" }, description = "Checks that all male members of a family have the same last name")
        protected boolean patriarchy;
    
        @Option(names = { "--mult-births" }, description = "Checks that no more then 5 siblings are born at a time")
        protected boolean multBirths;
    
        @Option(names = { "--bigamy" }, description = "Checks for overlapping marriages")
        protected boolean bigamy;
    
        @Option(names = { "--incest" }, description = "Checks for occurances of incest")
        protected boolean incest;
    
        @Option(names = { "--parent-age" }, description = "Checks that parents are not too old when a child is born")
        protected boolean parentAge;
    
        @Option(names = {"--unique-families" }, description = "Checks that no more than one family has the same spouse names/marriage date")
        protected boolean uniqueFamilies;
    
        @Option(names = {"--unique-individuals" }, description = "Checks that no more than one individual has the same name/birth date")
        protected boolean uniqueIndividuals;
    
        @Option(names = {"--unique-birth" }, description = "Checks that all children of a family with the same birth date have different names")
        protected boolean uniqueBirth;
    
        @Option(names = { "--valid-birth" }, description = "Checks that all births are valid")
        protected boolean validBirth;
    
        @Option(names = { "--valid-marriage" }, description = "Checks that all marriages are valid")
        protected boolean validMarriage;

    }

    private boolean addValidator(boolean option) {
        return all || option;
    }

    protected Validator buildValidator() {
        
        Validator validator = new DefaultValidator();

        if (addValidator(validate.correspondingEntries)) {
            validator = new CorrespondingEntries(validator);
        }

        if (addValidator(validate.patriarchy)) {
            validator = new MaleLastNames(validator);
        }

        if (addValidator(validate.multBirths)) {
            validator = new MultipleBirths(validator);
        }

        if (addValidator(validate.bigamy)) {
            validator = new NoBigamy(validator);
        }

        if (addValidator(validate.incest)) {
            validator = new NoIncest(validator);
        }

        if (addValidator(validate.parentAge)) {
            validator = new ParentsNotTooOld(validator);
        }

        if (addValidator(validate.uniqueFamilies)) {
            validator = new UniqueFamiliesBySpouses(validator);
        }

        if (addValidator(validate.uniqueIndividuals)) {
            validator = new UniqueIndividuals(validator);
        }

        if (addValidator(validate.uniqueBirth)) {
            validator = new UniqueNameBirthdays(validator);
        }

        if (addValidator(validate.validBirth)) {
            validator = new ValidBirth(validator);
        }

        if (addValidator(validate.validMarriage)) {
            validator = new ValidMarriage(validator);
        }

        return validator;
    }
    
}
