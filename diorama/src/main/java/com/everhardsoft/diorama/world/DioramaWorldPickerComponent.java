package com.everhardsoft.diorama.world;

import android.view.MotionEvent;

import com.everhardsoft.diorama.util.DioramaPickerRay;
import com.everhardsoft.diorama.world.object.DioramaBoxPickComponent;
import com.everhardsoft.diorama.world.object.DioramaObject;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by faisa on 12/4/2016.
 */

public class DioramaWorldPickerComponent extends DioramaWorldComponent {
    protected Queue<float[]> touchQueue = new LinkedList<>();
    protected float[] currentTouchQueue = null;
    protected Queue<PickInfo> pickInfos = new LinkedList<>();

    public float tMinDefault = 0f;
    public float tMaxDefault = 100000f;

    public boolean active = true;

    private float tMin = tMinDefault;
    private float tMax = tMaxDefault;

    @Override
    public void addObject(DioramaWorld world, DioramaObject object) {
    }
    @Override
    public void removeObject(DioramaWorld world, DioramaObject object) {
    }

    protected DioramaBoxPickComponent tempComponent;
    protected DioramaPickerRay pickerRay;
    protected PickInfo tempPickInfo;
    @Override
    public void update(DioramaWorld world, float delta) {
        if(tempPickInfo != null) {
            pickInfos.add(tempPickInfo);
            tempPickInfo = null;
        }
        currentTouchQueue = touchQueue.poll();
        if(!active || currentTouchQueue == null)
            return;
        tempPickInfo = new PickInfo();
        pickerRay.setRay(currentTouchQueue[0], currentTouchQueue[1]);
        tempPickInfo.pickerRay = pickerRay;
    }
    @Override
    public void touchEvent(DioramaWorld world, MotionEvent event, float relativeX, float relativeY) {
        if(active)
            touchQueue.add(new float[]{relativeX, relativeY});
    }
    @Override
    protected void initialize(DioramaWorld world) {
        pickerRay = new DioramaPickerRay(world);
    }
    @Override
    protected void deinitialize(DioramaWorld world) {
    }

    public void checkIntersect(DioramaObject object, DioramaBoxPickComponent dioramaBoxPickComponent) {
        if(!active || tempPickInfo == null)
            return;
        if(intersect(object, dioramaBoxPickComponent)) {
            tempPickInfo.intersectedObjects.add(object);
            if(tempPickInfo.pickedObject == null) {
                tempPickInfo.pickedObject = object;
                tempPickInfo.pickedObjectDistance = tMin;
            } else if(tempPickInfo.pickedObjectDistance > tMin) {
                tempPickInfo.pickedObject = object;
                tempPickInfo.pickedObjectDistance = tMin;
            }
        }
    }
    protected Matrix4f modelMatrix = new Matrix4f();
    protected Vector3f objectWorldSpace = new Vector3f();
    protected Vector3f delta = new Vector3f();
    protected boolean intersect(DioramaObject object, DioramaBoxPickComponent boxPickComponent) {
        if(pickerRay == null)
            return false;
        modelMatrix.set(object.getModelMatrix());
        objectWorldSpace.set(modelMatrix.m30(), modelMatrix.m31(), modelMatrix.m32());
        delta.set(new Vector3f().set(objectWorldSpace).sub(pickerRay.getOrigin()));
        tMin = tMinDefault;
        tMax = tMaxDefault;
        return  planeIntersect(
                        pickerRay, new Vector3f(modelMatrix.m00(), modelMatrix.m01(), modelMatrix.m02()),
                        delta, boxPickComponent.boundingBoxMin.x, boxPickComponent.boundingBoxMax.x) &&
                planeIntersect(
                        pickerRay, new Vector3f(modelMatrix.m10(), modelMatrix.m11(), modelMatrix.m12()),
                        delta, boxPickComponent.boundingBoxMin.y, boxPickComponent.boundingBoxMax.y) &&
                planeIntersect(
                        pickerRay, new Vector3f(modelMatrix.m20(), modelMatrix.m21(), modelMatrix.m22()),
                        delta, boxPickComponent.boundingBoxMin.z, boxPickComponent.boundingBoxMax.z);

    }
    protected float e;
    protected float f;
    protected float t1;
    protected float t2;
    protected float w;
    protected boolean planeIntersect(DioramaPickerRay pickerRay, Vector3f axis, Vector3f delta,
                                     float aabbMin, float aabbMax) {
        e = axis.dot(delta);
        f = pickerRay.getDirection().dot(axis);

        if(Math.abs(f) > .001f) {
            t1 = (e + aabbMin) / f;
            t2 = (e + aabbMax) / f;
            if (t1 > t2) {
                w = t1;
                t1 = t2;
                t2 = w;
            }
            if (t2 < tMax)
                tMax = t2;
            if (t1 > tMin)
                tMin = t1;
            if (tMax < tMin)
                return false;
        } else {
            if(-e+aabbMin > 0.0f || -e+aabbMax < 0.0f)
                return false;
        }
        return true;
    }
    public PickInfo pollInfo() {
        return pickInfos.poll();
    }
    public class PickInfo {
        public final HashSet<DioramaObject> intersectedObjects = new HashSet<>();
        public DioramaObject pickedObject = null;
        public float pickedObjectDistance;
        public DioramaPickerRay pickerRay;
        public Vector3f getNearestIntersectionPoint() {
            return new Vector3f(pickerRay.getOrigin()).add(new Vector3f(pickerRay.getDirection()).mul(pickedObjectDistance));
        }
    }
}
