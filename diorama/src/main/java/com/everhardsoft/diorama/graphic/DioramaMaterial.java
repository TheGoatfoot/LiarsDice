package com.everhardsoft.diorama.graphic;

import org.joml.Vector4f;

/**
 * Created by faisa on 12/13/2016.
 */

public class DioramaMaterial {
    public DioramaMaterial() {
    }
    public DioramaMaterial(DioramaMaterial material) {
        this.emission = material.emission;
        this.ambient = material.ambient;
        this.diffuse = material.diffuse;
        this.specular = material.specular;
        this.shininess = material.shininess;
    }
    public DioramaMaterial(Vector4f emission, Vector4f ambient, Vector4f diffuse, Vector4f specular, float shininess) {
        this.emission = emission;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
    public Vector4f emission = new Vector4f();
    public Vector4f ambient = new Vector4f();
    public Vector4f diffuse = new Vector4f();
    public Vector4f specular = new Vector4f();
    public float shininess = 0f;
}
