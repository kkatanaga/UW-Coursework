#include <stdio.h>
#include <math.h>
#include <limits.h>
#include <assert.h>

int digit_sum_iterative(int n);
int digit_sum_recursive(int n);

int main(void) {
    int sum_iteration = 0;
    int sum_recursive = 0;

    // Iterative tests
    printf("Iterative: ");

    // Lower limit test
    sum_iteration = digit_sum_iterative(0);
    assert(sum_iteration != -1);
    printf("0 => %d, ", sum_iteration);

    // Example input test
    sum_iteration = digit_sum_iterative(1234);
    assert(sum_iteration != -1);
    printf("1234 => %d, ", sum_iteration);

    // Random input test 1
    sum_iteration = digit_sum_iterative(987654321);
    assert(sum_iteration != -1);
    printf("987654321 => %d, ", sum_iteration);

    // Random input test 2
    sum_iteration = digit_sum_iterative(572957194);
    assert(sum_iteration != -1);
    printf("572957194 => %d, ", sum_iteration);

    // Upper limit test
    sum_iteration = digit_sum_iterative(pow(10,9)-1);
    assert(sum_iteration != -1);
    printf("(10^9)-1 => %d\n", sum_iteration);
    
    // Recursive tests
    printf("Recursive: ");

    // Lower limit test
    sum_recursive = digit_sum_recursive(0);
    assert(sum_recursive != -1);
    printf("0 => %d, ", sum_recursive);

    // Example input test
    sum_recursive = digit_sum_recursive(1234);
    assert(sum_recursive != -1);
    printf("1234 => %d, ", sum_recursive);

    // Random input test 1
    sum_recursive = digit_sum_recursive(987654321);
    assert(sum_recursive != -1);
    printf("987654321 => %d, ", sum_recursive);

    // Random input test 2
    sum_recursive = digit_sum_recursive(572957194);
    assert(sum_recursive != -1);
    printf("572957194 => %d, ", sum_recursive);

    // Upper limit test
    sum_recursive = digit_sum_recursive(pow(10,9)-1);
    assert(sum_recursive != -1);
    printf("(10^9)-1 => %d\n", sum_recursive);
    
    printf("All tests passed successfully.\n");
}

/*/////////////////////////////////////////////////////////////////////
// digit_sum_iterative(n) returns the decimal sum of the digits in n //
// requires: 0 <= n < 10^9; otherwise return -1                      //
// note: implemented using iteration and not recursion               //
/////////////////////////////////////////////////////////////////////*/

int digit_sum_iterative(int n) {
    int result = 0;
    if ( n < 0 || n >= pow(10,9) )                      // Checks if the input is within range
        return -1;

    do {
        result += n%10;                                 // Adds every digit
    } while ( (n /= 10) > 0);                           // Removes used digit
    
    return result;
}

/*/////////////////////////////////////////////////////////////////////
// digit_sum_recursive(n) returns the decimal sum of the digits in n //
// requires: 0 <= n < 10^9; otherwise returns -1                     //
// note: implemented using recursion and not iteration               //
/////////////////////////////////////////////////////////////////////*/

int digit_sum_recursive(int n) {
    if ( n < 0 || n >= pow(10,9) )                      // Checks if the input is within range
        return -1;

    if (n > 0)
        return digit_sum_recursive(n/10) + n%10;        // Adds every digit
    else
        return 0;                                       // Base case; ends the loop
}