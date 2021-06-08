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
		if(height <= 0.0 || age <= 0) {
			fev1 = 0;
			fev6 = 0;
			ratio = 0;
		}else {
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
		}
		res.put("fev1", fev1);
		res.put("fev6", fev6);
		res.put("ratio", ratio);
		return res;
	}
	
	public static String getDiagnose(float ratio) {
		if(ratio > 0.8) return "Phổi của bạn khỏe mạnh";
		else if(0.7 < ratio && ratio <= 0.8) return "Bạn có nguy cơ mắc hen suyễn";
		else if(0.65 < ratio && ratio <= 0.7) return "Bạn có nguy cơ mắc viêm phế quản";
		else if(0.3 < ratio && ratio <= 0.65) return "Bạn có nguy cơ mắc viêm phế quản mãn tính";
		else return "Bạn có nguy cơ mắc ung thư phổi";
	}
}
