#include <stdio.h>

/*\=============================================================================|
|   Program Name: main.c                                  					    |
|   Name: Keigo Katanaga                                        				|
|   Student ID: 110068805                                       				|
|   Date: 01/26/2022                                            			    |
|   Description: Simple boolean program. Compares between two inputs (0 & 1); 0 |
|   indicates False, 1 indicates True. AND & OR comparisons can be executed.    |
|=============================================================================\*/

int main(void) {
    int x = 0, y = 0, z = 3;                                                                  	    // x contains the first boolean, y contains the second boolean, z contains the command number
    char c;                                                                                         // acts as a buffer for character inputs

    while( z != 0) {                                                                                // Loops until 0 (command for exit) is entered
        puts("Choose a command number:");
        puts("0) Exit");
        puts("1) AND");
        puts("2) OR");
        printf("Input: ");

        while( ( !scanf("%d", &z) && scanf(" %c", &c) ) || (z != 0 && z != 1 && z != 2) )           // Loops when the input is a character or not 0, 1, or 2
            printf("Error: please enter 0 (Exit), 1 (AND), or 2 (OR): ");

        switch(z) {
            case 0:                                                                                 // Exit case
                puts("Exiting the program...");
                break;
            case 1:                                                                                 // AND case
                printf("Enter your first number (0 or 1): ");
                while( ( !scanf("%d", &x) && scanf(" %c", &c) ) || (x != 0 && x != 1) )                 // Keeps asking for the first input if it's neither 0 nor 1
                    printf("Error: please enter a 0 or 1: ");
                
                printf("Enter your second number (0 or 1): ");
                while( ( !scanf("%d", &y) && scanf(" %c", &c) ) || (y != 0 && y != 1) )                 // Keeps asking for the second input if it's neither 0 nor 1
                    printf("Error: please enter a 0 or 1: ");

                printf("%d AND %d is: %d\n", x, y, x & y);                                          // Prints the AND result
                break;
            case 2:                                                                                 // OR case
                printf("Enter your first number (0 or 1): ");
                while( ( !scanf("%d", &x) && scanf(" %c", &c) ) || (x != 0 && x != 1) )                 // Keeps asking for the first input if it's neither 0 nor 1
                    printf("Error: please enter a 0 or 1: ");
                
                printf("Enter your second number (0 or 1): ");
                while( ( !scanf("%d", &y) && scanf(" %c", &c)  ) || (y != 0 && y != 1) )                // Keeps asking for the second input if it's neither 0 nor 1
                    printf("Error: please enter a 0 or 1: ");

                printf("%d OR %d is: %d\n", x, y, x | y);                                           // Prints the OR result
                break;
        }
        puts("--------------------------------");                                                   // Closes the result from the main menu
    }
}