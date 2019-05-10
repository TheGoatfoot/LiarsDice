package com.everhardsoft.diorama.world.object.easer;

import org.joml.Vector3f;

/**
 * Created by faisa on 11/4/2016.
 */

public abstract class DioramaEaser {
    protected abstract float getValue(float t, float d, float delta);
    public void setValue(Vector3f target, Vector3f destination, float delta) {
        target.x = getValue(target.x, destination.x, delta);
        target.y = getValue(target.y, destination.y, delta);
        target.z = getValue(target.z, destination.z, delta);
    }
}
