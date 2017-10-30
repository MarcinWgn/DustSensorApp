package com.wegrzyn.marcin.testiot;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.things.pio.PeripheralManagerService;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;


/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {


    public static final String Key = "device";

    private Button button;
    private TextView textView;

    private static final String TAG = "maindebug";

    private DisplHt16k33 displHt16k33;

    private ButtonLed led;
    private ButtonLed led2;
    private ButtonLed led3;

    private HadrwareBtn hadrwareBtnC;
    private HadrwareBtn hadrwareBtnB;
    private HadrwareBtn hadrwareBtnA;

    private LedStrip ledStrip;

    private String UartName;
    private UartDevice uartDevice;

    private SensorSDS011  sds011 = new SensorSDS011();



    @Override
    protected void onStart() {
        super.onStart();

        try {
            uartDevice.registerUartDeviceCallback(uartDeviceCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    UartDeviceCallback uartDeviceCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            try {
                readUartBuffer(uart);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    };


    @Override
    protected void onStop() {
        super.onStop();
        uartDevice.unregisterUartDeviceCallback(uartDeviceCallback);
    }

    //    ------- Firebase-----------------------
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //----------------------------UART---------------------------------
        PeripheralManagerService manager = new PeripheralManagerService();
        List<String> deviceList = manager.getUartDeviceList();
        if (deviceList.isEmpty()) {
            Log.i(TAG, "No UART port available on this device.");
        } else {
          UartName = deviceList.get(0);
            Log.i(TAG, "List of available devices: " + deviceList);
            try {
                uartDevice = manager.openUartDevice(UartName);
                configureUartFrame(uartDevice);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // -------------------------------------------------------------------------



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Key);

        button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"--->"+dataSnapshot.toString());
                FirebaseDevice device = dataSnapshot.getValue(FirebaseDevice.class);

                if(device.isLedA())led.setLed(true);
                else led.setLed(false);
                if(device.isLedB())led2.setLed(true);
                else led2.setLed(false);
                if(device.isLedC())led3.setLed(true);
                else led3.setLed(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ledStrip = new LedStrip();
        ledStrip.setOneColor(Color.BLACK);

        hadrwareBtnC = new HadrwareBtn(HadrwareBtn.TouchC,KeyEvent.KEYCODE_C);
        hadrwareBtnB = new HadrwareBtn(HadrwareBtn.TouchB,KeyEvent.KEYCODE_B);
        hadrwareBtnA = new HadrwareBtn(HadrwareBtn.TouchA,KeyEvent.KEYCODE_A);

        displHt16k33 = new DisplHt16k33(2);
        displHt16k33.display("wait");

        led = new ButtonLed(ButtonLed.LedRed);
        led.setLed(false);

        led2 = new ButtonLed(ButtonLed.LedGreen);
        led.setLed(false);

        led3 = new ButtonLed(ButtonLed.LedBlue);
        led3.setLed(false);


        button.setOnClickListener(new View.OnClickListener() {

            int i = 100;
            @Override
            public void onClick(View view) {
                i--;
                if(i<0) i = 100;
                displHt16k33.display(i);
                new StripTask().execute(20);
            }
        });

    }

    public void configureUartFrame(UartDevice uart) throws IOException {
        // Configure the UART port
        uart.setBaudrate(9600);
        uart.setDataSize(8);
        uart.setParity(UartDevice.PARITY_NONE);
        uart.setStopBits(1);
    }

    public void readUartBuffer(UartDevice uart) throws IOException {
        // Maximum amount of data to read at one time
        final int maxCount = 10;
        byte[] buffer = new byte[maxCount];

        int count;
        while ((count = uart.read(buffer, buffer.length)) > 0) {
            Log.d(TAG, "Read: PM2.5: "+sds011.readPM2(buffer)+" PM10: "+sds011.readPM10(buffer)
            +" data: "+ sds011.readData(buffer));
            displHt16k33.display("b"+sds011.getSds011Data().getPM10str());
        }
    }



    public void writeUartData(UartDevice uart) throws IOException {
        byte[] buffer = new byte[1024];
        int count = uart.write(buffer, buffer.length);
        Log.d(TAG, "Wrote " + count + " bytes to peripheral");
    }

    private class StripTask extends AsyncTask<Integer,Void,Void> {

        int j = 0;

        @Override
        protected Void doInBackground(Integer... integers) {
            for (int i = 0 ; i < integers[0] ; i++){

                if(j>6) {
                    j = 0;
                    ledStrip.setOneColor(Color.GRAY);
                }
                ledStrip.modOneColor(j,Color.RED);
                j++;

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Log.e(TAG,"Error Thread sleep"+e);
                }
            }
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_C){
            led3.setLed(true);
            databaseReference.setValue(new FirebaseDevice(false,false,true));
        }
        else if (keyCode == KeyEvent.KEYCODE_B){
            led2.setLed(true);
            databaseReference.setValue(new FirebaseDevice(false,true,false));
            displHt16k33.display("b"+sds011.getSds011Data().getPM10str());
        }
        else if (keyCode == KeyEvent.KEYCODE_A){
            led.setLed(true);
            databaseReference.setValue(new FirebaseDevice(true,false,false));
            displHt16k33.display("a"+sds011.getSds011Data().getPM25str());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_C)
            led3.setLed(false);
        else if (keyCode == KeyEvent.KEYCODE_B)
            led2.setLed(false);
        else if (keyCode == KeyEvent.KEYCODE_A)
            led.setLed(false);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        displHt16k33.close();
        led.close();
        led2.close();
        led3.close();
        hadrwareBtnC.close();
        hadrwareBtnB.close();
        hadrwareBtnA.close();

        if (uartDevice != null) {
            try {
                uartDevice.close();
                uartDevice = null;
            } catch (IOException e) {
                Log.w(TAG, "Unable to close UART device", e);
            }
        }
    }

    }
