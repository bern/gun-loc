#include "accel.h"
#include "defines.h"

#include <avr/io.h>
#include <stdbool.h>

#include "twi.h"

bool init_accel(void) {
  // Set Internal Clock to PLL with X gyroscope, recommended
  // Disables Temperature Sensor
  uint8_t c = (1 << 3) | (1 << 0);
  if (i2c_writeReg(ACCEL_ADDR, PWR_MGMT_1, &c, 1) != 0) {
    return false;
  }
  set_sensitivity(1);
  // Set Motion Trigger Duration to 2ms
  c = 2;
  if (i2c_writeReg(ACCEL_ADDR, MOT_DUR, &c, 1) != 0) {
    return false;
  }
  // Clear Interrupts
  accel_clear_int();
  // Enable Motion Trigger Interrupts
  c = 0x40;
  if (i2c_writeReg(ACCEL_ADDR, INT_ENABLE, &c, 1) != 0) {
    return false;
  }
  return true;
}

void accel_clear_int(void) {
  uint8_t c;
  i2c_readReg(ACCEL_ADDR, INT_ENABLE, &c, 1);
}

bool set_sensitivity(uint8_t value) {
  // Set Motion Trigger Threshold to value * 1mg
  return i2c_writeReg(ACCEL_ADDR, MOT_THR, &value, 1) == 0;
}

bool test_accel(void) {
  uint8_t c;
  return (i2c_readReg(ACCEL_ADDR, WHO_AM_I, &c, 1) == 0) && (c == 0x68);
}
