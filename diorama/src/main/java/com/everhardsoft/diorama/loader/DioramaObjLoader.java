package com.everhardsoft.diorama.loader;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by faisa on 11/1/2016.
 */

public class DioramaObjLoader {
    private Resources resources;
    public DioramaObjLoader(Context context) {
        resources = context.getResources();
    }
    public DioramaObj load(int rawID) {
        InputStream inputStream = resources.openRawResource(rawID);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        ArrayList<Float> vertices = new ArrayList<>();
        ArrayList<Float> textureUVs = new ArrayList<>();
        ArrayList<Float> normals = new ArrayList<>();
        ArrayList<short[][]> faces = new ArrayList<>();
        String readLine;
        try {
            while((readLine = bufferedReader.readLine()) != null) {
                String[] strings = readLine.split("\\s+");
                if(strings[0].equals("v")) {
                    for (int i = 1; i <= 3; i++)
                        vertices.add(Float.valueOf(strings[i]));
                } else if(strings[0].equals("vt")) {
                    for (int i = 1; i <= 2; i++)
                        textureUVs.add(Float.valueOf(strings[i]));
                } else if(strings[0].equals("vn")) {
                    for (int i = 1; i <= 3; i++)
                        normals.add(Float.valueOf(strings[i]));
                } else if(strings[0].equals("f")) {
                    short[][] output = new short[3][3];
                    for (int i = 1; i <= 3; i++) {
                        String[] faceVertices = strings[i].split("/");
                        for (int j = 0; j < 3; j++)
                            output[i - 1][j] = Short.valueOf(faceVertices[j]);
                    }
                    faces.add(output);
                }
            }
        } catch (IOException e) {
        }
        return new DioramaObj(vertices, textureUVs, normals, faces);
    }
}
