package com.everhardsoft.diorama.world;

import android.view.MotionEvent;

import com.everhardsoft.diorama.exception.DioramaWorldException;
import com.everhardsoft.diorama.graphic.view.DioramaSurfaceView;
import com.everhardsoft.diorama.rule.DioramaRule;
import com.everhardsoft.diorama.util.DeltaTime;
import com.everhardsoft.diorama.world.object.DioramaCameraObject;
import com.everhardsoft.diorama.world.object.DioramaObject;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaWorld {
    protected ConcurrentLinkedHashMap<DioramaObject, Object> dioramaObjects = new ConcurrentLinkedHashMap.Builder<DioramaObject, Object>()
            .maximumWeightedCapacity(1024)
            .build();
    protected ConcurrentLinkedHashMap<DioramaWorldComponent, Object> worldComponents = new ConcurrentLinkedHashMap.Builder<DioramaWorldComponent, Object>()
            .maximumWeightedCapacity(1024)
            .build();
    protected DioramaCameraObject mainCamera = null;
    protected DioramaSurfaceView surfaceView = null;
    protected DioramaRule rule = null;
    protected DeltaTime deltaTime = new DeltaTime();
    protected boolean started = false;
    protected boolean paused = false;
    public Object DUMMY = new Object();
    protected Semaphore rendering = new Semaphore(1, true);
    public DioramaWorld(DioramaSurfaceView surfaceView, DioramaRule rule) {
        this.surfaceView = surfaceView;
        this.rule = rule;
    }
    public DioramaCameraObject addMainCamera() {
        mainCamera = new DioramaCameraObject(this);
        addObject(mainCamera);
        return mainCamera;
    }
    public void start() {
        if(started)
            throw new DioramaWorldException("World is already started.");
        addMainCamera();
        rule.onStart();
        _start();
    }
    public void restart() {
        stop();
        addMainCamera();
        rule.onRestart();
        _start();
    }
    private void _start() {
        started = true;
        paused = false;
        deltaTime.setStart();
        surfaceView.requestRender();
        rendering.acquireUninterruptibly();
    }
    public void stop() {
        started = false;
        rendering.acquireUninterruptibly();

        paused = false;
        rule.onStop();

        for(DioramaObject object:dioramaObjects.keySet())
            object.removed();
        for(DioramaWorldComponent component:worldComponents.keySet())
            component.removed(this);

        mainCamera = null;
        dioramaObjects.clear();
        worldComponents.clear();

        rendering.release();
    }
    public void pause() {
        if(!started || paused)
            return;
        paused = true;
        rule.onPause();
    }
    public void resume() {
        if(!started || !paused)
            return;
        paused = false;
        deltaTime.setStart();
        rule.onResume();
    }
    public <T> T getComponent(Class<T> componentClass) {
        for(DioramaWorldComponent component: worldComponents.keySet())
            if(componentClass.isInstance(component))
                return (T) component;
        return null;
    }
    public <T> T getComponent(Class<T> componentClass, String tag) {
        for(DioramaWorldComponent component: worldComponents.keySet())
            if(componentClass.isInstance(component) && component.tag.equals(tag))
                return (T) component;
        return null;
    }
    public <T> ArrayList<T> getComponents(Class<T> componentClass) {
        ArrayList<T> components = new ArrayList<>();
        for(DioramaWorldComponent component: worldComponents.keySet())
            if(componentClass.isInstance(component))
                components.add((T) component);
        return components;
    }
    public <T> ArrayList<T> getComponents(Class<T> componentClass, String tag) {
        ArrayList<T> components = new ArrayList<>();
        for(DioramaWorldComponent component: worldComponents.keySet())
            if(componentClass.isInstance(component) && component.tag.equals(tag))
                components.add((T) component);
        return components;
    }
    /**/
    public <T> T getObject(Class<T> objectClass) {
        for(DioramaObject object: dioramaObjects.keySet())
            if(objectClass.isInstance(object))
                return (T) object;
        return null;
    }
    public <T> T getObject(Class<T> objectClass, String tag) {
        for(DioramaObject object: dioramaObjects.keySet())
            if(objectClass.isInstance(object) && object.tag.equals(tag))
                return (T) object;
        return null;
    }
    public Set<DioramaObject> getAllObject() {
        return dioramaObjects.keySet();
    }
    public <T> ArrayList<T> getObjects(Class<T> objectClass) {
        ArrayList<T> objects = new ArrayList<>();
        for(DioramaObject object: dioramaObjects.keySet())
            if(objectClass.isInstance(object))
                objects.add((T) object);
        return objects;
    }
    public <T> ArrayList<T> getObjects(Class<T> objectClass, String tag) {
        ArrayList<T> objects = new ArrayList<>();
        for(DioramaObject object: dioramaObjects.keySet())
            if(objectClass.isInstance(object) && object.tag.equals(tag))
                objects.add((T) object);
        return objects;
    }
    public void addComponent(DioramaWorldComponent component) {
        component.added(this);
        worldComponents.put(component, DUMMY);
    }
    public void removeComponent(DioramaWorldComponent component) {
        component.removed(this);
        worldComponents.remove(component);
    }
    public void addObject(DioramaObject object) {
        for(DioramaWorldComponent component:worldComponents.keySet())
            component.addObject(this, object);
        dioramaObjects.put(object, DUMMY);
    }
    public void removeObject(DioramaObject object) {
        for(DioramaWorldComponent component:worldComponents.keySet())
            component.removeObject(this, object);
        dioramaObjects.remove(object);
        object.removed();
    }
    protected float delta;
    public void update() {
        delta = deltaTime.getDelta();
        for(DioramaWorldComponent component:worldComponents.keySet())
            component.update(this, delta);
        for(DioramaObject object:dioramaObjects.keySet())
            object.update(delta);
        rule.onUpdate(delta);
        if(started)
            surfaceView.requestRender();
        else
            rendering.release();
    }
    public DioramaCameraObject getMainCamera() {
        return mainCamera;
    }
    public boolean setMainCamera(DioramaCameraObject camera) {
        if(dioramaObjects.containsKey(camera)) {
            mainCamera = camera;
            return true;
        }
        return false;
    }
    public DioramaSurfaceView getSurfaceView() {
        return surfaceView;
    }
    public boolean isPaused() {
        return paused;
    }
    public boolean isStarted() {
        return started;
    }
    public void surfaceChanged() {
        for(DioramaCameraObject camera: getObjects(DioramaCameraObject.class))
            camera.updateSize();
    }
    public void touchEvent(MotionEvent e, float relativeX, float relativeY) {
        for(DioramaWorldComponent component:worldComponents.keySet())
            component.touchEvent(this, e, relativeX, relativeY);
        for(DioramaObject object:dioramaObjects.keySet())
            object.touchEvent(e, relativeX, relativeY);
        rule.onTouchEvent(e, relativeX, relativeY);
    }
}
