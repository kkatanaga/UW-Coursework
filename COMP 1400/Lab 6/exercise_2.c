#include <stdio.h>

int main(void)
{
    int num = 0;                                                                            // Contains an integer

    printf("Please enter an integer number:\n");                                            // Prints a prompt asking for an integer
    scanf("%d", &num);                                                                      // Receives the integer

    if ( num == 2 || num == 3 || ( num % 2 != 0 && num % 3 != 0 && num > 1 ) ) {            // Activates when the input is 2, 3, or any integers indivisible by 2 and 3 greater than 1
        printf("Your number (%d) is a prime number.\n", num);                               // Prints the prime number
    }                                                                                       // *End if()

    else {                                                                                  // Activates when the input is neither 2 nor 3 and is divisible by 2 and 3
        printf("Your number (%d) is not a prime number.\n", num);                           // Prints the non-prime number
    }                                                                                       // *End else

}                                                                                           // *End main()