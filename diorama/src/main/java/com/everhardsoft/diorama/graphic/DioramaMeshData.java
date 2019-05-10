package com.everhardsoft.diorama.graphic;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by faisa on 12/14/2016.
 */

public class DioramaMeshData {
    private FloatBuffer vertexData;
    private FloatBuffer textureUVData;
    private FloatBuffer normalData;
    public String material;
    private ShortBuffer dataDrawOrder;
    public int dataDrawOrderCount;
    public ShortBuffer getDataDrawOrder() {
        return dataDrawOrder;
    }
    public void setDataDrawOrder(ShortBuffer dataDrawOrder) {
        this.dataDrawOrder = dataDrawOrder;
    }
}
