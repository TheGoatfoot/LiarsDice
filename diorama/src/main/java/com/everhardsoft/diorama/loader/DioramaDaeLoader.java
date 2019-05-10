package com.everhardsoft.diorama.loader;

import android.content.Context;
import android.content.res.Resources;

import com.everhardsoft.diorama.graphic.DioramaMaterial;
import com.everhardsoft.diorama.graphic.DioramaMesh;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by faisa on 12/12/2016.
 */

public class DioramaDaeLoader {
    private Resources resources;
    private SAXParser parser;
    private DioramaDaeSAXHandler saxHandler = new DioramaDaeSAXHandler();
    private HashMap<String, DioramaMesh> meshes = null;
    private HashMap<String, DioramaMaterial> materials = null;
    public DioramaDaeLoader(Context context) {
        try {
            resources = context.getResources();
            parser = SAXParserFactory.newInstance().newSAXParser();
        } catch (SAXException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    public void load(int rawID) {
        InputStream inputStream = resources.openRawResource(rawID);

        try {
            parser.parse(inputStream, saxHandler);
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }

        meshes = saxHandler.getMeshes();
        materials = saxHandler.getMaterials();
    }
    public HashMap<String, DioramaMesh> getMeshes() {
        return meshes;
    }
    public HashMap<String, DioramaMaterial> getMaterials() {
        return materials;
    }
}
