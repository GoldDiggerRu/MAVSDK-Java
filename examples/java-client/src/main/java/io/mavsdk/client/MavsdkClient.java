package io.mavsdk.client;

import io.mavsdk.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class MavsdkClient {
    private static final Logger logger = LoggerFactory.getLogger(MavsdkClient.class);

    private System drone;
    private String ip;
    private int port;
    private boolean isConnected;
    private boolean isArmed;

    public MavsdkClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void connect() {
        dispose();
        drone = new System(ip, port);
        isConnected = true;
    }

    public String arm(){
        AtomicReference<String> errorMsg = new AtomicReference<>("");
        CountDownLatch latch = new CountDownLatch(1);
        drone.getAction().arm()
                .doOnComplete(() -> isArmed = true)
                .doOnError(throwable -> {
                    isArmed = false;
                    errorMsg.set(throwable.getMessage());
                }).subscribe(latch::countDown, throwable -> latch.countDown());
        try {
            latch.await();
        } catch (InterruptedException e) {
            errorMsg.set(e.getMessage());
        }
        return errorMsg.get();
    }

    public String ping(){
        logger.debug("Pinging...");
        return "Success!";
    }

    public String disarm(){
        AtomicReference<String> errorMsg = new AtomicReference<>("");
        CountDownLatch latch = new CountDownLatch(1);
        drone.getAction().disarm()
                .doOnComplete(() -> isArmed = false)
                .doOnError(throwable -> {
                    isArmed = true;
                    errorMsg.set(throwable.getMessage());
                }).subscribe(latch::countDown, throwable -> latch.countDown());
        try {
            latch.await();
        } catch (InterruptedException e) {
            errorMsg.set(e.getMessage());
        }
        return errorMsg.get();
    }

    public void dispose(){
        if (drone != null) {
            drone.dispose();
            isConnected = false;
        }
    }

    public static void main(String[] args){
//        java.lang.System.out.println("Hello Mavsdk!");
//        MavsdkClient client = null;
//        try {
//            client = new MavsdkClient("127.0.0.1", 50051);
//            java.lang.System.out.println("Connecting drone...");
//            client.connect();
//            java.lang.System.out.println("Arming");
//            String msg1 = client.arm();
//            java.lang.System.out.println("Arming complete with " + msg1);
//            java.lang.System.out.println("Disarming");
//            String msg2 = client.disarm();
//            java.lang.System.out.println("Disarming complete with " + msg2);
//        }
//        finally {
//            if (client != null) {
//                client.dispose();
//            }
//        }
    }
}
