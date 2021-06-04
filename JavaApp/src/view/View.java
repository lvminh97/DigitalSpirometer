package view;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class View extends JFrame{
	
	private static final long serialVersionUID = 300939840640391342L;
	
	private JComboBox<String> portDropList;
	private JButton scanPortBtn, connectBtn;
	private JButton testDrawBtn;
	private JLabel connectStatusLabel, fev1Label, fev6Label, fev16Label, commentLabel;
	
	private ChartPanel chartPanel;
	private XYSeries chartData;
	
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
		
		testDrawBtn = new JButton("Test draw");
		testDrawBtn.setSize(180, 30);
		testDrawBtn.setLocation(600, 16);
		this.add(testDrawBtn);
		
		JLabel label1 = new JLabel("Trạng thái: ");
		label1.setSize(100, 20);
		label1.setLocation(20, 65);
		this.add(label1);
		this.connectStatusLabel = new JLabel("Chưa kết nối");
		this.connectStatusLabel.setForeground(Color.red);
		this.connectStatusLabel.setSize(300, 20);
		this.connectStatusLabel.setLocation(130, 65);
		this.add(this.connectStatusLabel);
		
		this.chartData = new XYSeries("volumn");
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(chartData);
		JFreeChart chart = ChartFactory.createXYLineChart("Spirometer", "t(s)", "V(l)", dataset); 
		this.chartPanel = new ChartPanel(chart);
		this.chartPanel.setSize(750, 500);
		this.chartPanel.setLocation(20, 120);
		this.add(this.chartPanel);
		
		LegendPanel resultPanel = new LegendPanel(" Kết quả   ");
		resultPanel.setSize(350, 500);
		resultPanel.setLocation(800, 120);
		this.fev1Label = new JLabel("FEV1 = 0.00 lit");
		this.fev1Label.setSize(150, 20);
		this.fev1Label.setLocation(20, 50);
		resultPanel.add(this.fev1Label);
		this.fev6Label = new JLabel("FEV6 = 0.00 lit");
		this.fev6Label.setSize(150, 20);
		this.fev6Label.setLocation(20, 100);
		resultPanel.add(this.fev6Label);
		this.fev16Label = new JLabel("FEV1/FEV6 = 0.00");
		this.fev16Label.setSize(150, 20);
		this.fev16Label.setLocation(20, 150);
		resultPanel.add(this.fev16Label);
		this.commentLabel = new JLabel("Đánh giá: ...");
		this.commentLabel.setAlignmentY(TOP_ALIGNMENT);
		this.commentLabel.setVerticalAlignment(JLabel.TOP);
		this.commentLabel.setVerticalTextPosition(JLabel.TOP);
		this.commentLabel.setSize(250, 100);
		this.commentLabel.setLocation(20, 200);
		resultPanel.add(this.commentLabel);
		this.add(resultPanel);
		
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
	}

	public JComboBox<String> getPortDropList() {
		return portDropList;
	}

	public void setPortDropList(JComboBox<String> portDropList) {
		this.portDropList = portDropList;
	}

	public JButton getScanPortBtn() {
		return scanPortBtn;
	}

	public JButton getConnectBtn() {
		return connectBtn;
	}	
	
	public JButton getTestDrawBtn() {
		return testDrawBtn;
	}
	
	public XYSeries getChartData() {
		return this.chartData;
	}
	
	public JLabel getConnectStatusLabel() {
		return this.connectStatusLabel;
	}
}
