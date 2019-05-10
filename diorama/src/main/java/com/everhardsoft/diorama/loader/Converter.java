package com.everhardsoft.diorama.loader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by faisa on 12/13/2016.
 */

public class Converter {
    public final int SHORT_BYTE = Short.SIZE * Byte.SIZE;
    public final int FLOAT_BYTE = Float.SIZE * Byte.SIZE;
    public float[] convertToFloatArray(ArrayList<Float> floats) {
        float[] output = new float[floats.size()];
        for(int i = 0; i < floats.size(); i++)
            output[i] = floats.get(i);
        return output;
    }
    public short[] convertToShortArray(ArrayList<Short> shorts) {
        short[] output = new short[shorts.size()];
        for(int i = 0; i < shorts.size(); i++)
            output[i] = shorts.get(i);
        return output;
    }
    public FloatBuffer convertToFloatBuffer(ArrayList<Float> floats) {
        float[] input = convertToFloatArray(floats);
        FloatBuffer output;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(floats.size() * FLOAT_BYTE);
        byteBuffer.order(ByteOrder.nativeOrder());
        output = byteBuffer.asFloatBuffer();
        output.put(input);
        output.position(0);
        return output;
    }
    public ShortBuffer convertToShortBuffer(ArrayList<Short> shorts) {
        short[] input = convertToShortArray(shorts);
        ShortBuffer output;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(shorts.size() * SHORT_BYTE);
        byteBuffer.order(ByteOrder.nativeOrder());
        output = byteBuffer.asShortBuffer();
        output.put(input);
        output.position(0);
        return output;
    }
}
