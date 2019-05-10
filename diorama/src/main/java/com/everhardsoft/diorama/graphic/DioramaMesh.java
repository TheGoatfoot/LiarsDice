package com.everhardsoft.diorama.graphic;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaMesh {
    private FloatBuffer vertexData;
    private FloatBuffer textureUVData;
    private FloatBuffer normalData;
    private LinkedList<DioramaMeshData> meshesData = new LinkedList<>();
    private HashMap<String, DioramaMaterial> meshMaterials = new HashMap<>();

    public final int VERTEX_SIZE = 3;
    public final int UV_SIZE = 2;
    public final int NORMAL_SIZE = 3;

    public FloatBuffer getVertexData() {
        return vertexData;
    }
    public FloatBuffer getTextureUVData() {
        return textureUVData;
    }
    public FloatBuffer getNormalData() {
        return normalData;
    }

    public void setVertexData(FloatBuffer vertexData) {
        this.vertexData = vertexData;
    }
    public void setTextureUVData(FloatBuffer textureUVData) {
        this.textureUVData = textureUVData;
    }
    public void setNormalData(FloatBuffer normalData) {
        this.normalData = normalData;
    }

    public LinkedList<DioramaMeshData> getMeshesData() {
        return meshesData;
    }
    public HashMap<String, DioramaMaterial> getMeshMaterials() {
        return meshMaterials;
    }
}
