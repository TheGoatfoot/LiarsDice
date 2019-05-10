package com.everhardsoft.diorama.loader;

import com.everhardsoft.diorama.graphic.DioramaMaterial;
import com.everhardsoft.diorama.graphic.DioramaMesh;
import com.everhardsoft.diorama.graphic.DioramaMeshData;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by faisa on 12/12/2016.
 */

public class DioramaDaeSAXHandler extends DefaultHandler {
    private Converter converter = new Converter();
    private HashMap<String, DioramaMesh> meshes;
    private HashMap<String, DioramaMaterial> materials;
    @Override
    public void startDocument() throws SAXException {
        meshes = new HashMap<>();
        materials = new HashMap<>();
    }

    private boolean inEffect = false;
    private boolean inMesh = false;
    private boolean inPolylist = false;
    private boolean inP = false;

    private String materialSid;
    private String materialName;
    private DioramaMaterial material;
    private String meshId;
    private String meshName;
    private DioramaMesh mesh;
    private DioramaMeshData meshData;
    private String meshDataMaterial;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName) {
            case "effect":
                materialName = attributes.getValue("id").split("-")[0];
                material = new DioramaMaterial();
                inEffect = true;
                break;

            case "geometry":
                meshName = attributes.getValue("id").split("-")[0];
                //mesh = new DioramaMesh();
                vertices = new ArrayList<>();
                textureUVs = new ArrayList<>();
                normals = new ArrayList<>();
                inMesh = true;
                break;
            case "float_array":
                if(inMesh)
                    meshId = attributes.getValue("id").split("-")[2];
                break;
            case "polylist":
                inPolylist = true;
                meshDataMaterial = attributes.getValue("material");
                if(meshDataMaterial != null)
                    meshDataMaterial = meshDataMaterial.split("-")[0];
                drawOrder = new ArrayList<>();
                break;
            case "p":
                if(inPolylist)
                    inP = true;
                break;
        }
        if(inEffect)
            materialSid = attributes.getValue("sid");
    }
    private String[] contents;
    private ArrayList<Float> vertices;
    private ArrayList<Float> textureUVs;
    private ArrayList<Float> normals;
    private ArrayList<Short> drawOrder;
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        if(materialSid != null && inEffect) {
            contents = new String(ch, start, length).split(" ");
            switch (materialSid) {
                case "emission":
                    material.emission.set(
                            Float.parseFloat(contents[0]),
                            Float.parseFloat(contents[1]),
                            Float.parseFloat(contents[2]),
                            Float.parseFloat(contents[3])
                    );
                    break;
                case "ambient":
                    material.ambient.set(
                            Float.parseFloat(contents[0]),
                            Float.parseFloat(contents[1]),
                            Float.parseFloat(contents[2]),
                            Float.parseFloat(contents[3])
                    );
                    break;
                case "diffuse":
                    material.diffuse.set(
                            Float.parseFloat(contents[0]),
                            Float.parseFloat(contents[1]),
                            Float.parseFloat(contents[2]),
                            Float.parseFloat(contents[3])
                    );
                    break;
                case "specular":
                    material.specular.set(
                            Float.parseFloat(contents[0]),
                            Float.parseFloat(contents[1]),
                            Float.parseFloat(contents[2]),
                            Float.parseFloat(contents[3])
                    );
                    break;
                case "shininess":
                    material.shininess = Float.parseFloat(contents[0]);
                    break;
            }
            materialSid = null;
        } else if(meshId != null && inMesh) {
            switch (meshId) {
                case "positions":
                    contents = new String(ch, start, length).split(" ");
                    for (String fl: contents)
                        if(!fl.trim().isEmpty())
                            vertices.add(Float.parseFloat(fl));
                    //mesh.setVertexData(converter.convertToFloatBuffer(vertices));
                    break;
                case "normals":
                    contents = new String(ch, start, length).split(" ");
                    for (String fl: contents)
                        if(!fl.trim().isEmpty())
                            normals.add(Float.parseFloat(fl));
                    //mesh.setNormalData(converter.convertToFloatBuffer(normals));
                    break;
                case "map":
                    contents = new String(ch, start, length).split(" ");
                    for (String fl: contents)
                        if(!fl.trim().isEmpty())
                            textureUVs.add(Float.parseFloat(fl));
                    //mesh.setTextureUVData(converter.convertToFloatBuffer(textureUVs));
                    break;
            }
            meshId = null;
        } if(inPolylist) {
            if(inP) {
                contents = new String(ch, start, length).split(" ");
                for (String sh: contents)
                    drawOrder.add(Short.parseShort(sh));
                meshData = new DioramaMeshData();
                meshData.setDataDrawOrder(converter.convertToShortBuffer(drawOrder));
                meshData.dataDrawOrderCount = contents.length / 9;
                meshData.material = meshDataMaterial;
                //mesh.getMeshesData().add(meshData);
            }
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName) {
            case "effect":
                materials.put(materialName, material);
                inEffect = false;
                break;
            case "geometry":
                meshes.put(meshName, mesh);
                inMesh = false;
                break;
            case "polylist":
                inPolylist = false;
                break;
            case "p":
                if(inPolylist)
                    inP = false;
                break;
        }
    }
    @Override
    public void endDocument() throws SAXException {
    }
    public HashMap<String, DioramaMesh> getMeshes() {
        return meshes;
    }
    public HashMap<String, DioramaMaterial> getMaterials() {
        return materials;
    }
}
