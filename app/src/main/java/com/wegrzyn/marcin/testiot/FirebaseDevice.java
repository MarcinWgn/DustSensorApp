package com.wegrzyn.marcin.testiot;

/**
 * Created by wirea on 08.10.2017.
 */

public class FirebaseDevice {

    private boolean ledA;
    private boolean ledB;
    private boolean ledC;

    public FirebaseDevice(boolean ledA, boolean ledB, boolean ledC) {
        this.ledA = ledA;
        this.ledB = ledB;
        this.ledC = ledC;
    }

    public FirebaseDevice() {
    }

    public boolean isLedA() {
        return ledA;
    }

    public boolean isLedB() {
        return ledB;
    }

    public boolean isLedC() {
        return ledC;
    }


    @Override
    public String toString() {

        return " LedA: "+ledA+ " LedB: "+ledB+" LedC: "+ledC;
    }
}
