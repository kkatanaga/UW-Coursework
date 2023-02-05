#include <stdio.h>

int main(void)
{
    int num = 0;                                                                    // Contains the day number

    printf("Please enter a number between (including) 1-7:\n");                     // Asks for a number between (including) 1 and 7 
    scanf("%d", &num);                                                              // Receives the day number

    while (num < 1 || num > 7)
    {                                                                               // Loops when a non-day number is received
        printf("Error; please enter a number between (including) 1 and 7:\n");      // Prints an error and asks again
        scanf("%d", &num);                                                          // Receives the day number again
    }                                                                               // *End while()

    switch (num) 
    {                                                                               // Cases for each day number
        case 1:                                                                     // Activates when user inputs 1
        printf("Monday\n");                                                         // Prints "Monday"
        break;

        case 2:                                                                     // Activates when user inputs 2
        printf("Tuesday\n");                                                        // Prints "Tuesday"
        break;

        case 3:                                                                     // Activates when user inputs 3
        printf("Wednesday\n");                                                      // Prints "Wednesday"
        break;

        case 4:                                                                     // Activates when user inputs 4
        printf("Thursday\n");                                                       // Prints "Thursday"
        break;

        case 5:                                                                     // Activates when user inputs 5
        printf("Friday\n");                                                         // Prints "Friday"
        break;

        case 6:                                                                     // Activates when user inputs 6
        printf("Saturday\n");                                                       // Prints "Saturday"
        break;

        case 7:                                                                     // Activates when user inputs 7
        printf("Sunday\n");                                                         // Prints "Sunday"
        break;
    }                                                                               // *End switch()

}                                                                                   // *End main()