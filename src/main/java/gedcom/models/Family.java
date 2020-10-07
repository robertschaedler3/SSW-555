package gedcom.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Family {

    private String ID;
    private Individual husband;
    private Individual wife;

    private Date marriage;
    private Date divorce;

    private List<Individual> children;

    public Family(String ID) {
        this.ID = ID;
        this.children = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public Individual getHusband() {
        return husband;
    }

    public void setHusband(Individual husband) {
        this.husband = husband;
    }

    public Individual getWife() {
        return wife;
    }

    public void setWife(Individual wife) {
        this.wife = wife;
    }

    public Date getMarriage() {
        return marriage;
    }

    public void setMarriage(Date marriage) {
        this.marriage = marriage;
    }

    public Date getDivorce() {
        return divorce;
    }

    public void setDivorce(Date divorce) {
        this.divorce = divorce;
    }

    public List<Individual> getChildren() {
        return children;
    }

    public boolean addChild(Individual child) {
        return this.children.add(child);
    }

}
