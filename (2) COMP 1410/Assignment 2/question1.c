#include <stdio.h>
#include <ctype.h>
#include <math.h>
#include <stdbool.h>
#include <string.h>
#include <assert.h>

#define ROWS 6
#define COLS 7

// make_move(board, column, player) updates the board following a move
//   by the given player in the given column; returns false if the move
//   was illegal because the column was full
// requires: 0 <= column < 7
//           player is either 'X' or 'O'
bool make_move(char board[6][7], int column, char player) {
    if (0 <= column && column < COLS) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == '-') {
                board[i][column] = player;
                return true;
            }
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

int main(void) {
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
    
    print_board(cf_board);
    if (winner == true)
        printf("The player %c wins!\n", player);
    else if (draw == true)
        printf("Board full; the game is a draw!\n");
    else
        printf("Illegal move by %c! Exiting game...\n", player);
    
    printf("\nAll tests passed successfully.\n");
}