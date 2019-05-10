package com.everhardsoft.liarsdice.util;

import android.util.SparseIntArray;

import com.everhardsoft.bullet.RigidBody;
import com.everhardsoft.bullet.Transform;
import com.everhardsoft.diorama.world.object.DioramaBulletRigidBodyComponent;
import com.everhardsoft.diorama.world.object.DioramaObject;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Created by faisa on 11/8/2016.
 */

public class DiceUtil {
    public Vector3f transformVector(Quaternionf quaternion, Vector3f vec){
        float num = quaternion.x * 2f;
        float num2 = quaternion.y * 2f;
        float num3 = quaternion.z * 2f;
        float num4 = quaternion.x * num;
        float num5 = quaternion.y * num2;
        float num6 = quaternion.z * num3;
        float num7 = quaternion.x * num2;
        float num8 = quaternion.x * num3;
        float num9 = quaternion.y * num3;
        float num10 = quaternion.w * num;
        float num11 = quaternion.w * num2;
        float num12 = quaternion.w * num3;
        Vector3f result = new Vector3f();
        result.x = (1f - (num5 + num6)) * vec.x + (num7 - num12) * vec.y + (num8 + num11) * vec.z;
        result.y = (num7 + num12) * vec.x + (1f - (num4 + num6)) * vec.y + (num9 - num10) * vec.z;
        result.z = (num8 - num11) * vec.x + (num9 + num10) * vec.y + (1f - (num4 + num5)) * vec.z;
        return result;
    }
    public final Vector3f up          = new Vector3f(0f, 1f, 0f);
    public final Vector3f down        = new Vector3f(0f, -1f, 0f);
    public final Vector3f forward     = new Vector3f(0f, 0f, 1f);
    public final Vector3f backward    = new Vector3f(0f, 0f, -1f);
    public final Vector3f right       = new Vector3f(1f, 0f, 0f);
    public final Vector3f left        = new Vector3f(-1f, 0f, 0f);
    public int[] dieValues(DioramaObject[] dices) {
        int[] output = new int[dices.length];
        int c = 0;
        RigidBody rb;
        Quaternionf quaternion = new Quaternionf();
        Transform transform = new Transform();

        Vector3f upValue;
        Vector3f downValue;
        Vector3f forwardValue;
        Vector3f backwardValue;
        Vector3f rightValue;
        Vector3f leftValue;
        Vector3f champion;

        for (DioramaObject object: dices) {
            rb = object.getComponent(DioramaBulletRigidBodyComponent.class).rigidBody;
            rb.getTransform(transform);
            quaternion.set(transform.rotation);

            upValue         = transformVector(quaternion, up);
            downValue       = transformVector(quaternion, down);
            forwardValue    = transformVector(quaternion, forward);
            backwardValue   = transformVector(quaternion, backward);
            rightValue      = transformVector(quaternion, right);
            leftValue       = transformVector(quaternion, left);

            champion = getHighestY(upValue, downValue, forwardValue, backwardValue, rightValue, leftValue);

            if(champion == upValue)
                output[c++] = 1;
            else if(champion == downValue)
                output[c++] = 6;
            else if(champion == forwardValue)
                output[c++] = 3;
            else if(champion == backwardValue)
                output[c++] = 4;
            else if(champion == rightValue)
                output[c++] = 5;
            else if(champion == leftValue)
                output[c++] = 2;
            else
                output[c++] = 0;
        }
        return output;
    }
    public SparseIntArray dieValuesMap(DioramaObject... dice) {
        SparseIntArray output = new SparseIntArray();
        for(int i = 1; i <= 6; i++)
            output.put(i, 0);
        int[] dieValues = dieValues(dice);
        for(int i: dieValues)
            output.put(i, output.get(i) + 1);
        return output;
    }
    public int isDiesStable(DioramaObject[] dices) {
        Transform transform = new Transform();
        try {
            if(dices.length == 0)
                return 2;
            Vector3f linearVelocity = new Vector3f();
            for (DioramaObject dice : dices) {
                RigidBody rb = dice.getComponent(DioramaBulletRigidBodyComponent.class).rigidBody;
                if (rb == null)
                    return 0;
                rb.getLinearVelocity(linearVelocity);
                rb.getTransform(transform);
                if (linearVelocity.length() != 0 || transform.origin.y > 10f)
                    return 0;
            }
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }
    protected Vector3f getHighestY(Vector3f... vectors) {
        Vector3f output = null;
        for(Vector3f vector: vectors)
            if (output == null)
                output = vector;
            else if(vector.y > output.y)
                output = vector;
        return output;
    }
}
