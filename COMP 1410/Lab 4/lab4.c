#include <stdio.h>
#include <assert.h>
#include <string.h>

// swap_to_front(str, c) swaps the last character in the string str with the
//   character pointed to by c
// requires: str is a valid string that can be modified, length(str) >= 1
//           c points to a character in str
void swap_to_front(char str[], char * c) {
    char hold = *str;
    *str = *c;
    *c = hold;
}

// select_max(str) returns a pointer to the character of maximal ASCII value
//   in the string str (the first if there are duplicates)
// requires: str is a valid string, length(str) >= 1
char * select_max(char str[]) {
    char * max = str;

    for (; *str != '\0'; str++) {
        if (*str > *max)
            max = str;
    }
    return max;
}

// str_sort(str) sorts the characters in a string in decending order
// requires: str points to a valid string that can be modified
void str_sort(char str[]) {
    for (; *str != '\0'; str++)
        swap_to_front(str, select_max(str));
}

void print_original(char str[]) {
    printf("Original string: %s\n", str);
}

void print_descending(char str[]) {
    printf("In descending order: %s\n", str);
}

int main(void) {
    char string_1[] = "ethics";
    char string_2[] = "abcdefghi";
    char string_3[] = "zyxwvut";
    char string_4[] = "";
    char string_5[] = "a";

    puts("----------------[Test 1]----------------");

    // Test 1: string_1 = "ethics"

    // Assert the highest ASCII value
    assert ( *select_max(string_1) == 't' );
    print_original(string_1);

    // Sort to descending order
    str_sort(string_1);

    // Assert the proper order
    assert( strcmp(string_1, "tsihec") == 0 );
    print_descending(string_1);

    puts("\n----------------[Test 2]----------------");

    // Test 2: string_2 = "abcdefghi"

    // Assert the highest ASCII value
    assert ( *select_max(string_2) == 'i' );
    print_original(string_2);

    // Sort to descending order
    str_sort(string_2);

    // Assert the proper order
    assert( strcmp(string_2, "ihgfedcba") == 0 );
    print_descending(string_2);

    puts("\n----------------[Test 3]----------------");

    // Test 3: string_3 = "zyxwvut"

    // Assert the highest ASCII value
    assert ( *select_max(string_3) == 'z' );
    print_original(string_3);

    // Sort to descending order
    str_sort(string_3);

    // Assert the proper order
    assert( strcmp(string_3, "zyxwvut") == 0 );
    print_descending(string_3);

    puts("\n----------------[Test 4]----------------");

    // Test 4: string_4 = "" (empty; \0)

    // Assert the highest ASCII value
    assert ( *select_max(string_4) == '\0' );
    print_original(string_4);

    // Sort to descending order
    str_sort(string_4);

    // Assert the proper order
    assert( strcmp(string_4, "") == 0 );
    print_descending(string_4);

    puts("\n----------------[Test 5]----------------");

    // Test 5: string_5 = "a"
    
    // Assert the highest ASCII value
    assert ( *select_max(string_5) == 'a' );
    print_original(string_5);

    // Sort to descending order
    str_sort(string_5);

    // Assert the proper order
    assert( strcmp(string_5, "a") == 0 );
    print_descending(string_5);

    puts("\n----------------------------------------");

    printf("All tests passed successfully.\n");
}