package helperapp.chenchik.helprapp;

/**
 * Created by chenchik on 4/17/2016.
 */
public class PatListing {
    private String lid;
    private String title;
    private String price;
    private String latitude;
    private String longitude;
    private String url;
    private String category;

    public PatListing(String l, String t, String p, String la, String lo, String u, String c){
        lid = l;
        title = t;
        price = p;
        latitude = la;
        longitude = lo;
//        url = u;
        // replace with actual URL
        url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg";

        category = c;
    }
    public String getLid () {
        return lid;
    }
    public String getCategory(){
        return category;
    }
    public String getTitle(){
        return title;
    }
    public String getPrice(){
        return price;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getLongitude(){
        return longitude;
    }
    public String getURL(){
        return url;
    }
}
