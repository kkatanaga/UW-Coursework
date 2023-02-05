#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <limits.h>
#include <assert.h>

/*\=====================================================================|
|   Program Name: lab2.c                                  	            |
|   Name: Keigo Katanaga                                                |
|   Student ID: 110068805                                               |
|   Date: 02/10/2022                                                    |
|   Description: Two numbers are swapped so that the first number is    |
|   less than or equal to the second number (a <= b). This is done      |
|   using pointers. The difference between the two numbers is           |
|   calculated in absolute value.                                       |
|                                                                       |
|   The first number is the integer input before the second number:     |
|       order(first number, second number, difference)                  |
|=====================================================================\*/

// order(a, b, diff) orders the values pointed to by a and b so that *a <= *b;
//   *diff is set to absolute value of the difference between *a and *b;
//   returns true if the values were switched and false otherwise
// requires: a, b, and diff point to memory that can be modified
bool order(int * const a, int * const b, int * const diff) {
    *diff = abs(*a - *b);
    if(*a > *b) {
        int temp = *a;
        *a = *b;
        *b = temp;
        return true;
    }
    return false;
}

int main(void) {
    // Testing a > b (should swap; return true)
    int left = 14, right = 6, difference = 0;
    printf("Inputs: a = %d, b = %d, difference = %d\n", left, right, difference);
    assert(order(&left, &right, &difference) == true);
    printf("Result: a = %d, b = %d, difference = %d\n\n", left, right, difference);

    // Testing a < b (shouldn't swap; return false)
    left = -15; right = -10; difference = 0;
    printf("Inputs: a = %d, b = %d, difference = %d\n", left, right, difference);
    assert(order(&left, &right, &difference) == false);
    printf("Result: a = %d, b = %d, difference = %d\n\n", left, right, difference);

    // Testing a == b (no need to swap; return false)
    left = 10; right = 10; difference = 0;
    printf("Inputs: a = %d, b = %d, difference = %d\n", left, right, difference);
    assert(order(&left, &right, &difference) == false);
    printf("Result: a = %d, b = %d, difference = %d\n\n", left, right, difference);

    // Testing upper & lower edge case; a > b (should swap; return true)
    left = INT_MAX; right = INT_MIN; difference = 0;
    printf("Inputs: a = %d, b = %d, difference = %d\n", left, right, difference);
    assert(order(&left, &right, &difference) == true);
    printf("Result: a = %d, b = %d, difference = %d\n\n", left, right, difference);

    // Testing upper edge & lower edge case; a < b (no need to swap; return false)
    left = INT_MIN; right = INT_MAX; difference = 0;
    printf("Inputs: a = %d, b = %d, difference = %d\n", left, right, difference);
    assert(order(&left, &right, &difference) == false);
    printf("Result: a = %d, b = %d, difference = %d\n\n", left, right, difference);

    printf("All tests passed successfully.\n");
}