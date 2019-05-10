#include <jni.h>

#include <bullet/btBulletDynamicsCommon.h>

struct WorldObject {
    btBroadphaseInterface* broadphase;
    btDefaultCollisionConfiguration* collisionConfiguration;
    btCollisionDispatcher* dispatcher;
    btSequentialImpulseConstraintSolver* solver;
    btDiscreteDynamicsWorld* dynamicsWorld;
} *worldObject;

extern "C"
jboolean Java_com_everhardsoft_bullet_DynamicsWorld_isExistWorld(JNIEnv *env, jobject instance,
                                                             jlong worldPointer) {
    return (jboolean) ((btDiscreteDynamicsWorld*)worldPointer != NULL);
}

extern "C"
void Java_com_everhardsoft_bullet_DynamicsWorld_removeRigidBodyWorld(JNIEnv *env, jobject instance,
                                                                jlong worldPointer,
                                                                jlong rigidBodyPointer) {
    ((WorldObject*)worldPointer)->dynamicsWorld->removeRigidBody((btRigidBody*)rigidBodyPointer);
}

extern "C"
void Java_com_everhardsoft_bullet_DynamicsWorld_addRigidBodyWorld(JNIEnv *env, jobject instance,
                                                             jlong worldPointer,
                                                             jlong rigidBodyPointer) {
    ((WorldObject*)worldPointer)->dynamicsWorld->addRigidBody((btRigidBody*)rigidBodyPointer);
}

extern "C"
void Java_com_everhardsoft_bullet_DynamicsWorld_setWorldGravity(JNIEnv *env, jobject instance,
                                                            jlong worldPointer, jfloat x, jfloat y, jfloat z) {
    ((WorldObject*)worldPointer)->dynamicsWorld->setGravity(btVector3(x, y, z));
}

extern "C"
void Java_com_everhardsoft_bullet_DynamicsWorld_stepWorld(JNIEnv *env, jobject instance,
                                                            jlong worldPointer, jfloat timeStep) {
    ((WorldObject*)worldPointer)->dynamicsWorld->stepSimulation(timeStep, 2);
}

extern "C"
void Java_com_everhardsoft_bullet_DynamicsWorld_destroyWorld(JNIEnv *env, jobject instance,
                                                            jlong worldPointer) {
    worldObject = (WorldObject*)worldPointer;

    delete worldObject->dynamicsWorld;
    delete worldObject->solver;
    delete worldObject->dispatcher;
    delete worldObject->collisionConfiguration;
    delete worldObject->broadphase;
}

extern "C"
jlong Java_com_everhardsoft_bullet_DynamicsWorld_createWorld(JNIEnv *env, jobject instance) {
    worldObject = new WorldObject;

    worldObject->broadphase             = new btDbvtBroadphase();
    worldObject->collisionConfiguration = new btDefaultCollisionConfiguration();
    worldObject->dispatcher             = new btCollisionDispatcher(worldObject->collisionConfiguration);
    worldObject->solver                 = new btSequentialImpulseConstraintSolver;
    worldObject->dynamicsWorld          = new btDiscreteDynamicsWorld(
            worldObject->dispatcher,
            worldObject->broadphase,
            worldObject->solver,
            worldObject->collisionConfiguration
    );

    return (jlong)worldObject;
}