package com.everhardsoft.liarsdice.multiplayer.datagram;

import android.os.AsyncTask;

import com.everhardsoft.liarsdice.multiplayer.handler.DatagramHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import timber.log.Timber;

/**
 * Created by faisa on 9/18/2016.
 */
public class ListenDatagram extends AsyncTask<Void, Void, Void> {
    private boolean listening = true;
    public DatagramHandler datagramHandler = null;
    private DatagramSocket socket;

    private int listenerPort;

    public ListenDatagram(int port) {
        listenerPort = port;
    }

    public void stop() {
        listening = false;
        if(socket!= null)
            socket.close();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            socket = new DatagramSocket(listenerPort);
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, 1024);

            while(listening) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                InetAddress address = packet.getAddress();
                Timber.d("Received: '%s' from '%s'", message, address.getHostAddress());
                if(datagramHandler != null)
                    datagramHandler.onDatagramReceive(address, message);
            }
        } catch (Exception e) {
            Timber.w(e.toString());
        }
        return null;
    }
}
