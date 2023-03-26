package com.Itf;

public interface ServerNetListener {
public boolean zouqi(int ORole_id, int x, int y, int client_id);
public boolean takeqi(int ORole_id, int PRole_id, int client_id);
public boolean RefreshAsk(int client_id);
public boolean Client_Disconnected(float time, int client_id);
public boolean Client_AskDisconnected(int client_id);
public boolean ExtendData(String data, int client_id);
}
