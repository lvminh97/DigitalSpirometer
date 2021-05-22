package main;

import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class View extends JFrame{
	
	private static final long serialVersionUID = 300939840640391342L;
	
	private JComboBox<String> portDropList;
	private JButton scanPortBtn, connectBtn;
	
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
		
		this.portDropList = new JComboBox<>(new String[] {}) ;
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
		
		JPanel resultPanel = new JPanel();
		resultPanel.setBorder(BorderFactory.createTitledBorder("Kết quả"));
		resultPanel.setLayout(new GridBagLayout());
		resultPanel.setLocation(20, 80);
		this.getContentPane().add(resultPanel);
		
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	
	// Getter and Setter
	public JComboBox<String> getPortDropList() {
		return portDropList;
	}

	public void setPortDropList(JComboBox<String> portDropList) {
		this.portDropList = portDropList;
	}

	public JButton getScanPortBtn() {
		return scanPortBtn;
	}

	public void setScanPortBtn(JButton scanPortBtn) {
		this.scanPortBtn = scanPortBtn;
	}

	public JButton getConnectBtn() {
		return connectBtn;
	}

	public void setConnectBtn(JButton connectBtn) {
		this.connectBtn = connectBtn;
	}
	
	
}
