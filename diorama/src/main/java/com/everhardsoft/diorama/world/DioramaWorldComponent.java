package com.everhardsoft.diorama.world;

import android.view.MotionEvent;

import com.everhardsoft.diorama.world.object.DioramaObject;

/**
 * Created by faisa on 11/3/2016.
 */

public abstract class DioramaWorldComponent {
    public String tag = "";
    protected boolean isInitialized = false;
    protected void added(DioramaWorld world) {
        if(!isInitialized) {
            initialize(world);
            isInitialized = true;
        }
    }
    protected void removed(DioramaWorld world) {
        if(isInitialized) {
            deinitialize(world);
            isInitialized = false;
        }
    }
    public abstract void addObject(DioramaWorld world, DioramaObject object);
    public abstract void removeObject(DioramaWorld world, DioramaObject object);
    public abstract void update(DioramaWorld world, float delta);
    public abstract void touchEvent(DioramaWorld world, MotionEvent event, float relativeX, float relativeY);
    protected abstract void initialize(DioramaWorld world);
    protected abstract void deinitialize(DioramaWorld world);
}
