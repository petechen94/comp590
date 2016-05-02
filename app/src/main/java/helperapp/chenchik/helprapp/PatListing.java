package helperapp.chenchik.helprapp;

/**
 * Created by chenchik on 4/17/2016.
 */
public class PatListing {
    private int lid;
    private String title;
    private String price;
    private Double latitude;
    private Double longitude;
    private String url;
    private String category;

    public PatListing(int l, String t, String p, Double la, Double lo, String u, String c){
        lid = l;
        title = t;
        price = p;
        latitude = la;
        longitude = lo;
        url = u;
        category = c;
    }
    public int getLid () {
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
    public Double getLatitude(){
        return latitude;
    }
    public Double getLongitude(){
        return latitude;
    }
    public String getURL(){
        return url;
    }
}
