package com.everhardsoft.liarsdice.multiplayer.handler;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.activity.MenuActivity;
import com.everhardsoft.liarsdice.fragment.LANFragment;
import com.everhardsoft.liarsdice.fragment.LANLobbyClientFragment;

/**
 * Created by faisa on 10/11/2016.
 */

public class WebsocketClientHandler implements WebsocketHandler {
    private MenuActivity activity;
    public WebsocketClientHandler(MenuActivity activity) {
        this.activity = activity;
    }

    @Override
    public void clientClose(int code) {
        String message = "";
        if(Application.getResources().getInteger(R.integer.server_disconnect_safe) != code) {
            if (Application.getResources().getInteger(R.integer.server_disconnect_unsafe) == code)
                message += Application.getResources().getString(R.string.string_toast_disconnected_unsafe);
            else if (Application.getResources().getInteger(R.integer.server_disconnect_full) == code)
                message += Application.getResources().getString(R.string.string_toast_disconnected_full);
            else if (Application.getResources().getInteger(R.integer.server_disconnect_invalid) == code)
                message += Application.getResources().getString(R.string.string_toast_disconnected_invalid);
            else if (Application.getResources().getInteger(R.integer.server_disconnect_verify) == code)
                message += Application.getResources().getString(R.string.string_toast_disconnected_verify);
        }
        Application.makeToast(
                activity,
                Application.getResources().getString(
                        R.string.string_toast_disconnected,
                        Application.currentLobby.getLobbyName(),
                        message));
        activity.fragmentStackManager.popToNearest(LANFragment.class);
    }

    @Override
    public void clientVerified() {
        activity.fragmentStackManager.push(new LANLobbyClientFragment());
    }
}
