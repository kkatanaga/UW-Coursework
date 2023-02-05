#include <stdio.h>

int main(void)
{
    int num = 0;                                    // Contains an odd or even integer

    printf("Enter an integer number:\n");           // Asks for an odd or even integer
    scanf("%d", &num);                              // Inputs the integer

    if (num % 2 == 0) {
        printf("The input %d is even.\n", num);     // Prints "even" for the input
    }                                               // *End if()
    
    else {
        printf("The input %d is odd.\n", num);      // Prints "odd" for the input
    }                                               // *End else

}                                                   // *End main()