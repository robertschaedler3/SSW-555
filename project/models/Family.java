package project.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Family {

    private String ID;
    private String husband;
    private String wife;

    private Date marriage;
    private Date divorce;

    private List<String> children;

    public Family(String ID) {
        this.ID = ID;
        this.husband = "NA";
        this.wife = "NA";
        this.children = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getHusband() {
        return husband;
    }

    public void setHusband(String husband) {
        this.husband = husband;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
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

    public List<String> getChildren() {
        return children;
    }

    public boolean addChild(String ID) {
        return this.children.add(ID);
    }

}
