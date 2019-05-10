package com.everhardsoft.liarsdice.activity;

import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.everhardsoft.diorama.Diorama;
import com.everhardsoft.diorama.graphic.view.DioramaSurfaceView;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.fragment.ClearViewFragment;
import com.everhardsoft.liarsdice.fragment.ObstructionFragment;
import com.everhardsoft.liarsdice.fragment.manager.FragmentStackManager;
import com.everhardsoft.liarsdice.game.rule.GameRule;
import com.everhardsoft.liarsdice.task.GetDiceValue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManualPlayActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener,
        ClearViewFragment.OnFragmentInteractionListener,
        ObstructionFragment.OnFragmentInteractionListener {
    public FragmentStackManager fragmentStackManager;
    @BindView(R.id.activity_manualplay_castdie_castdie_swiperefreshlayout)
    public SwipeRefreshLayout castdieSwipe;
    @BindView(R.id.activity_manualplay_game_surfaceview)
    public DioramaSurfaceView dioramaSurfaceView;
    @BindView(R.id.activity_manualplay_castdie_castprompt_textview)
    public TextView castPrompt;
    @BindView(R.id.activity_manualplay_menu_dicecount_textview)
    public TextView diceCountTextView;
    public GameRule gameRule;
    public Diorama diorama;

    public ClearViewFragment clearViewFragment;
    public ObstructionFragment obstructionFragment;

    public LinearLayout resultLayout;

    public int diceCount;
    public int maximumDice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualplay);
        ButterKnife.bind(this);
        diorama = new Diorama(this, dioramaSurfaceView, gameRule = new GameRule());
        castdieSwipe.setOnRefreshListener(this);
        fragmentStackManager = new FragmentStackManager(getSupportFragmentManager(), R.id.activity_manualplay_fragments_layout);

        fragmentStackManager.pushEnter  = R.anim.in_slide_up;
        fragmentStackManager.pushExit   = R.anim.out_slide_up;
        fragmentStackManager.popEnter   = R.anim.in_slide_down;
        fragmentStackManager.popExit    = R.anim.out_slide_down;
        clearViewFragment = new ClearViewFragment();
        obstructionFragment = new ObstructionFragment();

        fragmentStackManager.push(clearViewFragment);

        resultLayout = ButterKnife.findById(this, R.id.activity_manualplay_result_layout);

        maximumDice = getResources().getInteger(R.integer.game_dice_maximum);
        diceCount = getResources().getInteger(R.integer.game_dice_default);
        diceCountTextView.setText(Integer.toString(diceCount));
    }
    @Override
    public void onBackPressed(){
        if(fragmentStackManager.pop()) {
            if(getDiceValue != null)
                getDiceValue.running = false;
            diorama.stop();
            super.onBackPressed();
        }
    }
    public GetDiceValue getDiceValue = null;
    @Override
    public void onRefresh() {
        if(getDiceValue != null)
            getDiceValue.running = false;
        resultLayout.removeAllViews();

        gameRule.castDie(diceCount);
        castPrompt.setVisibility(View.INVISIBLE);
        castdieSwipe.setRefreshing(false);

        getDiceValue = new GetDiceValue(this, this, resultLayout);
        getDiceValue.execute(gameRule.getDice());
    }
    @OnClick(R.id.activity_manualplay_menu_add_button)
    public void addDiceClicked() {
        if(diceCount == maximumDice)
            return;
        diceCount++;
        diceCountTextView.setText(Integer.toString(diceCount));
    }
    @OnClick(R.id.activity_manualplay_menu_subtract_button)
    public void subtractDiceClicked() {
        if(diceCount == 1)
            return;
        diceCount--;
        diceCountTextView.setText(Integer.toString(diceCount));
    }
    public void hideClicked() {
        fragmentStackManager.push(obstructionFragment);
    }
    public void showClicked() {
        fragmentStackManager.pop();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
