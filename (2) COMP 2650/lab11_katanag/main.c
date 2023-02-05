#include <stdio.h>
#include <math.h>
#include "conversion.h"
#include "print.h"

#define BYTE 8
#define MAX pow(2, BYTE) - 1

/*\=============================================================================|
|   Program Name: main.c                                  					    |
|   Name: Keigo Katanaga                                        				|
|   Student ID: 110068805                                       				|
|   Date: 03/29/2022                                            			    |
|   Description: Prints a Gray Code based on an input decimal.					|
|	Only inputs from 0 - 255.													|
|=============================================================================\*/

// Converts the input decimal to gray code
void to_Gray(int decimal, int result[]) {
	int binary[BYTE] = {};
	to_binary(decimal, binary);

	result[0] = binary[0];
	for (int i = 1; i < BYTE; i++) {
		result[i] = binary[i - 1] ^ binary[i];
	}
}

int main(void) {
    int input = 0, command = 2;                                                              			    // input contains the integer, command contains the command number
    int output[BYTE] = {};																				    // output contains the gray code
	char buffer;                                                                                            // acts as a buffer for character inputs

    while(1) {                                                                             				    // Loops until 0 (command for exit) is entered
        print_main_menu();

        while( ( !scanf("%d", &command) && scanf(" %c", &buffer) ) || (command != 0 && command != 1) )		// Loops when the input is a character or not 0 nor 1
            printf("Error: Please enter 0 (Exit), 1 (Gray Code): ");

        switch(command) {
            case 0:                                                                                 		// Exit case
                puts("Exiting the program...");																// Prints an exit message
				return 0;																					// Stops the whole program
                break;
            case 1:                                                                                 	    // Gray Code case
                printf("Enter an integer from 0 to 255: ");
                while( ( !scanf("%d", &input) && scanf(" %c", &buffer) ) || input < 0 || input > MAX)		// Keeps asking for an integer
                    printf("Error: please enter an integer: ");
				
				to_Gray(input, output);																		// Converts the input into gray code

                print_result(input, output);                                                                // Prints a result message
                break;
        }
        puts("\n--------------------------------");                                                   		// Closes the result from the main menu
    }
}