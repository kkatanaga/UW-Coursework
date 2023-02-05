#include <stdio.h>

void factorial(unsigned long base);

/*\=============================================================|
|   Program Name: exercise_b.c                                  |
|   Name: Keigo Katanaga                                        |
|   Student ID: 110068805                                       |
|   Date: 11/22/2021                                            |
|   Description: Calculates the factorial given a non-negative  |
|   integer input                                               |
|=============================================================\*/

int main()
{
    factorial(2);
    factorial(3);
    factorial(4);
    factorial(5);
    factorial(10);
    factorial(12);      // Maximum integer input; 13 and higher no longer works
    factorial(15);      // Output is too big
} 

void factorial(unsigned long base)
{
    printf("Factorial of %lu: ", base);     // Prints the base
    for (int i = base - 1; i > 0; i--)      // Loops for factorial multiples
        base = base * i;                    // base * i where base is the input and i is every factorial multiple less than the base

    printf("%lu\n", base);                  // Prints the factorial
}