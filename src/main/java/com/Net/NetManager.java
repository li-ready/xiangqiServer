package com.Net;


import com.Itf.ServerNetSender;
import com.Server.GameServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetManager extends Thread implements ServerNetSender {
    //private volatile boolean flag = true;
    private int port=6666;
    private String inputLine;
    private String temp1;
    private String temp2;
    private GameServer server;
    private Thread threadClient;
    private Thread threadServer;
    private ArrayList<ConnectThread> connects;
    //private ArrayList<Socket> sockets;
    private ServerSocket serverSocket;
    private boolean Contiune=true;
    public int intemp=0;
    private int playNum=2;
    private int playCount=0;
    public boolean jixuyunxing=true;//控制所有的匿名类是否继续运行
    public String intTOMess(int ss){
        temp1="["+ss+"]";
        return temp1;
    }
    //主逻辑
    public NetManager(GameServer server1) throws IOException, InterruptedException {
       serverSocket=new ServerSocket(port);
       server=server1;

       server.setManager(this);
       connects=new ArrayList<ConnectThread>();
        while(jixuyunxing){
            Thread.sleep(500);
            ConnectManager();
        }
    }
    public synchronized void ConnectManager() throws IOException {
        for (int i = 0; i < connects.size(); i++) {
            if(connects.get(i).live==false)
                connects.remove(i);
        }
        if(connects.size()<playNum){
            connect();
        }
    }
    public void MessToClient(String output,int id0){
        System.out.println("Connect0 id:"+connects.get(0).id+"  id0:"+id0);
        for (ConnectThread connectThread : connects) {
            if(connectThread.id==id0){
                System.out.println("send1");
                connectThread.MessToClient(output);
            }
        }
    }
    public void connect() throws IOException {
        System.out.println("Server started. Listening on port: "+port+"...");
        Socket socket = serverSocket.accept();
        System.out.println("New client connected: " + socket);
        socket.setSoTimeout(5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //添加in的引用
        System.out.println("connect1");
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //添加out的引用
        System.out.println("connect2");
        ConnectThread connect=new ConnectThread(in,out,socket,server);
        connects.add(connect);
        connect.start();//创建新线程
        }

}

