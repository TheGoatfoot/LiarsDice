package com.everhardsoft.liarsdice.game;

import android.content.Context;
import android.util.SparseIntArray;

import com.everhardsoft.bullet.RigidBody;
import com.everhardsoft.bullet.shape.BoxShape;
import com.everhardsoft.bullet.shape.CollisionShape;
import com.everhardsoft.bullet.shape.StaticPlaneShape;
import com.everhardsoft.diorama.graphic.DioramaMesh;
import com.everhardsoft.diorama.graphic.binder.DioramaTextureBinder;
import com.everhardsoft.diorama.loader.DioramaMeshLoader;
import com.everhardsoft.diorama.graphic.shader.DefaultShader;
import com.everhardsoft.diorama.world.DioramaBulletWorldComponent;
import com.everhardsoft.diorama.world.DioramaShaderWorldComponent;
import com.everhardsoft.diorama.world.DioramaWorld;
import com.everhardsoft.diorama.world.object.DioramaBoxPickComponent;
import com.everhardsoft.diorama.world.object.DioramaBulletRigidBodyComponent;
import com.everhardsoft.diorama.world.object.DioramaDrawableComponent;
import com.everhardsoft.diorama.world.object.DioramaObject;
import com.everhardsoft.liarsdice.R;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by faisa on 11/4/2016.
 */

public class ObjectMaker {
    protected DefaultShader defaultShader;
    protected HashSet<Integer> texturesPrebind = new HashSet<>();
    protected SparseIntArray texturesBind;
    protected HashSet<Integer> meshesPreload = new HashSet<>();
    protected HashMap<Integer, DioramaMesh> meshesLoad;
    protected DioramaTextureBinder textureBinder;
    protected DioramaMeshLoader meshLoader;

    public DioramaShaderWorldComponent shaderWorldComponent;

    protected CollisionShape boxShape = new BoxShape();

    protected List<CollisionShape> planes = new LinkedList<>();

    public void initialize(Context context) {
        textureBinder = new DioramaTextureBinder(context);
        meshLoader = new DioramaMeshLoader(context);
        shaderWorldComponent = new DioramaShaderWorldComponent();

        defaultShader = new DefaultShader(context);

        shaderWorldComponent.addShader(defaultShader);

        defaultShader.create();

        texturesPrebind.add(R.raw.dice_png);
        texturesPrebind.add(R.raw.table_jpg);
        texturesBind = textureBinder.bindTexture(texturesPrebind);

        meshesPreload.add(R.raw.dice_obj);
        meshesPreload.add(R.raw.table_obj);
        meshesLoad = meshLoader.loadMeshes(meshesPreload);

        boxShape.create(1f, 1f, 1f);
    }
    public void deinitialize() {
        textureBinder.deleteTexture(texturesBind);
    }
    protected RigidBody makePlaneRigidBody(Vector3f position, Vector3f orientation) {
        CollisionShape shape = new StaticPlaneShape();
        shape.create(orientation.x, orientation.y, orientation.z);

        planes.add(shape);

        RigidBody rigidBody = new RigidBody();
        rigidBody.create(
                shape, 0f,
                0f, 0f, 0f, 1f,
                position.x, position.y, position.z
        );

        return rigidBody;
    }
    protected RigidBody makeDiceRigidBody(Vector3f position, Quaternionf rotation) {
        RigidBody rigidBody = new RigidBody();
        rigidBody.create(
                boxShape, 1f,
                rotation.x, rotation.y, rotation.z, rotation.w,
                position.x, position.y, position.z
        );

        return rigidBody;
    }
    public DioramaObject makePlane(DioramaWorld world, DioramaBulletWorldComponent bulletWorld, Vector3f position, Vector3f orientation) {
        DioramaObject output = new DioramaObject(world);
        DioramaBulletRigidBodyComponent dioramaBulletRigidBodyComponent = bulletWorld.makePhysicsObject(output, makePlaneRigidBody(position, orientation));

        output.addComponent(dioramaBulletRigidBodyComponent);

        return output;
    }
    public DioramaObject makeDiceMock(DioramaWorld world, Vector3f position, Quaternionf rotation) {
        DioramaObject output = new DioramaObject(world);
        DioramaDrawableComponent dioramaDrawableComponent = new DioramaDrawableComponent();

        dioramaDrawableComponent.setMesh(meshesLoad.get(R.raw.dice_obj));
        dioramaDrawableComponent.setShader(defaultShader);
        dioramaDrawableComponent.setTexture(texturesBind.get(R.raw.dice_png));

        output.origin.set(position.x, position.y, position.z);
        output.quaternion.set(rotation);

        output.addComponent(dioramaDrawableComponent);

        return output;
    }
    public DioramaObject makeTable(DioramaWorld world, DioramaBulletWorldComponent bulletWorld, Vector3f position, Vector3f orientation) {
        DioramaObject output = makePlane(world, bulletWorld, position, orientation);
        DioramaDrawableComponent dioramaDrawableComponent = new DioramaDrawableComponent();

        dioramaDrawableComponent.setMesh(meshesLoad.get(R.raw.table_obj));
        dioramaDrawableComponent.setShader(defaultShader);
        dioramaDrawableComponent.setTexture(texturesBind.get(R.raw.table_jpg));

        output.addComponent(dioramaDrawableComponent);

        return output;
    }
    public DioramaObject makeDice(DioramaWorld world, DioramaBulletWorldComponent bulletWorld, Vector3f position, Quaternionf rotation, Vector3f torque) {
        DioramaObject output = makeDiceMock(world, position, rotation);
        DioramaBulletRigidBodyComponent dioramaBulletRigidBodyComponent = bulletWorld.makePhysicsObject(output ,makeDiceRigidBody(position, rotation));

        DioramaBoxPickComponent boxPickComponent = new DioramaBoxPickComponent();
        boxPickComponent.boundingBoxMin.set(-1, -1, -1);
        boxPickComponent.boundingBoxMax.set(1, 1, 1);

        output.addComponent(boxPickComponent);
        output.addComponent(dioramaBulletRigidBodyComponent);

        dioramaBulletRigidBodyComponent.rigidBody.applyTorque(torque);

        return output;
    }
}
