#include <jni.h>
#include <queue>
#include <bullet/btBulletDynamicsCommon.h>

#include <android/log.h>

#define  LOG_TAG    "Mee"

#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)

btCollisionShape* collisionShape;
btTransform transform;

btVector3 origin;
btQuaternion quaternion;

extern "C"
jboolean Java_com_everhardsoft_bullet_RigidBody_isExistRigidBody(JNIEnv *env, jobject instance,
                                                        jlong rigidBodyPointer) {
    return (jboolean) ((btRigidBody*)rigidBodyPointer != NULL);
}

extern "C"
void Java_com_everhardsoft_bullet_RigidBody_getTransformRigidBody(JNIEnv *env, jobject instance,
                                                             jlong rigidBodyPointer,
                                                             jobject transformPar) {
    transform = ((btRigidBody*)rigidBodyPointer)->getWorldTransform();

    jclass transformClass = NULL;
    jclass vectorClass = NULL;
    jclass quaternionClass = NULL;
    jobject tempObject = NULL;
    jfieldID fieldID = NULL;

    /**/
    transformClass = env->GetObjectClass(transformPar);

    fieldID = env->GetFieldID(transformClass, "origin", "Lorg/joml/Vector3f;");
    tempObject = env->GetObjectField(transformPar, fieldID);
    vectorClass = env->GetObjectClass(tempObject);

    fieldID = env->GetFieldID(transformClass, "rotation", "Lorg/joml/Quaternionf;");
    tempObject = env->GetObjectField(transformPar, fieldID);
    quaternionClass = env->GetObjectClass(tempObject);
    /**/

    origin = transform.getOrigin();
    quaternion = transform.getRotation();

    fieldID = env->GetFieldID(transformClass, "origin", "Lorg/joml/Vector3f;");
    tempObject = env->GetObjectField(transformPar, fieldID);

    fieldID = env->GetFieldID(vectorClass, "x", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)origin.getX());
    fieldID = env->GetFieldID(vectorClass, "y", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)origin.getY());
    fieldID = env->GetFieldID(vectorClass, "z", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)origin.getZ());


    fieldID = env->GetFieldID(transformClass, "rotation", "Lorg/joml/Quaternionf;");
    tempObject = env->GetObjectField(transformPar, fieldID);

    fieldID = env->GetFieldID(quaternionClass, "x", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)quaternion.getX());
    fieldID = env->GetFieldID(quaternionClass, "y", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)quaternion.getY());
    fieldID = env->GetFieldID(quaternionClass, "z", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)quaternion.getZ());
    fieldID = env->GetFieldID(quaternionClass, "w", "F");
    env->SetFloatField(tempObject, fieldID, (jfloat)quaternion.getW());
}

extern "C"
void Java_com_everhardsoft_bullet_RigidBody_getLinearVelocityRigidBody(JNIEnv *env, jobject instance,
                                                                  jlong rigidBodyPointer,
                                                                  jobject linearVelocity) {
    jclass vectorClass = NULL;
    jfieldID fieldID = NULL;

    origin = ((btRigidBody*)rigidBodyPointer)->getLinearVelocity();

    vectorClass = env->GetObjectClass(linearVelocity);

    fieldID = env->GetFieldID(vectorClass, "x", "F");
    env->SetFloatField(linearVelocity, fieldID, origin.getX());
    fieldID = env->GetFieldID(vectorClass, "y", "F");
    env->SetFloatField(linearVelocity, fieldID, origin.getY());
    fieldID = env->GetFieldID(vectorClass, "z", "F");
    env->SetFloatField(linearVelocity, fieldID, origin.getZ());

}

extern "C"
jint Java_com_everhardsoft_bullet_RigidBody_getActivationStateRigidBody(JNIEnv *env, jobject instance,
                                                                        jlong rigidBodyPointer) {
    return ((btRigidBody*)rigidBodyPointer)->getActivationState();;
}

extern "C"
void Java_com_everhardsoft_bullet_RigidBody_activateRigidBody(JNIEnv *env, jobject instance,
                                                            jlong rigidBodyPointer,
                                                            jboolean activate) {
    ((btRigidBody*)rigidBodyPointer)->activate(activate);
}

extern "C"
void Java_com_everhardsoft_bullet_RigidBody_applyTorqueRigidBody(JNIEnv *env, jobject instance,
                                                            jlong rigidBodyPointer,
                                                            jfloat x, jfloat y, jfloat z) {
    ((btRigidBody*)rigidBodyPointer)->applyTorque(btVector3(x, y, z));
}

extern "C"
void Java_com_everhardsoft_bullet_RigidBody_applyCentralImpulseRigidBody(JNIEnv *env, jobject instance,
                                                             jlong rigidBodyPointer, jfloat x,
                                                             jfloat y, jfloat z) {
    ((btRigidBody*)rigidBodyPointer)->applyCentralImpulse(btVector3(x, y, z));
}

extern "C"
jlong Java_com_everhardsoft_bullet_RigidBody_createRigidBody(JNIEnv *env, jobject instance,
                                                       jlong collisionShapePointer, jfloat mass,
                                                       jfloat quatX, jfloat quatY, jfloat quatZ,
                                                       jfloat quatW, jfloat x, jfloat y, jfloat z) {
    collisionShape = (btCollisionShape*)collisionShapePointer;

    btVector3 fallInertia(0, 0, 0);
    collisionShape->calculateLocalInertia(mass, fallInertia);

    return (jlong)new btRigidBody (
            mass,
            new btDefaultMotionState(btTransform(
                    btQuaternion(quatX, quatY, quatZ, quatW), btVector3(x, y, z))
            ),
            collisionShape,
            fallInertia
    );
}
extern "C"
void Java_com_everhardsoft_bullet_RigidBody_destroyRigidBody(JNIEnv *env, jobject instance,
                                                        jlong rigidBodyPointer) {
    delete (btRigidBody*)rigidBodyPointer;
}