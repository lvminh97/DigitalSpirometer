package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class View extends JFrame{
	
	private static final long serialVersionUID = 300939840640391342L;
	
	private JComboBox<String> portDropList;
	private JButton scanPortBtn, connectBtn;
	private JComboBox<String> genderSelect;
	private JEditorPane heightEdit, ageEdit;
	private JLabel connectStatusLabel, fev1Label, fev6Label, fev16Label, fev1sLabel, fev6sLabel, fev16sLabel, commentLabel;
	
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
		this.chartData.add(0, 0);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(chartData);
		JFreeChart chart = ChartFactory.createXYLineChart("Spirometer", "t(s)", "V(l)", dataset); 
		XYPlot xyPlot = (XYPlot) chart.getXYPlot();
		XYItemRenderer renderer = xyPlot.getRenderer();
		renderer.setSeriesPaint(0, Color.red);
		renderer.setSeriesStroke(0, new BasicStroke(2));
		xyPlot.setDomainCrosshairVisible(true);
		xyPlot.setRangeCrosshairVisible(true);
		ValueAxis domainAxis = xyPlot.getDomainAxis();
		ValueAxis rangeAxis = xyPlot.getRangeAxis();
		domainAxis.setRange(0.0, 8.0);
		rangeAxis.setRange(0.0, 12.0);
		this.chartPanel = new ChartPanel(chart);
		this.chartPanel.setSize(750, 500);
		this.chartPanel.setLocation(20, 120);
		this.add(this.chartPanel);
		
		LegendPanel inforPanel = new LegendPanel("Thông tin người dùng");
		inforPanel.setSize(350, 190);
		inforPanel.setLocation(800, 120);
		JLabel label2 = new JLabel("Giới tính");
		label2.setSize(100, 20);
		label2.setLocation(20, 50);
		inforPanel.add(label2);
		this.genderSelect = new JComboBox<>(new String[] {"Nam", "Nữ"}) ;
		this.genderSelect.setSize(140, 22);
		this.genderSelect.setLocation(130, 50);
		inforPanel.add(this.genderSelect);
		JLabel label3 = new JLabel("Chiều cao (m)");
		label3.setSize(100, 20);
		label3.setLocation(20, 90);
		inforPanel.add(label3);
		this.heightEdit = new JEditorPane();
		this.heightEdit.setText("0.00");
		this.heightEdit.setSize(140, 22);
		this.heightEdit.setLocation(130, 92);
		inforPanel.add(this.heightEdit);
		JLabel label4 = new JLabel("Tuổi");
		label4.setSize(100, 20);
		label4.setLocation(20, 130);
		inforPanel.add(label4);
		this.ageEdit = new JEditorPane();
		this.ageEdit.setText("0");
		this.ageEdit.setSize(140, 22);
		this.ageEdit.setLocation(130, 132);
		inforPanel.add(this.ageEdit);
		this.add(inforPanel);
		
		Font myFont = new Font("myFont", Font.BOLD, 13);
		LegendPanel resultPanel = new LegendPanel(" Kết quả   ");
		resultPanel.setSize(350, 300);
		resultPanel.setLocation(800, 320);
		JLabel label5 = new JLabel("Thực tế");
		label5.setFont(myFont);
		label5.setForeground(new Color(16, 161, 0));
		label5.setSize(150, 20);
		label5.setLocation(20, 40);
		resultPanel.add(label5);
		this.fev1Label = new JLabel("FEV1 = 0.00 lit");
		this.fev1Label.setSize(150, 20);
		this.fev1Label.setLocation(20, 80);
		resultPanel.add(this.fev1Label);
		this.fev6Label = new JLabel("FEV6 = 0.00 lit");
		this.fev6Label.setSize(150, 20);
		this.fev6Label.setLocation(20, 115);
		resultPanel.add(this.fev6Label);
		this.fev16Label = new JLabel("FEV1/FEV6 = 0.00");
		this.fev16Label.setSize(150, 20);
		this.fev16Label.setLocation(20, 150);
		resultPanel.add(this.fev16Label);
		JLabel label6 = new JLabel("Tính toán");
		label6.setFont(myFont);
		label6.setForeground(new Color(14, 22, 129));
		label6.setSize(150, 20);
		label6.setLocation(200, 40);
		resultPanel.add(label6);
		this.fev1sLabel = new JLabel("FEV1 = 0.00 lit");
		this.fev1sLabel.setSize(150, 20);
		this.fev1sLabel.setLocation(200, 80);
		resultPanel.add(this.fev1sLabel);
		this.fev6sLabel = new JLabel("FEV6 = 0.00 lit");
		this.fev6sLabel.setSize(150, 20);
		this.fev6sLabel.setLocation(200, 115);
		resultPanel.add(this.fev6sLabel);
		this.fev16sLabel = new JLabel("FEV1/FEV6 = 0.00");
		this.fev16sLabel.setSize(150, 20);
		this.fev16sLabel.setLocation(200, 150);
		resultPanel.add(this.fev16sLabel);
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
	
	public XYSeries getChartData() {
		return this.chartData;
	}
	
	public JLabel getConnectStatusLabel() {
		return this.connectStatusLabel;
	}
	
	public JLabel getFev1Label(){
		return this.fev1Label;
	}
	
	public JLabel getFev6Label(){
		return this.fev6Label;
	}
	
	public JLabel getFev16Label(){
		return this.fev16Label;
	}
	
	public JLabel getFev1sLabel(){
		return this.fev1sLabel;
	}
	
	public JLabel getFev6sLabel(){
		return this.fev6sLabel;
	}
	
	public JLabel getFev16sLabel(){
		return this.fev16sLabel;
	}
	
	public JComboBox<String> getGenderSelect(){
		return this.portDropList;
	}
	
	public JEditorPane getHeightEdit() {
		return this.heightEdit;
	}
	
	public JEditorPane getAgeEdit() {
		return this.ageEdit;
	}
}
