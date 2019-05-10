package com.everhardsoft.diorama.loader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaObj {
    private ArrayList<Float> vertices = new ArrayList<>();
    private ArrayList<Float> textureUVs = new ArrayList<>();
    private ArrayList<Float> normals = new ArrayList<>();
    private ArrayList<short[][]> faces = new ArrayList<>();
    private final int shortSize = Short.SIZE * Byte.SIZE;
    private final int floatSize = Float.SIZE * Byte.SIZE;
    public DioramaObj(ArrayList<Float> vertices, ArrayList<Float> textureUVs, ArrayList<Float> normals, ArrayList<short[][]> faces) {
        this.vertices = vertices;
        this.textureUVs = textureUVs;
        this.normals = normals;
        this.faces = faces;
    }
    private float[] convertToFloatArray(ArrayList<Float> floats) {
        float[] output = new float[floats.size()];
        for(int i = 0; i < floats.size(); i++)
            output[i] = floats.get(i);
        return output;
    }
    private short[] convertToShortArray(ArrayList<Short> shorts) {
        short[] output = new short[shorts.size()];
        for(int i = 0; i < shorts.size(); i++)
            output[i] = shorts.get(i);
        return output;
    }
    private FloatBuffer convertToFloatBuffer(ArrayList<Float> floats) {
        float[] input = convertToFloatArray(floats);
        FloatBuffer output;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(floats.size() * floatSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        output = byteBuffer.asFloatBuffer();
        output.put(input);
        output.position(0);
        return output;
    }
    private ShortBuffer convertToShortBuffer(ArrayList<Short> shorts) {
        short[] input = convertToShortArray(shorts);
        ShortBuffer output;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(shorts.size() * shortSize);
        byteBuffer.order(ByteOrder.nativeOrder());
        output = byteBuffer.asShortBuffer();
        output.put(input);
        output.position(0);
        return output;
    }
    public FloatBuffer getVertices() {
        return convertToFloatBuffer(vertices);
    }
    public FloatBuffer getTextureUVs() {
        ArrayList<Float> outputUVs = new ArrayList<>();
        for(short[][] face:faces) {
            for(int i = 0; i < 3; i++) {
                int location = (face[i][1] - 1) * 2;
                outputUVs.add(textureUVs.get(location++));
                outputUVs.add(1f - textureUVs.get(location));
            }
        }
        return convertToFloatBuffer(outputUVs);
    }
    public FloatBuffer getNormals() {
        return convertToFloatBuffer(normals);
    }
    public FloatBuffer getObj() {
        ArrayList<Float> output = new ArrayList<>();
        for (short[][] face : faces) {
            for (int i = 0; i < 3; i++) {
                //Vertex
                int vertexLocation = (face[i][0] - 1) * 3;
                output.add(vertices.get(vertexLocation++));
                output.add(vertices.get(vertexLocation++));
                output.add(vertices.get(vertexLocation));
                //Texture
                int textureUVLocation = (face[i][1] - 1) * 2;
                output.add(textureUVs.get(textureUVLocation++));
                output.add(1f - textureUVs.get(textureUVLocation));
                //Normal
                int normalLocation = (face[i][2] - 1) * 3;
                output.add(normals.get(normalLocation++));
                output.add(normals.get(normalLocation++));
                output.add(normals.get(normalLocation));
            }
        }
        return convertToFloatBuffer(output);
    }
    public ShortBuffer getObjDrawOrder() {
        ArrayList<Short> output = new ArrayList<>();
        short c = 0;
        for(short[][] face:faces)
            for(int i = 0; i < 3; i++)
                output.add(c++);
        return convertToShortBuffer(output);
    }
    public ShortBuffer getDrawOrder() {
        ArrayList<Short> output = new ArrayList<>();
        for(short[][] face:faces)
            for(int i = 0; i < 3; i++)
                output.add((short)(face[i][0]-1));
        return convertToShortBuffer(output);
    }
    public int getDrawOrderLength() {
        return faces.size() * 3;
    }
}
