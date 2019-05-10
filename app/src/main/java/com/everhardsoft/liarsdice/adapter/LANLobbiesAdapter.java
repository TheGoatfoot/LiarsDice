package com.everhardsoft.liarsdice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.bean.Lobby;

import java.util.ArrayList;

/**
 * Created by faisa on 10/6/2016.
 */

public class LANLobbiesAdapter extends BaseAdapter {
    private ArrayList<Lobby> lobbies;
    private LayoutInflater inflater;

    public LANLobbiesAdapter(Context context, ArrayList<Lobby> lobbies) {
        this.lobbies = lobbies;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lobbies.size();
    }

    @Override
    public Object getItem(int i) {
        return lobbies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        private TextView lobbyName;
        private TextView lobbyPlayer;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if(view == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.row_lobby, viewGroup, false);
            viewHolder.lobbyName = (TextView)view.findViewById(R.id.row_lobby_name_textview);
            viewHolder.lobbyPlayer = (TextView)view.findViewById(R.id.row_lobby_players_textview);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        Lobby lobby = lobbies.get(i);
        viewHolder.lobbyName.setText(
                Application.getResources().getString(
                        R.string.string_row_lobby_name,
                        lobby.getLobbyName()));
        viewHolder.lobbyPlayer.setText(
                Application.getResources().getString(
                        R.string.string_row_lobby_players,
                        lobby.getLobbyPlayer(),
                        Application.getResources().getInteger(R.integer.server_player_count_max),
                        (lobbies.size() > 1)?"s.":"."));

        return view;
    }
}
