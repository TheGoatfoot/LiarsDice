package com.everhardsoft.liarsdice.multiplayer.handler;

import java.net.InetAddress;

/**
 * Created by faisa on 9/18/2016.
 */
public interface DatagramHandler {
    void onDatagramReceive(InetAddress inetAddress, String data);
}
