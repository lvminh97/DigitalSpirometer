package utils;

import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import gnu.io.CommPortIdentifier;

public class Utils {
	
	private static float timestamp;
	private static float value;
	
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
	
	public static int processBuff(String buff){
		int resp = 0;
		try{
			JSONObject json = new JSONObject(buff);
			if(json.isNull("dev") == false && json.getString("dev").equals("spirometer") == true){
				resp = 1;
			}
			if(json.isNull("ts") == false && json.isNull("data") == false){
				timestamp = (float) (json.getInt("ts") / 1000.0);
				value = (float) json.getDouble("data");
				resp = 2;
			}
		}
		catch(JSONException err){
			//
		}
		return resp;
	}

	public static float getTimeStamp(){
		return timestamp;
	}
	
	public static float getValue() {
		return value;
	}	
}
