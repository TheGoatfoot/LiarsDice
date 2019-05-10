package com.everhardsoft.diorama.world;

import android.content.Context;
import android.view.MotionEvent;

import com.everhardsoft.diorama.graphic.shader.DioramaShader;
import com.everhardsoft.diorama.world.object.DioramaCameraObject;
import com.everhardsoft.diorama.world.object.DioramaObject;

import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by faisa on 12/10/2016.
 */

public class DioramaShaderWorldComponent extends DioramaWorldComponent {
    protected ConcurrentLinkedQueue<DioramaShader> shaders = new ConcurrentLinkedQueue<>();
    @Override
    public void addObject(DioramaWorld world, DioramaObject object) {
    }
    @Override
    public void removeObject(DioramaWorld world, DioramaObject object) {
    }
    DioramaCameraObject mainCamera;
    @Override
    public void update(DioramaWorld world, float delta) {
        mainCamera = world.getMainCamera();
        for(DioramaShader shader: shaders)
            shader.update(mainCamera.origin ,mainCamera.getViewMatrix(), mainCamera.getProjectionMatrix());
    }
    @Override
    public void touchEvent(DioramaWorld world, MotionEvent event, float relativeX, float relativeY) {
    }
    @Override
    protected void initialize(DioramaWorld world) {
    }
    @Override
    protected void deinitialize(DioramaWorld world) {
    }
    public void addShader(DioramaShader shader) {
        shaders.add(shader);
    }
}
