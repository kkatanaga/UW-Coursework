#include <stdio.h>

int ask_command(int * command) {
    char buffer;                                                                                         // acts as a buffer for character inputs
    printf("Input: ");
    while( ( !scanf("%d", command) && scanf(" %c", &buffer) ) || (*command != 0 && *command != 1 && *command != 2) )           // Loops when the input is a character or not 0, 1, or 2
        printf("Error: please enter 0 (Exit), 1 (Powers of 2), or 2 (Divisions of 2): ");
    return *command;
}

void ask_int(int * integer) {
    char buffer;

    while ( !scanf("%d", integer) && scanf(" %c", &buffer) )
        printf("Please enter an integer: ");
}

void ask_decimal(int * decimal) {
    printf("Enter a decimal number: ");
    return ask_int(decimal);
}

void ask_power(int * power) {
    printf("Enter a power: ");
	return ask_int(power);
}