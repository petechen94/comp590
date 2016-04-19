package helperapp.chenchik.helprapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, MediaPlayer.OnPreparedListener {

    private GoogleMap mMap;
    private GoogleApiClient c = null;
    Location loc;
    Marker marker;
    double oldWellLeftLong=-79.051586;
    double oldWellRightLong=-79.050876;
    double oldWellTopLat=35.912162;
    double oldWellBottomLat=35.911765;
    double polkPlaceLeftLong=-79.0509837;
    double polkPlaceRightLong=-79.049482;
    double polkPlaceTopLat=35.91119;
    double polkPlaceBottomLat=35.909556;
    double brooksLeftLong=-79.053613;
    double brooksRightLong=-79.052819;
    double brooksTopLat=35.910347;
    double brooksBottomLat=35.909469;
    LatLng brooks = new LatLng(35.909792, -79.053103);
    LatLng polkPlace = new LatLng(35.910776, -79.050558);
    LatLng oldWell = new LatLng(35.912382, -79.051207);
    MediaPlayer mp;
    int currentSound = 0;
    int myLength;
    boolean foundOnce = false;
    ArrayList<Listing> listings = new ArrayList<Listing>();
    Context _this;

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

        if(extras != null){
            String c = extras.getString("category");
            String t = extras.getString("title");
            String n = extras.getString("name");
            String p = extras.getString("phoneNumber");
            String pr = extras.getString("price");
            Location l = extras.getParcelable("location");
            String bmp = extras.getString("photo");
            Listing listing = new Listing(c, t, n, p, pr, l,bmp);
            if(!listingExist(listing)){
                listings.add(listing);
            }
            for(int i = 0; i < listings.size(); i++){
                Log.v("listing", i+" is : " + listings.get(i).getName());
            }
        }
        Location tempLoc = new Location("dummyLocation");
        tempLoc.setLatitude(35.912462);
        tempLoc.setLongitude(-79.051786);
        listings.add(new Listing("Bikes", "newnewtitle", "joe shmo", "5555555", "2", tempLoc, null));

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
        LatLng currentLatLng = new LatLng(35.909792, -79.053103);;
        if(loc != null) {
            currentLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            Log.v("CLASS GPS", loc.getLatitude() + ", " + loc.getLongitude());
        }
        try {
            mMap.setMyLocationEnabled(true);
        }catch(SecurityException ex){
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
        }
        catch (SecurityException ex) {
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
        if(mp != null) {

            mp.start();
            mp.seekTo(myLength);
        }
        super.onStart();
    }
    @Override
    protected void onStop() {
        c.disconnect();
        if(mp != null) {
            mp.pause();
            myLength = mp.getCurrentPosition();
        }
        super.onStop();
    }

    @Override
    protected void onPause() {
        c.disconnect();
        if(mp != null) {
            mp.pause();
            myLength = mp.getCurrentPosition();
        }
        super.onPause();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.v("CLASS GPS", location.getLatitude() + ", " + location.getLongitude());
        for(int i = 0; i < listings.size(); i++){
            Log.v("listing", i+" is : " + listings.get(i).getName());
        }
        loc = location;
        //this if statement for not constantly animating camera to new position
        if(!foundOnce){
            LatLng tmpLL = new LatLng(loc.getLatitude(), loc.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tmpLL, 17.0f));
            foundOnce = true;
        }

        Geocoder g = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> la = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            //Log.v("Address", la.get(0).toString());
            for(int i = 0; i < listings.size(); i++){
            //    Log.v("listing Title", i+" is : " + listings.get(i).getTitle());
            //    Log.v("listing Name", i+" is : " + listings.get(i).getName());
            //    Log.v("listing Phone", i+" is : " + listings.get(i).getPhone());
            //    Log.v("listing Price", i+" is : " + listings.get(i).getPrice());
            //    Log.v("listing filename", i+" is: "+ listings.get(i).getPicture());
            }

        }catch (Exception ex) {

        }

    }

    @Override
    public void onPrepared(MediaPlayer mp){
        if(mp != null) {
            mp.start();
        }
    }

    public void makeNewListing(View v){
        Intent x = new Intent(this, NewListingActivity.class);
        x.putExtra("currentLocation", loc);
        startActivity(x);
    }

    public boolean listingExist(Listing l){
        for(int i = 0; i < listings.size(); i++) {
            if(listings.get(i).getTitle().equals(l.getName())){
                return true;
            }
        }
        return false;

    }

    public void showPop(String s){
        //context is a the window object of the application, we want to put something on top of the current window object
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        //toast is an alert/popup
        Toast toast = Toast.makeText(context, s, duration);
        toast.show();
    }

    public void showListings(View v){
        for(int i = 0; i < listings.size(); i++){
            LatLng temp = new LatLng(listings.get(i).getLocation().getLatitude(), listings.get(i).getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions().position(temp).title(i+""));
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                @Override
                public boolean onMarkerClick(Marker arg0) {
                    Listing l = listings.get(Integer.parseInt(arg0.getTitle()));
                    Bitmap bmp = null;
                    String filename = l.getPicture();
                    if(filename != null) {
                        try {
                            FileInputStream is = _this.openFileInput(filename);
                            bmp = BitmapFactory.decodeStream(is);
                            is.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ImageView iv = null;
                        iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);
                        iv.setImageBitmap(bmp);
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else{
                        ImageView iv = null;
                        iv = ((ImageView) findViewById(R.id.photo));//.setImageBitmap(bm);
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.solid_gray);
                        iv.setImageBitmap(bm);
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                    //update textViews
                    TextView titleText = (TextView) findViewById(R.id.titleText);
                    titleText.setText("Title: "+l.getTitle());
                    TextView nameText = (TextView) findViewById(R.id.nameText);
                    nameText.setText("Name: "+l.getName());
                    TextView phoneText = (TextView) findViewById(R.id.phoneText);
                    phoneText.setText("Phone: "+l.getPhone());
                    TextView priceText = (TextView) findViewById(R.id.priceText);
                    priceText.setText("Price: $"+l.getPrice());


                    return true;
                }

            });
        }

    }

    public void updateText(TextView t, String text){
        t.setText(text);
    }

}

