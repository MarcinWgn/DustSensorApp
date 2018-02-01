package com.wegrzyn.marcin.dustsensorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import com.google.firebase.provider.FirebaseInitProvider;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView data = findViewById(R.id.TV_data);
        TextView pm2 = findViewById(R.id.TV_PM2);
        TextView pm10 = findViewById(R.id.TV_PM10);
        TextView press = findViewById(R.id.TV_press);
        TextView temp = findViewById(R.id.TV_temp);

        Intent intent = getIntent();

        if (intent != null&&intent.hasExtra(Intent.EXTRA_TEXT)) {
            SensorData sensorData = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            data.setText(sensorData.getFullDate());

            pm2.setText(SensorData.numberFormat(sensorData.getPM2()));
            pm2.setTextColor(Utils.setPm2Color(this, sensorData.getPM2()));

            pm10.setText(SensorData.numberFormat(sensorData.getPM10()));
            pm10.setTextColor(Utils.setPm10Color(this, sensorData.getPM10()));

            press.setText(SensorData.numberFormat(sensorData.getPress()));
            temp.setText(SensorData.numberFormat(sensorData.getTemp()));
        }
    }
}