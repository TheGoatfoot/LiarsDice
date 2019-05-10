package com.everhardsoft.liarsdice.multiplayer.handler;

import java.util.ArrayList;

/**
 * Created by faisa on 10/12/2016.
 */

public interface ClientHandler {
    void updatePlayerList(ArrayList<String> nicknames);
    void gameStart();
}
