#include <stdio.h>

int main(void)
{
    int n = 0;
    int sum = 0;
    int count = 0;

    puts("Enter an integer number greater than 1:");
    scanf("%d", &n);
    if (n > 1) {
        printf("The positive factors of %d are: ", n);
        for (int x = n; x >= 2; x--) {
            if (n % x == 0) {
                printf("%d ", n / x);
                sum += (n / x);
                count++;
            }
        }
        printf("\nThe average of the factors is: %.2f", (float) sum / (float) count);
    }
    else {
        puts("Invalid input; closing the program...");
    }
}