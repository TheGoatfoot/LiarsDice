package com.everhardsoft.diorama.world.object;

import com.everhardsoft.diorama.world.DioramaWorld;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaCameraObject extends DioramaObject {
    public static final int PERSPECTIVE = 0;
    public static final int ORTHOGONAL = 1;
    protected Matrix4f viewMatrix = new Matrix4f();
    protected Matrix4f projectionMatrix = new Matrix4f();
    protected int projectionMode = PERSPECTIVE;
    protected float orthogonalFrame = 5f;
    public float far = 100f;
    public float near = 1f;
    protected int width = 0;
    protected int height = 0;
    public Vector3f lookAt = new Vector3f();
    public Vector3f up = new Vector3f(0f, 1f, 0f);
    public DioramaCameraObject(DioramaWorld dioramaWorld) {
        super(dioramaWorld);
        updateSize();
    }
    public void updateSize() {
        this.width = world.getSurfaceView().getRenderWidth();
        this.height = world.getSurfaceView().getRenderHeight();

        updateProjection();
    }
    public Matrix4f getViewMatrix() {
        return viewMatrix.identity().lookAt(origin,lookAt,up);
    }
    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }
    public void updateProjection() {
        float ratio = ((float) width/height);
        switch (projectionMode) {
            case PERSPECTIVE:
                projectionMatrix.identity().perspective((float) Math.toRadians(90f), ratio, near, far);
                break;
            case ORTHOGONAL:
                projectionMatrix.identity().ortho(
                        -ratio * orthogonalFrame,
                        ratio * orthogonalFrame,
                        -1 * orthogonalFrame,
                        1 * orthogonalFrame,
                        near, far);
                break;
            default:
                setProjection(PERSPECTIVE);
        }
    }
    public void setProjection(int projection) {
        projectionMode = projection;
        updateProjection();
    }
    public void setOrthogonalFrame(float orthogonalFrame) {
        this.orthogonalFrame = orthogonalFrame;
        updateProjection();
    }
    public float getOrthogonalFrame() {
        return orthogonalFrame;
    }
    public int getProjectionMode() {
        return projectionMode;
    }
}
