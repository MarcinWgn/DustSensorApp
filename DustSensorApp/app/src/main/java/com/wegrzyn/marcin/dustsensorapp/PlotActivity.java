package com.wegrzyn.marcin.dustsensorapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PlotActivity extends AppCompatActivity {

    final static String TAG = PlotActivity.class.getSimpleName();
    float pm2Table[];
    float pm10Table[];
    int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        LineChart lineChart = findViewById(R.id.ViewPlot);


        Intent intent = getIntent();

        if(intent != null){
            if(intent.hasExtra(MainActivity.PM_2_TABLE)){
                pm2Table = intent.getFloatArrayExtra(MainActivity.PM_2_TABLE);
                size = pm2Table.length;
            }
            if(intent.hasExtra(MainActivity.PM_10_TABLE)){
                pm10Table = intent.getFloatArrayExtra(MainActivity.PM_10_TABLE);
            }
        }
        List<Entry> entriesPM2 = new ArrayList<Entry>();
        List<Entry> entriesPM10 = new ArrayList<>();

        for(int i = 0 ; i<size ; i++){

            entriesPM2.add(new Entry(i,pm2Table[i]));
            entriesPM10.add(new Entry(i,pm10Table[i]));
        }

        LineDataSet dataSet = new LineDataSet(entriesPM2,getString(R.string.pm2_label_chart));
        int colorPM2 = getResources().getColor(R.color.green);
        int colorPM10= getResources().getColor(R.color.blue);

        dataSet.setColor(colorPM2);
        dataSet.setCircleColor(colorPM2);

        LineDataSet dataSetPM10 = new LineDataSet(entriesPM10,getString(R.string.pm10_label_chart));

        dataSetPM10.setColor(colorPM10);
        dataSetPM10.setCircleColor(colorPM10);

        List<ILineDataSet> dataSets = new ArrayList<>();

        dataSets.add(dataSet);
        dataSets.add(dataSetPM10);

        LineData lineData = new LineData(dataSets);

        Description description = lineChart.getDescription();
        description.setEnabled(false);
        lineChart.setData(lineData);
        lineChart.invalidate();


        Log.d(TAG, "Table "+String.valueOf(pm2Table[9])+
                " "+String.valueOf(pm10Table[9])+
                " size: "+String.valueOf(size));
    }
}
