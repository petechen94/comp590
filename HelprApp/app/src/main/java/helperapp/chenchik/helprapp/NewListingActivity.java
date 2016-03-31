package helperapp.chenchik.helprapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;



/**
 * Created by chenchik on 3/28/2016.
 */
public class NewListingActivity extends AppCompatActivity{

    Location globalLoc = null;
    double globalLat;
    double globalLong;

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
        Log.v("dqnila","second func called");
        ImageView iv = null;
        Bitmap bm = (Bitmap) data.getExtras().get("data");
        iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);
        iv.setBackgroundResource(0);
        iv.setImageBitmap(bm);
    }


}
