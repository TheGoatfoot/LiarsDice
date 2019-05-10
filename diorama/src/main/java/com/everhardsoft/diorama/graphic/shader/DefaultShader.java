package com.everhardsoft.diorama.graphic.shader;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import com.everhardsoft.diorama.R;
import com.everhardsoft.diorama.graphic.DioramaMesh;
import com.everhardsoft.diorama.graphic.DioramaMeshData;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by faisa on 11/2/2016.
 */

public class DefaultShader extends DioramaShader {
    protected int vertexHandle = -1;
    protected int objectColorHandle = -1;
    protected int normalHandle = -1;
    protected int projectionHandle = -1;
    protected int cameraPositionHandle = -1;
    protected int viewHandle = -1;
    protected int modelHandle = -1;
    protected int textureHandle = -1;
    protected int textureUVHandle = -1;
    protected final float[] color = {
            1f, 1f, 1f
    };
    protected final Resources resources;
    public DefaultShader(Context context) {
        resources = context.getResources();
    }
    private float[] modelMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    @Override
    public void update(Vector3f cameraOrigin, Matrix4f viewMatrix, Matrix4f projectionMatrix) {
        viewMatrix.get(this.viewMatrix);
        projectionMatrix.get(this.projectionMatrix);
        GLES20.glUniform3f(cameraPositionHandle, cameraOrigin.x, cameraOrigin.y, cameraOrigin.z);
        GLES20.glUniformMatrix4fv(viewHandle, 1, false, this.viewMatrix, 0);
        GLES20.glUniformMatrix4fv(projectionHandle, 1, false, this.projectionMatrix, 0);
    }
    @Override
    public void drawFunction(DioramaMesh dioramaMesh, Matrix4f modelMatrix) {
        GLES20.glUseProgram(program);

        GLES20.glUniform3fv(objectColorHandle, 1, color, 0);

        dioramaMesh.getMeshData().position(0);
        GLES20.glEnableVertexAttribArray(vertexHandle);
        GLES20.glVertexAttribPointer(vertexHandle, dioramaMesh.POSITION_SIZE, GLES20.GL_FLOAT, false, dioramaMesh.STRIDE, dioramaMesh.getMeshData());

        dioramaMesh.getMeshData().position(dioramaMesh.POSITION_SIZE);
        GLES20.glEnableVertexAttribArray(textureUVHandle);
        GLES20.glVertexAttribPointer(textureUVHandle, dioramaMesh.UV_SIZE, GLES20.GL_FLOAT, false, dioramaMesh.STRIDE, dioramaMesh.getMeshData());

        dioramaMesh.getMeshData().position(dioramaMesh.POSITION_SIZE + dioramaMesh.UV_SIZE);
        GLES20.glEnableVertexAttribArray(normalHandle);
        GLES20.glVertexAttribPointer(normalHandle, dioramaMesh.NORMAL_SIZE, GLES20.GL_FLOAT, false, dioramaMesh.STRIDE, dioramaMesh.getMeshData());

        modelMatrix.get(this.modelMatrix);
        GLES20.glUniformMatrix4fv(modelHandle, 1, false, this.modelMatrix, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture);
        GLES20.glUniform1i(textureHandle, 0);

        GLES20.glDrawElements(GLES20.GL_TRIANGLES, dioramaMesh.getMeshDataDrawOrderCount(), GLES20.GL_UNSIGNED_SHORT, dioramaMesh.getMeshDataDrawOrder());
    }
    @Override
    protected void bindHandle() {
        vertexHandle = GLES20.glGetAttribLocation(program, "position");
        normalHandle = GLES20.glGetAttribLocation(program, "normal");
        textureUVHandle = GLES20.glGetAttribLocation(program, "textureUV");

        cameraPositionHandle = GLES20.glGetUniformLocation(program, "cameraPosition");
        modelHandle = GLES20.glGetUniformLocation(program, "model");
        viewHandle = GLES20.glGetUniformLocation(program, "view");
        projectionHandle = GLES20.glGetUniformLocation(program, "projection");
        textureHandle = GLES20.glGetUniformLocation(program, "texture");
        objectColorHandle = GLES20.glGetUniformLocation(program, "objectColor");
    }
    @Override
    protected String getVertexSource() {
        return resources.getString(R.string.shader_vertex_normal);
    }
    @Override
    protected String getFragmentSource() {
        return resources.getString(R.string.shader_fragment_normal);
    }
}
