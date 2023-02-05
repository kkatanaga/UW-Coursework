#include <stdio.h>
#include <string.h>
#include <assert.h>

// first_capital(str, n) returns the first capital letter in str;
//   returns 0 if str contains no capital letters
// requires: str is a string of length n
//           str contains only lower-case and upper-case letters
//           all lower-case letters appear before upper-case letters
char first_capital(const char str[], int n) {
    int left = 0;
    int right = n - 1;
    int mid = right / 2;

    while (left < right) {
        // Move right
        if (str[mid] >= 'a')
            left = mid + 1;

        // Move left
        else
            right = mid;

        mid = (right + left) / 2;
    }
    if (str[mid] < 'a')
        return str[mid];
    return '0';
}

int main(void) {
    char test_1[] = "abcABC";
    char test_2[] = "testTEST";
    char test_3[] = "aaaaaaaaaaaaaaaC";
    char test_4[] = "aBCDEFG";
    char test_5[] = "nouppercase";
    char result;

    result = first_capital(test_1, strlen(test_1));
    assert(result == 'A');
    printf("First capital of \"%s\" is: %c\n", test_1, result);

    result = first_capital(test_2, strlen(test_2));
    assert(result == 'T');
    printf("First capital of \"%s\" is: %c\n", test_2, result);

    result = first_capital(test_3, strlen(test_3));
    assert(result == 'C');
    printf("First capital of \"%s\" is: %c\n", test_3, result);

    result = first_capital(test_4, strlen(test_4));
    assert(result == 'B');
    printf("First capital of \"%s\" is: %c\n", test_4, result);

    result = first_capital(test_5, strlen(test_5));
    assert(result == '0');
    printf("First capital of \"%s\" is: %c\n", test_5, result);

    printf("\nAll tests passed successfully.\n");
}