#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <ctype.h>
#define SIZE 30

/*\=====================================================|
|   Program Name: exercise_b.c                          |
|   Name: Keigo Katanaga                                |
|   Student ID: 110068805                               |
|   Date: 11/16/2021                                    |
|   Description: Creates 30 random values and finds     |
|   the frequency of input num                          |
|=====================================================\*/

int main(void) {
    int vals[SIZE], num = 'a', freq = 0;                                                // Container for the random numbers 0-20, number to look for, and the frequency of that number
    char c;                                                                             // Buffer for non-integer input
    srand(time(NULL));                                                                  // Randomizes the seed

    for (int i = 0; i < SIZE; i++)                                                      // Loops until the array is full
        vals[i] = rand()%21;                                                                    // Inserts random numbers 0-20 in the array
  
    printf("Enter an integer number between 0 and 20:\n");                              // Asks for an integer within 0-20
    while ( !scanf("%d", &num) || (num < 0 || num > 20 ) ) {                            // Loops until an integer within 0-20 is entered
        if ( isalpha(num) ) {                                                                   // Activates when input is a character
            printf("Non-numerical; please enter a number between 0 and 20:\n");                     // Prints an error indicating a character input
			scanf(" %c", &c);                                                                       // Saves character in buffer
        }                                                                                               // *End if(character)
	    else {                                                                                  // Activates when input is less than 0 or greater than 20
            printf("Out of bounds; please enter a number between 0 and 20:\n");                     // Prints an error indicating an input not within 0-20
            num = 'a';                                                                              // Resets num
        }                                                                                               // *End else
	}	                                                                                                    // *End while(char or num<0 or num>20)
    
    for (int x = 0; x < SIZE; x++) {                                                    // Loops until all values in the array are checked
        if (vals[x] == num)                                                                     // Activates when the value in the array matches the input value
            freq++;                                                                                 // Adds 1 to the frequency counter
    }                                                                                                   // *End for(whole array)

    for (int y = 0; y < SIZE; y++) {                                                    // Loops until all values in the array are printed
        if (y%10 == 0 && y%100 != 10 && y != 10)
            printf("%dst value: %d\n", y+1, vals[y]);                                           // Prints the (y+1)st values
        else if (y%10 == 1 && y%100 != 11 && y != 11)
            printf("%dnd value: %d\n", y+1, vals[y]);                                           // Prints the (y+1)nd values
        else if (y%10 == 2 && y%100 != 12 && y != 12)
            printf("%drd value: %d\n", y+1, vals[y]);                                           // Prints the (y+1)rd values
        else
            printf("%dth value: %d\n", y+1, vals[y]);                                           // Prints the rest of the values
    }                                                                                               // *End for(whole array)

    printf("Number of %d's found: %d\n", num, freq);                                            // Prints the frequency
}                                                                                           // *End main()