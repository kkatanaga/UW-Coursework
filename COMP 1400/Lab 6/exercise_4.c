#include <stdio.h>

int main(void)
{
    int num = 0;                                            // Contains the multiplicand (num)

    printf("Please enter an integer multiplicand:\n");      // Asks for an integer multiplicand
    scanf("%d", &num);                                      // Receives the multiplicand

    for (int m = 1; m <= 10; ++m)
    {                                                       // Variable m contains the multiple; multiplies the multiplicand by multiples 1 to 10
        printf("%d * %d\t= %d\n", num, m, num * m);         // Prints the multiplicand, multiplier, and the product
    }                                                       // *End for(m <= 10)

}                                                           // *End main()