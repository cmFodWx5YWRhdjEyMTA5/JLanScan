/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jlanscan;


import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextArea;

public class PortScanner implements Runnable {
    private String ipAddr = "";
    ArrayList<Integer> ports;
    JTextArea myText;
    int startPort;
    int endPort;
    final String THREAD_TAG;
    int timeOut = 1000;
    int portCounter = 0;
    
    //construct that takes in an ip address to scan
    public PortScanner(String ip, int startPort, int endPort, JTextArea t)
    {
        ipAddr = ip;
        this.startPort = startPort;
        this.endPort = endPort;
        myText = t;    
        THREAD_TAG = startPort + "->" + endPort;
    }    
    
    //port scanning
    public void run()
    {
        System.out.println(THREAD_TAG);
        String ipPorts;        
        
        try
        {
            InetAddress addr = InetAddress.getByName(ipAddr);            
            ipPorts = "";            
            for(int i = startPort; i < endPort; i++)
            {
                try
                {
                    
                    Socket s = new Socket();//trying to open connection  
                    SocketAddress address = new InetSocketAddress(ipAddr, i);
                    s.connect(address, timeOut);
                    if(s.isConnected()) 
                    {
                        portCounter++;
                        ipPorts += "port " + i + " ------> open!!!" + "\n";//we found open port
                        s.close();//closing port  
                    }                                    
                    
                }
                catch(IOException e){ 
                    e.printStackTrace();
                }                
                
                //post results in text area
                myText.append(ipPorts);
                ipPorts = "";
                
            }
        }
        catch(UnknownHostException a) {  } 
    }
    
//    returns the number of open ports
    public int getPortCounter()
    {
        return portCounter;
    }
    
    
}
