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
 * {@link ObstructionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ObstructionFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    public ParentActivity<ManualPlayActivity> parentActivity;
    public ObstructionFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_obstruction, container, false);
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
    @OnClick(R.id.fragment_obstruction_show_button)
    public void showClicked() {
        parentActivity.getParentActivity().showClicked();
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
