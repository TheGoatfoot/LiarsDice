package com.everhardsoft.diorama;

import android.content.Context;
import android.view.MotionEvent;

import com.everhardsoft.diorama.graphic.event.DioramaSurfaceViewEvent;
import com.everhardsoft.diorama.graphic.view.DioramaSurfaceView;
import com.everhardsoft.diorama.rule.DioramaRule;
import com.everhardsoft.diorama.world.DioramaWorld;

/**
 * Created by faisa on 11/1/2016.
 */

public class Diorama implements DioramaSurfaceViewEvent{
    protected final DioramaSurfaceView surfaceView;
    protected final DioramaRule rule;
    protected final Context context;
    protected DioramaWorld world;
    protected int[] surfaceViewScreenLocation = new int[2];
    public Diorama(Context context, DioramaSurfaceView surfaceView, DioramaRule rule) {
        this.context = context;
        this.rule = rule;
        this.surfaceView = surfaceView;
        surfaceView.setEvent(this);

        world = new DioramaWorld(surfaceView, rule);
        rule.setGameWorld(world);
        rule.setContext(context);
    }
    @Override
    public void onSurfaceChanged() {
        surfaceView.getLocationOnScreen(surfaceViewScreenLocation);
        world.surfaceChanged();
    }
    @Override
    public void onSurfaceCreated() {
        rule.onSurfaceCreated();
        if(!world.isStarted())
            world.start();
    }
    @Override
    public void onSurfaceDestroyed() {
        rule.onSurfaceDestroyed();
    }
    @Override
    public void onTouchEvent(MotionEvent e) {
        world.touchEvent(e, e.getRawX() - (float) surfaceViewScreenLocation[0], e.getRawY() - (float) surfaceViewScreenLocation[1]);
    }
    @Override
    public void onPause() {
        world.pause();
    }
    @Override
    public void onResume() {
        world.resume();
    }
    @Override
    public void onDrawFrame() {
        world.update();
    }

    public void stop() {
        world.stop();
    }
}
