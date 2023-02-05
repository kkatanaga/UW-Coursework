#include <stdio.h>
#define MAX 8

// Prints the command selection menu
void print_command_selection() {
	puts("Choose a command number:");
	puts("0) Exit");
	puts("1) AND");
	puts("2) OR");
	puts("3) NOT");
	puts("4) 1's Complement");
	puts("5) 2's Complement");
	puts("6) 2's Complement*");
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
void print_octal(int binary[]) {
	char temp[9];
	int baseOctal = 0;

	// Converts the int array into a string
	for(int i = 0; i < 8; i++)
		sprintf(&temp[i], "%o", binary[i]);
	// Converts the string to an int of base 8 to remove padding
	sscanf(temp, "%o", &baseOctal);

	printf("%o", baseOctal);
}

// Prints decimal (base 10)
void print_decimal(int binary[]) {
	char temp[9];
	int baseDecimal = 0;

	// Converts the int array into a string
	for(int i = 0; i < 8; i++)
		sprintf(&temp[i], "%d", binary[i]);
	// Converts the string to an int of base 10 to remove padding
	sscanf(temp, "%d", &baseDecimal);

	printf("%d", baseDecimal);
}

// Prints hexadecimal (base 16)
void print_hexadecimal(int binary[]) {
	char temp[9];
	int baseHex = 0;

	// Converts the int array into a string
	for(int i = 0; i < 8; i++)
		sprintf(&temp[i], "%X", binary[i]);
	// Converts the string to an int of base 16 to remove padding
	sscanf(temp, "%X", &baseHex);

	printf("%X", baseHex);
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
    switch(command) {
		case 1:
			print_binary_operation(binaryOne, "AND", binaryTwo);
			break;
		case 2:
			print_binary_operation(binaryOne, "OR", binaryTwo);
			break;
		case 3:
			print_unary_operation(binaryOne, "NOT");
			break;
		case 4:
			print_unary_operation(binaryOne, "1's Complement");
			break;
		case 5:
			print_unary_operation(binaryOne, "2's Complement");
			break;
		case 6:
			print_unary_operation(binaryOne, "2's Complement*");
    }
    switch(base) {
		case 1:                                             // Binary case
			print_base("binary");
			print_binary(result);
			break;
		case 2:                                             // Octal case
			print_base("octal");
			print_octal(result);
			break;
		case 3:                                             // Decimal case
			print_base("decimal");
			print_decimal(result);
			break;
		case 4:                                             // Hexadecimal case
			print_base("hexadecimal");
			print_hexadecimal(result);
			break;
	}
}


