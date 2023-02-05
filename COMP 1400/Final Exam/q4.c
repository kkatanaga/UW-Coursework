#include <stdio.h>

int sumPositive(int n);
int sumOdd(int n);
float aveEven(int n);

int main(void)
{
    int i = 0;
    int n = 0;

    puts("Enter the number of runs to do:");
    while ( !scanf("%d", &i) || i <= 0 )
        puts("Please enter a positive integer (greater than 0):");

    for (int a = 1; a <= i; a++) {
        printf("Enter value %d:\n", a);
        while ( !scanf("%d", &n) || n <= 0)
            puts("Please enter a positive integer (greater than 0):");

        printf("Sum of the first %d positive integers: %d\n", n, sumPositive(n));
        printf("Sum of the first %d odd integers greater than or equal to %d: %d\n", n, n, sumOdd(n));
        printf("Average of the first %d even integers strictly greater than %d: %.2f\n", n, n, aveEven(n));
    }
}

int sumPositive(int n)
{
    int sum = 0;

    for (int x = 1; x <= n; x++)
        sum += x;

    return sum;
}

int sumOdd(int n)
{
    int sum = 0;
    int num = n;

    for (int x = 1; x <= n; x++) {
        sum += num;
        num += 2;
    }

    return sum;
}

float aveEven(int n)
{
    int sum = 0;
    int num = n;

    for (int x = 1; x <= n; x++) {
        if (num % 2 == 0)
            sum += (num += 2);
        else
            sum += (num += 1);
    }

    return sum / n;
}
