package com.everhardsoft.diorama.util;

import com.everhardsoft.diorama.world.DioramaWorld;
import com.everhardsoft.diorama.world.object.DioramaCameraObject;

import org.joml.Intersectionf;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Created by faisa on 12/4/2016.
 */

public class DioramaPickerRay {
    protected DioramaWorld world;
    protected Vector4f rayStartNDC = new Vector4f();
    protected Vector4f rayEndNDC = new Vector4f();

    protected Vector3f origin = new Vector3f();
    protected Vector3f direction = new Vector3f();

    public DioramaPickerRay(DioramaWorld world) {
        this.world = world;
    }
    public DioramaPickerRay(DioramaWorld world, float x, float y) {
        this.world = world;
        setRay(x, y);
    }

    protected Matrix4f inverseMatrix = new Matrix4f();
    protected float rayX;
    protected float rayY;
    protected DioramaCameraObject camera;
    protected Matrix4f projectionMatrix = new Matrix4f();
    protected Matrix4f viewMatrix = new Matrix4f();

    public void setRay(float x, float y) {
        camera = world.getMainCamera();

        projectionMatrix.set(camera.getProjectionMatrix());
        viewMatrix.set(camera.getViewMatrix());
        /*
        rayX = ((x / ((float) world.getSurfaceView().getRenderWidth())) - .5f) * 2f;
        rayY = ((y / ((float) world.getSurfaceView().getRenderHeight())) - .5f) * 2f;

        rayStartNDC.set(rayX, rayY, -1f, 1f);
        rayEndNDC.set(rayX, rayY, 0f, 1f);


        inverseMatrix.set(new Matrix4f(projectionMatrix).mul(viewMatrix)).invert();

        Vector4f screenPos = new Vector4f(rayX, -rayY, 1f, 1f);
        Vector4f worldPos = new Vector4f(screenPos).mul(inverseMatrix);

        origin.set(camera.origin);
        direction.set(new Vector3f(worldPos.x, worldPos.y, worldPos.z).normalize());
        */

        int[] viewport = {0, 0, world.getSurfaceView().getRenderWidth(), world.getSurfaceView().getRenderHeight()};
        new Matrix4f(projectionMatrix)
                .mul(viewMatrix)
                .unprojectRay(x, world.getSurfaceView().getRenderHeight()- y, viewport, origin, direction);

        direction.normalize();

    }
    public Vector3f getDirection() {
        return direction;
    }
    public Vector3f getOrigin() {
        return origin;
    }
}
