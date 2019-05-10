package com.everhardsoft.liarsdice.bean;

import org.java_websocket.WebSocket;

/**
 * Created by faisa on 9/26/2016.
 */

public class Player {
    private String nickname;
    private WebSocket websocket;

    public Player(String nickname, WebSocket webSocket) {
        this.nickname = nickname;
        this.websocket = webSocket;
    }

    public String getNickname() {
        return nickname;
    }

    public WebSocket getWebsocket() {
        return websocket;
    }
}
