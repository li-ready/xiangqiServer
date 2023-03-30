package com.com;

import com.Net.NetManager;
import com.Server.GameServer;

import java.io.IOException;
import java.net.ServerSocket;

public class StartServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        GameServer server=new GameServer();
        NetManager manager=new NetManager(server);
    }
}
