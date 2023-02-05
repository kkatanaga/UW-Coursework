#include <stdio.h>

#include "print.h"
#include "ask.h"

/*\=============================================================================|
|   Program Name: main.c                                  					    |
|   Name: Keigo Katanaga                                        				|
|   Student ID: 110068805                                       				|
|   Date: 04/09/2022                                            			    |
|   Description: Does powers or divisions of 2 using arithmetic shifts.			|
|	An input decimal and input power is needed, such that:						|
|		Power of 2: 															|
|			decimal * (2 ^ power)												|
|		Division of 2: 															|
|			decimal / (2 ^ power)												|
|=============================================================================\*/

int main(void) {
    int decimal, power, command;                                                            		// decimal contains the first boolean, power contains the second boolean, command contains the command number
    char buffer;                                                                                    // acts as a buffer for character inputs

    while(1) {                                                                                		// Loops until a break (command == 0) occurs
        print_main_menu();

		if (ask_command(&command) == 0) break;														// Stops the loop; base case

		ask_decimal(&decimal);																		// Asks for a decimal integer
		ask_power(&power);																			// Asks for a power integer

        switch(command) {
            case 1:                                                                                 // Powers of 2
            	printf("%d*(2^%d) = %d\n", decimal, power, decimal << power);
				break;
            case 2:                                                                                 // Division of 2
            	printf("%d/(2^%d) = %d\n", decimal, power, decimal >> power);
                break;
        }
        puts("--------------------------------");                                                   // Closes the result from the main menu
    }
	printf("Press enter to exit...");																// Prompts an exit message
	scanf("%c%c", &buffer, &buffer);
}