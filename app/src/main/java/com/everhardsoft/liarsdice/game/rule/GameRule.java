package com.everhardsoft.liarsdice.game.rule;

import android.os.SystemClock;
import android.view.MotionEvent;

import com.everhardsoft.diorama.rule.DioramaRule;
import com.everhardsoft.diorama.world.DioramaBulletWorldComponent;
import com.everhardsoft.diorama.world.object.DioramaObject;
import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.game.ObjectMaker;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Random;

/**
 * Created by faisa on 11/6/2016.
 */

public class GameRule extends DioramaRule {
    protected ObjectMaker objectMaker = new ObjectMaker();
    @Override
    public void onSurfaceCreated() {
        objectMaker.initialize(context);
    }
    @Override
    public void onSurfaceDestroyed() {
        objectMaker.deinitialize();
    }
    @Override
    public void onTouchEvent(MotionEvent e, float relativeX, float relativeY) {
    }
    @Override
    public void onUpdate(float delta) {
    }
    private DioramaBulletWorldComponent bulletWorldComponent;
    @Override
    public void onStart() {
        world.addComponent(objectMaker.shaderWorldComponent);
        world.addComponent(bulletWorldComponent = new DioramaBulletWorldComponent());
        bulletWorldComponent.getDynamicsWorld().setGravity(0f, -10f, 0f);
        bulletWorldComponent.deltaMultiplier = 2f;

        world.addObject(objectMaker.makeTable(world, bulletWorldComponent, new Vector3f(0f, 1f, 0f), new Vector3f(0f, 1f, 0f)));

        world.addObject(objectMaker.makePlane(world, bulletWorldComponent, new Vector3f(-12f, 0f, 0f), new Vector3f(1f, 0f, 0f)));
        world.addObject(objectMaker.makePlane(world, bulletWorldComponent, new Vector3f(12f, 0f, 0f), new Vector3f(-1f, 0f, 0f)));
        world.addObject(objectMaker.makePlane(world, bulletWorldComponent, new Vector3f(0f, 0f, 12f), new Vector3f(0f, 0f, -1f)));
        world.addObject(objectMaker.makePlane(world, bulletWorldComponent, new Vector3f(0f, 0f, -12f), new Vector3f(0f, 0f, 1f)));

        world.getMainCamera().origin.x = 10f;
        world.getMainCamera().origin.y = 25f;
        world.getMainCamera().origin.z = 0f;

        world.getMainCamera().lookAt.x = 5f;
        world.getMainCamera().lookAt.y = 0f;
        world.getMainCamera().lookAt.z = 0f;
    }
    @Override
    public void onRestart() {
    }
    @Override
    public void onStop() {
    }
    @Override
    public void onPause() {
    }
    @Override
    public void onResume() {
    }
    private Random random = new Random(SystemClock.uptimeMillis());
    private DioramaObject[] dices = null;
    private int diceCastDistance = Application.getResources().getInteger(R.integer.game_dicecast_distance);
    private int diceCastVerticalOffset =
            Application.getResources().getInteger(R.integer.game_dicecast_vertical_offset);
    private int diceCastRandomHorizontalOffset =
            Application.getResources().getInteger(R.integer.game_dicecast_random_horizontal_offset);
    private int diceCastRandomTorque =
            Application.getResources().getInteger(R.integer.game_dicecast_random_torque);
    public DioramaObject[] getDice() {
        return  dices;
    }
    public void castDie(int diceCount) {
        removeDies();
        dices = new DioramaObject[diceCount];
        Vector3f position = new Vector3f();
        for(int i = 0; i < diceCount; i++) {
            if(i % 2 == 0)
                position.set(
                        getRandomOffset(diceCastRandomHorizontalOffset),
                        diceCastDistance + diceCastVerticalOffset * i,
                        getRandomOffset(diceCastRandomHorizontalOffset));
            else
                position.set(
                        getRandomOffset(diceCastRandomHorizontalOffset),
                        diceCastDistance + diceCastVerticalOffset * i,
                        getRandomOffset(diceCastRandomHorizontalOffset));
            dices[i] = objectMaker.makeDice(world, bulletWorldComponent, position, new Quaternionf(),
                    new Vector3f(
                            random.nextInt(diceCastRandomTorque),
                            random.nextInt(diceCastRandomTorque),
                            random.nextInt(diceCastRandomTorque)));
        }
        for(DioramaObject d:dices)
            world.addObject(d);
    }
    public void removeDies() {
        if(dices != null)
            for(DioramaObject d:dices)
                world.removeObject(d);
    }
    private int getRandomOffset(int r) {
        return random.nextInt(r)-random.nextInt(r);
    }
}
