#include <stdio.h>
#include <stdbool.h>
#include <assert.h>

// all_odd_digits(n) returns true if all the digits in n are odd
// requires: 0 <= n < 10ˆ9
bool all_odd_digits (int n) {
    if (n % 2 != 0) {
        if (0 < n && n < 10) return true;
        else return all_odd_digits(n / 10);
    } 
    else
        return false;
}

int main(void) {
    assert( true == all_odd_digits(1) );
    assert( true == all_odd_digits(5) );
    assert( true == all_odd_digits(13579) );
    assert( false == all_odd_digits(21) );
    assert( false == all_odd_digits(10) );
    assert( false == all_odd_digits(212121) );
    assert( false == all_odd_digits(0) );
    assert( true == all_odd_digits(999999999) );
    printf("All tests passed successfully!\n");
}

/*
    all_odd_digits() tests whether every digit in a given input (n) is odd (return true), or even (return false).
    The function checks to see if n is odd by dividing by 2.
    If n isn't divisible by 2 (not even), then the function checks if n is within the interval (0, 10).
        If n is within the interval, n is a single digit. And since n is odd, the function returns true. (Base Case)
        Else, the function is greater than 10, but n is still odd, so remove the ones digit to check the next digit if odd; recursion starts.
    Else, n is divisible by 2 (even), so return false.
*/