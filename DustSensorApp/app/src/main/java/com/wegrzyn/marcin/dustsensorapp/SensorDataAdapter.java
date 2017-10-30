package com.wegrzyn.marcin.dustsensorapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by wirea on 28.10.2017.
 */

public class SensorDataAdapter extends ArrayAdapter<SensorData> {
    public SensorDataAdapter(@NonNull Context context, @NonNull List<SensorData> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_layout,parent,false);
        }

        TextView dateTextView = convertView.findViewById(R.id.Data);
        TextView pm2TextView = convertView.findViewById(R.id.PM2);
        TextView pm10TextView = convertView.findViewById(R.id.PM10);

        SensorData sensorData = getItem(position);

        String datestring = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sensorData.getDate());

        dateTextView.setText(datestring);
        float pm2 = sensorData.getPM2();
        float pm10 = sensorData.getPM10();
        pm2TextView.setText(String.valueOf(pm2));
        pm10TextView.setText(String.valueOf(pm10));
        return convertView;
    }

}
