package com.everhardsoft.liarsdice.fragment.manager;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.everhardsoft.liarsdice.R;

import java.lang.reflect.Array;
import java.util.Stack;

/**
 * Created by faisa on 10/4/2016.
 */

public class FragmentStackManager {
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Stack<Fragment> fragments;

    private int containerId;

    public int pushEnter    = R.anim.in_slide_left;
    public int pushExit     = R.anim.out_slide_left;
    public int popEnter     = R.anim.in_slide_right;
    public int popExit      = R.anim.out_slide_right;

    public FragmentStackManager(FragmentManager fragmentManager, int containerId) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;

        fragments = new Stack<>();
    }
    public void push(Fragment fragment) {
        push(fragment, true);
    }
    public void push(Fragment fragment, boolean pushStack) {
        fragmentTransaction = fragmentManager.beginTransaction();

        int enter;
        int exit;

        if(pushStack) {
            enter = pushEnter;
            exit = pushExit;
            fragments.push(fragment);
        } else {
            enter = popEnter;
            exit = popExit;
        }

        fragmentTransaction.setCustomAnimations(enter, exit);

        if(fragments.isEmpty())
            fragmentTransaction.add(containerId, fragment);
        else
            fragmentTransaction.replace(containerId, fragment);

        fragmentTransaction.commit();
    }

    public boolean pop() {
        Fragment fragment = fragments.pop();
        if(fragments.isEmpty())
            return true;
        else {
            push(fragments.peek(), false);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
        return false;
    }

    public void popToNearest(Class popper) {
        //Check
        Object[] fragmentsCheck = fragments.toArray();

        boolean found = false;
        for(Object object:fragmentsCheck)
            if(object.getClass().equals(popper))
                found = true;
        if(!found)
            return;

        boolean loop = true;
        while (loop) {
            if(fragments.size() == 1)
                loop = false;
            else if(!fragments.peek().getClass().equals(popper))
                pop();
            else
                loop = false;
        }
    }
}
