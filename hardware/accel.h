#ifndef ACCEL_H_
#define ACCEL_H_

#include <avr/io.h>
#include <stdbool.h>

bool init_accel(void);
void accel_clear_int(void);
bool set_sensitivity(uint8_t);
bool test_accel(void);

#endif
