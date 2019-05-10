package com.everhardsoft.diorama.loader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * Created by faisa on 11/6/2016.
 */

public class DioramaBitmapLoader {
    protected Resources resources;
    protected BitmapFactory.Options bitmapOptions;
    public DioramaBitmapLoader(Context context) {
        resources = context.getResources();
        bitmapOptions = new BitmapFactory.Options();
    }
    public HashMap<String, byte[]> loadBitmaps(HashMap<String, Integer> nameAndResourceIds) {
        HashMap<String, byte[]> output = new HashMap<>();
        Bitmap bitmap;
        ByteArrayOutputStream byteArrayOutputStream;
        for(String key: nameAndResourceIds.keySet()) {
            bitmap = BitmapFactory.decodeResource(resources, nameAndResourceIds.get(key), bitmapOptions);
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            output.put(key, byteArrayOutputStream.toByteArray());
            bitmap.recycle();
        }
        return output;
    }
}
