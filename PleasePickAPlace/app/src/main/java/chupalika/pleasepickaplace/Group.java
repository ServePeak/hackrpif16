package chupalika.pleasepickaplace;

import java.util.ArrayList;

/**
 * Created by aleung013 on 11/13/2016.
 */

public class Group {
    // Class that stores Group name, key, and current members
    private String name;
    private String key;
    private ArrayList<String> members;

    public Group(String aname,String akey){
        this.name = aname;
        this.key = akey;

        //TODO: API call to get members in this group
    }
    public String getKey() {return key;}

    public int numMembers(){
        return members.size();
    }

    public ArrayList<String> getMembers(){
        return members;
    }

    public void addMember(String user){
        members.add(user);
    }

    public String toString(){
        return name.toString();
    }

}
