#include <stdio.h>
#include <stdbool.h>
#include "arithmetic.h"
#include "conversion.h"
#define MAX 8

// Prints the command selection menu
void print_command_selection() {
	puts("Choose a command number:");
	puts("0) Exit");
	puts("1) Addition in Signed-Magnitude");
	puts("2) Subtraction in Signed-Magnitude");
	printf("Input: ");
}

// Prints the output base selection menu
void print_base_selection() {
	puts("Enter the base of the output:");
	puts("1) Binary");
	puts("2) Octal");
	puts("3) Decimal");
	puts("4) Hexadecimal");
	printf("Input: ");
}
// Prints binary (base 2)
void print_binary(int binary[]) {
    for(int i = 0; i < MAX; i++)
        printf("%d", binary[i]);
}

// Prints octal (base 8)
void print_octal(int binary[], bool sign_negative) {
	char temp[9];
	int base_octal = 0;

	// Converts the int array into a string
	for(int i = 0; i < MAX; i++)
		sprintf(&temp[i], "%o", binary[i]);
	// Converts the string to an int of base 8 to remove padding
	sscanf(temp, "%o", &base_octal);

	if (sign_negative == true)
		printf("-");

	printf("%o", base_octal);
}

// Prints decimal (base 10)
void print_decimal(int binary[], bool sign_negative) {
	char temp[9];
	int base_decimal = 0;

	// Converts the int array into a string
	for(int i = 0; i < MAX; i++)
		sprintf(&temp[i], "%d", binary[i]);
	// Converts the string to an int of base 10 to remove padding
	sscanf(temp, "%d", &base_decimal);

	if (sign_negative == true)
		base_decimal = -base_decimal;

	printf("%d", base_decimal);
}

// Prints hexadecimal (base 16)
void print_hexadecimal(int binary[], bool sign_negative) {
	char temp[9];
	int base_hex = 0;

	// Converts the int array into a string
	for(int i = 0; i < MAX; i++)
		sprintf(&temp[i], "%X", binary[i]);
	// Converts the string to an int of base 16 to remove padding
	sscanf(temp, "%X", &base_hex);

	if (sign_negative == true)
		printf("-");

	printf("%X", base_hex);
}

// Prints out what operation is done on two binary inputs
void print_binary_operation(int binaryOne[], char type[], int binaryTwo[]) {
	print_binary(binaryOne);
	printf(" %s ", type);
	print_binary(binaryTwo);
}

// Prints out what operation is done on one binary input
void print_unary_operation(int binaryOne[], char type[]) {
	printf("%s of ", type);
	print_binary(binaryOne);
}

// Prints out the chosen base (binary, octal, decimal, hexadecimal)
void print_base(char base[]) {
	printf(" in %s yields:\n", base);
}

// Prints the inputs and their resulting output
void print_result(int binaryOne[], int binaryTwo[], int result[], int command, int base) {
	bool sign = check_sign(result);

    switch(command) {
		case 1:
			print_binary_operation(binaryOne, "+", binaryTwo);
			break;
		case 2:
			print_binary_operation(binaryOne, "-", binaryTwo);
			break;
    }
    switch(base) {
		case 1:                                             // Binary case
			print_base("binary");
			if (sign == true)
				printf("-");
			print_binary(result);
			break;
		case 2:                                             // Octal case
			print_base("octal");
			to_octal(result);
			print_octal(result, sign);
			break;
		case 3:                                             // Decimal case
			print_base("decimal");
			to_decimal(result);
			print_decimal(result, sign);
			break;
		case 4:                                             // Hexadecimal case
			print_base("hexadecimal");
			to_hexadecimal(result);
			print_hexadecimal(result, sign);
			break;
	}
}


