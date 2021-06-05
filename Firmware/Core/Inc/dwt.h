#ifndef _DWT_H
#define _DWT_H

#include "main.h"

void dwt_init(void){
	if(!(CoreDebug->DEMCR & CoreDebug_DEMCR_TRCENA_Msk)){
		CoreDebug->DEMCR |= CoreDebug_DEMCR_TRCENA_Msk;
		DWT->CYCCNT = 0;
		DWT->CTRL |= DWT_CTRL_CYCCNTENA_Msk;
	}
}

void dwt_clear(void){
	DWT->CYCCNT = 0;
}

void dwt_delay_us(uint32_t us){
	uint32_t startTick = DWT->CYCCNT;
	uint32_t delayTicks = us * (SystemCoreClock / 1000000);
	while(DWT->CYCCNT - startTick < delayTicks);
}

void dwt_delay_ms(uint16_t ms){
	while(ms--){
		dwt_delay_us(1000);
	}
}

uint32_t dwt_get_micros(){
	return DWT->CYCCNT / (SystemCoreClock / 1000000);
}

uint32_t dwt_get_millis(){
	return DWT->CYCCNT / (SystemCoreClock / 1000);
}

#endif
