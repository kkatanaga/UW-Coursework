#include <stdio.h>

int main(void)
{
    int inNum = 0, sum = 0;                                     // Contains the input numbers, the sum, and the number of inputs

    for (int i = 1; i <= 10; ++i)
    {
        switch (i) {
            case 1:                                             // Activates on the first loop
            printf("Please enter the 1st number:\n");           // Prints the first prompt
            break;

            case 2:                                             // Activates on the second loop
            printf("Please enter the 2nd number:\n");           // Prints the second prompt
            break;

            case 3:                                             // Activates on the third loop
            printf("Please enter the 3rd number:\n");           // Prints the third prompt
            break;

            default:                                            // Activates on the fourth-tenth loop
            printf("Please enter the %dth number:\n", i);       // Prints the fourth-tenth prompt
            break;
        }                                                       // *End switch(i)
            
        scanf("%d", &inNum);                                    // Asks for a number
        sum += inNum;                                           // Adds the sum of two numbers
    }                                                           // *End for(i < 10)

    printf("Your sum is: %d\n", sum);                           // Prints the sum of 10 integer numbers
    printf("Your average is: %.2f\n", (float) sum / 10);        // Prints the average of 10 integer numbers to 2 decimal places

}                                                               // *End main()