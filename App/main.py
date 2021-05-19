from PyQt5 import QtGui, QtWidgets, QtCore
from PyQt5.QtCore import QDir, QSize, QSizeF, Qt, QUrl
from PyQt5.QtGui import QTransform, QIcon
from PyQt5.QtWidgets import (QApplication, QComboBox, QFileDialog, QFrame,
        QLineEdit, QListWidget, QPlainTextEdit, QLabel, 
        QPushButton, QSlider, QStyle, QVBoxLayout,
        QWidget, QTableWidget, QTableWidgetItem,
        QMessageBox, QCheckBox, QGroupBox)
import os
import sys
import warnings
from PlotCanvas import PlotCanvas
from serial import Serial, SerialException
import threading
import time

class App(QWidget):
    def __init__(self):
        super().__init__()

        self.connected = False
        self.port = ""
        self.serial = None
        self.baudrate = 0
        self.buffer = ""
        self.data = {}

        self.initUI()
        
    def initUI(self):
        self.setWindowTitle("Digital Spirometer - App")
        self.setGeometry(60, 60, 1200, 650)
        self.setFixedSize(1000, 650)

        self.label1 = QLabel(self)
        self.label1.setText("Chọn cổng")
        self.label1.resize(80, 20)
        self.label1.move(20, 20)

        self.portList = QComboBox(self)
        self.portList.resize(80, 25)
        self.portList.move(100, 20)
        # self.portList.addItem("COM1")
        # self.portList.addItem("COM2")
        self.scanPort()

        self.connectBtn = QPushButton("Kết nối", self)
        self.connectBtn.resize(100, 28)
        self.connectBtn.move(210, 19)
        self.connectBtn.clicked.connect(self.connectPort)

        self.graphView = PlotCanvas(self, width = 7, height = 4.5)
        self.graphView.move(320, 80)
        # self.graphView.plot(list(range(400, 701, 10)), [1] + [0] * 30, 'w')

        self.resultBox = QGroupBox('Đánh giá tình trạng sức khỏe', self)
        self.resultBox.resize(270, 250)
        self.resultBox.move(20, 80)
        # self.ratioText = QLabel(self)
        # self.ratioText.resize(200, 180)
        # self.ratioText.move(350, 30)
        # self.ratioText.setStyleSheet("font-weight:bold;")

        self.show()

    def scanPort(self):
        for i in range(256):
            port = "COM" + str(i + 1)
            try:
                s = Serial(port)
                s.close()
                self.portList.addItem(port)
            except (OSError, SerialException):
                pass

    def connectPort(self):
        if not self.connected:
            self.port = self.portList.currentText()
            try:
                self.serial = Serial(self.port, self.baudrate, timeout = 2)
                # Send detecting command to detect whether is connected with spirometer
                self.serial.write("{cmd:detect}\n".encode())
                # Wait the response from spirometer
                resp = self.serial.readline().decode()
                if resp.find("{dev:spirometer}") != -1: # right response
                    self.connected = True
                    self.connectBtn.setText("Ngắt kết nối")
                else:
                    self.dispMessageBox("Error", "Chưa kết nối đúng thiết bị. Vui lòng kiểm tra lại!")
            except SerialException:
                self.dispMessageBox("Error", "Không thể kết nối tới cổng " + self.port + "!!")
        else:
            self.disconnect()

    def disconnect(self):
        if self.serial is None:
            pass
        else:
            self.serial.close()
            self.serial = None 
        self.connected = False 
        self.connectBtn.setText("Kết nối")

    def handleData(self):
        while True:
            if self.connected:
                c = self.serial.read(1).decode()
                self.buffer += c

    def dispMessageBox(self, title, text):
        msgBox = QMessageBox(self)
        msgBox.setIcon(QMessageBox.Information)
        msgBox.setText(text)
        msgBox.setWindowTitle(title)
        msgBox.setStandardButtons(QMessageBox.Ok)
        msgBox.show()

    def setColorBox(self, box, RGB): ## box = 0 -> sampleColor; box = 1 -> computeColor
        # styleSheet = 'border: 1px solid #a9a8b3; background-color: #' + '%02x%02x%02x' % (RGB[0], RGB[1], RGB[2])   
        # if box == 0:
        #     self.sampleColorBox.setStyleSheet(styleSheet)
        # else:
        #     self.computeColorBox.setStyleSheet(styleSheet) 
        pass    

if __name__ == '__main__':
    os.system("color 0a")
    os.system("cls")
    warnings.filterwarnings('ignore') 
    app = QApplication(sys.argv)
    exe = App()
    # Child-thread for receiving data from port
    thread = threading.Thread(target = exe.handleData)
    thread.start()
    #Run app
    sys.exit(app.exec_())
