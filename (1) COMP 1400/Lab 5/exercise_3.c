#include <stdio.h>

int main(void)
{
    int year = 0;                                                                           // Contains the year

    printf("Please enter the year:\n");                                                     // Asks for a year
    scanf("%d", &year);                                                                     // Inputs the year

    if( (year % 4 == 0 && year % 100 != 0) || (year % 100 == 0 && year % 400 == 0) ) {      // Activates when the year is divisible by 4 and ISN'T a century year, OR it IS a century year and is divisible by 400
        printf("The year %d is a leap year.\n", year);                                      // Prints "leap year" if true
    }                                                                                       // *End if()
    
    else {
        printf("The year %d is not a leap year.\n", year);                                  // Prints "not a leap year" if false
    }                                                                                       // *End else
    
}                                                                                           // *End main()