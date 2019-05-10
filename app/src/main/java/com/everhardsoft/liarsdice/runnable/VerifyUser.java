package com.everhardsoft.liarsdice.runnable;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.multiplayer.websocket.Server;

import org.java_websocket.WebSocket;

/**
 * Created by faisa on 10/9/2016.
 */

public class VerifyUser implements Runnable {
    private WebSocket webSocket;
    private Server server;

    public VerifyUser(Server server, WebSocket webSocket) {
        this.webSocket = webSocket;
        this.server = server;
    }

    @Override
    public void run() {
        if(webSocket.isClosed())
            return;
        if(!server.isPlayerExist(webSocket))
            webSocket.close(Application.getResources().getInteger(R.integer.server_disconnect_verify));
    }
}
