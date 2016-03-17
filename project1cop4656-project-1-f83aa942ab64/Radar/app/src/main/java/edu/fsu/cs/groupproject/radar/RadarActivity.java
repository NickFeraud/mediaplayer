package edu.fsu.cs.groupproject.radar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class RadarActivity  extends AppCompatActivity implements LocationListener {


    private RadarView mRadarView;
    TextView t;
    boolean set = true;
    Location temp = new Location("");

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.radar);
        Bundle extras = getIntent().getExtras();

        if(extras != null){
            double latitude = extras.getDouble("latitude");
            double longitude = extras.getDouble("longitude");
            temp.setLatitude(latitude);
            temp.setLongitude(longitude);
        }
        t = (TextView) findViewById(R.id.textView);
        mRadarView = (RadarView) findViewById(R.id.radar_view);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, this);
    }

    public void onLocationChanged(Location location) {
        if (set) {
            mRadarView.m = (int) location.distanceTo(temp);
            set = false;
        }
        if (location.distanceTo(temp) != 0) {
            mRadarView.OnlocationChange((int) location.distanceTo(temp));
            t.setText(location.distanceTo(temp)+" meters to move");
            mRadarView.invalidate();
        }
        if(location.distanceTo(temp) <= 5){
            mRadarView.OnlocationChange(0);
            Toast.makeText(RadarActivity.this, "You are near your object! Look around!", Toast.LENGTH_LONG).show();
        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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

        if (id == R.id.map){
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
