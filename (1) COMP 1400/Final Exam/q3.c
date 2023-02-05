#include <stdio.h>
#include <math.h>

int isSymmetric(int myArray[], int size);

int main(void)
{
    int s = 9;
    int array[s];

    puts("Enter 9 integer numbers:");
    for (int i = 0; i < s; i++)
        scanf("%d", &array[i]);

    if (isSymmetric(array, s) == 0)
        puts("The input is symmetric!");
    else
        puts("The input is NOT symmetric!");

}

int isSymmetric(int myArray[], int size)
{
    for (int x = 0; x < floor(size / 2); x++) {
        if (myArray[x] != myArray[size - x - 1])
            return 1;
    }
    return 0;
}