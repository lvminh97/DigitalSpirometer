package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import org.json.JSONObject;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import view.View;
import utils.Utils;

public class Controller implements ActionListener, EventListener, SerialPortEventListener{

	private final int DATARATE = 921600;
	private final int TIMEOUT = 2000;
	
	private View view = null;
	
	private HashMap<String, CommPortIdentifier> portList;  // map port's name with port instance
	private CommPortIdentifier selectedPortIdentifier = null; // opened port
	private SerialPort serialPort = null; 
	
	private InputStream inputStream = null; // input stream that serve transmit and receive through Serial port
	private OutputStream outputStream = null; // output stream that serve transmit and receive through Serial port
	private String buff = "";
	
	private boolean isConnect = false;
	private boolean isDetect = false;
	
	private Double fev1, fev6;
	
	public Controller() {
		this.view = new View();
		//
		this.portList = Utils.scanPorts();
		for(Map.Entry<String, CommPortIdentifier> me: this.portList.entrySet()) {
			this.view.getPortDropList().addItem(me.getKey());
		}
		//
		this.view.getScanPortBtn().addActionListener(this);
		this.view.getConnectBtn().addActionListener(this);
	}

	private boolean initIOStream(){
		try{
			this.inputStream = serialPort.getInputStream();
			this.outputStream = serialPort.getOutputStream();
			return true;
		}
		catch(IOException e){
			System.out.println("IO Streams failed to open. (" + e.toString() + ")");
			return false;
		}
	}
	
	private void connect(){
		String selectedPort = (String) this.view.getPortDropList().getSelectedItem();
		this.selectedPortIdentifier = (CommPortIdentifier) this.portList.get(selectedPort);
		CommPort commPort = null;
		
		try{
			commPort = this.selectedPortIdentifier.open("Dev1", TIMEOUT);
			this.serialPort = (SerialPort) commPort;
			this.serialPort.setSerialPortParams(DATARATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			this.serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			this.isConnect = true;
			this.view.getConnectBtn().setText("Ngắt kết nối");
			this.view.getConnectStatusLabel().setText("Đang tìm thiết bị...");
			this.view.getConnectStatusLabel().setForeground(Color.blue);
//			System.out.println(selectedPort + " opened successfully!!!");
		}
		catch(PortInUseException e){
			this.isConnect = false;
			System.out.println(selectedPort + " is in use. (" + e.toString() + ")");
		}
		catch(Exception e){
			this.isConnect = false;
			System.out.println("Failed to open " + selectedPort + "(" + e.toString() + ")");
		}
	}
	
	private void disconnect(){
		try{
			this.serialPort.removeEventListener();
			this.serialPort.close();
			this.inputStream.close();
			this.outputStream.close();
			this.serialPort = null;
			this.inputStream = null;
			this.outputStream = null;
			this.view.getConnectBtn().setText("Kết nối");
			this.isConnect = false;
			this.isDetect = false;
			this.view.getConnectStatusLabel().setText("Chưa kết nối");
			this.view.getConnectStatusLabel().setForeground(Color.red);
//			System.out.println("Disconnected!");
		}
		catch(Exception e){
			System.out.println("Failed to close " + serialPort.getName() + "(" + e.toString() + ")");
		}
	}
	
	private void initListener(){
		try{
			this.serialPort.addEventListener(this);
			this.serialPort.notifyOnDataAvailable(true);
		}
		catch(TooManyListenersException e){
			System.out.println("Too many listeners. (" + e.toString() + ")");
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == this.view.getScanPortBtn()){
			this.portList = Utils.scanPorts();
			this.view.getPortDropList().removeAllItems();
			for(Map.Entry<String, CommPortIdentifier> me: this.portList.entrySet()) {
				this.view.getPortDropList().addItem(me.getKey());
			}
		}
		else if(event.getSource() == this.view.getConnectBtn()){
			if(!this.isConnect){
				this.connect();
				this.initIOStream();
				this.initListener();
				Utils.sendData(this.outputStream, "{cmd:detect}\n");
			}
			else{
				this.disconnect();
			}
		}
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE){
			try{
				while(inputStream.available() > 0){
					byte data = (byte) inputStream.read();
					String text = new String(new byte[] {data});
					if(text.equals("\r") || text.equals("\n")){
						if(buff.length() > 0){
							JSONObject json = new JSONObject(buff);
							// Detect spirometer
							if(json.isNull("dev") == false && json.getString("dev").equals("spirometer") == true){
								this.isDetect = true;
								this.view.getConnectStatusLabel().setText("Đã kết nối với thiết bị");
								this.view.getConnectStatusLabel().setForeground(new Color(14, 162, 64));
							}
							// Reset chart when receive first value of new measurement
							if(this.isDetect == true && json.isNull("reset") == false && json.getInt("reset") == 1){
								this.view.getChartData().clear();
								this.view.getChartData().add(0, 0);
							}
							// Update chart when receive data from spirometer
							if(this.isDetect == true && json.isNull("ts") == false && json.isNull("data") == false){
								float timestamp = (float) (json.getInt("ts") / 1000.0);
								float value = (float) json.getDouble("data");
								this.view.getChartData().add(timestamp, value);
							}
							// Receive FEV1
							if(this.isDetect == true && json.isNull("fev1") == false){
								this.fev1 = json.getDouble("fev1");
								this.view.getFev1Label().setText("FEV1 = " + this.fev1 + " lit");
							}
							// Receive FEV6
							if(this.isDetect == true && json.isNull("fev6") == false){
								this.fev6 = json.getDouble("fev6");
								this.view.getFev6Label().setText("FEV6 = " + fev6 + " lit");
								this.view.getFev16Label().setText("FEV1/FEV6 = " + String.format("%.2f", fev1 / fev6 * 100) + "%"); 
								this.view.getDiagnoseLabel().setText("Chẩn đoán: " + Utils.getDiagnose((float) (fev1 / fev6)));
								// compute by ideal formulus
								HashMap<?,?> idealValue = Utils.computeFVC(this.view.getGenderSelect().getSelectedIndex(), 
										Float.parseFloat(this.view.getHeightEdit().getText()), 
										Integer.parseInt(this.view.getAgeEdit().getText()));
								this.view.getFev1sLabel().setText("FEV1 = " + idealValue.get("fev1") + " lit");
								this.view.getFev6sLabel().setText("FEV6 = " + idealValue.get("fev6") + " lit");
								this.view.getFev16sLabel().setText("FEV1/FEV6 = " + String.format("%.2f", 100 * (float) idealValue.get("ratio")) + "%");
							}
							buff = "";
						}
					}
					else{
						buff += text;
					}
				}
			}
			catch(Exception ex){
				System.out.println("Failed to read data. (" + ex.toString() + ")");
			}
		}
		
	}	
}
