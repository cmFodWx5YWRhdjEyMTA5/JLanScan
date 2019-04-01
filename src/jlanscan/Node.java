/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlanscan;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by george on 5/18/17.
 */

public class Node {
    String ip;
    String mac;
    String canonicalHostName;
    String hostName;
    String remark;
    boolean isReachable;
    int progressBar;

    public Node(String ip) { 
        this.ip = ip;
    }
    
    public Node(String ip, String mac) {
        this.ip = ip;
        this.mac = mac;
    }

    public Node(String ip, String mac, String hostname, int progressBar){
        this.ip = ip;
        this.mac = mac;
        this.hostName = hostname;
        this.progressBar = progressBar;        
    }

    public void setProgressBar(int progressBar) {
        this.progressBar = progressBar;
    }

    public String getIp() {
        return ip;
    }

    public String getHostName() {
        return hostName;
    }

    public String getMac() {
        return mac;
    }

    public String getRemark() {
        return remark;
    }

    public boolean isReachable() {
        return isReachable;
    }

    @Override
    public String toString() {
        return "IP: " + ip + "\n" +
                "MAC: " + mac + "\n" +
                "HostName:\t" + hostName;
    }
}