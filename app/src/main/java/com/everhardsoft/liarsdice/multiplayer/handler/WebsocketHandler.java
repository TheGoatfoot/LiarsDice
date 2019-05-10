package com.everhardsoft.liarsdice.multiplayer.handler;

/**
 * Created by faisa on 10/9/2016.
 */

public interface WebsocketHandler {
    void clientClose(int code) ;
    void clientVerified() ;
}
