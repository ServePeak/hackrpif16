package chupalika.pleasepickaplace;

import java.util.ArrayList;

/**
 * Created by aleung013 on 11/13/2016.
 */

public class Group {
    // Class that stores Group name, key, and current members
    private String name;
    private String key;
    private ArrayList<String> members; // TODO: change this to the thing that can update listeners when it updates

    public Group(String aname,String akey){
        this.name = aname;
        this.key = akey;
        //TODO: API call to get group members
    }


}
