package com.everhardsoft.diorama.world.object;

import android.view.MotionEvent;

import com.everhardsoft.diorama.graphic.DioramaMesh;
import com.everhardsoft.diorama.graphic.shader.DioramaShader;

/**
 * Created by faisa on 11/2/2016.
 */

public class DioramaDrawableComponent extends DioramaObjectComponent {
    protected DioramaMesh dioramaMesh = null;
    protected DioramaShader dioramaShader = null;
    protected int texture = -1;
    public boolean draw = true;
    public void setMesh(DioramaMesh dioramaMesh) {
        this.dioramaMesh = dioramaMesh;
    }
    public void setShader(DioramaShader dioramaShader) {
        this.dioramaShader = dioramaShader;
    }
    public void setTexture(int texture) {
        this.texture = texture;
    }
    @Override
    public void update(DioramaObject object, float delta) {
        if(dioramaMesh == null || dioramaShader == null || !draw)
            return;
        dioramaShader.setTexture(texture);
        dioramaShader.draw(dioramaMesh, object.getModelMatrix());
    }
    @Override
    public void touchEvent(DioramaObject object, MotionEvent event, float relativeX, float relativeY) {
    }
    @Override
    protected void initialize(DioramaObject object) {
    }
    @Override
    protected void deinitialize(DioramaObject object) {
    }
}
