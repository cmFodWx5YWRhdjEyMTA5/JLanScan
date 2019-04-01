package jlanscan;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * class to Get LocalHost TCP configuration: ip, subnet, router
 * @author jorge
 */
public class LocalHost {
    String ipV6;
    String ipV4;
    String hostname;
    String ifaceName;
    String macAddr;
    Enumeration<NetworkInterface> nets = null;
    
    /**
     * Constructor LocalHost initializing Network Interfaces and Enumerating
     * @throws UnknownHostException
     * @throws SocketException 
     */
    public LocalHost() throws UnknownHostException, SocketException {
        hostname = "localhost";        
        nets = NetworkInterface.getNetworkInterfaces();          
        Collections.list(nets).forEach((netint) -> {
            try {
                if(!netint.isLoopback()) {
                    displayInterfaceInformation(netint);
                    getMacAddress(netint);
                }
            } catch (SocketException ex) {
                Logger.getLogger(LocalHost.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnknownHostException ex) {
                Logger.getLogger(LocalHost.class.getName()).log(Level.SEVERE, null, ex);
            }            
        });
    }
    
    /**
     * Display information on local network interfaces 
     * @param netint 
     */
    public void displayInterfaceInformation(NetworkInterface netint) {    
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) {
            //not display information on loopback address
            if(inetAddress.getHostAddress().contains("%")) {
                String[] iface = inetAddress.getHostAddress().split("%");
                ipV6 = iface[0];
                ifaceName = iface[1];                
            }
            ipV4 = inetAddress.getHostAddress();
        }
    }   
    
    /**
     * display mac address information for network interface
     * @param netint
     * @throws SocketException
     * @throws UnknownHostException 
     */
    public void getMacAddress(NetworkInterface netint) throws SocketException, UnknownHostException {         
        byte[] mac = netint.getHardwareAddress();
        if(mac != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));        
            }
            macAddr = sb.toString();
        }
    }

    public String getIpAddressV4() {
        return ipV4;
    }

    public String getHostname() {
        return hostname;
    }

    public String getIpV6() {
        return ipV6;
    }

    public String getIfaceName() {
        return ifaceName;
    }

    public String getMacAddr() {
        return macAddr;
    }
    
    @Override
    public String toString() {
        return "Localhost: interface: " + ifaceName +" ipV4: " + ipV4 +" ipV6: "+ ipV6 +" hostname: "+ hostname +" mac: "+macAddr;
    }
    
}
