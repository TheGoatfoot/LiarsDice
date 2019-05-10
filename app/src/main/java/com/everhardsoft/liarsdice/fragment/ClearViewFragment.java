package com.everhardsoft.liarsdice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.activity.ManualPlayActivity;
import com.everhardsoft.liarsdice.fragment.helper.ParentActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClearViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ClearViewFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    public ParentActivity<ManualPlayActivity> parentActivity;
    public ClearViewFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clearview, container, false);
        ButterKnife.bind(this, view);
        parentActivity = new ParentActivity<>(this);
        return view;
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
    @OnClick(R.id.fragment_clearview_hide_button)
    public void hideClicked() {
        parentActivity.getParentActivity().hideClicked();
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
