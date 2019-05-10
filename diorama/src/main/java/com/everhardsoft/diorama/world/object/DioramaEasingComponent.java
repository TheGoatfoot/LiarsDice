package com.everhardsoft.diorama.world.object;

import android.view.MotionEvent;

import com.everhardsoft.diorama.world.object.easer.DioramaEaser;

import org.joml.Vector3f;

/**
 * Created by faisa on 11/3/2016.
 */

public class DioramaEasingComponent extends DioramaObjectComponent {
    public Vector3f destination = new Vector3f();
    public float easingSpeed = 1f;
    protected Vector3f target = null;
    protected DioramaEaser easer = null;
    public DioramaEasingComponent(DioramaEaser easer, Vector3f target) {
        setEaser(easer);
        setTarget(target);
    }
    public void setEaser(DioramaEaser easer) {
        this.easer = easer;
    }
    public void setTarget(Vector3f target) {
        this.target = target;
    }
    @Override
    public void update(DioramaObject object, float delta) {
        easer.setValue(target, destination, delta * easingSpeed);
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
