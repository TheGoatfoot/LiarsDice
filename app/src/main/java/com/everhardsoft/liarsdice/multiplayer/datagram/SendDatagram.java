package com.everhardsoft.liarsdice.multiplayer.datagram;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

import timber.log.Timber;

/**
 * Created by faisa on 9/15/2016.
 */
public class SendDatagram extends AsyncTask<String, Void, Void> {
    private int listenerPort;

    public SendDatagram(int port) {
        listenerPort=port;
    }

    @Override
    protected Void doInBackground(String... strings) {
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet;
            InetAddress ip;

            Timber.d("Sending '%s' to '%s'", strings[1], strings[0]);
            if(strings[0].equals("broadcast")) {
                Enumeration<NetworkInterface> networkInterfaceEnum = NetworkInterface.getNetworkInterfaces();
                while(networkInterfaceEnum.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaceEnum.nextElement();
                    List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();
                    for (InterfaceAddress interfaceAddress : interfaceAddresses) {
                        if (interfaceAddress.getBroadcast() != null) {
                            packet = new DatagramPacket(strings[1].getBytes(), strings[1].length(), interfaceAddress.getBroadcast(), listenerPort);
                            socket.send(packet);
                        }
                    }
                }
            } else {
                ip = InetAddress.getByName(strings[0]);
                packet = new DatagramPacket(strings[1].getBytes(), strings[1].length(), ip, listenerPort);
                socket.send(packet);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
