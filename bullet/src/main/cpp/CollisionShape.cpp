#include <jni.h>

#include <bullet/btBulletDynamicsCommon.h>

extern "C"
void Java_com_everhardsoft_bullet_shape_CollisionShape_destroyCollisionShape(JNIEnv *env, jobject instance,
                                                                                jlong collisionShapePointer) {
    delete ((btCollisionShape*)collisionShapePointer);
}

extern "C"
jlong Java_com_everhardsoft_bullet_shape_StaticPlaneShape_createStaticPlaneShape(JNIEnv *env, jobject instance,
                                                                                    jfloat x, jfloat y, jfloat z) {
    return ((jlong)new btStaticPlaneShape(btVector3(x, y ,z), 1));
}

extern "C"
jlong Java_com_everhardsoft_bullet_shape_BoxShape_createBoxShape(JNIEnv *env, jobject instance,
                                                                 jfloat x, jfloat y, jfloat z) {
    return ((jlong)new btBoxShape(btVector3(x, y ,z)));
}
