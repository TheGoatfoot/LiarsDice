package com.everhardsoft.bullet;

import com.everhardsoft.bullet.shape.BoxShape;
import com.everhardsoft.bullet.shape.CollisionShape;

import org.joml.Vector3f;

import java.util.concurrent.Semaphore;

/**
 * Created by faisa on 12/2/2016.
 */

public class RigidBody extends BulletObject {
    static {
        System.loadLibrary("bullet-wrapper-lib");
    }
    private native boolean isExistRigidBody(long rigidBodyPointer);
    private native long createRigidBody(long collisionShapePointer, float mass,
                                        float quatX, float quatY, float quatZ, float quatW,
                                        float x, float y, float z);
    private native void destroyRigidBody(long rigidBodyPointer);
    private native void getTransformRigidBody(long rigidBodyPointer, Transform transform);
    private native void getLinearVelocityRigidBody(long rigidBodyPointer, Vector3f linearVelocity);
    private native int getActivationStateRigidBody(long rigidBodyPointer);
    private native void activateRigidBody(long rigidBodyPointer, boolean activate);
    private native void applyTorqueRigidBody(long rigidBodyPointer, float x, float y, float z);
    private native void applyCentralImpulseRigidBody(long rigidBodyPointer, float x, float y, float z);

    public static final int ACTIVE_TAG = 1;
    public static final int ISLAND_SLEEPING = 2;
    public static final int WANTS_DEACTIVATION = 3;
    public static final int DISABLE_DEACTIVATION = 4;
    public static final int DISABLE_SIMULATION = 5;

    protected Transform transform = new Transform();
    protected Vector3f linearVelocity = new Vector3f();

    protected Semaphore edit = new Semaphore(1, true);

    public void getTransform(Transform transform) {
        transform.origin.set(this.transform.origin);
        transform.rotation.set(this.transform.rotation);
    }

    public void getLinearVelocity(Vector3f linearVelocity) {
        linearVelocity.set(this.linearVelocity);
    }

    public int getActivationState() {
        return getActivationStateRigidBody(pointer);
    }

    public void activate(boolean activate) {
        activateRigidBody(pointer, activate);
    }

    public void applyTorque(Vector3f torque) {
        applyTorqueRigidBody(pointer, torque.x, torque.y, torque.z);
    }

    public void applyCentralImpulse(Vector3f impulse) {
        applyCentralImpulseRigidBody(pointer, impulse.x, impulse.y, impulse.z);
    }

    public void update() {
        edit.acquireUninterruptibly();
        if(isExistRigidBody(pointer)) {
            getTransformRigidBody(pointer, transform);
            getLinearVelocityRigidBody(pointer, linearVelocity);
        }
        edit.release();
    }

    public void create(CollisionShape collisionShape, float mass,
                       float quatX, float quatY, float quatZ, float quatW,
                       float x, float y, float z) {
        if(isCreated())
            return;
        pointer = createFunction(collisionShape, mass, quatX, quatY, quatZ, quatW, x, y, z);
        update();
    }

    @Override
    protected long createFunction() {
        BoxShape box = new BoxShape();
        box.create(1f, 1f, 1f);
        return createFunction(new BoxShape(), 1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f);
    }
    protected long createFunction(CollisionShape collisionShape, float mass,
                                  float quatX, float quatY, float quatZ, float quatW,
                                  float x, float y, float z) {
        return createRigidBody(collisionShape.getPointer(), mass, quatX, quatY, quatZ, quatW, x, y, z);
    }

    @Override
    protected void destroyFunction() {
        edit.acquireUninterruptibly();
        destroyRigidBody(pointer);
        edit.release();
    }
}
