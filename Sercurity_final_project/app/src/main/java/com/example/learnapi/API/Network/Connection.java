package com.example.learnapi.API.Network;


public class Connection {
    public String BaseURL;

    public void CreateLocalConnection(String IPAddress , String port) {
        this.BaseURL = "http://"+IPAddress+":"+port+"/";
    }

    public void CreateConnection(String LinkAPI) {
        this.BaseURL = LinkAPI;
    }
}
