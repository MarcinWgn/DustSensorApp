package com.wegrzyn.marcin.dustsensorapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static com.wegrzyn.marcin.dustsensorapp.Utils.setPm10Color;
import static com.wegrzyn.marcin.dustsensorapp.Utils.setPm2Color;

/**
 * Created by Marcin Węgrzyn on 27.01.2018.
 *            wireamg@gmail.com
 */

public class SensorAdapter extends RecyclerView.Adapter<SensorAdapter.SensorAdapterViewHolder> {

    private List<SensorData> list;
    private Context context;
    final private ListItemClickListener listItemClickListener;

    public interface ListItemClickListener {
        void onListItemCickListener (int clickedItemIndex);
    }


    SensorAdapter(Context context, List<SensorData> list, ListItemClickListener listItemClickListener) {
        this.list = list;
        this.context = context;
        this.listItemClickListener = listItemClickListener;
    }

    class SensorAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView PM2;
        TextView PM10;
        TextView dayWeek;
        TextView data;


        SensorAdapterViewHolder(View itemView) {
            super(itemView);
            PM2 = itemView.findViewById(R.id.TV_PM2);
            PM10 = itemView.findViewById(R.id.TV_PM10);
            dayWeek = itemView.findViewById(R.id.TV_Week_Day);
            data = itemView.findViewById(R.id.TV_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemCickListener(getAdapterPosition());
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

        holder.dayWeek.setText(list.get(position).getWeekDay());

        holder.data.setText(list.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setData(SensorData sensorData){

        list.add(sensorData) ;
    }
    public void clearData(){
        list.clear();
    }
}
