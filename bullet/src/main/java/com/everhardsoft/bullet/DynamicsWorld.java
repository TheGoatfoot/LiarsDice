package com.everhardsoft.bullet;

/**
 * Created by faisa on 12/2/2016.
 */

public class DynamicsWorld extends BulletObject{
    static {
        System.loadLibrary("bullet-wrapper-lib");
    }
    private native long createWorld();
    private native void destroyWorld(long worldPointer);
    private native boolean isExistWorld(long worldPointer);
    private native void setWorldGravity(long worldPointer, float x, float y, float z);
    private native void stepWorld(long worldPointer, float timeStep);
    private native void addRigidBodyWorld(long worldPointer, long rigidBodyPointer);
    private native void removeRigidBodyWorld(long worldPointer, long rigidBodyPointer);

    public void step(float timeStep) {
        if(isExistWorld(pointer))
            stepWorld(pointer, timeStep);
    }
    public void setGravity(float x, float y, float z) {
        setWorldGravity(pointer, x, y, z);
    }
    public void addRigidBody(RigidBody rigidBody) {
        addRigidBodyWorld(pointer, rigidBody.getPointer());
    }
    public void removeRigidBody(RigidBody rigidBody) {
        removeRigidBodyWorld(pointer, rigidBody.getPointer());
    }

    @Override
    protected long createFunction() {
        return createWorld();
    }

    @Override
    protected void destroyFunction() {
        destroyWorld(pointer);
    }
}
