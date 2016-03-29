#ifndef SERIAL_H_
#define SERIAL_H_

#include <stdbool.h>
#include <stdio.h>

bool init_serial(unsigned int baud);
int send(char c, FILE* stream);
int recieve(FILE* stream);

#endif
