package com.wegrzyn.marcin.dustsensorapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wirea on 27.01.2018.
 */

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorAdapterViewHolder> {

    List<SensorData> list;
    Context context;


    public SensorAdapter(Context context, List<SensorData> list) {
        this.list = list;
        this.context = context;
    }

    public class SensorAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView PM2;
        TextView PM10;

        TextView temp;
        TextView press;

        TextView data;


        public SensorAdapterViewHolder(View itemView) {
            super(itemView);
            PM2 = itemView.findViewById(R.id.TV_PM2);
            PM10 = itemView.findViewById(R.id.TV_PM10);
            temp = itemView.findViewById(R.id.TV_temp);
            press = itemView.findViewById(R.id.TV_pressure);
            data = itemView.findViewById(R.id.TV_date);
        }
    }

    @Override
    public SensorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);

        return new SensorAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SensorAdapterViewHolder holder, int position) {
        holder.PM2.setText(String.valueOf(list.get(position).getPM2()));
        holder.PM2.setTextColor(setPm2Color(context,list.get(position).getPM2()));

        holder.PM10.setText(String.valueOf(list.get(position).getPM10()));
        holder.PM10.setTextColor(setPm10Color(context,list.get(position).getPM10()));

        holder.temp.setText(String.valueOf(list.get(position).getTemp()));
        holder.press.setText(String.valueOf(list.get(position).getPress()));

        holder.data.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setData(SensorData sensorData){

        list.add(sensorData) ;
    }
    private int setPm10Color(Context context, float pm ){
        if(pm < 50)return ContextCompat.getColor(context,R.color.green);
        else if (pm < 100) return ContextCompat.getColor(context,R.color.yellow);
        else if (pm < 150) return ContextCompat.getColor(context,R.color.orange);
        else return ContextCompat.getColor(context, R.color.red);

    }
    private int setPm2Color(Context context, float pm ){
        if(pm < 30)return ContextCompat.getColor(context,R.color.green);
        else if (pm < 60) return ContextCompat.getColor(context,R.color.yellow);
        else if (pm < 120) return ContextCompat.getColor(context,R.color.orange);
        else return ContextCompat.getColor(context, R.color.red);

    }

}
