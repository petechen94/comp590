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
    private String username;
    private String phone;
    private String email;

    public PatListing(String l, String t, String p, String la, String lo, String u, String c, String us, String ph, String e){
        lid = l;
        title = t;
        price = p;
        latitude = la;
        longitude = lo;
//        url = u;
        // replace with actual URL
        url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg";
        category = c;
        username = us;
        phone = ph;
        email = e;
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
    public String getUsername(){
        return username;
    }
    public String getPhone(){
        return phone;
    }
    public String getEmail(){
        return email;
    }

}
