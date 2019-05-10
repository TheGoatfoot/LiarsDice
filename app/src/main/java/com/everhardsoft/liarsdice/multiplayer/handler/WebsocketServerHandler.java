package com.everhardsoft.liarsdice.multiplayer.handler;

import com.everhardsoft.liarsdice.activity.MenuActivity;
import com.everhardsoft.liarsdice.fragment.LANLobbyHostFragment;

/**
 * Created by faisa on 10/11/2016.
 */

public class WebsocketServerHandler implements WebsocketHandler {
    private MenuActivity activity;
    public WebsocketServerHandler(MenuActivity activity) {
        this.activity = activity;
    }

    @Override
    public void clientClose(int code) {
    }

    @Override
    public void clientVerified() {
        activity.fragmentStackManager.push(new LANLobbyHostFragment());
    }
}
