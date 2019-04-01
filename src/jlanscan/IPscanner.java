/*
 * Network scan looking for live host 
 */

package jlanscan;


import jlanscan.runnable.IPscannerRunnable;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
public class IPscanner {
    
    static final int NUMTHREADS = 254;
    int numOfHost = 254;
    String ip;
    String subnet; 
    int startIp = 1;
    int range = (numOfHost / NUMTHREADS); //range to be scanned by every thread
    int stopIp = startIp + range;
    ExecutorService executor = Executors.newFixedThreadPool(NUMTHREADS);
    IPscannerRunnable[] task = new IPscannerRunnable[NUMTHREADS];
    static List<Node> resultList = new ArrayList<>();
    static final String NIX_ARP_TABLE = "/proc/net/arp";
    static final String ARP_FLAG = "0x2";
    String winArpTable;
    LocalHost inetHost;
    List<Node> hostList;
    JTextArea textArea;
    static final int TIMEOUT = 3;   
       
    public IPscanner() throws UnknownHostException, SocketException
    {
        inetHost = new LocalHost();
        ip = inetHost.getIpAddressV4();
        subnet = ip.substring(0, ip.lastIndexOf("."));
        hostList = new ArrayList();
//        textArea = t;    
        System.out.println(ip);
        System.out.println("scanning network " + subnet +".0...");
    }  
    
    
    public void scanNetwork() throws InterruptedException {        
        for(int i = 0; i < NUMTHREADS; i++) {
            task[i] = new IPscannerRunnable(startIp, stopIp, subnet);
            executor.execute(task[i]);
            startIp = stopIp+1;
            stopIp = startIp + range;
        }
        executor.shutdown();
        executor.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        executor.shutdownNow();
        
        readArpTable();
    } 
    
    public void readArpTable() {
        try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(NIX_ARP_TABLE), "UTF-8"));
                reader.readLine(); // Skip header.
                String line;
                while ((line = reader.readLine()) != null) {
//                    System.out.println("ARPFILE"+ line);
                    String[] arpLine = line.split("\\s+");
                    //if arp line contains flag 0x2 we parse host
                    if(arpLine[2].equals(ARP_FLAG)) {
                        final String ip = arpLine[0];
                        final String mac = arpLine[3];
//                        ia = InetAddress.getByName(ip);
//                        Log.v("PARSED", ip +" "+flag+" "+mac + " "+ ia.getCanonicalHostName());
                        Node node = new Node(ip, mac);
                        hostList.add(node);                        
//                        scanProgress.setProgress(node.progressBar);
                    }
                }
            }
            catch (Exception ex) {

            }
    }
    
    
    
    public static void main(String[] args) throws UnknownHostException, SocketException {
        IPscanner ipscan = new IPscanner();
        try {
            ipscan.scanNetwork();
        } catch (InterruptedException ex) {
            Logger.getLogger(IPscanner.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Node node: ipscan.hostList) {
            System.out.println(node.ip +" "+ node.mac);
        }
    }
       
    
}
