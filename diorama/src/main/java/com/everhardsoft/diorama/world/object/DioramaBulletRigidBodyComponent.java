package com.everhardsoft.diorama.world.object;

import android.view.MotionEvent;

import com.everhardsoft.bullet.RigidBody;
import com.everhardsoft.bullet.Transform;
import com.everhardsoft.diorama.world.DioramaBulletWorldComponent;

/**
 * Created by faisa on 11/2/2016.
 */

public class DioramaBulletRigidBodyComponent extends DioramaObjectComponent {
    public final RigidBody rigidBody;
    protected final DioramaBulletWorldComponent bulletWorldComponent;
    protected Transform transform = new Transform();
    public DioramaBulletRigidBodyComponent(DioramaBulletWorldComponent bulletWorldComponent, RigidBody rigidBody) {
        this.bulletWorldComponent = bulletWorldComponent;
        this.rigidBody = rigidBody;
    }
    @Override
    public void update(DioramaObject object, float delta) {
        if(object.world.isPaused())
            return;
        rigidBody.update();
        rigidBody.getTransform(transform);
        object.quaternion.set(transform.rotation);
        object.origin.set(transform.origin);
    }
    @Override
    public void touchEvent(DioramaObject object, MotionEvent event, float relativeX, float relativeY) {
    }
    @Override
    protected void initialize(DioramaObject object) {
    }
    @Override
    protected void deinitialize(DioramaObject object) {
        bulletWorldComponent.destroyPhysicsObject(rigidBody);
    }
}
