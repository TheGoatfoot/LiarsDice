package com.everhardsoft.liarsdice.bean;

import java.net.InetAddress;

/**
 * Created by faisa on 10/6/2016.
 */

public class Lobby {
    private String lobbyName;
    private int lobbyPlayer;
    private InetAddress lobbyAddress;
    public Lobby(String lobbyName, int lobbyPlayer, InetAddress lobbyAddress) {
        this.lobbyName = lobbyName;
        this.lobbyPlayer = lobbyPlayer;
        this.lobbyAddress = lobbyAddress;
    }
    public String getLobbyName() {
        return lobbyName;
    }
    public int getLobbyPlayer() {
        return lobbyPlayer;
    }
    public InetAddress getLobbyAddress() {
        return lobbyAddress;
    }
}
