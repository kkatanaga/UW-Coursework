#include <stdio.h>

int main(void)
{
    int num, max = -32768, min = 32767, i = 1;                          // Contains the inputs, maximum, minimum, and number of comparisons

    while (i <= 3)
    {                                                                   // Loops three times
        if (i == 1) {                                                   // Activates only on the first comparison
            printf("Please enter three integers separated by \\n:\n");  // Asks for the first integer
            scanf("%d", &num);                                          // Takes in the first integer
        }

        else {                                                          // Activates on comparisons after the first
            printf("Please enter another integer:\n");                  // Asks for the second or third integer
            scanf("%d", &num);                                          // Takes in the second or third integer
        }                                                               // *End else

        if(num > max) {                                                 // Compares the input if it is bigger than the current max
            max = num;                                                  // Variable max now holds the bigger number in the comparison
        }                                                               // *End if()

        if(num < min) {                                                 // Compares the input if it is smaller than the current min
            min = num;                                                  // Variable min now holds the smaller number in the comparison
        }                                                               // *End else if()
            
        ++i;                                                            // Adds one to the number of comparisons
    }                                                                   // *End while()

    printf("The largest integer is: %d\n", max);                        // Prints the largest integer
    printf("The smallest integer is: %d\n", min);                       // Prints the smallest integer
    
}                                                                       // *End main()