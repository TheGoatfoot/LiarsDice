package com.everhardsoft.diorama.world.object.easer;

/**
 * Created by faisa on 11/4/2016.
 */

public class DioramaLinearInterpolationEaser extends DioramaEaser {
    @Override
    protected float getValue(float t, float d, float delta) {
        return t + delta * (d-t);
    }
}
