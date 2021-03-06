#ifndef _MPXV7002_H
#define _MPXV7002_H

#include "main.h"
#include "math.h"

#define A1 0.0277	// diameter of large tube, unit: m
#define A2 0.0190	// diameter of narrow tube, unit: m
#define p	 2.0		// density of air - here is CO2, unit: kg/m3

double c1, c2;			// coefficients in formulus that compute flow rate

void mpxv7002_init(void){ // init coefficients
	c1 = 3.14 * A2 * A2 / 4.0;
	float tmp = A2 / A1;
	c2 = 2 / (p * (1 - tmp * tmp * tmp * tmp));
}

double mpxv7002_get_pressure(uint16_t adc){
	float volt;
	volt = (adc - 146) / 4032.0 * 3300; // convert adc value into voltage (range 3300mV)
	return (volt - 1650) / 1650 * 2000;  // conpute pressure between 2 pipe of sensor
}

double mpxv7002_get_flowrate(double pressure){
	// compute the flow rate follow Bernouli equation
	return c1 * sqrt(c2 * pressure); 
}
#endif
