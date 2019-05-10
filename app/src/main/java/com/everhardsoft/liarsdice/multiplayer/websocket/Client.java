package com.everhardsoft.liarsdice.multiplayer.websocket;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.multiplayer.handler.ClientHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.WebsocketHandler;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import timber.log.Timber;

/**
 * Created by faisa on 10/7/2016.
 */

public class Client extends WebSocketClient {
    private WebsocketHandler websocketHandler = null;
    private ClientHandler clientHandler = null;
    private JSONParser jsonParser;

    private int disconnectCode;

    public Client(String address, int port) throws URISyntaxException {
        super(new URI("ws://" + address + ":" + port));
        jsonParser = new JSONParser();
        disconnectCode = Application.getResources().getInteger(R.integer.server_disconnect_unsafe);
    }

    public void setWebsocketHandler(WebsocketHandler websocketHandler) {
        this.websocketHandler = websocketHandler;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Timber.d("Connected to server.");
        sendJoin();
    }

    @Override
    public void onMessage(String message) {
        Timber.d("WebSocket Received: '%s'", message);
        JSONObject json;
        try {
            json =  (JSONObject) jsonParser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        handleMessage(json);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        if(websocketHandler != null)
            websocketHandler.clientClose(disconnectCode);
    }

    @Override
    public void onError(Exception ex) {
    }

    private void handleMessage(JSONObject json) {
        Object type = json.get(Application.getResources().getString(R.string.json_type));
        Object payload = json.get(Application.getResources().getString(R.string.json_type_payload));
        if(type.equals(Application.getResources().getString(R.string.json_reply))) {
            Object status = json.get(Application.getResources().getString(R.string.json_reply_status));
            if(payload.equals(Application.getResources().getString(R.string.json_request_join)))
                if(status.equals(Application.getResources().getString(R.string.json_reply_status_ok)))
                    websocketHandler.clientVerified();
        } else if(type.equals(Application.getResources().getString(R.string.json_request))) {
            if(payload.equals(Application.getResources().getString(R.string.json_request_disconnect)))
                disconnectCode = (int)json.get(Application.getResources().getString(R.string.json_request_disconnect_code));
        } else if(type.equals(Application.getResources().getString(R.string.json_update))) {
            if(payload.equals(Application.getResources().getString(R.string.json_update_playerlist))) {
                if(clientHandler == null)
                    return;
                JSONArray jsonPlayers = (JSONArray)json.get(Application.getResources().getString(R.string.json_update_playerlist_array));
                ArrayList<String> nicknames = new ArrayList<>();
                for(Object nickname:jsonPlayers)
                    nicknames.add((String)nickname);
                clientHandler.updatePlayerList(nicknames);
            } else if(payload.equals(Application.getResources().getString(R.string.json_update_game_start))) {
                clientHandler.gameStart();
            }
        }
    }

    public void sendJoin() {
        JSONObject json = new JSONObject();

        json.put(
                Application.getResources().getString(R.string.json_type),
                Application.getResources().getString(R.string.json_request));
        json.put(
                Application.getResources().getString(R.string.json_type_payload),
                Application.getResources().getString(R.string.json_request_join));
        json.put(Application.getResources().getString(R.string.json_request_join_nickname), Application.getNickname());

        send(json.toJSONString());
    }

    public void requestPlayerlist() {
        JSONObject json = new JSONObject();

        json.put(
                Application.getResources().getString(R.string.json_type),
                Application.getResources().getString(R.string.json_request));
        json.put(
                Application.getResources().getString(R.string.json_type_payload),
                Application.getResources().getString(R.string.json_request_playerlist));

        send(json.toJSONString());

    }
}
