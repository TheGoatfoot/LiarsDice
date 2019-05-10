package com.everhardsoft.liarsdice.fragment.helper;


import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by faisa on 10/10/2016.
 */

public class ParentActivity<T extends Activity> {
    private T parentActivity;
    public ParentActivity(Fragment fragment){
        parentActivity = (T)fragment.getActivity();
    }
    public T getParentActivity() {
        return parentActivity;
    }
}
