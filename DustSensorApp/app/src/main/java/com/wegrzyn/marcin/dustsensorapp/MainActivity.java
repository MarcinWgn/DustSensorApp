package com.wegrzyn.marcin.dustsensorapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorAdapter.ListItemClickListener,
SharedPreferences.OnSharedPreferenceChangeListener{

    private final static String Tag = MainActivity.class.getSimpleName();

    private static final String SENSOR_DATA = "SensorData";
    public static final String PM_2_TABLE = "pm2table";
    public static final String PM_10_TABLE = "pm10table";

    private List<SensorData> sensorDataList = new ArrayList<>();

    private SensorAdapter adapter;

    private ProgressBar progressBar;
    private static FirebaseDatabase firebaseDatabase;

    private int numberItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        sharedPref();

        progressBar = findViewById(R.id.progress);
        RecyclerView recyclerView = findViewById(R.id.dust_recycler_view);


        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new SensorAdapter(this,sensorDataList, this);

        recyclerView.setAdapter(adapter);


        if(firebaseDatabase==null){
            firebaseDatabase= FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
        }


        queryFirebase();
    }

    private void queryFirebase() {

        progressBar.setVisibility(View.VISIBLE);
        adapter.clearData();
        sensorDataList.clear();

        DatabaseReference databaseReference = firebaseDatabase.getReference(SENSOR_DATA);
        Query query = databaseReference.limitToLast(numberItem);

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorData sensorData = dataSnapshot.getValue(SensorData.class);
                setData(sensorData);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(Tag,"onChildChange");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(Tag,"onChildRemoved");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(Tag,"onChildMoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(Tag,"onCanceled");
            }
        });

        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void sharedPref(){
        SharedPreferences sharedPreferences
                = PreferenceManager.getDefaultSharedPreferences(this);
        getPreferences(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void getPreferences(SharedPreferences sharedPreferences) {
        String numberString = sharedPreferences.getString(getResources()
                .getString(R.string.key_number_elements),getString(R.string._10));
        if(TextUtils.isDigitsOnly(numberString)&&0!=Integer.parseInt(numberString)){
            numberItem = Integer.parseInt(numberString);
        }
    }

    private void setData(SensorData sensorData) {
        adapter.setData(sensorData);
        adapter.notifyDataSetChanged();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       if (id == R.id.plot){
            Intent intent = new Intent(this,PlotActivity.class);
            int size = sensorDataList.size();

            float pm2Table [] = new float[size];
            float pm10Table [] = new float[size];

            for (int i = 0; i < size ; i++){
                pm2Table[i] = sensorDataList.get(i).getPM2();
                pm10Table[i] = sensorDataList.get(i).getPM10();
            }
            intent.putExtra(PM_2_TABLE,pm2Table);
            intent.putExtra(PM_10_TABLE,pm10Table);
            startActivity(intent);

            return true;
        }
        if(id == R.id.action_settings){
           Intent intent = new Intent(this, SettingsActivity.class);
           startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListItemCickListener(int clickedItemIndex) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT,sensorDataList.get(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.key_number_elements))){
            getPreferences(sharedPreferences);
            queryFirebase();
            Log.d(SettingsFragment.TAG,"onSharedPreferenceChanged");

    }
}
}
