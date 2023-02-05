#include <stdio.h>
#include <assert.h>
#include <stdbool.h>

// matrix_equals(n, m, a, b) returns true exactly when the n x m matrices
//   pointed to by a and b are equal
// requires: n >= 1, m >= 1
//           a and b point to the (0, 0) element of an n x m matrix
bool matrix_equals(const int n, const int m, const int * a, const int * b) {
    const int * size = a + (n * m);
    
    for (; a < size; a++, b++) {
        if (*a != *b)
            return false;
    }
    return true;
}

// matrix_add(n, m, a, b, c) computes the sum of the matrices a and b;
//   the result is stored in the matrix c
// requires: n >= 1, m >= 1
//           a, b, and c point to the (0, 0) element of an n x m matrix
//           the memory c points to can be modified
void matrix_add(const int n, const int m, const int * a, const int * b, int * c) {
    const int * size = c + (n * m);

    for (; c < size; c++, a++, b++)
        *c = *a + *b;
}

// matrix_print_equal(n, m, matrix[][]) prints a matrix as a result of an equation
void matrix_print_equal(const int n, const int m, const int matrix[n][m]) {
    for (int i = 0; i < n; i++) {
        if (i == (n-1)/2)
            printf("=");

        printf("\t[\t");

        for (int j = 0; j < m; j++)
            printf("%d\t", matrix[i][j]);
        puts("]");
    }
    puts("========================================================================");
}

// matrix_print_pair(n, m, matrixOne[][], matrixTwo[][]) prints the addition operation of two matrices
void matrix_print_pair(const int n, const int m, const int matrixOne[n][m], const int matrixTwo[n][m]) {
    for (int i = 0; i < n; i++) {
        printf("[\t");

        // Prints first matrix row by row
        for (int j = 0; j < m; j++)
            printf("%d\t", matrixOne[i][j]);

        printf("]\t");

        if (i == (n-1)/2)
            printf("+");

        printf("\t[\t");

        // Prints the second matrix row by row
        for (int j = 0; j < m; j++)
            printf("%d\t", matrixTwo[i][j]);

        puts("]");
    }
    puts("------------------------------------------------------------------------");
}

// matrix_print(n, m, matrixOne[][], matrixTwo[][], result[][]) simplifies the print call of the whole equation
void matrix_print(const int n, const int m, const int matrixOne[n][m], const int matrixTwo[n][m], const int result[n][m]) {
    matrix_print_pair(n, m, matrixOne, matrixTwo);
    matrix_print_equal(n, m, result);
}

int main(void) {
    // First pair
    int matrixOne[4][6] = { {1,2,3,4,5,6},
                            {2,3,4,5,6,7},
                            {3,4,5,6,7,8},
                            {4,5,6,7,8,9}   };

    int matrixTwo[4][6] = { {9,8,7,6,5,4},
                            {8,7,6,5,4,3},
                            {7,6,5,4,3,2},
                            {6,5,4,3,2,1}   };

    // Second pair
    int matrixThree[5][3] = {   {5,2,7},
                                {1,3,-3},
                                {-4,7,9},
                                {2,5,1},
                                {8,-8,0} };

    int matrixFour[5][3] = {    {6,3,-4},
                                {-7,8,6},
                                {6,5,-9},
                                {9,-2,5},
                                {2,8,4} };

    // Third pair
    int matrixFive[2][2] = {    {19,58},
                                {-48,11}};

    int matrixSix[2][2] = {     {-84,25},
                                {6,-93}};

    // Fourth pair (expected to fail)
    int matrixSeven[3][3] = {   {1,1,1},
                                {1,1,1},
                                {1,1,1} };

    int matrixEight[3][3] = {};

    // Sum containers
    int resultOne[4][6] = {};
    int resultTwo[5][3] = {};
    int resultThree[2][2] = {};
    int resultFour[3][3] = {};

    // Expected sums/results
    int expectedOne[4][6] = {   {10,10,10,10,10,10},
                                {10,10,10,10,10,10},
                                {10,10,10,10,10,10},
                                {10,10,10,10,10,10} };

    int expectedTwo[5][3] = {   {11,5,3},
                                {-6,11,3},
                                {2,12,0},
                                {11,3,6},
                                {10,0,4}    };

    int expectedThree[2][2] = { {-65,83},
                                {-42,-82}   };

    // Not expected result; expected matrix is a 3 x 3 with 1 in every position
    int expectedFour[3][3] = {};

    // Number of rows and columns of each pair
    const int rowOne = 4, colOne = 6;
    const int rowTwo = 5, colTwo = 3;
    const int rowThree = 2, colThree = 2;
    const int rowFour = 3, colFour = 3;

    puts("========================================================================");

    // First pair; tests positive numbers; every element's sum should equal 10
    matrix_add(rowOne, colOne, *matrixOne, *matrixTwo, *resultOne);

    puts("Sum of 1st pair:");
    matrix_print(rowOne, colOne, matrixOne, matrixTwo, resultOne);

    assert(matrix_equals(rowOne, colOne, *resultOne, *expectedOne) == true);

    // Second pair; tests random positive and negative numbers
    matrix_add(rowTwo, colTwo, *matrixThree, *matrixFour, *resultTwo);

    puts("Sum of 2nd pair:");
    matrix_print(rowTwo, colTwo, matrixThree, matrixFour, resultTwo);

    assert(matrix_equals(rowTwo, colTwo, *resultTwo, *expectedTwo) == true);

    // Third pair; tests smaller matrix
    matrix_add(rowThree, colThree, *matrixFive, *matrixSix, *resultThree);

    puts("Sum of 3rd pair:");
    matrix_print(rowThree, colThree, matrixFive, matrixSix, resultThree);

    assert(matrix_equals(rowThree, colThree, *resultThree, *expectedThree) == true);

    // Fourth pair; tests failure
    matrix_add(rowFour, colFour, *matrixSeven, *matrixEight, *resultFour);

    puts("Sum of 3rd pair:");
    matrix_print(rowFour, colFour, matrixSeven, matrixEight, resultFour);

    assert(matrix_equals(rowFour, colFour, *resultFour, *expectedFour) == false);

    printf("All tests passed successfully.\n");
}