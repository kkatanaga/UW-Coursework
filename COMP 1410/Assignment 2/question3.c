#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include <string.h>
#include <stdbool.h>
#include <assert.h>

#define SUBSTRING_SIZE 50

int count_pair(const char str[]) {
    int total = 0;
    for (int s = 0; str[s] != '\0'; s++) {
        if (str[s] == '(')              // Counts how many open parenthesis there are in str[]
            total++;
    }
    return total;
}

// "Unfolds" by peeling off a complete parenthesis while keeping track of the level of unfolding.
// Returns true when the input is fully unfolded (no parenthesis).
bool unfold(const char str[], int * level, char result[]) {
	char * first_open;
	char * last_close;
    memset(result, '\0', SUBSTRING_SIZE);
	
    if (count_pair(str) == 0)
        return true;                                                // Return true if input doesn't contain any parenthesis

    first_open = strchr(str, '(');                                  // Pointer to the first open parenthesis
    last_close = strrchr(str, ')');                                 // Pointer to the last close parenthesis

    strncpy(result, first_open + 1, last_close - first_open - 1);   // Copies anything within the first open and last close parenthesis
    (*level)++;
    
    if (count_pair(result) == 0)
        return true;                                                // Return true if result doesn't contain any parenthesis

    return false;                                                   // Return false if result still contains parenthesis
}

// Cuts out a complete substring closed by '(' and ')'. Returns the index of the closing parenthesis.
int partition(const char str[], char substr[]) {
	int open_count = 0;
	int last_index = 0;
    memset(substr, '\0', SUBSTRING_SIZE);
	// Find the closing pair of first '('
	for (int s = 0; str[s] != '\0'; s++) {
		if (str[s] == '(') {
			open_count++;
		}
		else if (str[s] == ')') {
			last_index = s;
			open_count--;
		}

        // open_count == 0 means a complete parenthesis is reached; could be str itself, or it could be a part of str.
		if (open_count == 0) {
			break;
		}
	}
	strncpy(substr, str, last_index + 1);

	return last_index;  // Returns where the partition ends
}

// deepest_substring(str, out) updates out to be the deepest substring of
//   str; the first is used if multiple possibilities exist
// requires:
//   str is a string with balanced parenthesis
//   out points to enough memory to store the deepest substring of str
void deepest_substring(const char str[], char out[]) {
	char substr[SUBSTRING_SIZE] = {};
	char unfolded_substr[SUBSTRING_SIZE] = {};
    char partition_holder[SUBSTRING_SIZE] = {};
	int partition_close_index = 0;
    char temp_partition[SUBSTRING_SIZE] = {};

    int level_max = 0;
    int level_temp = 0;
	char * first_open;
	char * str_open = strchr(str, '(');

    bool no_levels = false;

	while (str_open != NULL && count_pair(str_open) > 0) {
        strcpy(substr, str_open);

        while (no_levels == false) {
            first_open = strchr(substr, '(');                                       // Finds the location of '(' in substr

            strcpy(substr, first_open);                                             // Removes anything before the '(' in substr

            partition(substr, temp_partition);                                      // Grabs a partition in substr, closed by complete parenthesis.

            no_levels = unfold(temp_partition, &level_temp, unfolded_substr);       // Removes the outermost parenthesis and checks if there are more inside

            strcpy(substr, unfolded_substr);                                        // substr now contains 1 less pair of parenthesis

        }
        no_levels = false;

        if (level_max < level_temp) {
            level_max = level_temp;                                                 // Keeps track of the highest level
            strcpy(out, unfolded_substr);                                           // The highest level (deepest)
        }

        level_temp = 0;                                                             // Resets the level
        partition_close_index = partition(str_open, partition_holder);              // Finds the next partition and the index it ends at
	    str_open = strchr(str_open + partition_close_index, '(');                   // Moves to the first open parenthesis after the previous partition
	}
}

int main(void) {
    char string_1[] = "a+((b+c)+d)";
    char string_2[] = "(b+c)+(a+d)";
    char string_3[] = "a+((b+c)+(d+e))";
    char string_4[] = "(a+(b+c))+(d+((e+f)+(g+h)))"; // e+f is deepest
    char string_5[] = "(a+(b+c))+(d+e)";

    char result_1[SUBSTRING_SIZE] = {};
    char result_2[SUBSTRING_SIZE] = {};
    char result_3[SUBSTRING_SIZE] = {};
    char result_4[SUBSTRING_SIZE] = {};
    char result_5[SUBSTRING_SIZE] = {};

    deepest_substring(string_1, result_1);
    assert(strcmp(result_1, "b+c") == 0);
    printf("String 1 = %s\n Result = %s\n", string_1, result_1);

    deepest_substring(string_2, result_2);
    assert(strcmp(result_2, "b+c") == 0);
    printf("String 2 = %s\n Result = %s\n", string_2, result_2);

    deepest_substring(string_3, result_3);
    assert(strcmp(result_3, "b+c") == 0);
    printf("String 3 = %s\n Result = %s\n", string_3, result_3);

    deepest_substring(string_4, result_4);
    assert(strcmp(result_4, "e+f") == 0);
    printf("String 4 = %s\n Result = %s\n", string_4, result_4);

    deepest_substring(string_5, result_5);
    assert(strcmp(result_5, "b+c") == 0);
    printf("String 5 = %s\n Result = %s\n", string_5, result_5);

    printf("\nAll tests passed successfully.\n");
}