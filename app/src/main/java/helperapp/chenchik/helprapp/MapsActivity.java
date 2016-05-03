package helperapp.chenchik.helprapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import helperapp.chenchik.helprapp.library.UserFunctions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, MediaPlayer.OnPreparedListener {

    private helperapp.chenchik.helprapp.library.JSONParser jsonParser;

    private GoogleMap mMap;
    private GoogleApiClient c = null;
    Location loc;
    Marker marker;
    double oldWellLeftLong = -79.051586;
    double oldWellRightLong = -79.050876;
    double oldWellTopLat = 35.912162;
    double oldWellBottomLat = 35.911765;
    double polkPlaceLeftLong = -79.0509837;
    double polkPlaceRightLong = -79.049482;
    double polkPlaceTopLat = 35.91119;
    double polkPlaceBottomLat = 35.909556;
    double brooksLeftLong = -79.053613;
    double brooksRightLong = -79.052819;
    double brooksTopLat = 35.910347;
    double brooksBottomLat = 35.909469;
    LatLng brooks = new LatLng(35.909792, -79.053103);
    LatLng polkPlace = new LatLng(35.910776, -79.050558);
    LatLng oldWell = new LatLng(35.912382, -79.051207);
    MediaPlayer mp;
    int currentSound = 0;
    int myLength;
    boolean foundOnce = false;
    ArrayList<Listing> listings = new ArrayList<Listing>();
    ArrayList<PatListing> listingswithinrad = new ArrayList<>();
    Context _this;
    double rad;

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        _this = this;

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String c = extras.getString("category");
            String t = extras.getString("title");
            String n = extras.getString("name");
            String p = extras.getString("phoneNumber");
            String pr = extras.getString("price");
            Location l = extras.getParcelable("location");
            String bmp = extras.getString("photo");
            Listing listing = new Listing(c, t, n, p, pr, l, bmp);
            if (!listingExist(listing)) {
                listings.add(listing);
            }
            for (int i = 0; i < listings.size(); i++) {
                Log.v("listing", i + " is : " + listings.get(i).getName());
            }
        }
        Location tempLoc = new Location("dummyLocation");
        tempLoc.setLatitude(35.912462);
        tempLoc.setLongitude(-79.051786);
        listings.add(new Listing("Bike", "newnewtitle", "joe shmo", "5555555", "2", tempLoc, null));

        c = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng currentLatLng = new LatLng(35.909792, -79.053103);
        ;
        if (loc != null) {
            currentLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Log.v("CLASS GPS", loc.getLatitude() + ", " + loc.getLongitude());
        }
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("TAG", "We are connected to Google Services");
        try {
            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(1000);
            mLocationRequest.setFastestInterval(500);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(c, mLocationRequest, this);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        c.connect();
        if (mp != null) {

            mp.start();
            mp.seekTo(myLength);
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        c.disconnect();
        if (mp != null) {
            mp.pause();
            myLength = mp.getCurrentPosition();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        c.disconnect();
        if (mp != null) {
            mp.pause();
            myLength = mp.getCurrentPosition();
        }
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("CLASS GPS", location.getLatitude() + ", " + location.getLongitude());
        for (int i = 0; i < listings.size(); i++) {
            Log.v("listing", i + " is : " + listings.get(i).getName());
        }
        loc = location;
        //this if statement for not constantly animating camera to new position
        if (!foundOnce) {
            LatLng tmpLL = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tmpLL, 17.0f));
            foundOnce = true;
        }

        Geocoder g = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> la = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            //Log.v("Address", la.get(0).toString());
            for (int i = 0; i < listings.size(); i++) {
                //    Log.v("listing Title", i+" is : " + listings.get(i).getTitle());
                //    Log.v("listing Name", i+" is : " + listings.get(i).getName());
                //    Log.v("listing Phone", i+" is : " + listings.get(i).getPhone());
                //    Log.v("listing Price", i+" is : " + listings.get(i).getPrice());
                //    Log.v("listing filename", i+" is: "+ listings.get(i).getPicture());
            }

        } catch (Exception ex) {

        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
        }
    }

    public void makeNewListing(View v) {
        Intent x = new Intent(this, NewListingActivity.class);
        x.putExtra("currentLocation", loc);
        startActivity(x);
    }

    public boolean listingExist(Listing l) {
        for (int i = 0; i < listings.size(); i++) {
            if (listings.get(i).getTitle().equals(l.getName())) {
                return true;
            }
        }
        return false;

    }

    public void showPop(String s) {
        //context is a the window object of the application, we want to put something on top of the current window object
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        //toast is an alert/popup
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    public void showListings(View v) throws JSONException {
//        mMap.clear();
        //double rad = Integer.parseInt(((EditText) findViewById(R.id.listingRadius)).getText().toString());
        String radString = ((EditText) findViewById(R.id.listingRadius)).getText().toString();
        //Log.v("rad is ",rad+"");

        if (!radString.equals("")) {
            //String
            rad = Integer.parseInt(radString);

//            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
//
//            HashMap<String,String> listing = new HashMap<String, String>();
//            listing = db.getListingDetails();

            NetAsync(v);
        } else {
            showPop("please set a radius.");
        }
    }


    private class NetCheck extends AsyncTask<String, String, Boolean> {

        private ProgressDialog nDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(MapsActivity.this);
            nDialog.setMessage("Loading..");
            nDialog.setTitle("Checking Network");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args) {


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
        protected void onPostExecute(Boolean th) {

            if (th == true) {
                nDialog.dismiss();
                new ProcessRegister().execute();
            } else {
                nDialog.dismiss();
                Log.v("", "Error in Network Connection");
            }
        }
    }


    private class ProcessRegister extends AsyncTask<String, String, JSONObject> {

        /**
         * Defining Process dialog
         **/
        private ProgressDialog pDialog;

        String email, password, fname, lname, uname;

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
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.getListings();

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {

            //JSONArray c = getJsonArray(json);
            double currentLat = loc.getLatitude();
            double currentLong = loc.getLongitude();

            listingswithinrad.clear();

            try {
                JSONArray c = json.getJSONArray("listings");
                Log.v("c", "" + c);
                for (int i = 0; i < c.length(); i++) {
                    JSONObject obj = c.getJSONObject(i);
                    Log.v("obj", "" + obj);
                    String tempLat = obj.getString("latitude");
                    String tempLong = obj.getString("longitude");
                    Log.v("tempLat", "" + tempLat);
                    double diff = Math.sqrt(Math.pow(Math.abs(currentLat - Double.parseDouble(tempLat)), 2) +
                            Math.pow(Math.abs(currentLong - Double.parseDouble(tempLong)), 2));
                    Log.v("", "" + diff + ", rad: "+ rad);
                    Log.v("", "" + (diff <= rad));

                    if (diff <= rad) {
                        Log.v("within range", "woohoo");
                        listingswithinrad.add(
                                new PatListing(
                                        obj.getString("lid"),
                                        obj.getString("title"),
                                        obj.getString("price"),
                                        obj.getString("latitude"),
                                        obj.getString("longitude"),
                                        obj.getString("url"),
                                        obj.getString("category"),
                                        obj.getString("username"),
                                        obj.getString("phone"),
                                        obj.getString("email")));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("listingsinrad size", "" + listingswithinrad.size());
            for (int i = 0; i < listingswithinrad.size(); i++) {
                LatLng temp = new LatLng(Double.parseDouble(listingswithinrad.get(i).getLatitude()), Double.parseDouble(listingswithinrad.get(i).getLongitude()));
                Log.v("Latlng", "" + temp);
                Log.v("cat", "" + listingswithinrad.get(i).getCategory());
                switch (listingswithinrad.get(i).getCategory()) {
                    case "Bike":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bikesmall)));
                        break;
                    case "Skateboard":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.skateboardsmall)));
                        break;
                    case "Surfboard":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.wavessmall)));
                        break;
                    case "Snowboard":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.snowsmall)));
                        break;
                    case "Skis":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.skismall)));
                        break;
                    case "Rollerblade":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.rollerskatessmall)));
                        break;
                    case "Tent":
                        mMap.addMarker(new MarkerOptions()
                                .position(temp)
                                .title(i + "")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tentsmall)));
                        break;

                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker arg0) {
//                        Listing l = listings.get(Integer.parseInt(arg0.getTitle()));
                        PatListing l = listingswithinrad.get(Integer.parseInt(arg0.getTitle()));
                        Bitmap bmp = null;
                        String imgurl = l.getURL();
                        int loader = R.drawable.solid_gray;
                        //String fileName = "http://13lobsters.com/helpr/images/ygggg-1462214213203.jpg";
                        String fileName = "http://13lobsters.com/helpr/images/" + l.getURL() + ".jpg";
                        ImageLoader imgLoader;
                        imgLoader = new ImageLoader(getApplicationContext());
                        Log.v("URL", "" + imgurl);
                        if(fileName != null) {
                            try {
                                URL theimgurl = new URL(imgurl);
                                //try this url = "http://0.tqn.com/d/webclipart/1/0/5/l/4/floral-icon-5.jpg"
                                HttpGet httpRequest = null;

                                httpRequest = new HttpGet(theimgurl.toURI());

                                HttpClient httpclient = new DefaultHttpClient();
                                HttpResponse response = (HttpResponse) httpclient
                                        .execute(httpRequest);

                                HttpEntity entity = response.getEntity();
                                BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
                                InputStream input = b_entity.getContent();

                                Bitmap bitmap = BitmapFactory.decodeStream(input);

                                ImageView iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);
                                iv.setImageBitmap(bitmap);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ImageView iv = null;
                            iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);
                            imgLoader.DisplayImage(fileName, loader, iv);
                            //iv.setImageBitmap(bmp);

                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        else{
                            ImageView iv = null;
                            iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);
                            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.solid_gray);
                            iv.setImageBitmap(bm);
                            iv.setScaleType(ImageView.ScaleType.FIT_XY);
                        }

                        Log.v("Finished finding URL", "");
                        //update textViews
                        TextView titleText = (TextView) findViewById(R.id.titleText);
                        titleText.setText("Title: " + l.getTitle());
                        TextView nameText = (TextView) findViewById(R.id.nameText);
                        nameText.setText("Username: " + l.getUsername());
                        TextView emailText = (TextView) findViewById(R.id.emailText);
                        emailText.setText("Email: "+l.getEmail());
                        TextView phoneText = (TextView) findViewById(R.id.phoneText);
                        phoneText.setText("Phone: " + l.getPhone());
                        TextView priceText = (TextView) findViewById(R.id.priceText);
                        priceText.setText("Price: $" + l.getPrice());

                        return true;

                    }
                });
            }
            Log.v("PR onpost execute", "done");

            pDialog.dismiss();
        }
    }
    public void NetAsync(View view) {
        new NetCheck().execute();
    }

    public void updateText(TextView t, String text) {
        t.setText(text);
    }


    public void createRequest(View v) {
        Intent x = new Intent(this, NewRequestActivity.class);
        x.putExtra("currentLocation", loc);
        startActivity(x);
    }
}





