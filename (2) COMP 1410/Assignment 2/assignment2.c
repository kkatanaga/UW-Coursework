#include <stdio.h>
#include <ctype.h>
#include <math.h>
#include <stdbool.h>
#include <string.h>
#include <assert.h>

#define ROWS 6
#define COLS 7
#define SUBSTRING_SIZE 50

// make_move(board, column, player) updates the board following a move
//   by the given player in the given column; returns false if the move
//   was illegal because the column was full
// requires: 0 <= column < 7
//           player is either 'X' or 'O'
bool make_move(char board[6][7], int column, char player) {
    for (int i = ROWS - 1; i >= 0; i--) {
        if (board[i][column] == '-') {
            board[i][column] = player;
            return true;
        }
    }

    return false;
}

// check_win(board) returns true if the given player has 4 connected
//   pieces on the board
bool check_win(char board[6][7], char player) {
    int row_anchor_index_max = 2;   // Middle index of the board; middle cut crosswise
	int col_anchor_index_max = 3;   // Middle of the index of the board; middle cut lengthwise

    // Horizontal Check; x is the anchor on column 0
    for (int x = 0; x < ROWS; x++) {

        // y is the anchor on a row of x
        for (int y = 0; y <= col_anchor_index_max; y++) {

            // Checks 4 horizontal elements, working out of column x and row y
            for (int j = y, horizontal_count = 1; j <= y + 3; j++, horizontal_count++) {
                if (board[x][j] != player)
                    horizontal_count = 0;

                if (horizontal_count == 4)
                    return true;
            }
        }
    }

    // Vertical Check; y is the anchor on row 0
    for (int y = 0; y < COLS; y++) {

        // x is the anchor on a column of y
        for (int x = 0; x <= row_anchor_index_max; x++) {

            // Checks 4 vertical elements, working out of row y and column x
            for (int i = x, vertical_count = 1; i <= x + 3; i++, vertical_count++) {
                if (board[i][y] != player)
                    vertical_count = 0;

                if (vertical_count == 4)
                    return true;
            }
        }
    }

    // Diagonal check by using the top-left elements as an anchor
    // x anchors on the (0,0), (1,0), and (2,0) positions
    for (int x = 0; x <= row_anchor_index_max; x++) {

        // y anchors on the (0,0), (0,1), (0,2), and (0, 3) positions
        for (int y = 0; y <= col_anchor_index_max; y++) {

            // Checks 4 diagonal elements, working out of x and y
            for (int i = x, j = y, diagonal_count = 1; i <= x + 3 && j <= y + 3; i++, j++, diagonal_count++) {
                if (board[i][j] != player)
                    diagonal_count = 0;
                    
                if (diagonal_count == 4)
                    return true;
            }
        }
    }
    // Diagonal check by using the top-right elements as an anchor
    // x anchors on the (0,6), (1,6), and (2,6) positions
    for (int x = 0; x <= row_anchor_index_max; x++) {

        // y anchors on the (0,6), (0,5), (0,4), and (0, 3) positions
        for (int y = COLS - 1; y >= col_anchor_index_max; y--) {

            // Checks 4 diagonals from top right to bottom left out from anchor y
            for (int i = x, j = y, diagonal_count = 1; i <= x + 3 && j >= y - 3; i++, j--, diagonal_count++) {
                if (board[i][j] != player)
                    diagonal_count = 0;

                if (diagonal_count == 4)
                    return true;
            }
        }
    }
    return false;
}

bool check_draw(char board[6][7]) {
    for (int i = 0; i < ROWS; i++) {
        for (int j = 0; j < COLS; j++) {
            if (board[i][j] == '-')
                return false;
        }
    }
    return true;
}

void print_board(char board[6][7]) {
    puts("|---------------------------|");
    for (int i = 0; i < ROWS; i++) {
        printf("|");
        for (int j = 0; j < COLS; j++)
            printf(" %c |", board[i][j]);
        puts("\n|---------------------------|");
    }
    puts("  0   1   2   3   4   5   6");
}

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


    // ========--------========[Question 1]========--------========


    char cf_board[ROWS][COLS] = {};
    memset(cf_board, '-', sizeof(cf_board));

    char player = 'X';
    char buffer = '\0';

    int turn = 0;
    int move_index = 0;

    bool winner = false;
    bool draw = false;
    bool invalid = false;

    do {
        if (turn++ % 2 == 0)
            player = 'X';
        else
            player = 'O';

        print_board(cf_board);
        printf("Player %c: ", player);

        while ( (!scanf("%d", &move_index) && scanf(" %c", &buffer)) || move_index < 0 || move_index >= COLS || make_move(cf_board, move_index, player) == false) {
            if (buffer != '\0' || move_index < 0 || move_index >= COLS) {
                invalid = true;
                break;
            }
            else
                printf("Column full! Enter a different column number: ");
        }

        winner = check_win(cf_board, player);
        draw = check_draw(cf_board);
    } while (winner == false && draw == false && invalid == false);

    puts("\n========--------========[Question 1]========--------========\n");

    print_board(cf_board);
    if (winner == true)
        printf("The player %c wins!\n", player);
    else if (draw == true)
        printf("Board full; the game is a draw!\n");
    else
        printf("Illegal move by %c! Exiting game...\n", player);

    char test_board_1[ROWS][COLS] = {};
    memset(test_board_1, '-', sizeof(test_board_1));
    for (int i = 1; i < ROWS; i++)
        test_board_1[i][3] = 'X';

    char test_board_2[ROWS][COLS] = {};
    memset(test_board_2, '-', sizeof(test_board_2));
    for (int j = 2; j < 6; j++)
        test_board_2[2][j] = 'O';

    char test_board_3[ROWS][COLS] = {};
    memset(test_board_3, '-', sizeof(test_board_3));
    for (int i = 1, j = 2; i < 5; i++, j++)
        test_board_3[i][j] = 'O';

    // Test vertical
    assert(check_win(test_board_1, 'X') == true);
    assert(check_win(test_board_1, 'O') == false);
    puts("\ntest_board_1");
    print_board(test_board_1);

    // Test horizontal
    assert(check_win(test_board_2, 'O') == true);
    assert(check_win(test_board_2, 'X') == false);
    puts("\ntest_board_2");
    print_board(test_board_2);

    // Test diagonal
    assert(check_win(test_board_3, 'O') == true);
    assert(check_win(test_board_3, 'X') == false);
    puts("\ntest_board_3");
    print_board(test_board_3);


    // ========--------========[Question 2]========--------========


    puts("\n========--------========[Question 2]========--------========\n");
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


    // ========--------========[Question 3]========--------========


    puts("\n========--------========[Question 3]========--------========\n");
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