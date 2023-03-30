package com.Server;


import com.Itf.ServerNetListener;
import com.Net.NetManager;

import java.util.ArrayList;
import java.util.Random;

public class GameServer implements ServerNetListener {
    public int[] RBint;

    public NetManager getManager() {
        return manager;
    }

    public void setManager(NetManager manager) {
        this.manager = manager;
    }

    private NetManager manager;
    private NetManager managerTemp;
    Random random=new Random();
    public int id0=1000;
    public int id1=1000;
    int idcount=-1;
    private boolean RB=true;
    private String temp1;
    private String temp2;
    private int def;
    private ArrayList<Integer> LoseConnectPlayers=new ArrayList<Integer>();
    private ArrayList<Integer> players=new ArrayList<Integer>();
    private int playMax=2;
    public synchronized ArrayList<Integer> getLoseConnectPlayers() {
        return LoseConnectPlayers;
    }
    public synchronized void setLoseConnectPlayers(ArrayList<Integer> loseConnectPlayers) {
        LoseConnectPlayers = loseConnectPlayers;
    }
    public int getClientMess(String inputLine){
        int client_id=Integer.parseInt(inputLine.substring(0,4));
        System.out.println("get imformaiton");//待删除
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
            case "refresh0":{
                RefreshAsk(client_id);break;
            }
            case "Zouqi000":{
                String[] result = temp2.split(",");
                    if(result.length>2) {
                        zouqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]), client_id);
                    }
                break;
                }

            case "RBask000":{
                System.out.println("get RBask client_id= "+client_id);
                RBset(client_id);
                break;
            }
            case "Takeqi00":{
                String[] result = temp2.split(",");
                if(result.length>1) {
                    takeqi(Integer.parseInt(result[0]), Integer.parseInt(result[1]), client_id);
                }
                break;
            }
            case "ExData00":{
                ExtendData(temp2,client_id);break;
            }
            case "Adisconn":{
                Client_AskDisconnected(client_id); break;
            }
            case "HasloseC":{
                //client_id是发信息的id
                LoseConnectWait(client_id); break;
            }
        }
        //清除断连序列
        for (int j = 0; j < LoseConnectPlayers.size(); j++) {
            if(LoseConnectPlayers.get(j)==client_id){
                LoseConnectPlayers.remove(j);
            }
        }
        return 0;
    }
    /*public void setclient_id(int id) {
        out.println("Idset000:"+ id);
        System.out.println("setid="+id);
        RBset(id);
    }*/
    //断线等待重连的方法
    public void LoseConnectWait(int id){//connect线程依然在运行,并调用了这个方法\
        boolean jixu=true;
        LoseConnectPlayers.add(id);//加入断连队列
        long timestart=System.currentTimeMillis();
        long time=0;
        while(jixu){
            try {
                Thread.sleep(1000);
                time=timestart-System.currentTimeMillis();
                for (Integer player:LoseConnectPlayers) {
                    jixu=false;
                    if(player==id){
                        jixu=true;
                    }
                    //30秒超时
                    if(time>30000){
                        jixu=false;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(time<30000){

            Refresh(id);
        }
        else{
            win(managerOPSelect(id));
            //待添加,id的player已经断连,启动输赢方法
        }
    }
    public void win(int winPlayer){
       manager.MessToClient("WinLose0:"+0,winPlayer);
       manager.MessToClient("WinLose0:"+1,managerOPSelect(winPlayer));
    }
    public int managerOPSelect(int id){//找到对家,如果输入是1000或者没有对家,输出1000
        int OpPlayer=1000;
            if(players.size()==2){
            OpPlayer=(players.get(0)==id)?players.get(1):players.get(0);
        }
       return OpPlayer;
    }
    public int randomId(){
        int out=1000;
        while((out==1000)|(out==id0)|(out==id1)) {
            out = random.nextInt(8997);
            out = out + 1001;
        }
        return out;
    }
    //有没有这个链接记录着
    public boolean client_id_here(int id){
        if(id==1000)return false;
        if((id==id0)|(id==id1))return true;
        return false;
    }
    public int set_client_id_cal(int id){//待添加断线重连功能
        boolean t=true;
        System.out.println("set_id id:"+id);
        for (int i : players) {
            System.out.println("players has:"+i);
            if(i==id){//可能是断线重连
                System.out.println("has same name");
                for (int j = 0; j < getLoseConnectPlayers().size(); j++) {
                    {
                        if (LoseConnectPlayers.get(j)==id){
                            LoseConnectPlayers.remove(j);
                        }
                    }
                }
                return id;
            }
        }
            if(players.size()<playMax){
                int a=randomId();System.out.println("setId:"+a);
                players.add(a);
                return a;
        }else{
            return -1;
        }
    }
    public GameServer(){
        manager=null;
        RBint=new int[2];
        RBint[0]=1000;RBint[1]=1000;
    }
    public int zouqi(int ORole_id, int x, int y, int client_id) {
        System.out.println("zouqiing:"+client_id);//待删除
        manager.MessToClient("Zouqi000:"+ORole_id+","+x+","+y,managerOPSelect(client_id));
        return 0;
    }
    public int takeqi(int ORole_id, int PRole_id, int client_id) {//client_id是发送方客户端
        System.out.println("takeqi:"+client_id);//待删除
        manager.MessToClient("Takeqi00:"+ORole_id+","+PRole_id,managerOPSelect(client_id));
        return 0;
    }
    public int RefreshAsk(int client_id) {
        return -1;
    }
    public int Client_Disconnected(float time, int client_id) {//client无告知断联
        return -1;
    }
    public int Client_AskDisconnected(int client_id) {
        return -1;
    }
    public int ExtendData(String data, int client_id) {
        return -1;
    }
    public int client_Mustdisconnect(int id) {
        return 0;
    }
    //一下是模板

    public void Refresh(int id) {
    }
    public void ExtendData(String data) {
    }

    public void  RBset(int id){
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i)==id){
                System.out.println("send RBset:"+"RBset000:"+i+"id:"+id);
                manager.MessToClient("RBset000:"+i,id);
            }
        }
    }

}
