#include <stdio.h>
#include <assert.h>

// sum_odd_in_array(a, n, count) returns the sum of the odd integers
//   in the array a of length n; *count is updated to the number of
//   odd integers in the array
// requires: count points to memory that can be updated
int sum_odd_in_array (int a[], int n, int * count) {
    int sum = 0;
    (*count) = 0;

    for (int i = 0; i < n; i++) {
        if (a[i] % 2 != 0) {
            sum += a[i];
            (*count)++;
        }
    }
    return sum;
}

int main(void) {
    int array_1[10] = {1,2,3,4,5,6,7,8,9,0};    // Sum = 25, odd numbers = 5
    int array_2[3]  = {2,4,6};                  // Sum = 0, odd numbers = 0
    int array_3[1]  = {1};                      // Sum = 1, odd numbers = 1
    int array_4[5]  = {53,12,75,22,39};         // Sum = 167, odd numbers = 3

    int result, odds;

    result = sum_odd_in_array(array_1, 10, &odds);
    assert(result == 25);
    assert(odds == 5);
    printf("Array 1: Sum = %d\t# of odds = %d\n", result, odds);

    result = sum_odd_in_array(array_2, 3 , &odds);
    assert(result == 0);
    assert(odds == 0);
    printf("Array 2: Sum = %d\t# of odds = %d\n", result, odds);

    result = sum_odd_in_array(array_3, 1 , &odds);
    assert(result == 1);
    assert(odds == 1);
    printf("Array 3: Sum = %d\t# of odds = %d\n", result, odds);

    result = sum_odd_in_array(array_4, 5 , &odds);
    assert(result == 167);
    assert(odds == 3);
    printf("Array 4: Sum = %d\t# of odds = %d\n", result, odds);

    printf("All tests passed successfully!\n");
}

/*
    sum_odd_in_array() counts and adds every odd number integer in the array and returns the sum.
        The function initializes the starting sum as 0, and sets the number of odds to 0.
        The for loop goes through the array a[] of size n.
            If the current index, a[i], contains an odd integer, then add said odd integer to the sum and increment the counter for odd numbers.
            Else, a[i] contains an even integer, so do nothing and move to the next index.
        Return the sum.
*/