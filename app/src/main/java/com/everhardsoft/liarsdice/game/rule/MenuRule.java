package com.everhardsoft.liarsdice.game.rule;

import android.opengl.GLES20;
import android.view.MotionEvent;

import com.everhardsoft.bullet.RigidBody;
import com.everhardsoft.diorama.rule.DioramaRule;
import com.everhardsoft.diorama.world.DioramaBulletWorldComponent;
import com.everhardsoft.diorama.world.DioramaWorldPickerComponent;
import com.everhardsoft.diorama.world.object.DioramaBoxPickComponent;
import com.everhardsoft.diorama.world.object.DioramaBulletRigidBodyComponent;
import com.everhardsoft.diorama.world.object.DioramaCameraObject;
import com.everhardsoft.diorama.world.object.DioramaEasingComponent;
import com.everhardsoft.diorama.world.object.DioramaObject;
import com.everhardsoft.diorama.world.object.easer.DioramaLinearInterpolationEaser;
import com.everhardsoft.liarsdice.game.ObjectMaker;

import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import timber.log.Timber;

/**
 * Created by faisa on 11/4/2016.
 */

public class MenuRule extends DioramaRule {
    protected ObjectMaker objectMaker = new ObjectMaker();
    @Override
    public void onSurfaceCreated() {
        objectMaker.initialize(context);
    }
    @Override
    public void onSurfaceDestroyed() {
        objectMaker.deinitialize();
    }
    @Override
    public void onTouchEvent(MotionEvent e, float relativeX, float relativeY) {
    }
    @Override
    public void onUpdate(float delta) {
        DioramaWorldPickerComponent.PickInfo pickInfo = worldPickerComponent.pollInfo();
        if(pickInfo!=null) {
            if(pickInfo.pickedObject != null) {
                DioramaBulletRigidBodyComponent rigidBodyComponent = pickInfo.pickedObject.getComponent(DioramaBulletRigidBodyComponent.class);
                if(rigidBodyComponent != null) {
                    if(rigidBodyComponent.rigidBody.getActivationState() != RigidBody.ACTIVE_TAG)
                        rigidBodyComponent.rigidBody.activate(true);
                    rigidBodyComponent.rigidBody.applyCentralImpulse(new Vector3f(0f, 20f, 0f));
                }
                testObject.origin.set(pickInfo.getNearestIntersectionPoint());
            }
        }
    }
    @Override
    public void onStart() {
        setupScene();
        cameraMainMenu();
    }
    @Override
    public void onRestart() {
    }
    @Override
    public void onStop() {
    }
    @Override
    public void onPause() {
    }
    @Override
    public void onResume() {
    }
    private DioramaEasingComponent cameraPositionEasing;
    private DioramaEasingComponent cameraLookAtEasing;
    private DioramaWorldPickerComponent worldPickerComponent;
    private DioramaBulletWorldComponent bulletWorldComponent;

    private DioramaObject testObject;
    protected void setupScene() {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        world.addComponent(objectMaker.shaderWorldComponent);
        world.addComponent(bulletWorldComponent = new DioramaBulletWorldComponent());
        bulletWorldComponent.getDynamicsWorld().setGravity(0f, -10f, 0f);
        bulletWorldComponent.deltaMultiplier = 2f;
        world.addComponent(worldPickerComponent = new DioramaWorldPickerComponent());

        //MainMenu dice
        world.addObject(objectMaker.makeDice(world, bulletWorldComponent, new Vector3f(3f, 20f, 0f), new Quaternionf(), new Vector3f(0f, 100f, 0f)));
        world.addObject(objectMaker.makeDice(world, bulletWorldComponent, new Vector3f(0f, 20f, 3f), new Quaternionf(), new Vector3f(100f, -100f, 0f)));
        world.addObject(objectMaker.makeDice(world, bulletWorldComponent, new Vector3f(0f, 20f, 0f), new Quaternionf(), new Vector3f(0f, -100f, 0f)));

        //Multiplayer dice
        world.addObject(objectMaker.makeDiceMock(world, new Vector3f(-15f, 15f, 14.5f), new Quaternionf(new AxisAngle4f(45f, 1f, 0f, 0f))));
        world.addObject(testObject = objectMaker.makeDiceMock(world, new Vector3f(-15f, 15f, 17.5f), new Quaternionf(new AxisAngle4f(45f, 0f, 1f, 0f))));

        world.addObject(objectMaker.makeTable(world, bulletWorldComponent, new Vector3f(0f, 2f, 0f), new Vector3f(0f, 1f, 0f)));

        world.getMainCamera().origin.x = 12f;
        world.getMainCamera().origin.y = 12f;
        world.getMainCamera().origin.z = 12f;
        world.getMainCamera().lookAt.y = 2f;
        //world.getMainCamera().setProjection(DioramaCameraObject.ORTHOGONAL);

        cameraPositionEasing = new DioramaEasingComponent(new DioramaLinearInterpolationEaser(), world.getMainCamera().origin);
        cameraLookAtEasing = new DioramaEasingComponent(new DioramaLinearInterpolationEaser(), world.getMainCamera().lookAt);
        world.getMainCamera().addComponent(cameraPositionEasing);
        world.getMainCamera().addComponent(cameraLookAtEasing);

        DioramaObject obj = new DioramaObject(world);
        DioramaBoxPickComponent box = new DioramaBoxPickComponent();
        box.boundingBoxMin.set(-20, 0, -20);
        box.boundingBoxMax.set(20, 4, 20);
        obj.addComponent(box);

        world.addObject(obj);
    }
    public void cameraMainMenu() {
        if(cameraPositionEasing == null || cameraLookAtEasing == null)
            return;
        cameraPositionEasing.destination.set(8.5f,8.5f,8.5f);
        cameraPositionEasing.easingSpeed = 0.25f;

        cameraLookAtEasing.destination.set(-1f,0f,-3f);
        cameraLookAtEasing.easingSpeed = 1f;
    }
    public void cameraMultiplayer() {
        if(cameraPositionEasing == null || cameraLookAtEasing == null)
            return;
        cameraPositionEasing.destination.set(-12f,20f,16f);
        cameraPositionEasing.easingSpeed = 0.25f;

        cameraLookAtEasing.destination.set(-15f,15f,16f);
        cameraLookAtEasing.easingSpeed = 1f;
    }
}
