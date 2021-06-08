package utils;

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
		try{
			out.write(s.getBytes());
			out.flush();
		}
		catch(Exception e){
			System.out.println("Failed to write data. (" + e.toString() + ")");
		}
	}	
	
	public static HashMap<String, Float> computeFVC(int gender, float height, int age){
		HashMap<String, Float> res = new HashMap<>();
		float fev1, fev6, ratio;
		if(gender == 0) { // male
			fev1 = (float) (4.3 * height - 0.029 * age - 2.49);
			fev6 = (float) (5.76 * height - 0.026 * age - 4.34);
			ratio = fev1 / fev6;
		}
		else { // female
			fev1 = (float) (3.95 * height - 0.025 * age - 2.6);
			fev6 = (float) (4.43 * height - 0.026 * age - 2.89);
			ratio = fev1 / fev6;
		}
		res.put("fev1", fev1);
		res.put("fev6", fev6);
		res.put("ratio", ratio);
		return res;
	}
	
}
