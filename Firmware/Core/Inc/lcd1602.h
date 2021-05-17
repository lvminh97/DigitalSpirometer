#ifndef LCD1602_H
#define LCD1602_H

#include "main.h"

#define LCD_RS(s) HAL_GPIO_WritePin(LCD_RS_GPIO_Port, LCD_RS_Pin, s ? GPIO_PIN_SET : GPIO_PIN_RESET)
#define LCD_EN(s) HAL_GPIO_WritePin(LCD_EN_GPIO_Port, LCD_EN_Pin, s ? GPIO_PIN_SET : GPIO_PIN_RESET)
#define LCD_D4(s) HAL_GPIO_WritePin(LCD_D4_GPIO_Port, LCD_D4_Pin, s ? GPIO_PIN_SET : GPIO_PIN_RESET)
#define LCD_D5(s) HAL_GPIO_WritePin(LCD_D5_GPIO_Port, LCD_D5_Pin, s ? GPIO_PIN_SET : GPIO_PIN_RESET)
#define LCD_D6(s) HAL_GPIO_WritePin(LCD_D6_GPIO_Port, LCD_D6_Pin, s ? GPIO_PIN_SET : GPIO_PIN_RESET)
#define LCD_D7(s) HAL_GPIO_WritePin(LCD_D7_GPIO_Port, LCD_D7_Pin, s ? GPIO_PIN_SET : GPIO_PIN_RESET)

void LCD_Enable(void){
	LCD_EN(1);
	HAL_Delay(1);
	LCD_EN(0);
	HAL_Delay(1);
}

void LCD_Send4Bit(unsigned char data){
	LCD_D4((data & 0x01));
	LCD_D5((data & 0x02));
	LCD_D6((data & 0x04));
	LCD_D7((data & 0x08));
	LCD_Enable();
}
void LCD_SendCommand(unsigned char command){
	LCD_Send4Bit(command >> 4);
	LCD_Send4Bit(command);
}
void LCD_Clear(void){
	LCD_RS(0);
	LCD_SendCommand(0x01);
	HAL_Delay(5);
}
void LCD_Init(void){
	LCD_RS(0);
	HAL_Delay(20);
	LCD_Send4Bit(0x03);
	HAL_Delay(5);
	LCD_Send4Bit(0x03);
	HAL_Delay(11);
	LCD_Send4Bit(0x03);
	LCD_Send4Bit(0x02);
	LCD_SendCommand(0x28);
	LCD_SendCommand(0x0C);
	LCD_SendCommand(0x06);
	LCD_SendCommand(0x01);
}
void LCD_Gotoxy(unsigned char x, unsigned char y){
	LCD_RS(0);
	if(y == 0){
		LCD_SendCommand(0x80 | x);
	}
	else{
		LCD_SendCommand(0x80 | 0x40 | x);
	}
	HAL_Delay(1);
}
void LCD_PutChar(unsigned char data){
	LCD_RS(1);
	LCD_SendCommand(data);
}
void LCD_PutString(char *s){
	uint8_t idx;
	LCD_RS(1);
	for(idx = 0; s[idx]; idx++){
		LCD_SendCommand(s[idx]);
	}
}

#endif
