#include <stdio.h>

int main(void)
{
    int rowCount = 0;                                               // Contains the number of rows

    printf("Please enter a positive integer:\n");                   // Asks for a positive integer (acts as the number of rows & columns)

    while (!scanf("%d", &rowCount) || rowCount <= 0) 
    {                                                               // Receives the number of rows of the right triangle
        printf("Please enter a positive integer (non-zero):\n");    // Prints an error when input is less than or equal to 0
    }                                                               // *End while(!decimal || rowCount <= 0)

    for (int row = 1; row <= rowCount; ++row)
    {                                                               // Moves between rows until the number entered (rowCount) is reached
        for (int col = 1; col <= row; ++col)
        {                                                           // Moves around the column(s) of a row; fills a line with *'s
            printf("*");                                            // Prints an asterisk (*)

            if (col == row) {                                       // Activates when the column pos. is equal to the row pos.
                printf("\n");                                       // Prints a new line
            }                                                       // *End if(col == row)

        }                                                           // *End for(col <= row)

    }                                                               // *End for(row <= rowCount)

}                                                                   // *End main()