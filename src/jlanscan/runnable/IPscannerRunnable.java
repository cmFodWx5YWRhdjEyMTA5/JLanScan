/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jlanscan.runnable;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import jlanscan.Node;

public class IPscannerRunnable implements Runnable {
    int startIp, stopIp;
    String subnet;
    private List<Node> liveHosts;
    private static final String TAG = "IPSCANRUNNABLE";
    private final static int TIMEOUT = 2000;
    private final static int PORT = 7;

    public IPscannerRunnable(int startIp, int stopIp, String subnet) {
        this.startIp = startIp;
        this.stopIp = stopIp;
        this.subnet = subnet;
        liveHosts = new ArrayList<>();
    }
    
    public void run() {
        Socket socket = null;
        for(int i = startIp; i < stopIp; i++) {
            String ip = subnet + "." + i;
            socket = new Socket();
            try {
                InetAddress ipAdd = InetAddress.getByName(ip);
                System.out.println(ipAdd.getHostAddress());
                byte[] ipBytes = ipAdd.getAddress();
                socket.setTcpNoDelay(true);                
                socket.connect(new InetSocketAddress(InetAddress.getByAddress(ipBytes), PORT), TIMEOUT);              
            }
            catch (Exception ex){

            }
        }
        
    }
    
    public List<Node> getLiveHosts() {
        return liveHosts;
    }
    
}
