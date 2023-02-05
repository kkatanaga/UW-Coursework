#include <stdio.h>
#include <math.h>
#include <unistd.h>

#include "logic.h"
#include "complement.h"
#include "conversion.h"
#include "arithmetic.h"
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
        while( (!scanf("%d", &command) && scanf(" %c", &c)) || (command != 1 && command != 2) ) {
        	if(command == 0) {
        		puts("Exiting the program...");
        		return 0;
        	}
            printf("Error: Please enter a specified selection: ");
        }

        // Applies the selected operation
        switch(command) {
            case 1:                                             // Signed magnitude addition case
                ask_two_binaries(x, y);
                func_signed_2s_addition(x, y, z);
                break;
            case 2:                                             // Signed magnitude subtraction case
                ask_two_binaries(x, y);
                func_signed_2s_subtraction(x, y, z);
                break;
        }

        print_base_selection();
		// Loops when the input is not any of the specified bases
		while( (!scanf("%d", &base) && scanf(" %c", &c)) || (base != 1 && base != 2 && base != 3 && base != 4) )
			printf("Error: Please enter a specified selection: ");

		// Prints the input(s) and output (output is printed in the specified radix)
		print_result(x, y, z, command, base);
        puts("\n--------------------------------");             // Separates the result from the main menu
    } while(command != 0);
}
