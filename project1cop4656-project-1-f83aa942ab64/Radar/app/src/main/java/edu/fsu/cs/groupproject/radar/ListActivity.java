package edu.fsu.cs.groupproject.radar;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by silve_000 on 3/1/2016.
 */
public class ListActivity extends AppCompatActivity {
    //testing testing 123
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity_radar);
        final ListView listview = (ListView) findViewById(R.id.listview);
        //get from db
        Cursor mCursor;
        mCursor = getContentResolver().query(
                MyContentProvider.CONTENT_URI,
                null,
                null,
                null,
                null);
        int iterator=0; //iterator
        String[] values = new String[10];
      /*  while (!mCursor.isAfterLast()) {
           //values[iterator]=mCursor.getString(mCursor.getColumnIndex(MyContentProvider.COLUMN_USERNAME)) +" | "+
            //        mCursor.getString(mCursor.getColumnIndex(MyContentProvider.COLUMN_ITEMNAME ));
          //  Toast.makeText(ListActivity.this," "+iterator,Toast.LENGTH_SHORT).show();
                    iterator++;

            //Toast.makeText(ListActivity.this,MyContentProvider.COLUMN_ITEMNAME+iterator,Toast.LENGTH_SHORT).show();
            mCursor.moveToNext();
        }
        Toast.makeText(ListActivity.this," "+values.length,Toast.LENGTH_SHORT).show();
*/
        final Vector<Vector<Double>> coords = new Vector<Vector<Double>>();


        mCursor.moveToNext();
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < mCursor.getCount(); ++i) {
            list.add(mCursor.getString(mCursor.getColumnIndex(MyContentProvider.COLUMN_ITEMNAME)) +" | "+
                    mCursor.getString(mCursor.getColumnIndex(MyContentProvider.COLUMN_USERNAME )));
            Vector<Double> vd =new Vector<Double>();
            vd.add(mCursor.getDouble(mCursor.getColumnIndex(MyContentProvider.COLUMN_LATITUDE)));
            vd.add(mCursor.getDouble(mCursor.getColumnIndex(MyContentProvider.COLUMN_LONGITUDE)));
            coords.add(vd);

            mCursor.moveToNext();
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
//Toast.makeText(ListActivity.this,""+coords.get(position).get(0)+" "+coords.get(position).get(1), Toast.LENGTH_LONG).show();
                Bundle extras = new Bundle();
                extras.putDouble("latitude", coords.get(position).get(0));
                extras.putDouble("longitude", coords.get(position).get(1));

                Intent radarIntent = new Intent(ListActivity.this, RadarActivity.class);

                radarIntent.putExtras(extras);

                startActivity(radarIntent);
            }

        });

    }
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

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
        if (id == R.id.store) {
            Intent myIntent = new Intent(this, StoreActivity.class);
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
}
