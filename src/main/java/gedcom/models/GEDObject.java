package gedcom.models;

import gedcom.logging.Logger;

public class GEDObject {

    protected String ID;

    protected Logger LOGGER = Logger.getInstance();

    public GEDObject(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return this.ID;
    }

}
