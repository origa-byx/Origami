package com.ori.origami;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @by: origami
 * @date: {2021-06-03}
 * @info:
 **/
public class Print {

    private static Print mPrint;

    private Print(){ }

    public synchronized static Print getInstance(){
        if(mPrint == null){ mPrint = new Print(); }
        return mPrint;
    }
//--------------------------------------------------------------------------

    private Socket mSocket;

    private String ip;

    private int port;

    private OutputStream outputStream;

    public boolean connect(String ip, int port){
        if(mSocket != null && this.ip.equals(ip) && this.port == port){
            return true;
        }
        try {
            if(mSocket != null){ mSocket.close(); }
            this.ip = ip;
            this.port = port;
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            mSocket = new Socket();
            mSocket.setSoTimeout(20000);
            mSocket.connect(socketAddress);
            outputStream = mSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
