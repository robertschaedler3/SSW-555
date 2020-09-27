package project.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import project.interfaces.Gender;

public class Individual {

    private String ID;
    private String name;

    private Gender gender;

    private Date birthday;
    private Date death;

    private List<String> famc;
    private List<String> fams;

    public Individual(String ID) {
        this.ID = ID;
        this.name = "NA";
        this.gender = Gender.NA;
        this.famc = new ArrayList<>();
        this.fams = new ArrayList<>();
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getDeath() {
        return death;
    }

    public void setDeath(Date death) {
        this.death = death;
    }

    public List<String> getChildrenFamily() {
        return famc;
    }

    public boolean addChildFamily(String ID) {
        return this.famc.add(ID);
    }

    public List<String> getSpouseFamily() {
        return fams;
    }

    public boolean addSpouseFamily(String ID) {
        return this.fams.add(ID);
    }

    public long age() {
        if (birthday == null) {
            throw new IllegalStateException();
        }

        long diff;
        if (death != null) {
            diff = death.getTime() - birthday.getTime();
        } else {
            diff = (new Date()).getTime() - birthday.getTime();
        }
        return diff / (1000l * 60 * 60 * 24 * 365);
    }

    public boolean alive() {
        if (birthday == null) {
            throw new IllegalStateException();
        }
        return (death == null) ? true : false;
    }
    
    public Collection<Individual> getChildren(Collection<Individual> individuals) {
    	ArrayList<Individual> myChildren = new ArrayList<Individual>();
    	
    	for (Individual indi : individuals) {
    		// for each family where indi is a child, check if this is a spouse
    		for(String famcStr : indi.famc) {
    			if(this.fams.contains(famcStr)) myChildren.add(indi);
    		}
    	}
    	
    	return myChildren;
    }

}
