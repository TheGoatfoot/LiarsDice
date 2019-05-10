package com.everhardsoft.bullet.shape;

/**
 * Created by faisa on 12/2/2016.
 */

public class StaticPlaneShape extends CollisionShape{
    static {
        System.loadLibrary("bullet-wrapper-lib");
    }
    private native long createStaticPlaneShape(float x, float y, float z);

    @Override
    protected long createFunction(float... floats) {
        return createStaticPlaneShape(floats[0], floats[1], floats[2]);
    }
}
