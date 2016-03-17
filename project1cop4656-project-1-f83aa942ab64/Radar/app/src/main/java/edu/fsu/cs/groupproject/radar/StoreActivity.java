package edu.fsu.cs.groupproject.radar;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by silve_000 on 3/1/2016.
 */
public class StoreActivity extends AppCompatActivity implements LocationListener {

    private TextView dateTextView, latTextView, longTextView;
    private EditText locationEditText, objectEditText, personEditText;
    String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity_radar);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        dateTextView.setText(currentDateTimeString);
        latTextView = (TextView) findViewById(R.id.latTextView);
        longTextView = (TextView) findViewById(R.id.longTextView);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }

        //LocationListener Setup
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }


    public void onLocationChanged(Location location) {
        latTextView.setText("" + location.getLatitude());
        longTextView.setText("" + location.getLongitude());
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
        if (id == R.id.list) {
            Intent myIntent = new Intent(this, ListActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (id == R.id.radar) {
            Intent myIntent = new Intent(this, RadarActivity.class);
            startActivity(myIntent);
            return true;
        }
        if (id == R.id.map){
            Intent myIntent = new Intent(this, MainActivity.class);
            startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void onStoreButtonClick(View view) {
        //Store everything in database
        locationEditText = (EditText) findViewById(R.id.locationEditText);
        objectEditText = (EditText) findViewById(R.id.objectEditText);
        personEditText = (EditText) findViewById(R.id.personEditText);
        String long_string = longTextView.getText().toString();
        double longitude = Double.parseDouble(long_string);
        String lat_string = latTextView.getText().toString();
        double latitude = Double.parseDouble(lat_string);

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(MyContentProvider.COLUMN_LATITUDE, latitude);
        mNewValues.put(MyContentProvider.COLUMN_LONGITUDE, longitude);
        mNewValues.put(MyContentProvider.COLUMN_LOCHINT, locationEditText.getText().toString());
        mNewValues.put(MyContentProvider.COLUMN_ITEMNAME, objectEditText.getText().toString());
        mNewValues.put(MyContentProvider.COLUMN_DATESTORED, currentDateTimeString);
        mNewValues.put(MyContentProvider.COLUMN_USERNAME, personEditText.getText().toString());

        Uri mNewUri = getContentResolver().insert(
                MyContentProvider.CONTENT_URI, mNewValues);
        clear();
    }

    private void clear(){
        locationEditText.setText("");
        personEditText.setText("");
        objectEditText.setText("");
    }

}

