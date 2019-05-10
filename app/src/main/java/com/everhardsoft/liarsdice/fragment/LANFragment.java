package com.everhardsoft.liarsdice.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.activity.MenuActivity;
import com.everhardsoft.liarsdice.adapter.LANLobbiesAdapter;
import com.everhardsoft.liarsdice.bean.Lobby;
import com.everhardsoft.liarsdice.fragment.helper.ParentActivity;
import com.everhardsoft.liarsdice.multiplayer.handler.WebsocketClientHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.WebsocketServerHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.DatagramHandler;
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
 * {@link LANFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LANFragment
        extends
        Fragment
        implements
        SwipeRefreshLayout.OnRefreshListener,
        DatagramHandler,
        AdapterView.OnItemClickListener {
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.fragment_lan_lobbylist_listview)
    public ListView lobbyList;

    @BindView(R.id.fragment_lan_nickname_textview)
    public EditText nickname;

    @BindView(R.id.fragment_lan_lobbiesrefresh_swiperefreshlayout)
    public SwipeRefreshLayout lobbiesRefresh;

    @BindView(R.id.fragment_lan_lobbiesempty_textview)
    public TextView lobbiesEmpty;

    private ArrayList<Lobby> lobbies;
    private LANLobbiesAdapter lanLobbiesAdapter;

    private ParentActivity<MenuActivity> parent;

    private WebsocketClientHandler clientHandler;
    private WebsocketServerHandler serverHandler;

    //View Controller
    public LANFragment() {
        lobbies = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lan, container, false);
        ButterKnife.bind(this, view);
        parent = new ParentActivity<>(this);

        jsonParser = new JSONParser();

        lobbyList.setEmptyView(lobbiesEmpty);
        lobbies = new ArrayList<>();
        lanLobbiesAdapter = new LANLobbiesAdapter(getContext(), lobbies);
        lobbyList.setAdapter(lanLobbiesAdapter);
        lobbyList.setOnItemClickListener(this);

        lobbiesRefresh.setOnRefreshListener(this);
        clientHandler = new WebsocketClientHandler(parent.getParentActivity());
        serverHandler = new WebsocketServerHandler(parent.getParentActivity());

        nickname.setText(Application.getNickname());

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

        Application.setNickname(nickname.getText().toString());
    }

    @Override
    public void onPause() {
        Application.stopListening();
        lobbies.clear();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Application.startListening(this);
        Application.stopServer();
        Application.stopClient();
        sendLobbyRequest();
    }

    @OnClick(R.id.fragment_lan_createlobby_button)
    public void createLobbyClicked() {
        Application.startServer();
        Application.startClient(getResources().getString(R.string.address_local), serverHandler);
    }

    //Interface
    @Override
    public void onRefresh() {
        Application.stopClient();
        sendLobbyRequest();
        lanLobbiesAdapter.notifyDataSetChanged();
        lobbiesRefresh.setRefreshing(false);
    }

    JSONParser jsonParser;
    JSONObject json;
    @Override
    public void onDatagramReceive(InetAddress inetAddress, String data) {
        try {
            json = (JSONObject)jsonParser.parse(data);
            Object type = json.get(Application.getResources().getString(R.string.json_type));
            Object payload = json.get(Application.getResources().getString(R.string.json_type_payload));
            if(type.equals(Application.getResources().getString(R.string.json_reply))) {
                if(payload.equals(Application.getResources().getString(R.string.json_request_lobby))) {
                    Lobby lobby = new Lobby(
                            json.get(Application.getResources().getString(R.string.json_request_lobby_name)).toString(),
                            ((Long)json.get(Application.getResources().getString(R.string.json_request_lobby_players))).intValue(),
                            inetAddress);
                    lobbies.add(lobby);

                    parent.getParentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lanLobbiesAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            } catch (ParseException | NullPointerException e) {
            Timber.w(e.toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Lobby lobby = (Lobby)adapterView.getItemAtPosition(i);
        Application.currentLobby = lobby;
        Application.makeToast(
                parent.getParentActivity(),
                Application.getResources().getString(
                        R.string.string_toast_connecting,
                        lobby.getLobbyName()));
        Application.setNickname(nickname.getText().toString());
        Application.startClient(
                lobby.getLobbyAddress().getHostAddress(),
                clientHandler);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    //Methods
    protected void sendLobbyRequest() {
        lobbies.clear();
        lanLobbiesAdapter.notifyDataSetChanged();

        JSONObject json = new JSONObject();

        json.put(Application.getResources().getString(R.string.json_type), Application.getResources().getString(R.string.json_request));
        json.put(Application.getResources().getString(R.string.json_type_payload), Application.getResources().getString(R.string.json_request_lobby));
        new SendDatagram(Application.getResources().getInteger(R.integer.server_datagram_port)).executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                "broadcast",
                json.toJSONString());
    }
}
