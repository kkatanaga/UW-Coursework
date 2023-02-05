#include <stdio.h>
#include <assert.h>
#define ROW 3
#define COL 10

// row_sums(n, m, matrix, sums) updates sums to contain the row sums of the
//   given n by m matrix
// requires: 1 <= n <= 10, 1 <= m <= 10
//           sums points to an array of length at least n
void row_sums(int n, int m, int matrix [n][10], int * sums) {
    for (int row = 0; row < n; row++) {
        for (int col = 0; col < n; col++)
            *sums += matrix[row][col];
        sums++;
    }
}

int main(void) {
    int test[ROW][COL] = {  {1,2,3},
                            {4,5,6},
                            {7,8,9} };
    int result[ROW] = {0, 0, 0};

    row_sums(ROW, COL, test, result);

    for (int i = 0; i < ROW; i++) {
        printf("%d ", result[i]);
    }
    puts("");

    assert(6 == result[0]);
    assert(15 == result[1]);
    assert(24 == result[2]);
}