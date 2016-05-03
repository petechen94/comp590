package helperapp.chenchik.helprapp.library;

/**
 * Author :Raj Amal
 * Email  :raj.amalw@example.android.loginapp.com
 * Website:www.example.android.loginapp.com
 **/

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserFunctions {

    private helperapp.chenchik.helprapp.library.JSONParser jsonParser;

    //URL of the PHP API
    // it was previously set to index
//    private static String apiURL = "http://cs.unc.edu/~pl/590proj/index.php";
//    private static String apiURL = "http://cs.unc.edu/~pl/590proj/index.php";
//    private static String apiURL = "http://cs.unc.edu/~pl/590proj/index.php";
//    private static String apiURL = "http://cs.unc.edu/~pl/590proj/index.php";

    private static String apiURL = "http://13lobsters.com/helpr/";

    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    private static String new_listing_tag = "newlisting";
    private static String get_listings_tag = "getlistings";

    // constructor
    public UserFunctions(){
        jsonParser = new helperapp.chenchik.helprapp.library.JSONParser();
    }

    /**
     * Function to Login
     **/

    public JSONObject loginUser(String email, String password){
        // Building Parameters
        Log.v("I'm here in", "loginUser");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));
        Log.v("userfunc/params", "" + params);
        JSONObject json = jsonParser.getJSONFromUrl(apiURL, params);
        Log.v("Login JSON obj", "" + json);
        return json;
    }

    /**
     * Function to change password
     **/

    public JSONObject chgPass(String newpas, String email){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", chgpass_tag));

        params.add(new BasicNameValuePair("newpas", newpas));
        params.add(new BasicNameValuePair("email", email));
        JSONObject json = jsonParser.getJSONFromUrl(apiURL, params);
        return json;
    }





    /**
     * Function to reset the password
     **/

    public JSONObject forPass(String forgotpassword){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", forpass_tag));
        params.add(new BasicNameValuePair("forgotpassword", forgotpassword));
        JSONObject json = jsonParser.getJSONFromUrl(apiURL, params);
        return json;
    }






     /**
      * Function to  Register
      **/
    public JSONObject registerUser(String fname, String lname, String email, String uname, String password){
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("fname", fname));
        params.add(new BasicNameValuePair("lname", lname));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("uname", uname));
        params.add(new BasicNameValuePair("password", password));
        JSONObject json = jsonParser.getJSONFromUrl(apiURL,params);
        return json;
    }

    public JSONObject newListing(String title, String price, String latitude, String longitude, String imgURL, String category){
        // Building Parameters
        Log.v("I'm here in", "newListing");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", new_listing_tag));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("price", price));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        params.add(new BasicNameValuePair("url", title));
        params.add(new BasicNameValuePair("category", category));
        Log.v("newListing params", "" + params);

        JSONObject json = jsonParser.getJSONFromUrl(apiURL,params);
        Log.v("newListing JSON obj", "" + json);

        return json;
    }

    public JSONObject getListings(){
        // Building Parameters
        Log.v("I'm here in", "getListings");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", get_listings_tag));
        JSONObject json = jsonParser.getJSONFromUrl(apiURL,params);
        Log.v("getListings JSON obj", "" + json);

        return json;
    }


    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     * */
    public boolean logoutUser(Context context){
        helperapp.chenchik.helprapp.library.DatabaseHandler db = new helperapp.chenchik.helprapp.library.DatabaseHandler(context);
        db.resetTables();
        return true;
    }

}

