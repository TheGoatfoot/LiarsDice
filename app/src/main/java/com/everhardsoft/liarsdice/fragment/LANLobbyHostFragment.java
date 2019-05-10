package com.everhardsoft.liarsdice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.everhardsoft.liarsdice.multiplayer.handler.DatagramHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.ServerHandler;
import com.everhardsoft.liarsdice.multiplayer.datagram.SendDatagram;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.InetAddress;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LANLobbyHostFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LANLobbyHostFragment extends Fragment implements DatagramHandler, ClientHandler, ServerHandler {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragment_lanlobby_host_playerlist_listview)
    public ListView playerList;

    private ArrayList<String> playerNicknames;
    private LobbyPlayersAdapter lobbyPlayersAdapter;

    private ParentActivity<MenuActivity> parent;

    public LANLobbyHostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lanlobby_host, container, false);
        ButterKnife.bind(this, view);
        parent = new ParentActivity<>(this);

        jsonParser = new JSONParser();

        playerNicknames = new ArrayList<>();
        lobbyPlayersAdapter = new LobbyPlayersAdapter(getContext(), playerNicknames);
        playerList.setAdapter(lobbyPlayersAdapter);

        Application.setClientHandler(this);
        Application.setServerHandler(this);

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

    @Override
    public void onPause() {
        Application.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Application.startListening(this);
    }

    @OnClick(R.id.fragment_lanlobby_host_start_button)
    public void startClicked() {
        Application.startGame();
    }

    //Interface
    JSONParser jsonParser;
    JSONObject json;
    @Override
    public void onDatagramReceive(InetAddress inetAddress, String data) {
        try {
            json = (JSONObject) jsonParser.parse(data);
            Object type = json.get(Application.getResources().getString(R.string.json_type));
            Object payload = json.get(Application.getResources().getString(R.string.json_type_payload));
            if(type.equals(Application.getResources().getString(R.string.json_request))) {
                if(payload.equals(Application.getResources().getString(R.string.json_request_lobby))) {
                    JSONObject json = new JSONObject();
                    json.put(
                            Application.getResources().getString(R.string.json_type),
                            Application.getResources().getString(R.string.json_reply));
                    json.put(
                            Application.getResources().getString(R.string.json_type_payload),
                            Application.getResources().getString(R.string.json_request_lobby));
                    json.put(
                            Application.getResources().getString(R.string.json_request_lobby_name),
                            Application.getNickname());
                    json.put(
                            Application.getResources().getString(R.string.json_request_lobby_players),
                            Application.getServerPlayerCount());
                    new SendDatagram(Application.getResources().getInteger(R.integer.server_datagram_port))
                            .executeOnExecutor(
                                    AsyncTask.THREAD_POOL_EXECUTOR,
                                    inetAddress.getHostAddress(),
                                    json.toJSONString());
                }
            }
        } catch (ParseException | NullPointerException e) {
            e.printStackTrace();
        }
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

    @Override
    public void messageToast(String messsage) {
        Application.makeToast(parent.getParentActivity(), messsage);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    //Method
}
