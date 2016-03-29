#include "defines.h"

#include <avr/interrupt.h>
#include <avr/io.h>
#include <avr/sleep.h>
#include <stdbool.h>
#include <stdlib.h>
#include <util/delay.h>

#include "serial.h"
#include "accel.h"

void init(void);
void sleep(void);

int main(void) {
  init();
  printf("Initialized!\n");
  while (1) {
    sleep();
  }
  return 0;
}

void init(void) {
  // Set PortD 2 as input
  DDRD &= ~(1 << DDD2);
  PORTD &= ~(1 << PORTD2);
  // Rising Edge interrupts
  EICRA |= (1 << ISC01) | (1 << ISC00);
  // Enable INT0
  EIMSK |= (1 << INT0);
  init_serial(BAUD);
  // Serial interrupts
  UCSR0B |= (1 << RXCIE0);
  _delay_ms(1000);
  if ((test_accel() && init_accel()) == false) {
    while (1) {
      printf("Error initializing Accelerometer\n");
      _delay_ms(3000);
    }
  }
  set_sleep_mode(SLEEP_MODE_IDLE);
  sei();
}

void sleep(void) {
  sleep_enable();
  sei();
  sleep_cpu();
  sleep_disable();
  cli();
}

ISR(INT0_vect) {
  cli();
  printf("OW!\n");
  accel_clear_int();
  sei();
}

ISR(USART_RX_vect) {
  static uint8_t value = 0;
  cli();
  char c = UDR0;
  if (c == '\r') {
    if (set_sensitivity(value)) {
      printf("Sensitivity value set to %umg\n", value);
    } else {
      printf("Failed to set Sensitivity\n");
    }
    value = 0;
  } else if (c >= '0' && c <= '9') {
    if (value * 10 < value) {
      value = c - '0';
    } else {
      value = value * 10 + (c - '0');
    }
  } else {
    value = 0;
  }
  sei();
}
