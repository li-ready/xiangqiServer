package com.com;

import com.Net.NetManager;
import com.Server.AnotherThread;
import com.Server.GameServer;

import java.io.IOException;
import java.net.ServerSocket;

public class StartServer {
    public static void main(String[] args) throws IOException {
        GameServer server=new GameServer();
        ServerSocket socket=new ServerSocket(6666);
        NetManager manager0=new NetManager(socket);
        NetManager manager1=new NetManager(socket);
        server.SetManager(manager1);server.SetManager(manager0);
        AnotherThread thread1=new AnotherThread(manager0,server);
        thread1.start();
        manager1.setServer(server);
    }
}
