package com.Server;

import com.Net.NetManager;

import java.io.IOException;

public class AnotherThread extends Thread{
    private NetManager manager;
    private GameServer server;
    @Override
    public void run() {
        super.run();
        try {
            manager.setServer(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public AnotherThread(NetManager manager1,GameServer server0){
        server=server0;
        manager=manager1;
    }
}
