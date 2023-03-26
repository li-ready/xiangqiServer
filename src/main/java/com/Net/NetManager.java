package com.Net;


import com.Itf.ServerNetSender;
import com.Server.GameServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ServerSocket serverSocket;
    private boolean Contiune=true;
    public int intemp=0;
    public String intTOMess(int ss){
        temp1="["+ss+"]";
        return temp1;
    }
    public NetManager(ServerSocket socket)
    {
       serverSocket=socket;
    }
    public void setServer(GameServer server0) throws IOException {
        server=server0;
        while(true) {
            connect();
            System.out.println("connect success");
            Contiune=true;
            AccpetMess();
            System.out.println("disconnect2");
        }
    }
    @Override
    public void zouqi(int ORole_id, int x, int y) {
        System.out.println("zouqiing");//待删除
        out.println("Zouqi000:"+ ORole_id+","+x+","+y);
    }

    @Override
    public void takeqi(int ORole_id, int PRole_id) {
        out.println("Takeqi00:"+ ORole_id+ ","+PRole_id);
    }

    @Override
    public void Refresh(String refresh_array) {

    }

    @Override
    public void ExtendData(String data) {

    }

    @Override
    public void setclient_id(int id) {
        out.println("Idset000:"+ id);
        System.out.println("setid="+id);
        RBset(id);
    }
    public void  RBset(int id){
        if(server.RBint[0]==1000){
            server.RBint[0]=id;
            out.println("RBset000:"+ 0);
        }
        else {
            if (server.RBint[1] == 1000) {
                server.RBint[1] = id;
                out.println("RBset000:" + 1);
            }
        }
    }
    @Override
    public void client_Mustdisconnect(String data) {

    }

    @Override
    public void connect() throws IOException {
        System.out.println("Server started. Listening on port: "+port+"...");
            clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("connect1");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("connect2");

            //client_id= server.connet_setclient_id();//id分配功能待完善
        }
    public void AccpetMess() {
        System.out.println("AccpetMess");
        try {
            boolean def=true;
            while (def&&((inputLine = in.readLine()) != null)) {
                int client_id=Integer.parseInt(inputLine.substring(0,4));
                System.out.println("收到信息");//待删除
                System.out.println(inputLine);
                temp1=inputLine.substring(4,12);
                System.out.println(temp1);
                int i=inputLine.indexOf(":");
                System.out.println("i= "+i);
                if(i!=-1){
                    temp2=inputLine.substring(i+1,inputLine.length());
                }
                System.out.println(temp2);
               switch (temp1){
                   case "refresh0":def=server.RefreshAsk(client_id);break;
                   case "Zouqi000":{

                       System.out.println("zouqi use");//待删除
                       int x=0;int y=0;
                       String[] result = temp2.split(",");
                       System.out.println(1);
                       System.out.println(2);
                       System.out.println("group count= "+result.length);//待删除
                      for (String ss : result) {
                          System.out.println(ss);
                      }//待删除
                       System.out.println(3);
                       if(result.length>2) {
                           System.out.println(4);//待删除
                           System.out.println(Integer.parseInt(result[0]) + "  " + Integer.parseInt(result[1]) + "  " + Integer.parseInt(result[2]));
                           System.out.println(5);//待删除
                           def=server.zouqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]), client_id);
                       }
                       break;
                   }
                   case "Takeqi00":{
                       String[] result = temp2.split(",");
                       if(result.length>1) {
                           def=server.takeqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]), client_id);
                       }
                       break;
                   }
                   case "ExData00":{
                       def=server.ExtendData(temp2,client_id);break;
                   }
                   case "Adisconn":{
                       def=server.Client_AskDisconnected(client_id);
                   }
                   case "Idsend00":{
                       int idt=server.client_id_cal(client_id);
                       System.out.println("idt= "+idt);
                       if(idt==-1){//断连功能
                           serverSocket.close();
                           serverSocket=null;
                       }
                       else{
                           setclient_id(idt);
                           System.out.println("set id = "+idt);
                           System.out.println("client = " + clientSocket);
                       }
                   }
               }
            }
            if(def==false){
                serverSocket.close();
                serverSocket=null;
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
            Contiune=false;
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                Contiune=false;
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}

