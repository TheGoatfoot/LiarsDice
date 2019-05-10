package com.everhardsoft.liarsdice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.bean.Player;

import java.util.ArrayList;

/**
 * Created by faisa on 10/7/2016.
 */

public class LobbyPlayersAdapter extends BaseAdapter {
    private ArrayList<String> playerNicknames;
    private LayoutInflater inflater;

    public LobbyPlayersAdapter(Context context, ArrayList<String> playerNicknames) {
        this.playerNicknames = playerNicknames;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return playerNicknames.size();
    }

    @Override
    public Object getItem(int i) {
        return playerNicknames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private static class ViewHolder {
        private TextView playerName;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if(view == null) {
            viewHolder = new ViewHolder();

            view = inflater.inflate(R.layout.row_player, viewGroup, false);
            viewHolder.playerName = (TextView)view.findViewById(R.id.row_player_name_textview);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        String nickname = playerNicknames.get(i);
        viewHolder.playerName.setText(nickname);

        return view;
    }
}
