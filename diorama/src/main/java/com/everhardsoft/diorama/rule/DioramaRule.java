package com.everhardsoft.diorama.rule;

import android.content.Context;
import android.view.MotionEvent;

import com.everhardsoft.diorama.world.DioramaWorld;

/**
 * Created by faisa on 11/1/2016.
 */

public abstract class DioramaRule {
    protected Context context;
    protected DioramaWorld world;
    public void setContext(Context context) {this.context=context;}
    public void setGameWorld(DioramaWorld world) {this.world =world;}
    public abstract void onSurfaceCreated();
    public abstract void onSurfaceDestroyed();
    public abstract void onTouchEvent(MotionEvent e, float relativeX, float relativeY);
    public abstract void onUpdate(float delta);
    public abstract void onStart();
    public abstract void onRestart();
    public abstract void onStop();
    public abstract void onPause();
    public abstract void onResume();
}
