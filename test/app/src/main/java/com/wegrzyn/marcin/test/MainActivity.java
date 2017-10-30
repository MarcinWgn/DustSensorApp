package com.wegrzyn.marcin.test;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {



    private TextView textView;
    private Button button;

    public static byte[] sleep ={(byte) 0xAA, (byte) 0xB4,0x06,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xFF, (byte) 0xFF,0x05, (byte) 0xAB};
    public static byte[] work ={(byte) 0xAA, (byte) 0xB4,0x06,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xFF, (byte) 0xFF,0x06, (byte) 0xAB};
    public static byte[] continious = {(byte) 0xAA, (byte) 0xB4,0x08,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xFF, (byte) 0xFF,0x07, (byte) 0xAB};
    public static byte[] query = {(byte) 0xAA, (byte) 0xB4,0x02,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xFF, (byte) 0xFF,0x00, (byte) 0xAB};
    public static byte[] cycle1min = {(byte) 0xAA, (byte) 0xB4,0x08,0x01,0x01,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xFF, (byte) 0xFF,0x08, (byte) 0xAB};
    public static byte[] cycle30min = {(byte) 0xAA, (byte) 0xB4,0x08,0x01,0x1E,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00, (byte) 0xFF, (byte) 0xFF,0x08, (byte) 0xAB};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.tv);
        button = findViewById(R.id.btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               String s = Integer.toHexString(checkSum(cycle30min));
                textView.setText(s);
            }
        });
    }

    private byte checkSum(byte[] bytes){
        byte sum = 0;
        for (int i = 2 ; i<bytes.length-2 ; i++){
            sum +=Byte.toUnsignedInt(bytes[i]);
        }
        return (byte) sum;
    }

}
