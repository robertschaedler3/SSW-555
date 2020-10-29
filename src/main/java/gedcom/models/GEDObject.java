package gedcom.models;

public class GEDObject {

    protected String ID;

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
