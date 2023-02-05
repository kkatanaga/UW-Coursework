#include <stdio.h>

void decToBin(int decimal);

/*\=============================================================|
|   Program Name: exercise_c.c                                  |
|   Name: Keigo Katanaga                                        |
|   Student ID: 110068805                                       |
|   Date: 11/22/2021                                            |
|   Description: Converts a number of base 10 (decimal) to base |
|   2 (binary).                                                 |
|=============================================================\*/

int main()
{
    decToBin(1);
    decToBin(2);
    decToBin(10);
    decToBin(1001);
    decToBin(90250);
}

void decToBin(int decimal)
{
    int i = 0;
    int binary[30];

    printf("Decimal: %d\n", decimal);
    
    while (decimal != 0) {                  // Divides input by 2 until input is 0
        binary[i] = decimal % 2;            // Inserts one digit of binary in the array
        decimal /= 2;                       // Divides the input by 2
        i++;                                // Moves to next index
    }

    printf("Binary: ");
    for (int x = i-1; x >= 0; x--)          // Prints the array backwards since the calculation outputs a flipped binary
        printf("%d", binary[x]);
        
    printf("\n\n");
}