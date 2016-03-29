#include "serial.h"

#include <stdio.h>

#include <avr/io.h>
#include "defines.h"

static FILE uart_stio = FDEV_SETUP_STREAM(send, recieve, _FDEV_SETUP_WRITE);

bool init_serial(unsigned int baud) {
  // Baud Rate
  UBRR0 = USART_UBRR(baud);
  // U2X0, double speed
  UCSR0A |= (1 << U2X0);
  // Enable RX, TX, and RX Interrupts
  UCSR0B = (1 << RXEN0) | (1 << TXEN0);
  // 8 data bits, 1 stop bit
  UCSR0C = (1 << UCSZ01) | (1 << UCSZ00);
  // Tie stdin and stdout to send/recieve
  stdout = &uart_stio;
  stdin = &uart_stio;
  return true;
}

int send(char c, FILE* stream) {
  if (c == '\n') {
    send('\r', stream);
  }
  while (!(UCSR0A & (1 << UDRE0))) {
  }
  UDR0 = c;
  return 0;
}

int recieve(FILE* stream) {
  while (!(UCSR0A & (1 << RXC0))) {
  }
  return UDR0;
}
