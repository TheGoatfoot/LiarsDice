package com.everhardsoft.liarsdice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.activity.MenuActivity;
import com.everhardsoft.liarsdice.adapter.LobbyPlayersAdapter;
import com.everhardsoft.liarsdice.fragment.helper.ParentActivity;
import com.everhardsoft.liarsdice.multiplayer.handler.ClientHandler;

import org.json.simple.parser.JSONParser;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LANLobbyClientFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LANLobbyClientFragment extends Fragment implements ClientHandler {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragment_lanlobby_client_playerlist_listview)
    public ListView playerList;

    private ArrayList<String> playerNicknames;
    private LobbyPlayersAdapter lobbyPlayersAdapter;

    private ParentActivity<MenuActivity> parent;
    private JSONParser jsonParser;

    public LANLobbyClientFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lanlobby_client, container, false);
        ButterKnife.bind(this, view);
        parent = new ParentActivity<>(this);

        jsonParser = new JSONParser();

        playerNicknames = new ArrayList<>();
        lobbyPlayersAdapter = new LobbyPlayersAdapter(getContext(), playerNicknames);
        playerList.setAdapter(lobbyPlayersAdapter);

        Application.setClientHandler(this);

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

    //Interface
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void updatePlayerList(ArrayList<String> nicknames) {
        Timber.d("Player List Update %s", nicknames.toString());
        playerNicknames.clear();
        playerNicknames.addAll(nicknames);
        parent.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lobbyPlayersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void gameStart() {

    }

    //Method
}
