package com.everhardsoft.diorama.loader;

import android.content.Context;

import com.everhardsoft.diorama.graphic.DioramaMesh;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by faisa on 11/6/2016.
 */

public class DioramaMeshLoader {
    protected final DioramaObjLoader objLoader;
    protected final DioramaDaeLoader daeLoader;
    public DioramaMeshLoader(Context context) {
        objLoader = new DioramaObjLoader(context);
        daeLoader = new DioramaDaeLoader(context);
    }
    public HashMap<Integer, DioramaMesh> loadMeshes(HashSet<Integer> resourceIds) {
        HashMap<Integer, DioramaMesh> output = new HashMap<>();
        for(Integer resourceId: resourceIds)
            output.put(resourceId, new DioramaMesh(objLoader.load(resourceId)));
        return output;
    }
}
