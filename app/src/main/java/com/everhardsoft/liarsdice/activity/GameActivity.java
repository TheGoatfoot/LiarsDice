package com.everhardsoft.liarsdice.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.everhardsoft.liarsdice.R;
import com.everhardsoft.diorama.Diorama;
import com.everhardsoft.diorama.graphic.view.DioramaSurfaceView;
import com.everhardsoft.liarsdice.fragment.ClearViewFragment;
import com.everhardsoft.liarsdice.game.rule.GameRule;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends AppCompatActivity {
    @BindView(R.id.activity_game_game_surfaceview)
    public DioramaSurfaceView dioramaSurfaceView;
    @BindView(R.id.dicevalue)
    public TextView diceValue;
    public Diorama diorama;
    public GameRule gameRule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.bind(this);
        gameRule = new GameRule();
        diorama = new Diorama(this, dioramaSurfaceView, gameRule);
    }
    @Override
    protected void onPause() {
        super.onPause();
        dioramaSurfaceView.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        dioramaSurfaceView.onResume();
    }
    @OnClick(R.id.castdie)
    public void castDie() {
        gameRule.castDie(6);
    }
}
