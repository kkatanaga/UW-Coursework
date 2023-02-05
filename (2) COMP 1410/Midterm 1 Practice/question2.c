#include <stdio.h>
#include <assert.h>

// count_ones(n) returns the number of 1s in the decimal representation of n
// requires: 0 <= n < 10^9
int count_ones(int n) {
    int counter = 0;
    
    while (n != 0) {
        if (n % 10 == 1)
            counter++;
        n /= 10;
    }
    
    return counter;
}

// count_ones_up_to_n(n) returns how many 1s are in the decimal representation
//   of the integers between 1 and n
// requires: 0 <= n < 10^5
int count_ones_up_to_n(int n) {
    int counter = 0;
    if (n == 1)
        return 1;
    counter = count_ones(n);
    return counter + count_ones_up_to_n(n - 1);
}

int main(void) {
    int input;
    int result;

    puts("Enter your number:");
    scanf("%d", &input);

    result = count_ones_up_to_n(input);

    assert(count_ones_up_to_n(10) == 2);
    assert(count_ones_up_to_n(11) == 4);
    assert(count_ones_up_to_n(20) == 12);
    
    printf("There are %d 1's in %d.\n", result, input);
}