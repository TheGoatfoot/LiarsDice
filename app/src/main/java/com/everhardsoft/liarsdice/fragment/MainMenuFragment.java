package com.everhardsoft.liarsdice.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.activity.GameActivity;
import com.everhardsoft.liarsdice.activity.ManualPlayActivity;
import com.everhardsoft.liarsdice.activity.MenuActivity;
import com.everhardsoft.liarsdice.fragment.helper.ParentActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainMenuFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private ParentActivity<MenuActivity> parent;

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mainmenu, container, false);
        ButterKnife.bind(this, view);
        parent = new ParentActivity<>(this);
        return view;
    }

    @OnClick(R.id.fragment_mainmenu_settings_button)
    public void settingsClicked() {
        parent.getParentActivity().fragmentStackManager.push(new SettingsFragment());
    }

    @OnClick(R.id.fragment_mainmenu_multiplayer_button)
    public void multiplayerClicked() {
        parent.getParentActivity().fragmentStackManager.push(new MultiplayerFragment());
    }
    @OnClick(R.id.fragment_mainmenu_manualplay_button)
    public void manualplayClicked() {
        Intent intent = new Intent(getContext(), ManualPlayActivity.class);
        startActivity(intent);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        parent.getParentActivity().dioramaRule.cameraMainMenu();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
