package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import view.View;
import utils.Utils;

public class Controller implements ActionListener, EventListener, SerialPortEventListener{

	private View view = null;
	
	private HashMap<String, CommPortIdentifier> portList;  // map port's name with port instance
	private CommPortIdentifier selectedPortIdentifier = null; // opened port
	private SerialPort serialPort = null; 
	
	private InputStream inputStream = null; // input stream that serve transmit and receive through Serial port
	private OutputStream outputStream = null; // output stream that serve transmit and receive through Serial port
	private String buff = "";
	
	private boolean isConnect = false;
	private boolean isDetect = false;
	
	private int TIMEOUT = 2000;
	
	private ArrayList<Float> data = null;
	
	private float cnt = 0;
	
	public Controller() {
		this.view = new View();
		//
		this.portList = Utils.scanPorts();
		for(Map.Entry<String, CommPortIdentifier> me: this.portList.entrySet()) {
			this.view.getPortDropList().addItem(me.getKey());
		}
		//
		data = new ArrayList<Float>();
		//
		this.view.getScanPortBtn().addActionListener(this);
		this.view.getConnectBtn().addActionListener(this);
		this.view.getTestDrawBtn().addActionListener(this);
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
			commPort = this.selectedPortIdentifier.open("{cmd:detect}\n", TIMEOUT);
			this.serialPort = (SerialPort) commPort;
			this.serialPort.setSerialPortParams(4800, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			this.serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			this.isConnect = true;
			this.view.getConnectBtn().setText("Ngắt kết nối");
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
		else if(event.getSource() == this.view.getTestDrawBtn()) {
			System.out.println("Test draw");
			this.view.getChartData().add(cnt, Math.random());
			this.cnt += 0.01;
		}
		
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if(event.getEventType() == SerialPortEvent.DATA_AVAILABLE){
			try{
				byte data = (byte) inputStream.read();
				String text = new String(new byte[] {data});
				if(text.equals("\r") || text.equals("\n")){
					if(buff.length() > 0){
						int resp = Utils.processBuff(buff);
						if(resp == 1){
							this.isDetect = true;
						}
						else if(resp == 2  && this.isDetect){
							this.data.add(Utils.getValue());
							if(this.data.size() > 1000) {
								this.data.remove(0);
							}
							System.out.println("Len: " + this.data.size());
						}
						buff = "";
					}
				}
				else{
					buff += text;
				}
			}
			catch(Exception ex){
				System.out.println("Failed to read data. (" + ex.toString() + ")");
			}
		}
		
	}	
}
