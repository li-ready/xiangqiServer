package com.Net;
import com.Server.GameServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
public class ConnectThread extends Thread{
    public boolean live=true;
    private String inputLine;
    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;
    private GameServer server;
    public int id=1000;
    private long time;
    private long deltatime;
    boolean cz=true;
    public ConnectThread(BufferedReader in0,PrintWriter out0,Socket socket0,GameServer server1){
        server=server1;
        in=in0;out=out0;socket=socket0;
        inputLine=new String();
    }
    @Override
    public void run() {
        super.run();
        System.out.println("AccpetMess from:"+id);
      /*  new Thread(){
            @Override
            public void run() {
                super.run();
                while(true){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(cz) {
                        time = System.currentTimeMillis(); cz=false;
                    }
                    deltatime=System.currentTimeMillis()-time;
                    if(deltatime>5000){
                        live=false;break;
                    }
                }
            }
        }.start();*/
        try{
            while (((inputLine = in.readLine()) != null)) {
                cz=true;//收到消息了,没掉线计时器重置
                    if ("Idsend00".equals(inputLine.substring(4, 12)))
                    {
                        id=server.set_client_id_cal(Integer.parseInt(inputLine.substring(0,4)));
                        out.println("Idset000:"+id);
                        if(id==-1){
                            live=false;break;
                        }
                    }
                    else {
                        server.getClientMess(inputLine);
                    }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
            live=false;
            server.getClientMess(id+"HasloseC:");//断线处理
        } finally {live=false;
        }
    }

    public void MessToClient(String output){
        System.out.println("send2");
        out.println(output);
    }
}
