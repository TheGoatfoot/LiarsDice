package com.everhardsoft.liarsdice.multiplayer.websocket;

import com.everhardsoft.liarsdice.Application;
import com.everhardsoft.liarsdice.R;
import com.everhardsoft.liarsdice.bean.Player;
import com.everhardsoft.liarsdice.game.master.LDMaster;
import com.everhardsoft.liarsdice.multiplayer.handler.ServerHandler;
import com.everhardsoft.liarsdice.runnable.VerifyUser;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Created by faisa on 10/7/2016.
 */

public class Server extends WebSocketServer {
    public final int playerCountMax;
    public final int playerCountMin;

    private ConcurrentLinkedHashMap<Player, Object> players;
    private ConcurrentLinkedHashMap<Player, Object> playingPlayers;

    private JSONParser jsonParser;

    private ServerHandler serverHandler;

    private boolean playing = false;
    private Object DUMMY = new Object();

    private LDMaster ldMaster = new LDMaster();

    private Runnable tick = new Runnable() {
        @Override
        public void run() {
            JSONObject json = new JSONObject();
            json.put(
                    Application.getResources().getString(R.string.json_type),
                    Application.getResources().getString(R.string.json_update));
            json.put(
                    Application.getResources().getString(R.string.json_type_payload),
                    Application.getResources().getString(R.string.json_update_tick));
            broadcastMessage(json.toJSONString());
        }
    };

    public Server(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));

        playerCountMax = Application.getResources().getInteger(R.integer.server_player_count_max);
        playerCountMin = Application.getResources().getInteger(R.integer.server_player_count_min);

        players = new ConcurrentLinkedHashMap.Builder<Player, Object>()
                .maximumWeightedCapacity(playerCountMax)
                .build();
        playingPlayers = new ConcurrentLinkedHashMap.Builder<Player, Object>()
                .maximumWeightedCapacity(playerCountMax)
                .build();

        jsonParser = new JSONParser();

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(
                tick,
                Application.getResources().getInteger(R.integer.server_tick_interval),
                Application.getResources().getInteger(R.integer.server_tick_interval),
                TimeUnit.SECONDS);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        if(getPlayerCount() >= playerCountMax) {
            Timber.d("Connected %s but rejected.", conn.getRemoteSocketAddress().getHostName());
            conn.close(Application.getResources().getInteger(R.integer.server_disconnect_full));
            return;
        }

        Timber.d("Connected %s", conn.getRemoteSocketAddress().getHostName());
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.schedule(
                new VerifyUser(this, conn),
                Application.getResources().getInteger(R.integer.server_verify_duration),
                TimeUnit.SECONDS);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Player player = getPlayer(conn);

        if(player != null)
            removePlayer(player);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Timber.d("WebSocket Received: '%s'", message);
        JSONObject json;
        try {
            json =  (JSONObject) jsonParser.parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        handleMessage(conn, json);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
    }

    //Game
    public void startGame() {
        if (getPlayerCount() < playerCountMin) {
            serverHandler.messageToast("Not enough player to start.");
            return;
        }
        ldMaster.startRound((String[]) players.keySet().toArray());
        broadcastGameStart();
    }

    //Handle
    private void handleRequestJoin(WebSocket conn, JSONObject json) {
        if(getPlayerCount() >= playerCountMax) {
            conn.close(Application.getResources().getInteger(R.integer.server_disconnect_full));
            return;
        }
        String nickname = json.get(Application.getResources().getString(R.string.json_request_join_nickname)).toString();
        nickname = handleNickname(nickname);

        JSONObject jsonSend = new JSONObject();

        jsonSend.put(
                Application.getResources().getString(R.string.json_type),
                Application.getResources().getString(R.string.json_reply));
        jsonSend.put(
                Application.getResources().getString(R.string.json_type_payload),
                Application.getResources().getString(R.string.json_request_join));
        jsonSend.put(Application.getResources().getString(R.string.json_reply_status), Application.getResources().getString(R.string.json_reply_status_ok));

        conn.send(jsonSend.toJSONString());
        addPlayer(new Player(nickname, conn));
    }

    private String handleNickname(String nickname) {
        if(nickname.isEmpty())
            nickname = Application.getResources().getString(R.string.preference_multiplayer_nickname_default);
        if(isPlayerExist(nickname)) {
            boolean found = false;
            int counter = 0;
            String newNickname;
            while (!found){
                newNickname = nickname+"[" + counter++ + "]";
                if(!isPlayerExist(nickname)) {
                    found = true;
                    nickname = newNickname;
                }
            }
        }
        return nickname;
    }

    private void handleMessage(WebSocket conn, JSONObject json) {
        Object type = json.get(Application.getResources().getString(R.string.json_type)).toString();
        Object payload = json.get(Application.getResources().getString(R.string.json_type_payload)).toString();
        if(type.equals(Application.getResources().getString(R.string.json_request)))
            if(payload.equals(Application.getResources().getString(R.string.json_request_join))) {
                handleRequestJoin(conn, json);
            } else if(payload.equals(Application.getResources().getString(R.string.json_request_playerlist))) {
                conn.send(createPlayerlistJSON().toJSONString());
            }
    }

    //Method
    private JSONObject createPlayerlistJSON() {
        JSONObject json = new JSONObject();
        JSONArray playerList = new JSONArray();
        playerList.addAll(getNicknames());
        json.put(
                Application.getResources().getString(R.string.json_type),
                Application.getResources().getString(R.string.json_update));
        json.put(
                Application.getResources().getString(R.string.json_type_payload),
                Application.getResources().getString(R.string.json_update_playerlist));
        json.put(
                Application.getResources().getString(R.string.json_update_playerlist_array),
                playerList);
        return json;
    }

    private void addPlayer(Player player) {
        players.put(player, DUMMY);
        broadcastPlayerList();
    }

    private void removePlayer(Player player) {
        players.remove(player);
        broadcastPlayerList();
    }

    private void broadcastGameStart() {
        JSONObject json = new JSONObject();
        json.put(
                Application.getResources().getString(R.string.json_type),
                Application.getResources().getString(R.string.json_update));
        json.put(
                Application.getResources().getString(R.string.json_type_payload),
                Application.getResources().getString(R.string.json_update_game_start));
        broadcastMessage(json.toJSONString());
    }

    public void broadcastDisconnect(int codeId) {
        JSONObject json = new JSONObject();
        json.put(
                Application.getResources().getString(R.string.json_type),
                Application.getResources().getString(R.string.json_request));
        json.put(
                Application.getResources().getString(R.string.json_type_payload),
                Application.getResources().getString(R.string.json_request_disconnect));
        json.put(
                Application.getResources().getString(R.string.json_request_disconnect_code),
                Application.getResources().getInteger(codeId));
        broadcastMessage(json.toJSONString());
    }

    public void broadcastPlayerList() {
        broadcastMessage(createPlayerlistJSON().toJSONString());
    }

    public void broadcastMessage(String message) {
        for (Player player : players.keySet()) {
            try {
                player.getWebsocket().send(message);
            } catch (Exception e) {
                Timber.w(e.toString());
            }
        }
    }
    public boolean isPlayerExist(WebSocket webSocket) {
        for(Player player:players.keySet())
            if(player.getWebsocket() == webSocket)
                return true;
        return false;
    }

    public boolean isPlayerExist(String nickname) {
        for(Player player:players.keySet())
            if(player.getNickname().equals(nickname))
                return true;
        return false;
    }
    public boolean isPlayerPlaying(String nickname) {
        for(Player player:playingPlayers.keySet())
            if(player.getNickname().equals(nickname))
                return true;
        return false;
    }
    public Player getPlayer(WebSocket conn) {
        for(Player player: players.keySet())
            if(player.getWebsocket() == conn)
                return player;
        return null;
    }
    public int getPlayerCount() {
        return players.size();
    }

    public ArrayList<String> getNicknames() {
        ArrayList<String> nicknames = new ArrayList<>();
        for(Player player:players.keySet())
            nicknames.add(player.getNickname());
        return nicknames;
    }
    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }
    public boolean isPlaying() {
        return playing;
    }
}
