package com.everhardsoft.bullet.shape;

/**
 * Created by faisa on 12/2/2016.
 */

public class BoxShape extends CollisionShape {
    static {
        System.loadLibrary("bullet-wrapper-lib");
    }
    private native long createBoxShape(float x, float y, float z);

    @Override
    protected long createFunction(float... floats) {
        return createBoxShape(floats[0], floats[1], floats[2]);
    }
}
