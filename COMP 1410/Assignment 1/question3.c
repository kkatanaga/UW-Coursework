#include <stdio.h>
#include <limits.h>
#include <ctype.h>
#include <assert.h>

/*\=============================================================|
|   Program Name: question3.c                                  	|
|   Name: Keigo Katanaga                                        |
|   Student ID: 110068805                                       |
|   Date: 02/04/2022                                            |
|   Description: Applies the Collatz sequence to an input.      |
|   A Collatz sequence acts depending on an odd or even input:  |
|       for input n:                                            |
|           if odd, then 3n+1;                                  |
|           if even, then n/2;                                  |
|       repeat until n = 1.                                     |
|   This program counts how many steps are taken until the      |
|   input reaches 1. The resulting number is then printed       |
|   along with the input.                                       |
|                                                               |
|   Takes inputs from the user.                                 |
|=============================================================\*/

// collatz(n) returns the number of steps it takes to reach 1 by
//   by repeatedly applying the Collatz mapping on n; prints each
//   number in the sequence starting at n
// requires: 1 <= n
int collatz(int n) {
    if (n <= 1) {
        printf("%d\n", n);
        return 0;
    }

    printf("%d -> ", n);

    if ( (n % 2) == 0 )
        return 1 + collatz(n/2);
    else
        return 1 + collatz( (3 * n) + 1 );
}

// printResult(start,steps) prints the result specific to the 
//   number of steps taken and returns nothing
// requires: 1 <= start, steps = collatz(start)
void printResult(int start, int steps) {
    if (steps == 1)
        printf("Collatz sequence of %d: %d step\n\n", start, steps);
    else
        printf("Collatz sequence of %d: %d steps\n\n", start, steps);
}

int main(void) {
    int count, input;
    char buf;

    do {
        printf("Enter an integer, or enter 'X' to exit: ");
        while( (!scanf("%d", &input) && scanf(" %c", &buf)) || input < 1) {     // Loops if the input is a character or less than 1
            if ( (buf = toupper(buf)) == 'X' )                                  // Special case: input X exits the whole loop
                break;
            printf("Error: Invalid integer. Please try again: ");
        }
        count = collatz(input);                                                 // Counts how many steps are taken in the Collatz sequence with the input entered
        printResult(input, count);                                              // Prints the result
    } while (buf != 'X');
    puts("Exiting loop...");

    puts("--------------------------------");                                   // Separates the user interface from test cases

    // Test easy inputs
    assert( (count = collatz(1)) == 0);                                         // Tests lower edge
    printResult(1,count);
    
    assert( (count = collatz(2)) == 1);
    printResult(2,count);

    assert( (count = collatz(3)) == 7);
    printResult(3,count);

    assert( (count = collatz(5)) == 5);                                         // Tests example input & output from instructions
    printResult(5,count);

    assert( (count = collatz(10)) == 6);
    printResult(10,count);

    assert( (count = collatz(11)) == 14);
    printResult(11,count);

    assert( (count = collatz(-1)) == 0);                                        // Test negative input
    printResult(-1,count);
}