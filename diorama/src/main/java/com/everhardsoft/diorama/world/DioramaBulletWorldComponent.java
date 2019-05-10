package com.everhardsoft.diorama.world;

import android.view.MotionEvent;

import com.everhardsoft.bullet.DynamicsWorld;
import com.everhardsoft.bullet.RigidBody;
import com.everhardsoft.diorama.world.object.DioramaBulletRigidBodyComponent;
import com.everhardsoft.diorama.world.object.DioramaObject;

import java.util.concurrent.Semaphore;

/**
 * Created by faisa on 11/3/2016.
 */

public class DioramaBulletWorldComponent extends DioramaWorldComponent {
    protected DynamicsWorld dynamicsWorld;
    protected Semaphore edit = new Semaphore(1, true);
    @Override
    public void addObject(DioramaWorld world, DioramaObject object) {
    }
    @Override
    public void removeObject(DioramaWorld world, DioramaObject object) {
    }
    public float deltaMultiplier = 1f;
    protected float delta;
    protected float fixedTimeStep = 1f/60f;
    @Override
    public void update(DioramaWorld world, float delta) {
        if(!world.isPaused()) {
            this.delta = delta * deltaMultiplier;
            if(this.delta < fixedTimeStep)
                this.delta = fixedTimeStep;
            edit.acquireUninterruptibly();
            dynamicsWorld.step(this.delta);
            edit.release();
        }
    }
    @Override
    public void touchEvent(DioramaWorld world, MotionEvent event, float relativeX, float relativeY) {
    }
    @Override
    protected void initialize(DioramaWorld world) {
        dynamicsWorld = new DynamicsWorld();
        dynamicsWorld.create();
    }
    @Override
    protected void deinitialize(DioramaWorld world) {
        edit.acquireUninterruptibly();
        dynamicsWorld.destroy();
        edit.release();
    }
    protected DioramaBulletRigidBodyComponent rigidBodyComponent;
    public DioramaBulletRigidBodyComponent makePhysicsObject(DioramaObject object, RigidBody rigidBody) {
        edit.acquireUninterruptibly();
        dynamicsWorld.addRigidBody(rigidBody);
        object.addComponent(rigidBodyComponent = new DioramaBulletRigidBodyComponent(this, rigidBody));
        edit.release();
        return rigidBodyComponent;
    }
    public void destroyPhysicsObject(RigidBody rigidBody) {
        edit.acquireUninterruptibly();
        dynamicsWorld.removeRigidBody(rigidBody);
        rigidBody.destroy();
        edit.release();
    }
    public DynamicsWorld getDynamicsWorld() {
        return dynamicsWorld;
    }
}
