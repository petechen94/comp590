package helperapp.chenchik.helprapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



/**
 * Created by chenchik on 3/28/2016.
 */
public class NewListingActivity extends AppCompatActivity{

    Location globalLoc = null;
    double globalLat;
    double globalLong;
    static final CharSequence categories[] = new CharSequence[] {"Bikes", "Longboards", "Speakers", "Furniture", "Other"};
    String chosenCategory = null;

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

        Log.v("loc is right now", " "+ globalLoc);
        Log.v("latitude is:", ""+globalLat);
        Log.v("longitude is:", ""+globalLong);
        //Spinner dropdown = (Spinner)findViewById(R.id.categories);
        //String[] items = new String[]{"category","Bike", "Longboard", "Roller Blades", "Other"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //dropdown.setAdapter(adapter);

        //CharSequence colors[] = new CharSequence[] {"red", "green", "blue", "black"};






        //x = 0;
        //y = 1000;



    }
    public void runCamera(View v){
        Log.v(" camera button clicked", "");
        Intent x = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(x, 1);
        //((AnimationDrawable)img.getBackground)
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

        //ImageView iv = null;
        //Bitmap bm = (Bitmap) data.getExtras().get("data");
                //iv.setBackgroundResource(0);
        //rotatedBitmap.setHeight(220);

        iv.setImageBitmap(rotatedBitmap);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);



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
        //Intent x = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.unc.edu") );

        //x.putExtra("currentLocation", loc);
        startActivity(x);


    }
    public void submitListing(View v){
        if(chosenCategory != null){

        }
        else{
            showPop("Please chose a category.");
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


}
