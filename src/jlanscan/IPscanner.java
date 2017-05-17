/*
 * Network scan looking for live host 
 */

package jlanscan;


import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JTextArea;
public class IPscanner implements Runnable{
    
    final String THREAD_TAG;
    JTextArea textArea;
    int startIp;
    int stopIp;
    int range;
    String subnet;
    static final int TIMEOUT = 2000;
    String ipadd = "";            
    boolean ping;  
    int numOfHost = 0;
       
    public IPscanner(int startIp, int stopIp, String subnet, JTextArea t)
    {
        this.startIp = startIp;
        this.stopIp = stopIp;
        this.subnet = subnet;
        textArea = t;
        THREAD_TAG = startIp + "";       
    }
    
    //returns the number of live host detected in range
    public int getLiveHosts()
    {
        return numOfHost;
    }
    
    public void run(){       
        
        //scanning network
        for(int i = startIp; i < stopIp; i++)
        {
            try
            {
                InetAddress address = InetAddress.getByName(subnet + i);
                System.out.println("Pinging: " + subnet + i);
                //scanProgressBar.setValue(i);//updating progress bar
                ping = address.isReachable(TIMEOUT);
                if(ping)
                {
                    System.out.println(subnet + i +" ----> is alive!"+"\n");
                    ipadd += subnet + i +" ----> is alive!\n";
                    numOfHost++;
                }
//                else
//                {
//                    ipadd += subnet + i +"\n";
//                }
                //updating text area               
                textArea.append(ipadd); 
                ipadd = "";
                
            }
            catch(IOException e) {  }           
        }            
            
    }    
    
}
