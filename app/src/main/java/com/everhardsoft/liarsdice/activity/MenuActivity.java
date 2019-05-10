package com.everhardsoft.liarsdice.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.everhardsoft.diorama.Diorama;
import com.everhardsoft.diorama.graphic.view.DioramaSurfaceView;
import com.everhardsoft.diorama.rule.DioramaRule;
import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.fragment.LANLobbyClientFragment;
import com.everhardsoft.liarsdice.fragment.LANLobbyHostFragment;
import com.everhardsoft.liarsdice.fragment.manager.FragmentStackManager;
import com.everhardsoft.liarsdice.fragment.LANFragment;
import com.everhardsoft.liarsdice.fragment.MainMenuFragment;
import com.everhardsoft.liarsdice.fragment.MultiplayerFragment;
import com.everhardsoft.liarsdice.fragment.SettingsFragment;
import com.everhardsoft.liarsdice.game.rule.MenuRule;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class MenuActivity
        extends AppCompatActivity
        implements
        MainMenuFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener,
        MultiplayerFragment.OnFragmentInteractionListener,
        LANFragment.OnFragmentInteractionListener,
        LANLobbyHostFragment.OnFragmentInteractionListener,
        LANLobbyClientFragment.OnFragmentInteractionListener{
    public FragmentStackManager fragmentStackManager;
    @BindView(R.id.activity_menu_background_layout)
    public RelativeLayout backgroundLayout;
    @BindView(R.id.activity_menu_background_surfaceview)
    public DioramaSurfaceView dioramaSurfaceView;
    protected Diorama diorama;
    public MenuRule dioramaRule;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ButterKnife.bind(this);
        fragmentStackManager = new FragmentStackManager(getSupportFragmentManager(), R.id.activity_menu_foreground_layout);
        fragmentStackManager.push(new MainMenuFragment());
        diorama = new Diorama(this, dioramaSurfaceView, dioramaRule = new MenuRule());
    }
    @Override
    public void onBackPressed(){
        if(fragmentStackManager.pop()) {
            diorama.stop();
            super.onBackPressed();
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {
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
}
