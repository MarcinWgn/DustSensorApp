package com.wegrzyn.marcin.dustsensorapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String Tag = MainActivity.class.getSimpleName();

    private static final String SENSOR_DATA = "SensorData";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private List<SensorData> sensorDataList = new ArrayList<>();
    private SensorDataAdapter sensorDataAdapter;

    private ListView listView;

    private TextView PM2txt;
    private TextView PM10txt;
    private TextView DataTxt;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        listView = findViewById(R.id.listView);

        PM2txt =findViewById(R.id.PM2TV);
        PM10txt =findViewById(R.id.PM10TV);
        DataTxt = findViewById(R.id.DataTV);

        progressBar = findViewById(R.id.progress);


        sensorDataAdapter = new SensorDataAdapter(this,sensorDataList);
        listView.setAdapter(sensorDataAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(SENSOR_DATA);
        Query query = databaseReference.limitToLast(6);
        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                SensorData sensorData = dataSnapshot.getValue(SensorData.class);
                sensorDataAdapter.add(sensorData);

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setData(sensorDataList.get(i));
            }
        });
    }

    private void setData(SensorData sensorData) {
        PM2txt.setText(String.valueOf(sensorData.getPM2()));
        PM10txt.setText(String.valueOf(sensorData.getPM10()));
        DataTxt.setText(sensorData.getDate().toString());
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
        if (id == R.id.exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
