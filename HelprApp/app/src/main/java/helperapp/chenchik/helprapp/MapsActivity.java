package helperapp.chenchik.helprapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TextView;

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

    //int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here, thisActivity is the current activity
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/




        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);








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

            //square
            //0___________1
            //|          |
            //|          |
            //|          |
            //|__________|
            //2         3
/*
            double oldWellLeftLong=-79.051786;
            double oldWellRightLong=-79.050676;
            double oldWellTopLat=35.912462;
            double oldWellBottomLat=35.911665;
            double polkPlaceLeftLong=-79.0509837;
            double polkPlaceRightLong=-79.049482;
            double polkPlaceTopLat=35.911619;
            double polkPlaceBottomLat=35.909556;
            double brooksLeftLong=-79.053613;
            double brooksRightLong=-79.052819;
            double brooksTopLat=35.910347;
            double brooksBottomLat=35.909469;
*/
            LatLng[] brooksArr = {new LatLng(brooksTopLat, brooksLeftLong)
                    , new LatLng(brooksTopLat, brooksRightLong)
                    , new LatLng(brooksBottomLat, brooksLeftLong)
                    , new LatLng(brooksBottomLat, brooksRightLong)};

            LatLng[] polkPlaceArr = {new LatLng(polkPlaceTopLat, polkPlaceLeftLong)
                    , new LatLng(polkPlaceTopLat, polkPlaceRightLong)
                    , new LatLng(polkPlaceBottomLat, polkPlaceLeftLong)
                    , new LatLng(polkPlaceBottomLat, polkPlaceRightLong)};

            LatLng[] oldWellArr = {new LatLng(oldWellTopLat, oldWellLeftLong)
                    , new LatLng(oldWellTopLat, oldWellRightLong)
                    , new LatLng(oldWellBottomLat, oldWellLeftLong)
                    , new LatLng(oldWellBottomLat, oldWellRightLong)};
            // Add a marker in Sydney and move the camera

            //35.909792, -79.053103
            //35.909643, -79.053297
            //kkmarker = mMap.addMarker(new MarkerOptions().position(brooks).title("Marker in Brooks"));
            /*mMap.addMarker(new MarkerOptions().position(brooksArr[0]).title("0"));
            mMap.addMarker(new MarkerOptions().position(brooksArr[1]).title("1"));
            mMap.addMarker(new MarkerOptions().position(brooksArr[2]).title("2"));
            mMap.addMarker(new MarkerOptions().position(brooksArr[3]).title("3"));

            mMap.addMarker(new MarkerOptions().position(oldWellArr[0]).title("0"));
            mMap.addMarker(new MarkerOptions().position(oldWellArr[1]).title("1"));
            mMap.addMarker(new MarkerOptions().position(oldWellArr[2]).title("2"));
            mMap.addMarker(new MarkerOptions().position(oldWellArr[3]).title("3"));

            mMap.addMarker(new MarkerOptions().position(polkPlaceArr[0]).title("0"));
            mMap.addMarker(new MarkerOptions().position(polkPlaceArr[1]).title("1"));
            mMap.addMarker(new MarkerOptions().position(polkPlaceArr[2]).title("2"));
            mMap.addMarker(new MarkerOptions().position(polkPlaceArr[3]).title("3"));*/

            //marker.remove();
            //mMap.addMarker(new MarkerOptions().position(polkPlace).title("Marker in Polk Place"));
            //mMap.addMarker(new MarkerOptions().position(oldWell).title("Marker in old Well"));
            //if(loc == null) {
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(brooks));
            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17.0f));
            //}else {
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            //}

        }catch(SecurityException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("TAG", "We are connected to Google Services");
        try {
            // Location loc = LocationServices.FusedLocationApi.getLastLocation(c);
            // Log.v("LOC", "" + loc.getLatitude() + ", " + loc.getLongitude());

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

        //loc = location;

        if ((location.getLatitude() >= brooksBottomLat && location.getLatitude() <= brooksTopLat) &&
                (location.getLongitude() <= brooksRightLong && location.getLongitude() >= brooksLeftLong)){
            marker = mMap.addMarker(new MarkerOptions().position(brooks).title("Marker in Brooks"));
            if(mp == null && currentSound != 1) {
                //mp = MediaPlayer.create(this, R.raw.coolkids);
                //mp.start();
            }
            currentSound = 1;
        }
        else if ((location.getLatitude() >= polkPlaceBottomLat && location.getLatitude() <= polkPlaceTopLat) &&
                (location.getLongitude() <= polkPlaceRightLong && location.getLongitude() >= polkPlaceLeftLong)){
            marker = mMap.addMarker(new MarkerOptions().position(polkPlace).title("Polk Place"));
            if(mp == null && currentSound != 2) {
                //mp = MediaPlayer.create(this, R.raw.givemesomesunshine);
                //mp.start();
            }
            currentSound = 2;
        }
        else if ((location.getLatitude() >= oldWellBottomLat && location.getLatitude() <= oldWellTopLat) &&
                (location.getLongitude() <= oldWellRightLong && location.getLongitude() >= oldWellLeftLong)){
            marker = mMap.addMarker(new MarkerOptions().position(oldWell).title("Marker in Old Well"));
            if(mp == null && currentSound != 3) {
                //mp = MediaPlayer.create(this, R.raw.sugar);
                //mp.start();
            }
            currentSound = 3;
        }

        else{
            //marker = mMap.addMarker(new MarkerOptions().position(brooks).title("Marker in Brooks"));
            //marker.remove();
            //marker = null;
            //marker = null;
            mMap.clear();
            if(mp != null) {
                mp.stop();
                mp.release();
            }
            mp = null;
            currentSound = 0;

        }

        Geocoder g = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> la = g.getFromLocation(location.getLatitude(), location.getLongitude(), 1);


            //TextView text = (TextView) findViewById(R.id.addressText);
            //text.setText("");
            //text.append(la.get(0).toString());
            Log.v("Address", la.get(0).toString());

            // LatLng currentLatLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17.0f));


            //la = g.getFromLocationName("Sitterson Hall", 1);
            //Log.v("From name", la.get(0).toString());

        }catch (Exception ex) {

        }

    }

    @Override
    public void onPrepared(MediaPlayer mp){
        if(mp != null) {
            mp.start();
        }
    }



    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/

    public void makeNewListing(View v){
        Log.v("its", "workingggggg");
        Intent x = new Intent(this, NewListingActivity.class);
        //Intent x = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.unc.edu") );
        startActivity(x);


    }
}

