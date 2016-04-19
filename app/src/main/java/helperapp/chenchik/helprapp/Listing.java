package helperapp.chenchik.helprapp;

import android.graphics.Bitmap;
import android.location.Location;

/**
 * Created by chenchik on 4/17/2016.
 */
public class Listing {

    private String category;
    private String title;
    private String name;
    private String phone;
    private String price;
    private Location loc;
    private String picture;
    public Listing(String cat, String t, String n, String p, String pr, Location l, String pic){
        category = cat;
        title = t;
        name = n;
        phone = p;
        price = pr;
        loc = l;
        picture = pic;
    }

    public String getCategory(){
        return category;
    }
    public String getTitle(){
        return title;
    }
    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public String getPrice(){
        return price;
    }
    public Location getLocation(){
        return loc;
    }
    public String getPicture(){
        return  picture;
    }
}
