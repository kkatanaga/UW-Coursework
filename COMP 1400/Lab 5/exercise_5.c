#include <stdio.h>

int main(void)
{
    int x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12;                                                              // Contains each digit of the first 12 digits of ISBN-13
    int chkDgt = 0;                                                                                                     // Contains the sum of products -> remainder of 10 subtracted from 10 -> check digit

    printf("Please enter the first 12 digits of ISBN-13:\n");                                                           // Prints a message sking for the first 12 digits of ISBN-13
    scanf("%1d%1d%1d%1d%1d%1d%1d%1d%1d%1d%1d%1d", &x1, &x2, &x3, &x4, &x5, &x6, &x7, &x8, &x9, &x10, &x11, &x12);       // Takes the 12 digits of ISBN-13 and saves each digit into their own containers

    chkDgt = (x1*1)+(x2*3)+(x3*1)+(x4*3)+(x5*1)+(x6*3)+(x7*1)+(x8*3)+(x9*1)+(x10*3)+(x11*1)+(x12*3);                    // Alternates between *1 and *3, then adds every product
    chkDgt = 10 - (chkDgt % 10);                                                                                        // Remainder of the previously calculated sum subtracted from 10
    
    printf("Check digit: %d\n", chkDgt);                                                                                // Prints the Check Digit calculated

}                                                                                                                       // *End main()