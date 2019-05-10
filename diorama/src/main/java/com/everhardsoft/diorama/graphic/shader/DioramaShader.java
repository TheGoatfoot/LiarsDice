package com.everhardsoft.diorama.graphic.shader;

import android.opengl.GLES20;
import android.util.Log;

import com.everhardsoft.diorama.graphic.DioramaMesh;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Created by faisa on 11/1/2016.
 */

public abstract class DioramaShader {
    protected int program = -1;
    protected int vertexShader = -1;
    protected int fragmentShader = -1;
    protected int texture = -1;
    public void create() {
        destroy();
        vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getVertexSource());
        fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentSource());
        if(fragmentShader == -1 || vertexShader == -1) {
            destroy();
            return;
        }
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
        bindHandle();
    }
    public abstract void update(Vector3f cameraOrigin, Matrix4f viewMatrix, Matrix4f projectionMatrix);
    public void destroy() {
        if(program != -1)
            GLES20.glDeleteProgram(program);
        if(vertexShader != -1)
            GLES20.glDeleteShader(vertexShader);
        if(fragmentShader != -1)
            GLES20.glDeleteShader(fragmentShader);
        program = -1;
        vertexShader = -1;
        fragmentShader = -1;
    }
    public void setTexture(int texture) {
        this.texture = texture;
    }
    public void draw(DioramaMesh dioramaMesh, Matrix4f modelMatrix) {
        if(program == -1)
            return;
        drawFunction(dioramaMesh,modelMatrix);
    }
    public abstract void drawFunction(DioramaMesh dioramaMesh, Matrix4f modelMatrix);
    protected abstract void bindHandle();
    protected abstract String getVertexSource();
    protected abstract String getFragmentSource();
    private int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.e("sdd", GLES20.glGetShaderInfoLog(shader));
        if (compileStatus[0] == 0)
            return -1;
        return shader;
    }
}
