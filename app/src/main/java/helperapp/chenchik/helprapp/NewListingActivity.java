package helperapp.chenchik.helprapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import helperapp.chenchik.helprapp.library.DatabaseHandler;
import helperapp.chenchik.helprapp.library.UserFunctions;


/**
 * Created by chenchik on 3/28/2016.
 */
public class NewListingActivity extends AppCompatActivity{

    Location globalLoc = null;
    double globalLat;
    double globalLong;
    static final CharSequence categories[] = new CharSequence[] {"Bike", "Skateboard", "Surfboard" , "Snowboard", "Skis", "Rollerblade", "Tent"};
    String chosenCategory = null;
    String imgURL = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg";
    Bitmap globalPicture = null;
    HashMap<String,String> user = new HashMap<String, String>();

    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_TITLE = "title";
    private static String KEY_PRICE = "price";

    TextView registerErrorMsg;
    String priceText, titleText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        //sets error msg loc
        registerErrorMsg = (TextView) findViewById(R.id.errormsg);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            globalLoc = extras.getParcelable("currentLocation");

            // need to round or could have a buffer overflow as double is converted to String for sending via JSON
            globalLat = Math.round(globalLoc.getLatitude() * 100000000000.0)/100000000000.0;
            globalLong = Math.round(globalLoc.getLongitude()* 100000000000.0)/100000000000.0;
        }

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        /**
         * Hashmap to load data from the Sqlite database
         **/
//        HashMap<String,String> user = new HashMap<String, String>();
        user = db.getUserDetails();

        Log.v("loc is right now", " "+ globalLoc);
        Log.v("latitude is:", ""+globalLat);
        Log.v("longitude is:", ""+globalLong);
    }
    public void runCamera(View v){
        Log.v(" camera button clicked", "");
        Intent x = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(x, 1);
    }
    @Override
    protected void onActivityResult(int rc, int resc, Intent data){
        //**rc** is request code
        //request code helps with identifying which intent was passed in originally
        //result code **resc** is the result code, it can only be 1 of 2 options...
        //RESULT_OK if the result was okay
        //RESULT_CANCELLED if the result was canlled on the other users side or if something went wrong.
        //**data** carries the result data, like the data from the camera
        Log.v("dqnila", "second func called");
        ImageView iv = null;
        iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);

        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.postRotate(90);


        Bitmap scaledBitmap = Bitmap.createScaledBitmap((Bitmap)data.getExtras().get("data"), iv.getWidth(), iv.getHeight(), true);
        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        iv.setImageBitmap(rotatedBitmap);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        //rotatedBitmap = rotatedBitmap.compress(Bitm)
        globalPicture = rotatedBitmap;



    }

    public void selectCategory(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose a category for your item");
        builder.setItems(categories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                Log.v("color chosen is", "" + categories[which]);
                chosenCategory = categories[which].toString();
                Log.v("the chosen category is", chosenCategory);
            }
        });
        builder.show();
    }
    public void cancelListing(View v){
        globalLoc = null;
        globalLat = 0.00;
        globalLong = 0.00;
        chosenCategory = null;
        Intent x = new Intent(this, MapsActivity.class);
        startActivity(x);
    }
    public void submitListing(View v){
        EditText editTitle = (EditText) findViewById(R.id.editTitle);
        titleText = editTitle.getText().toString();
        Log.v("text title is", ""+titleText);


//        EditText editName = (EditText) findViewById(R.id.editName);
        String nameText = user.get("email");

//        editName.getText().toString();

        EditText editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        String phoneNumberText = editPhoneNumber.getText().toString();

        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        priceText = editPrice.getText().toString();
        int priceNum = 0;
        if(priceText.equals("")){
            priceNum = -1; //dummy value
        }
        else{
            priceNum = Integer.parseInt(priceText);
            Log.v("price is", ""+priceNum);
        }




        if(chosenCategory == null){
            showPop("Please choose a category.");
        }
        else if(titleText.equals("")){
            showPop("Please create a title");
        }
//        else if(nameText.equals("")){
//            showPop("Please enter your name");
//        }
        else if(phoneNumberText.equals("")){
            showPop("please enter your phone number");
        }
        //else if(priceNum == -1){
        else if(priceText.equals("")){
            showPop("please enter a price per hour");
        }
        else if(globalPicture == null){
            showPop("please snap a photo");
        }
        else{
            NetAsync(v);

//            try {
//
//                //Write file
//                String filename = nameText + "-" + titleText + ".png";
//                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
//                globalPicture = scaleDownBitmap(globalPicture, 3);
//                globalPicture.compress(Bitmap.CompressFormat.PNG, 100, stream);
//
//                //Cleanup
//                stream.close();
//                globalPicture.recycle();
//
//                //Pop intent
//                Intent x = new Intent(this, MapsActivity.class);
//
//
//                //insert SQL code here
//
//                x.putExtra("lat", globalLat);
//                x.putExtra("long", globalLong);
//                x.putExtra("location", globalLoc);
//                x.putExtra("title", titleText);
//                x.putExtra("name", nameText);
//                x.putExtra("phoneNumber", phoneNumberText);
//                x.putExtra("price", priceText);
//                x.putExtra("photo", filename);
//                x.putExtra("category", chosenCategory);
//                x.putExtra("type", "Listing");
//
//                //in1.putExtra("image", filename);
//                startActivity(x);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }
    public void showPop(String s){
        //context is a the window object of the application, we want to put something on top of the current window object
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        //toast is an alert/popup
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    /*public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }*/

    public Bitmap scaleDownBitmap(Bitmap bm, int howMuch){
        int newWidth = bm.getWidth()/howMuch;
        int newHeight = bm.getHeight()/howMuch;

        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }

    private class NetCheck extends AsyncTask<String,String,Boolean>
    {
        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog = new ProgressDialog(NewListingActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){


/**
 * Gets current device state and checks for working internet connection by trying Google.
 **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;

        }
        @Override
        protected void onPostExecute(Boolean th){

            if(th == true){
                nDialog.dismiss();
                new ProcessRegister().execute();
            }
            else{
                nDialog.dismiss();
                registerErrorMsg.setText("Error in Network Connection");
            }
        }
    }





    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String email,password,fname,lname,uname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            inputUsername = (EditText) findViewById(R.id.uname);
//            inputPassword = (EditText) findViewById(R.id.pword);
//            fname = inputFirstName.getText().toString();
//            lname = inputLastName.getText().toString();
//            email = inputEmail.getText().toString();
//            uname= inputUsername.getText().toString();
//            password = inputPassword.getText().toString();
            pDialog = new ProgressDialog(NewListingActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            Log.v("titleText: ", titleText + ", priceText: " + priceText);
            Log.v("", "Lat: " + String.valueOf(globalLat) + ", Long: " + String.valueOf(globalLong) + "Imgurl: " + imgURL + ", Cat: " + chosenCategory);

            JSONObject json = userFunction.newListing(titleText, priceText, String.valueOf(globalLat), String.valueOf(globalLong), imgURL, chosenCategory);

            return json;


        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
             * Checks for success message.
             **/
            Log.v("beforePostExecute", "" + json);
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    Log.v("tryPostExecute", "" + json);
                    registerErrorMsg.setText("");
                    String res = json.getString(KEY_SUCCESS);
                    String red = json.getString(KEY_ERROR);

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");

                        registerErrorMsg.setText("Successfully Registered");


                         DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                         Log.v("after success", "" + json);
                         JSONObject json_listing = json.getJSONObject("listing");

                         /**
                         * Removes all the previous data in the SQlite database
                         **/

//                        UserFunctions logout = new UserFunctions();
//                        logout.logoutUser(getApplicationContext());
//                        db.addListing(json_listing.getString(KEY_TITLE), json_listing.getString(KEY_PRICE));
                        /**
                         * Stores registered data in SQlite Database
                         * Launch Registered screen
                         **/

                        Intent mapsactivity = new Intent(getApplicationContext(), helperapp.chenchik.helprapp.MapsActivity.class);

                        /**
                         * Close all views before launching Registered screen
                         **/
                        mapsactivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(mapsactivity);


                        finish();
                    }

                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                        registerErrorMsg.setText("User already exists");
                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                        registerErrorMsg.setText("Invalid Email id");
                    }

                }


                else{
                    pDialog.dismiss();

                    registerErrorMsg.setText("Error occured in registration");
                }

            } catch (JSONException e) {
                e.printStackTrace();


            }
        }}
    public void NetAsync(View view){
        new NetCheck().execute();
    }
}
