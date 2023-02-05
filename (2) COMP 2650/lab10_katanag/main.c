#include <stdio.h>
#include <string.h>

#include "arithmetic.h"

#define INPUT_VARIABLE_COUNT 3
#define OUTPUT_VARIABLE_COUNT 1
#define TRUTH_TABLE_ROW_COUNT 8

void build_left_side(int truth_table[][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT]) {
	int row[INPUT_VARIABLE_COUNT] = {};
	int result[INPUT_VARIABLE_COUNT] = {};

	//build the left side of truth table for all combinations of 1's and 0's
	for(int i = 0; i < TRUTH_TABLE_ROW_COUNT - 1; i++){
		//accessing the element of i-th row

		//increment
		func_increment(row, result);	// From arithmetic.c

		//put into the next row: (i+1)-th row
		for (int j = 0; j < INPUT_VARIABLE_COUNT; j++)
			truth_table[i+1][j] = result[j];
	}
}

void build_right_side(int truth_table[][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT]) {
	char buffer;
	//build the right side of truth table for the output variables
	for (int i = 0; i < TRUTH_TABLE_ROW_COUNT; i++) {
		//for each output variable F1, F2, ...
		for (int j = INPUT_VARIABLE_COUNT; j < INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT; j++) {
			printf("Row #%d of output F: ", i);
			while( ( !scanf("%d", &truth_table[i][j]) && scanf(" %c", &buffer) ) || (truth_table[i][j] != 0 && truth_table[i][j] != 1) )  // Loops if input is neither 0 nor 1
				printf("Error: Row #%d: ", i);
		}
	}
}

void to_minterm(int truth_table[][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT], const char variables[INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT]) {
	for (int o = 0; o < OUTPUT_VARIABLE_COUNT; o++) {
		int zero_count = 0;
		printf("Output Variable F%d = ", o + 1);					// Prints the minterms of an output F

		for (int i = 0; i < TRUTH_TABLE_ROW_COUNT; i++) {
			if (truth_table[i][INPUT_VARIABLE_COUNT + o] == 0) {	// Skips a loop if an output is 0
				zero_count++;
				continue;
			}

			if (i > 0)												// Prints the '+' after the first and before the last minterms
				printf(" + ");

			for (int j = 0; j < INPUT_VARIABLE_COUNT; j++) {
				if (truth_table[i][j] == 0)							// Prints the variable'
					printf("%c'", variables[j]);
				else
					printf("%c", variables[j]);						// Prints the variable
			}
		}
		if (zero_count == TRUTH_TABLE_ROW_COUNT)
			printf("False");
		puts("");
	}
}

void to_maxterm(int truth_table[][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT], const char variables[INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT]) {
	for (int o = 0; o < OUTPUT_VARIABLE_COUNT; o++) {
		int one_count = 0;
		printf("Output Variable F%d = ", o + 1);					// Prints the maxterms of an output F

		for (int i = 0; i < TRUTH_TABLE_ROW_COUNT; i++) {
			if (truth_table[i][INPUT_VARIABLE_COUNT + o] == 1) {	// Skips a loop if an output is 1
				one_count++;
				continue;
			}

			printf("(");
			for (int j = 0; j < INPUT_VARIABLE_COUNT; j++) {
				if (j > 0)
					printf(" + ");
				if (truth_table[i][j] == 1)							// Prints the variable'
					printf("%c'", variables[j]);
				else
					printf("%c", variables[j]);						// Prints the variable
			}
			printf(")");
		}
		if (one_count == TRUTH_TABLE_ROW_COUNT)
			printf("False");
		puts("");
	}
}

void print_table(int truth_table[][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT], const char variables[INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT]) {
	puts("-----------");             // Separates the result from the main menu

	// Variable Header (Inputs)
	for (int i = 0; i < INPUT_VARIABLE_COUNT - 1; i++)
		printf("%c, ", variables[i]);															// Prints the variable header except the result variable and last input variable
	printf("%c : ", variables[INPUT_VARIABLE_COUNT - 1]);										// Prints the last input variable

	// Variable Header (Outputs)
	for (int i = INPUT_VARIABLE_COUNT; i < INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT - 1; i++)
		printf("%c, ", variables[i]);															// Prints the output variables except the last
	printf("%c\n", variables[INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT - 1]);				// Prints the last output variable

    puts("-----------");             // Separates the result from the main menu

	for (int i = 0; i < TRUTH_TABLE_ROW_COUNT; i++) {
		// Inputs
		for (int j = 0; j < INPUT_VARIABLE_COUNT - 1; j++)
			printf("%d, ", truth_table[i][j]);													// Prints a row of binary except the rightmost digit
		printf("%d : ", truth_table[i][INPUT_VARIABLE_COUNT - 1]);								// Prints the rightmost digit

		// Outputs
		for (int j = INPUT_VARIABLE_COUNT; j < INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT - 1; j++)
			printf("%d", truth_table[i][j]);													// Prints a row of outputs except the last
		printf("%d\n", truth_table[i][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT - 1]);		// Prints the last output
	}

    puts("-----------");             // Separates the result from the main menu
}
int main(void) {
    setbuf(stdout, NULL);

	int truth_table[TRUTH_TABLE_ROW_COUNT][INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT] = {};
	const char variables[INPUT_VARIABLE_COUNT + OUTPUT_VARIABLE_COUNT] =  {'Z', 'Y', 'X', 'F'};

	int input;
	char buffer;

	build_left_side(truth_table);

	build_right_side(truth_table);

	print_table(truth_table, variables);

	puts("Choose a command.");
	puts("0) Exit");
	puts("1) Canonical SoP");
	puts("2) Canonical PoS");
	printf("Command: ");
	while ( ( !scanf("%d", &input) && scanf(" %c", &buffer) ) || (input != 0 && input != 1 && input != 2) )
		puts("Please enter a proper command number.");
	
	if (input == 0) {
		puts("Exiting program...");
		return 0;
	}

	switch (input) {
	case 1:
		to_minterm(truth_table, variables);
		break;
	case 2:
		to_maxterm(truth_table, variables);
		break;
	}

	printf("Press enter to exit...");
	scanf("%c%c", &buffer, &buffer);	// Stops main.exe from closing automatically
}