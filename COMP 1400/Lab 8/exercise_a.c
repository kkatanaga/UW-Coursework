#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <ctype.h>

/*\=============================================================|
|   Program Name: exercise_a.c                                  |
|   Name: Keigo Katanaga                                        |
|   Student ID: 110068805                                       |
|   Date: 11/16/2021                                            |
|   Description: Sorts n random values through bubble sort;     |
|   n specifies the range value of random values.               |
|=============================================================\*/

int main(void) {
    clock_t start, end;                                                                 // Contains the initial time, and the final time of the sort
    int n = 'a', temp;                                                                  // Contains the number of values to be sorted, a value temporarily
    char c;                                                                             // Buffer for a non-integer input

    srand(time(NULL));                                                                  // Randomizes the seed
    printf("Specify how many numbers to sort:\n");                                      // Asks for the number of values to be sorted
    while ( !scanf("%d", &n) || n < 0) {                                                // Loops until a non-negative integer is entered
        if ( isalpha(n) ) {                                                                     // Activates when input is a character
            printf("Non-numerical; please enter an integer:\n");                                    // Prints an error indicating a character input
            scanf(" %c", &c);                                                                       // Saves character in buffer
        }                                                                                               // *End if(character)
        else {                                                                                  // Activates when input is negative
            printf("Negative array; please enter a non-negative integer:\n");                       // Prints an error indicating a negative input
            n = 'a';                                                                                // Resets n
        }                                                                                               // *End else
    }                                                                                                       // *End while(non-integer or negative)

    int rando[n];                                                                       // Defines the array

    for (int x = 0; x < n; x++)                                                         // Loops until the last index is reached
        rando[x] = rand()%(n+1);                                                                // Inputs a random number from 1 to n

    start = clock();                                                                    // Starts the timer (initial time)
    for (int i = 1; i < n; i++) {                                                       // Loops n-1 times
        for (int j = 1; j <= n-i; j++) {                                                        // Loops n-i times
            if (rando[j-1] > rando[j]) {                                                            // Activates when the value before is greater than the value after
                temp = rando[j-1];                                                                      // Temporarily contains the value before
                rando[j-1] = rando[j];                                                                  // Value before = Value after
                rando[j] = temp;                                                                        // Value after = Value before
            }                                                                                               // *End if(before>after)
        }                                                                                                       // *End for(j<=n-i)
    }                                                                                                               // *End for(i<=n-1)
    end = clock();                                                                      // Ends the timer (final time)

    for (int y = 0; y < n; y++) {                                                       // Loops until all values in the array are printed
        if (y%10 == 0 && y%100 != 10 && y != 10)
            printf("%dst value: %d\n", y+1, rando[y]);                                          // Prints the (y+1)st values
        else if (y%10 == 1 && y%100 != 11 && y != 11)
            printf("%dnd value: %d\n", y+1, rando[y]);                                          // Prints the (y+1)nd values
        else if (y%10 == 2 && y%100 != 12 && y != 12)
            printf("%drd value: %d\n", y+1, rando[y]);                                          // Prints the (y+1)rd values
        else
            printf("%dth value: %d\n", y+1, rando[y]);                                          // Prints the rest of the values
    }                                                                                               // *End for(whole array)

    printf("Time elapsed (ticks): %.2f\n", difftime(end, start));                       // Calculates the ticks elapsed
    printf("Time elapsed (secs): %.2f\n", difftime(end, start)/CLOCKS_PER_SEC);         // Calculates the seconds elapsed
}                                                                                           // *End main()