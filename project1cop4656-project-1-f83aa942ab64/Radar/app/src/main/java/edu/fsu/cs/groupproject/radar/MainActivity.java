package edu.fsu.cs.groupproject.radar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    //******************************
    //Class Variables
    //******************************


    //Map Fragment
    private MapFragment mapFragment;
    private GoogleMap mMap;
    private UiSettings mUiSettings;

    //Layout Elements
    private EditText searchBar;
    private Button searchButton;

    //Data Storage
    ArrayList<Marker> markerArray;

    //******************************
    //Activity Lifecycle
    //******************************

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_radar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.store) {
            Intent myIntent = new Intent(this, StoreActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (id == R.id.list) {
            Intent myIntent = new Intent(this, ListActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (id == R.id.radar){
            Intent myIntent = new Intent(this, RadarActivity.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Map Fragment Setup (Calls back onMapReady(:) )
        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Other Activity Element Setup
        searchBar = (EditText) findViewById(R.id.searchEditText);
        searchButton = (Button) findViewById(R.id.searchButton);

        //TODO: This implementation will be changed with backend stuff
        //Set up list of markers
        markerArray = new ArrayList<Marker>();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }

        //LocationListener Setup
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(newLocation)        // Sets the center of the map to location user
                        .zoom(18)                   // Sets the zoom
                        // .bearing(90)             // Sets the orientation of the camera to east
                        // .tilt(40)                // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Log.i("MainActivity", "onLocationChanged(:)");
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("MainActivity", "onStatusChanged(:)");
            }

            public void onProviderEnabled(String provider) {
                Log.i("MainActivity", "onProviderEnabled(:)");
            }

            public void onProviderDisabled(String provider) {
                Log.i("MainActivity", "onProviderDisabled(:)");
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //******************************
    //Map Fragment Delegate Methods
    //******************************



    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mUiSettings = mMap.getUiSettings();

        //disables Scrolling
        mUiSettings.setScrollGesturesEnabled(false);



    }




    //******************************
    //Helper Functions
    //******************************

    //Add a marker from doubles latitude and longitude
    private void addMarkerFromCoordinates (double Lat, double Long) {
        LatLng newLocation = new LatLng(Lat, Long);
        Marker newMarker = mMap.addMarker(new MarkerOptions().position(newLocation).title("New Marker"));
        markerArray.add(newMarker);
    }

    //Iterates though markers and removes
    private void removeAllMarkers() {
        for (Marker thisMarker : markerArray) {
            thisMarker.remove();
        }
    }


    //TODO: Could be useful to implement
    //Removes a marker at coordinates, if one exists. If so, returns true
    //Else returns false
    private boolean removeMarkerAtCoordinate(double Lat, double Long) {
        return true;
    }

}
