package io.mavsdk.client;

import io.mavsdk.System;

public class MavsdkClient {
    private System drone;
    private String ip;
    private int port;
    private  boolean isConnected;

    public MavsdkClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        dispose();
        drone = new System(ip, port);
        isConnected = true;
    }

    public void dispose(){
        if (drone != null) {
            drone.dispose();
            isConnected = false;
        }
    }

    public static void main(String[] args){
        java.lang.System.out.println("Hello Mavsdk!");
    }
}
