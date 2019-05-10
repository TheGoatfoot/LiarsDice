package com.everhardsoft.bullet.shape;

import com.everhardsoft.bullet.BulletObject;

/**
 * Created by faisa on 12/2/2016.
 */

public abstract class CollisionShape extends BulletObject{
    static {
        System.loadLibrary("bullet-wrapper-lib");
    }
    private native void destroyCollisionShape(long collisionShapePointer);

    public void create(float... floats) {
        if(isCreated())
            return;
        pointer = createFunction(floats);
    }
    @Override
    protected long createFunction() {
        return createFunction(0f);
    }
    protected abstract long createFunction(float... floats);

    @Override
    protected void destroyFunction() {
        destroyCollisionShape(pointer);
    }
}
