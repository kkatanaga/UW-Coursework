#include <stdio.h>

#define BYTE 8

// Prints a binary number
void print_binary(int binary[]) {
    for(int i = 0; i < BYTE; i++)
        printf("%d", binary[i]);
}

void print_main_menu(void) {
    puts("Choose a command number:");
    puts("0) Exit");
    puts("1) Gray Code");
    printf("Command: ");
}

void print_result(int input, int output[]) {
    printf("Gray code of %d is: ", input);																// Prints the input, integer
    print_binary(output);																				// Prints the output, gray code
}