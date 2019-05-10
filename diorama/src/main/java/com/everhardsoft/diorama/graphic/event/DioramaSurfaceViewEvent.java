package com.everhardsoft.diorama.graphic.event;

import android.view.MotionEvent;

/**
 * Created by faisa on 12/5/2016.
 */

public interface DioramaSurfaceViewEvent {
    void onSurfaceChanged();
    void onSurfaceCreated();
    void onSurfaceDestroyed();
    void onDrawFrame();
    void onTouchEvent(MotionEvent e);
    void onPause();
    void onResume();
}
