#include <stdio.h>
#include <stdbool.h>
#include <limits.h>
#include <assert.h>

/*\=====================================================================|
|   Program Name: question2.c                                  			|
|   Name: Keigo Katanaga                                        		|
|   Student ID: 110068805                                       		|
|   Date: 02/04/2022                                            		|
|   Description: Checks whether the input is a prime number or not.     |
|   It does so by counting how many possible divisors an input has,     |
|   and whether there's exactly 2 divisors (prime) or not (not prime).  |
|                                                                       |
|   Does not take inputs from user.                                     |
|=====================================================================\*/

// num_divisors_up_to_k(n,k) returns the number of positive divisors
//   of n that are less than or equal to k
// requires: 1 <= k <= n
int num_divisors_up_to_k(int n, int k) {
    if (k < 1 || n < k)
        return 0;
    
    if (n % k == 0)
        return 1 + num_divisors_up_to_k(n,k-1);
    else
        return num_divisors_up_to_k(n,k-1);
}

// is_prime(n) returns true if n is a prime number and false otherwise
// requires: 1 <= n
bool is_prime(int n) {
    if ( num_divisors_up_to_k(n,n) == 2 )
        return true;
    else
        return false;
}

int main(void) {
    bool test;                                          // Contains the test result for whether a number is prime or not
    
    // Test common prime and non-prime numbers
    assert( (test = is_prime(0)) == false);
    printf("Check if 0 is Prime: %d\n", test);

    assert( (test = is_prime(1)) == false);
    printf("Check if 1 is Prime: %d\n", test);

    assert( (test = is_prime(2)) == true);
    printf("Check if 2 is Prime: %d\n", test);

    assert( (test = is_prime(3)) == true);
    printf("Check if 3 is Prime: %d\n", test);

    assert( (test = is_prime(4)) == false);
    printf("Check if 4 is Prime: %d\n", test);

    assert( (test = is_prime(5)) == true);
    printf("Check if 5 is Prime: %d\n", test);

    assert( (test = is_prime(6)) == false);
    printf("Check if 6 is Prime: %d\n", test);

    assert( (test = is_prime(9)) == false);             // Test example input & output from instructions
    printf("Check if 9 is Prime: %d\n", test);

    assert( (test = is_prime(-1)) == false);            // Test negative input
    printf("Check if -1 is Prime: %d\n", test);

//     assert( (test = is_prime(INT_MAX)) == true);        // Test upper edge case
//     printf("Check if INT_MAX is Prime: %d\n", test);

//     assert( (test = is_prime(INT_MIN)) == false);       // Test lower edge case
//     printf("Check if INT_MIN is Prime: %d\n", test);
}