package main;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class View extends JFrame{
	
	private JComboBox<String> portDropList;
	private JButton scanPortBtn, connectBtn;
	
	private ArrayList<String> portList;
	
	public View() {
		this.initUI();
	}
	
	private void initUI() {
		this.setTitle("Digital Spirometer Monitoring Application");
		this.setSize(1200, 700);
		this.setLocation(100, 100);
		
		JLabel portLbl = new JLabel("Chọn cổng: ");
		portLbl.setSize(100, 20);
		portLbl.setLocation(20, 20);
		this.add(portLbl);
		
		this.portDropList = new JComboBox<>(new String[] {"COM1", "COM2"}) ;
		this.portDropList.setSize(140, 22);
		this.portDropList.setLocation(130, 20);
		this.add(this.portDropList);
		
		this.connectBtn = new JButton("Kết nối");
		this.connectBtn.setSize(140, 30);
		this.connectBtn.setLocation(290, 16);
		this.add(this.connectBtn);
		
		this.scanPortBtn = new JButton("Quét cổng");
		this.scanPortBtn.setSize(150, 30);
		this.scanPortBtn.setLocation(440, 16);
		this.add(this.scanPortBtn);
				
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}
	
}
