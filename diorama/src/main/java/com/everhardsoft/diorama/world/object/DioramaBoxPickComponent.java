package com.everhardsoft.diorama.world.object;

import android.view.MotionEvent;

import com.everhardsoft.diorama.world.DioramaWorldPickerComponent;

import org.joml.Vector3f;

/**
 * Created by faisa on 12/4/2016.
 */

public class DioramaBoxPickComponent extends DioramaObjectComponent {
    public final Vector3f boundingBoxMin = new Vector3f(-1f, -1f, -1f);
    public final Vector3f boundingBoxMax = new Vector3f(1f, 1f, 1f);
    @Override
    public void update(DioramaObject object, float delta) {
        for(DioramaWorldPickerComponent worldPickerComponent: object.world.getComponents(DioramaWorldPickerComponent.class))
            worldPickerComponent.checkIntersect(object, this);
    }
    @Override
    public void touchEvent(DioramaObject object, MotionEvent event, float relativeX, float relativeY) {
    }
    @Override
    protected void initialize(DioramaObject object) {
    }
    @Override
    protected void deinitialize(DioramaObject object) {
    }
}
