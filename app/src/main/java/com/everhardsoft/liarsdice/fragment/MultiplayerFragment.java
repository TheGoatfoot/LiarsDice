package com.everhardsoft.liarsdice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.activity.MenuActivity;
import com.everhardsoft.liarsdice.fragment.helper.ParentActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MultiplayerFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private ParentActivity<MenuActivity> parent;

    public MultiplayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multiplayer, container, false);
        ButterKnife.bind(this, view);
        parent = new ParentActivity<>(this);
        return view;
    }

    @OnClick(R.id.fragment_multiplayer_lan_button)
    public void lanClicked() {
        parent.getParentActivity().fragmentStackManager.push(new LANFragment());
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
        parent.getParentActivity().dioramaRule.cameraMultiplayer();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
