#include <stdio.h>

int main(void)
{
    int num1 = 0, num2 = 0;                                                         // num1 is the dividend, and num2 is the divisor

    printf("Enter the first number:\n");                                            // Asks for num1; dividend
    scanf("%d", &num1);                                                             // Inputs the dividend to num1

    printf("Enter the second number:\n");                                           // Asks for num2; divisor
    scanf("%d", &num2);                                                             // Inputs the divisor to num2

    printf("The remainder of %d divided by %d is %d.\n", num1, num2, num1 % num2);  // Prints the output while calculating the remainder

}                                                                                   // *End main()