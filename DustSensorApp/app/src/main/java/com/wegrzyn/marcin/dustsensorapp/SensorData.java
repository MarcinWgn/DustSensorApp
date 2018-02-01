package com.wegrzyn.marcin.dustsensorapp;

import android.icu.text.DecimalFormat;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Marcin Węgrzyn on 28.10.2017.
 *            wireamg@gmail.com
 */

public class SensorData implements Parcelable{

    private long PosixTime;
    private float PM2;
    private float PM10;
    private float Temp;
    private float Press;


    public SensorData(float PM2, float PM10, float Press, float Temp, long PosixTime) {
        this.PM2 = PM2;
        this.PM10 = PM10;
        this.Press = Press;
        this.Temp = Temp;
        this.PosixTime = PosixTime;
    }

    private SensorData(Parcel in) {
        PosixTime = in.readLong();
        PM2 = in.readFloat();
        PM10 = in.readFloat();
        Temp = in.readFloat();
        Press = in.readFloat();
    }

    public static final Creator<SensorData> CREATOR = new Creator<SensorData>() {
        @Override
        public SensorData createFromParcel(Parcel in) {
            return new SensorData(in);
        }

        @Override
        public SensorData[] newArray(int size) {
            return new SensorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(PosixTime);
        dest.writeFloat(PM2);
        dest.writeFloat(getPM10());
        dest.writeFloat(Temp);
        dest.writeFloat(Press);
    }

    SensorData() {
        PM10 = 0;
        PM2 = 0;
        Press = 0;
        Temp = 0;
    }

    float getPM2() {
        return PM2;
    }

    float getPM10() {
        return PM10;
    }

    float getTemp() {
        return Temp;
    }

    float getPress() {
        return Press;
    }

    long getPosixTime() {return PosixTime; }

    void setPosixTime(long posixTime) { PosixTime = posixTime; }

    void setPM2(float PM2) {
        this.PM2 = PM2;
    }

    void setPM10(float PM10) {
        this.PM10 = PM10;
    }

    void setTemp(float temp) {
        Temp = temp;
    }

    void setPress(float press) {
        Press = press;
    }

    String getDate(){
       Date date = new Date(PosixTime);
       SimpleDateFormat dateFormat = new SimpleDateFormat (" dd.MM.yyyy  HH:mm");
        return dateFormat.format(date);
    }
    String getWeekDay(){
        Date date = new Date(PosixTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat ("E");
        return dateFormat.format(date);
    }
    String getFullDate(){
        Date date = new Date(PosixTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat ("E dd.MM.yyyy  HH:mm");
        return dateFormat.format(date);
    }

    static String numberFormat(float number){
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.#");
        return df.format(number);
    }

    @Override
    public String toString() {
        return "PM 2.5: " +
                String.valueOf(PM2) +
                "\n" +
                "PM 10: " +
                String.valueOf(PM10) +
                "\n" +
                "Temp: " +
                String.valueOf(Temp) +
                "\n" +
                "Press: " +
                String.valueOf(Press) +
                "\n" +
                new Date(PosixTime).toString();
    }


}

