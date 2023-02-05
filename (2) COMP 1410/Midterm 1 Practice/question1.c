#include <stdio.h>
#include <assert.h>

// count_ones(n) returns the number of 1s in the decimal representation of n
// requires: 0 <= n < 10^9
int count_ones(int n) {
    int counter = 0;
    if (n == 0)
        return counter;
    if (n % 10 == 1)
        counter++;
    return counter + count_ones(n / 10);
}

int main(void) {
    int input;
    int result;

    puts("Enter your number:");
    scanf("%d", &input);

    result = count_ones(input);

    assert(count_ones(1) == 1);
    assert(count_ones(10) == 1);
    assert(count_ones(11111) == 5);
    assert(count_ones(1231) == 2);
    
    printf("There are %d 1's in %d.\n", result, input);
}