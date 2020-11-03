package gedcom.cli;

import java.io.File;
import java.io.FileNotFoundException;

import gedcom.models.GEDFile;
import picocli.CommandLine.Parameters;

public class FileParameter {

    @Parameters(index = "0", description = "The GEDCOM input file.")
    private String filePath;

    protected GEDFile getFile() {
        try {
            File file = new File(filePath);
            return new GEDFile(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

}
