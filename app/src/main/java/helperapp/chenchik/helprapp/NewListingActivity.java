package helperapp.chenchik.helprapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.HashMap;

import helperapp.chenchik.helprapp.library.DatabaseHandler;


/**
 * Created by chenchik on 3/28/2016.
 */
public class NewListingActivity extends AppCompatActivity{

    Location globalLoc = null;
    double globalLat;
    double globalLong;
    static final CharSequence categories[] = new CharSequence[] {"Bike", "Skateboard", "Surfboard" , "Snowboard", "Skis", "Rollerblade", "Tent"};
    String chosenCategory = null;
    Bitmap globalPicture = null;
    HashMap<String,String> user = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_listing);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            globalLoc = extras.getParcelable("currentLocation");
            globalLat = globalLoc.getLatitude();
            globalLong = globalLoc.getLongitude();
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
        String titleText = editTitle.getText().toString();
        Log.v("text title is", ""+titleText);


//        EditText editName = (EditText) findViewById(R.id.editName);
        String nameText = user.get("email");

//        editName.getText().toString();

        EditText editPhoneNumber = (EditText) findViewById(R.id.editPhoneNumber);
        String phoneNumberText = editPhoneNumber.getText().toString();

        EditText editPrice = (EditText) findViewById(R.id.editPrice);
        String priceText = editPrice.getText().toString();
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
            try {
                //Write file
                String filename = nameText + "-" + titleText + ".png";
                FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                globalPicture = scaleDownBitmap(globalPicture, 3);
                globalPicture.compress(Bitmap.CompressFormat.PNG, 100, stream);

                //Cleanup
                stream.close();
                globalPicture.recycle();

                //Pop intent
                Intent x = new Intent(this, MapsActivity.class);


                //insert SQL code here

                x.putExtra("lat", globalLat);
                x.putExtra("long", globalLong);
                x.putExtra("location", globalLoc);
                x.putExtra("title", titleText);
                x.putExtra("name", nameText);
                x.putExtra("phoneNumber", phoneNumberText);
                x.putExtra("price", priceText);
                x.putExtra("photo", filename);
                x.putExtra("category", chosenCategory);
                x.putExtra("type", "Listing");

                //in1.putExtra("image", filename);
                startActivity(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
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


}
