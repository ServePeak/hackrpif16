package chupalika.pleasepickaplace;

/**
 * Created by ipb on 11/13/2016.
 */
public class Restaurant {
    private String id;
    private String name;
    private String rating;
    private String location;
    private String price;
    private String desc;
    private String dist;

    public Restaurant(String i,String n, String r, String l, String p, String d, String dist) {
        this.id = i;
        this.name = n;
        this.rating = r;
        this.location = l;
        this.price = p;
        this.desc = d;
        this.dist = dist;
    }
    public String getId() {return id;}
    public String getRating() {return rating;}
    public String getLocation() {return location;}
    public String getPrice() {return price;}
    public String getDesc() {return desc;}
    public String getDist() {return dist;}

    public String toString() {return name.toString();}
}