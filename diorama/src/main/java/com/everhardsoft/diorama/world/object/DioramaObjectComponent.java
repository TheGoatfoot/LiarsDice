package com.everhardsoft.diorama.world.object;

import android.view.MotionEvent;

/**
 * Created by faisa on 11/1/2016.
 */

public abstract class DioramaObjectComponent {
    public String tag = "";
    protected boolean isInitialized = false;
    protected void added(DioramaObject object) {
        if(!isInitialized) {
            initialize(object);
            isInitialized = true;
        }
    }
    protected void removed(DioramaObject object) {
        if(isInitialized) {
            deinitialize(object);
            isInitialized = false;
        }
    }
    public abstract void update(DioramaObject object, float delta);
    public abstract void touchEvent(DioramaObject object, MotionEvent event, float relativeX, float relativeY);
    protected abstract void initialize(DioramaObject object);
    protected abstract void deinitialize(DioramaObject object);
}
