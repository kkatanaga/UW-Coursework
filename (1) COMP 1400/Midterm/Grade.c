#include <stdio.h>
#include <ctype.h>

int main(void)
{
    char type = 'x', gradeLet = 'x'; 
    int gradeNum = -1;

    printf("Please enter the conversion type (N/n for number to letter, C/c for letter to word\n");
    scanf("%c", &type);

    if (type == 'N' || type == 'n') {
        printf("Please enter the number grade (positive integers [0,100]):\n");
        scanf("%d", &gradeNum);

        if (gradeNum >= 90 && gradeNum <= 100) {
            printf("The letter grade of %d is A+\n", gradeNum);
        }

        else if (gradeNum >= 85 && gradeNum < 90) {
            printf("The letter grade of %d is A\n", gradeNum);
        }

        else if (gradeNum >= 80 && gradeNum < 85) {
            printf("The letter grade of %d is A-\n", gradeNum);
        }

        else if (gradeNum >= 77 && gradeNum < 80) {
            printf("The letter grade of %d is B+\n", gradeNum);
        }

        else if (gradeNum >= 73 && gradeNum < 77) {
            printf("The letter grade of %d is B\n", gradeNum);
        }

        else if (gradeNum >= 70 && gradeNum < 73) {
            printf("The letter grade of %d is B-\n", gradeNum);
        }

        else if (gradeNum >= 60 && gradeNum < 70) {
            printf("The letter grade of %d is C\n", gradeNum);
        }

        else if (gradeNum >= 50 && gradeNum < 60) {
            printf("The letter grade of %d is D\n", gradeNum);
        }

        else if (gradeNum >= 0 && gradeNum < 50) {
            printf("The letter grade of %d is F\n", gradeNum);
        }

        else {
            printf("The number grade %d is invalid\n", gradeNum);
        }

    }

    else if (type == 'C' || type == 'c') {
        printf("Please enter the letter grade (character):\n");
        scanf(" %c", &gradeLet);
        gradeLet = toupper(gradeLet);
        
        switch (gradeLet) {
            case 'A':
                printf("The word grade of %c is \"Excellent\"\n", gradeLet);
                break;
            
            case 'B':
                printf("The word grade of %c is \"Good\"\n", gradeLet);
                break;
            
            case 'C':
                printf("The word grade of %c is \"Fair\"\n", gradeLet);
                break;
            
            case 'D':
                printf("The word grade of %c is \"Pass\"\n", gradeLet);
                break;
            
            case 'F':
                printf("The word grade of %c is \"Failure\"\n", gradeLet);
                break;
            
            default: 
                printf("The letter grade \"%c\" is invalid\n", gradeLet);
        }
    }

    else {
        printf("Goodbye!\n");
    }

}