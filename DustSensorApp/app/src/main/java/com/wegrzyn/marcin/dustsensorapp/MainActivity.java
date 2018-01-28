package com.wegrzyn.marcin.dustsensorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class MainActivity extends AppCompatActivity implements SensorAdapter.ListItemClickListener {

    private final static String Tag = MainActivity.class.getSimpleName();

    private static final String SENSOR_DATA = "SensorData";

    private List<SensorData> sensorDataList = new ArrayList<>();

    private SensorAdapter adapter;

    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);


        progressBar = findViewById(R.id.progress);
        RecyclerView recyclerView = findViewById(R.id.dust_recycler_view);

        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new SensorAdapter(this,sensorDataList, this);

        recyclerView.setAdapter(adapter);


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(SENSOR_DATA);
        Query query = databaseReference.limitToLast(10);
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
    }

    private void setData(SensorData sensorData) {
        Log.d("Dane", sensorData.toString());
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
        if (id == R.id.exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemCickListener(int clickedItemIndex) {
        Log.d(Tag,"Click Item: ---> "+ String.valueOf(clickedItemIndex));
    }
}
