package com.everhardsoft.liarsdice.task;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v4.content.res.ResourcesCompat;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.everhardsoft.diorama.world.object.DioramaObject;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.util.DiceUtil;

import butterknife.ButterKnife;

/**
 * Created by faisa on 11/9/2016.
 */

public class GetDiceValue extends AsyncTask<DioramaObject, Void, Void> {
    public DiceUtil diceUtil = new DiceUtil();
    public boolean running = true;
    protected LinearLayout output;
    protected Context context;
    protected Resources resources;
    protected Activity parent;
    public class AddViewRunnable implements Runnable {
        public int value = 0;
        public int die = 0;
        @Override
        public void run() {
            View view = View.inflate(context, R.layout.view_dicevalue, null);
            TextView value = ButterKnife.findById(view, R.id.view_dicevalue_value_textview);
            ImageView die = ButterKnife.findById(view, R.id.view_dicevalue_die_imageview);
            int dieRes = R.drawable.one;
            switch (this.die) {
                case 1:
                    dieRes = R.drawable.one;
                    break;
                case 2:
                    dieRes = R.drawable.two;
                    break;
                case 3:
                    dieRes = R.drawable.three;
                    break;
                case 4:
                    dieRes = R.drawable.four;
                    break;
                case 5:
                    dieRes = R.drawable.five;
                    break;
                case 6:
                    dieRes = R.drawable.six;
                    break;
            }

            die.setImageDrawable(ResourcesCompat.getDrawable(resources, dieRes, null));
            value.setText(resources.getString(R.string.activity_manualplay_result_value_textview, this.value));
            output.addView(view);
        }
    }
    public GetDiceValue(Context context, Activity parent, LinearLayout output) {
        super();
        this.output = output;
        this.context = context;
        this.parent = parent;
        resources = context.getResources();
    }
    @Override
    protected Void doInBackground(DioramaObject... dices) {
        running = true;
        while (diceUtil.isDiesStable(dices) != 1 && running) {}
        if(!running)
            return null;
        SparseIntArray dieValues = diceUtil.dieValuesMap(dices);
        int value;
        for(int i = 1; i <= 6; i++) {
            value = dieValues.get(i);
            if(value != 0) {
                AddViewRunnable addViewRunnable = new AddViewRunnable();
                addViewRunnable.value = value;
                addViewRunnable.die = i;
                parent.runOnUiThread(addViewRunnable);
            }
        }
        return null;
    }
}
