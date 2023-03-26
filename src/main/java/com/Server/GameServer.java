package com.Server;


import com.Itf.ServerNetListener;
import com.Net.NetManager;

import java.util.Random;
import java.util.function.DoubleToIntFunction;

public class GameServer implements ServerNetListener {
    public int[] RBint;
    private NetManager manager0;
    private NetManager manager1;
    private NetManager managerTemp;
    Random random=new Random();
    public int id0=1000;
    public int id1=1000;
    int idcount=-1;
    private boolean RB=true;
    public NetManager managerOPSelect(int id){
        if(id==id0)
        {
            return manager1;
        }
        if(id==id1){
            return manager0;
        }
        return null;
    }
    public int randomId(){
        int out=1000;
        while((out==1000)|(out==id0)|(out==id1)) {
            out = random.nextInt(8997);
            out = out + 1001;
        }
        return out;
    }
    public int client_id_cal(int id){
        if(id==1000){
            if(id0==1000){
                id0=randomId();
                return id0;
            }
            if(id1==1000){
                id1=randomId();
                return id1;
            }
            return -1;
        }
        if((id==id0)|(id==id1)){
            return id;
        }
        return -1;
    }
    public GameServer(){
        manager0=null;manager1=null;
        RBint=new int[2];
        RBint[0]=1000;RBint[1]=1000;
    }
    public void SetManager(NetManager manager){
        if(manager0==null)
        { manager0=manager;}
        else{
            manager1=manager;
        }
    }
    @Override
    public boolean zouqi(int ORole_id, int x, int y, int client_id) {
        managerTemp= managerOPSelect(client_id);
        if(managerTemp==null)return false;
        else{
            managerTemp.zouqi(ORole_id,x,y);
            return true;
        }
    }

    @Override
    public boolean takeqi(int ORole_id, int PRole_id, int client_id) {
        managerTemp= managerOPSelect(client_id);
        if(managerTemp==null)return false;
        else{
            managerTemp.takeqi(ORole_id,PRole_id);
            return true;
        }
    }

    @Override
    public boolean RefreshAsk(int client_id) {
        return false;
    }

    @Override
    public boolean Client_Disconnected(float time, int client_id) {//client无告知断联
        return false;
    }

    @Override
    public boolean Client_AskDisconnected(int client_id) {
        return false;
    }

    @Override
    public boolean ExtendData(String data, int client_id) {
        return false;
    }

}
