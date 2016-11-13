package chupalika.pleasepickaplace;

/**
 * Created by ipb on 11/13/2016.
 */
public class Restaurant {
    private String name;
    private String rating;
    private String location;
    private String price;
    private String desc;

    public Restaurant(String n, String r, String l, String p, String d) {
        this.name = n;
        this.rating = r;
        this.location = l;
        this.price = p;
        this.desc = d;
    }

    public String getRating() {return rating;}
    public String getLocation() {return location;}
    public String getPrice() {return price;}
    public String getDesc() {return desc;}

    public String toString() {return name.toString();}
}