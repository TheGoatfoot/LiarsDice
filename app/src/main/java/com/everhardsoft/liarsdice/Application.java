package com.everhardsoft.liarsdice;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.everhardsoft.liarsdice.bean.Lobby;
import com.everhardsoft.liarsdice.multiplayer.handler.ClientHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.ServerHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.WebsocketHandler;
import com.everhardsoft.liarsdice.multiplayer.handler.DatagramHandler;
import com.everhardsoft.liarsdice.multiplayer.datagram.ListenDatagram;
import com.everhardsoft.liarsdice.multiplayer.websocket.Client;
import com.everhardsoft.liarsdice.multiplayer.websocket.Server;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import timber.log.Timber;

/**
 * Created by faisa on 10/4/2016.
 */

public class Application {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static Resources resources;
    private static ListenDatagram listenDatagram;
    private static Server server;
    private static Client client;

    private static String storedNickname;

    public static Lobby currentLobby;

    public static void initialize(Context context) {
        if(BuildConfig.DEBUG && Timber.treeCount() == 0)
            Timber.plant(new Timber.DebugTree());
        resources = context.getResources();

        sharedPreferences = context.getSharedPreferences(
                resources.getString(R.string.key_preference),
                Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
    }


    public static Resources getResources() {
        return resources;
    }

    public static String getNickname() {
        if(storedNickname == null)
            return sharedPreferences.getString(
                    resources.getString(R.string.preference_multiplayer_nickname),
                    resources.getString(R.string.preference_multiplayer_nickname_default));
        return storedNickname;
    }

    public static void setNickname(String nickname) {
        String nicknameFinal = nickname.trim();
        if(nicknameFinal.length() > 12)
            nicknameFinal = "Too Long";
        if(nicknameFinal.isEmpty()) {
            editor.putString(
                    resources.getString(R.string.preference_multiplayer_nickname),
                    resources.getString(R.string.preference_multiplayer_nickname_default)
            );
            storedNickname = resources.getString(R.string.preference_multiplayer_nickname_default);
        } else {
            editor.putString(
                    resources.getString(R.string.preference_multiplayer_nickname),
                    nicknameFinal
            );
            storedNickname = nicknameFinal;
        }
        editor.commit();
    }

    public static void makeToast(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public static void stopClient() {
        if(client != null) {
            client.setWebsocketHandler(null);
            client.close();
            client = null;
            Timber.d("Client stopped.");
        }
    }

    public static void startClient(String address, @Nullable WebsocketHandler listener) {
        stopClient();
        try {
            client = new Client(address, getResources().getInteger(R.integer.server_websocket_port));
            client.setWebsocketHandler(listener);
            client.connect();
            Timber.d("Client started.");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void stopServer() {
        try {
            if (server != null) {
                server.broadcastDisconnect(R.integer.server_disconnect_safe);
                server.stop();
                server = null;
                Timber.d("Server stopped.");
            }
        }catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() {
        stopServer();
        try {
            server = new Server(getResources().getInteger(R.integer.server_websocket_port));
            server.start();
            Timber.d("Server started.");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static int getServerPlayerCount() {
        return server.getPlayerCount();
    }

    public static void startListening(DatagramHandler datagramHandler) {
        stopListening();
        listenDatagram = new ListenDatagram(resources.getInteger(R.integer.server_datagram_port));
        listenDatagram.datagramHandler = datagramHandler;
        listenDatagram.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void stopListening() {
        if(listenDatagram != null)
            listenDatagram.stop();
    }

    public static void setClientHandler(ClientHandler clientHandler) {
        client.setClientHandler(clientHandler);
    }

    public static void setServerHandler(ServerHandler serverHandler) {
        server.setServerHandler(serverHandler);
    }

    public static void requestPlayerlist() {
        client.requestPlayerlist();
    }

    public static void startGame() {
        server.startGame();
    }
}
