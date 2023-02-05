#include <stdio.h>
#include <limits.h>
#include <assert.h>

/*\=========================================================|
|   Program Name: question1.c                               |
|   Name: Keigo Katanaga                                    |
|   Student ID: 110068805                                   |
|   Date: 02/04/2022                                        |
|   Description: Calculates the combination of two inputs.  |
|                                                           |
|   Does not take inputs from user.                         |
|=========================================================\*/

// choose(n,m) returns how many ways there are to choose m items from
//   a set of n items
// requires: 0 <= m, 0 <= n
int choose(int n, int m) {
    if (m == n || m == 0)
        return 1;
    else
        return choose(n-1,m) + choose(n-1,m-1);
}

int main(void) {
    int result;                                             // Initialize the container for the number of combinations

    // Test every combination of 4 items
    assert( (result = choose(4,0)) == 1 );
    printf("4 Choose 0: %d\n", result);

    assert( (result = choose(4,1)) == 4 );
    printf("4 Choose 1: %d\n", result);

    assert( (result = choose(4,2)) == 6 );                  // Test example input & output from instructions
    printf("4 Choose 2: %d\n", result);

    assert( (result = choose(4,3)) == 4 );
    printf("4 Choose 3: %d\n", result);

    assert( (result = choose(4,4)) == 1 );
    printf("4 Choose 4: %d\n", result);

    assert( (result = choose(INT_MAX,0)) == 1 );            // Test upper & lower edge cases
    printf("INT_MAX Choose 0: %d\n", result);

    assert( (result = choose(INT_MAX,INT_MAX / 2)) > 0 );   // Test upper edge case
    printf("INT_MAX Choose 0: %d\n", result);
}