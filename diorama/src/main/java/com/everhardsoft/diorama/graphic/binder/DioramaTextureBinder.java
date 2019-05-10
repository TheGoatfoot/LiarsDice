package com.everhardsoft.diorama.graphic.binder;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by faisa on 11/2/2016.
 */

public class DioramaTextureBinder {
    private Resources resources;
    private BitmapFactory.Options bitmapOptions;
    public DioramaTextureBinder(Context context) {
        resources = context.getResources();
        bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
    }
    public SparseIntArray bindTexture(HashSet<Integer> resourceIds) {
        SparseIntArray output = new SparseIntArray();

        int[] handles = new int[resourceIds.size()];

        GLES20.glGenTextures(resourceIds.size(), handles, 0);
        int c = 0;
        for(Integer resourceId: resourceIds) {
            output.put(resourceId, handles[c]);
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId, bitmapOptions);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[c++]);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();
        }

        return output;
    }

    public void deleteTexture(SparseIntArray handles) {
        int[] intHandles = new int[handles.size()];
        int c = 0;
        for(int i = 0; i < handles.size(); i++)
            intHandles[c++] = handles.get(i);
        GLES20.glDeleteTextures(handles.size(), intHandles, 0);
    }
}
