package com.example.demo;

public class ServerDAO {
    private long serverId;
    private String serverName;
    private int serverCores;
    private int serverCpus;
    private String serverOs;

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getServerCores() {
        return serverCores;
    }

    public void setServerCores(int serverCores) {
        this.serverCores = serverCores;
    }

    public int getServerCpus() {
        return serverCpus;
    }

    public void setServerCpus(int serverCpus) {
        this.serverCpus = serverCpus;
    }

    public String getServerOs() {
        return serverOs;
    }

    public void setServerOs(String serverOs) {
        this.serverOs = serverOs;
    }
}
