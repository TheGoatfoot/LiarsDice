package com.everhardsoft.diorama.world.object;

import android.view.MotionEvent;

import com.everhardsoft.diorama.world.DioramaWorld;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaObject {
    protected ConcurrentLinkedHashMap<DioramaObjectComponent, Object> dioramaObjectComponents = new ConcurrentLinkedHashMap.Builder<DioramaObjectComponent, Object>()
            .maximumWeightedCapacity(1024)
            .build();
    public String tag = "";
    public Vector3f origin = new Vector3f();
    public Vector3f scale = new Vector3f(1f, 1f, 1f);
    public Quaternionf quaternion = new Quaternionf();
    public final DioramaWorld world;
    protected Matrix4f modelMatrix = new Matrix4f();
    public DioramaObject(DioramaWorld world) {
        this.world = world;
    }
    public void update(float delta) {
        refreshModelMatrix();
        for(DioramaObjectComponent component: dioramaObjectComponents.keySet())
            component.update(this, delta);
    }
    public void touchEvent(MotionEvent event, float relativeX, float relativeY) {
        for(DioramaObjectComponent component: dioramaObjectComponents.keySet())
            component.touchEvent(this, event, relativeX, relativeY);
    }
    public <T> T getComponent(Class<T> componentClass) {
        for(DioramaObjectComponent component: dioramaObjectComponents.keySet())
            if(componentClass.isInstance(component))
                return (T) component;
        return null;
    }
    public <T> T getComponent(Class<T> componentClass, String tag) {
        for(DioramaObjectComponent component: dioramaObjectComponents.keySet())
            if(componentClass.isInstance(component) && component.tag.equals(tag))
                return (T) component;
        return null;
    }
    public <T> ArrayList<T> getComponents(Class<T> componentClass) {
        ArrayList<T> components = new ArrayList<>();
        for(DioramaObjectComponent component:dioramaObjectComponents.keySet())
            if(componentClass.isInstance(component))
                components.add((T) component);
        return components;
    }
    public <T> ArrayList<T> getComponents(Class<T> componentClass, String tag) {
        ArrayList<T> components = new ArrayList<>();
        for(DioramaObjectComponent component:dioramaObjectComponents.keySet())
            if(componentClass.isInstance(component) && component.tag.equals(tag))
                components.add((T) component);
        return components;
    }
    public void addComponent(DioramaObjectComponent component) {
        component.added(this);
        dioramaObjectComponents.put(component, world.DUMMY);
    }
    public void removeComponent(DioramaObjectComponent component) {
        component.removed(this);
        dioramaObjectComponents.remove(component);
    }
    public void removed() {
        for(DioramaObjectComponent component: dioramaObjectComponents.keySet())
            component.removed(this);
        dioramaObjectComponents.clear();
    }
    protected void refreshModelMatrix() {
        modelMatrix.identity()
                .translate(origin.x, origin.y, origin.z)
                .rotate(quaternion)
                .scale(scale.x, scale.y, scale.z);
    }
    public Matrix4f getModelMatrix() {
        return modelMatrix;
    }
}
