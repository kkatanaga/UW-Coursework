#include <stdio.h>
#include <math.h>

#include "logic.h"
#include "complement.h"
#include "conversion.h"
#include "ask.h"
#include "print.h"

#define MAX 8

int main(void) {
    setbuf(stdout, NULL);

    int x[MAX], y[MAX], z[MAX];                                 // x contains the first binary, y contains the second binary, z contains the result
    int command = -1;											// Contains the command number
    int base = 0;												// Contains the base number
    char c;

    do {                                                        // Loops until 0 (command for exit) is entered
    	print_command_selection();
        // Loops when the input is not any of the command numbers
        while( (!scanf("%d", &command) && scanf(" %c", &c)) || (command != 1 && command != 2 && command != 3 && command != 4 && command != 5 && command != 6) ) {
        	if(command == 0) {
        		puts("Exiting the program...");
        		return 0;
        	}
            printf("Error: Please enter a specified selection: ");
        }

        // Applies the selected operation
        switch(command) {
            case 1:                                             // AND case
                ask_two_binaries(x, y);
                func_and(x, y, z);
                break;
            case 2:                                             // OR case
                ask_two_binaries(x, y);
                func_or(x, y, z);
                break;
            case 3:                                             // NOT case
                ask_one_binary(x);
                func_not(x, z);
                break;
            case 4:                                             // 1's Complement case
                ask_one_binary(x);
                func_1s_comp(x, z);
                break;
            case 5:                                             // 2's Complement case
                ask_one_binary(x);
                func_2s_comp(x, z);
                break;
            case 6:                                             // 2's Complement* case
                ask_one_binary(x);
                func_2s_comp_star(x, z);
                break;
        }

        print_base_selection();
		// Loops when the input is not any of the specified bases
		while( (!scanf("%d", &base) && scanf(" %c", &c)) || (base != 1 && base != 2 && base != 3 && base != 4) )
			printf("Error: Please enter a specified selection: ");

		// Converts binary to selected base
		switch(base) {
			case 1:                                             // Binary case
				break;
			case 2:                                             // Octal case
				to_octal(z);
				break;
			case 3:                                             // Decimal case
				to_decimal(z);
				break;
			case 4:                                             // Hexadecimal case
				to_hexadecimal(z);
				break;
		}

		// Prints the input(s) and output
		print_result(x, y, z, command, base);
        puts("\n--------------------------------");             // Separates the result from the main menu
    } while(command != 0);
}
