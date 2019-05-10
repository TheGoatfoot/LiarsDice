package com.everhardsoft.diorama.util;

import android.os.SystemClock;

/**
 * Created by faisa on 10/19/2016.
 */

public class DeltaTime {
    private long lastCall;
    public DeltaTime() {
        setStart();
    }

    public void setStart() {
        lastCall = SystemClock.uptimeMillis();
    }

    public float getDelta() {
        long currentTime = SystemClock.uptimeMillis();
        long output = currentTime - lastCall;
        lastCall = currentTime;
        return Long.valueOf(output).floatValue() / 1000.f;
    }
}
