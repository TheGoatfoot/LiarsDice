package com.everhardsoft.diorama.graphic.view;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.everhardsoft.diorama.graphic.event.DioramaSurfaceViewEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer{
    protected DioramaSurfaceViewEvent event;
    protected int renderWidth = 0;
    protected int renderHeight = 0;
    public DioramaSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        setRenderer(this);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        event.onSurfaceDestroyed();
    }
    @Override
    public void onPause() {
        super.onPause();
        event.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        event.onResume();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.event.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        event.onSurfaceCreated();
    }
    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.renderWidth = width;
        this.renderHeight = height;
        event.onSurfaceChanged();
    }
    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
        event.onDrawFrame();
    }
    public int getRenderWidth() {
        return renderWidth;
    }
    public int getRenderHeight() {
        return renderHeight;
    }
    public void setEvent(DioramaSurfaceViewEvent event) {
        this.event = event;
    }
}
