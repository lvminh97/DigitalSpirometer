package main;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;

import gnu.io.CommPortIdentifier;

public class Utils {
	
	public static HashMap<String, CommPortIdentifier> scanPorts(){
		Enumeration<?> ports = CommPortIdentifier.getPortIdentifiers();
		HashMap<String, CommPortIdentifier> portMap = new HashMap<>();
		while(ports.hasMoreElements()){
			CommPortIdentifier currPort = (CommPortIdentifier) ports.nextElement();
			if(currPort.getPortType() == CommPortIdentifier.PORT_SERIAL){
				portMap.put(currPort.getName(), currPort);
			}
		}
		return portMap;
	}
	
	public static void sendData(OutputStream out, String s) {
		byte[] b = s.getBytes();
		try{
			out.flush();
			out.write(b);
		}
		catch(Exception e){
			System.out.println("Failed to write data. (" + e.toString() + ")");
		}
	}
	
	public static int processBuff(String buff){
		if(buff.indexOf("{dev:spirometer}") != -1){
			return 1;
		}
		else if(buff.indexOf("{data:") != -1){
			return 2;
		}
		return 0;
	}
}
