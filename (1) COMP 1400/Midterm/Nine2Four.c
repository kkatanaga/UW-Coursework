#include <stdio.h>

int main(void)
{
    long inDig = 0;
    int digL = 0, digM = 0, digR = 0;
    int rsltS = 0;

    printf("Please enter your Student ID (9 digits):\n");
    scanf("%ld", &inDig);

    digL = inDig / 1000000;
    digM = (inDig % 1000000) / 1000;
    digR = inDig % 1000;

    digL = (digL / 100) + ( (digL % 100) / 10 ) + (digL % 10);
    digM = (digM / 100) + ( (digM % 100) / 10 ) + (digM % 10);
    digR = (digR / 100) + ( (digR % 100) / 10 ) + (digR % 10);

    if (digL > 9 || digM > 9 || digR > 9) {
        digL = (digL / 100) + ( (digL % 100) / 10 ) + (digL % 10);
        digM = (digM / 100) + ( (digM % 100) / 10 ) + (digM % 10);
        digR = (digR / 100) + ( (digR % 100) / 10 ) + (digR % 10);

        if (digL > 9 || digM > 9 || digR > 9) {
            digL = (digL / 100) + ( (digL % 100) / 10 ) + (digL % 10);
            digM = (digM / 100) + ( (digM % 100) / 10 ) + (digM % 10);
            digR = (digR / 100) + ( (digR % 100) / 10 ) + (digR % 10);
        }
    }
    
    rsltS = digL + digM + digR;

    if (rsltS > 9) {
        rsltS = (rsltS/10) + (rsltS%10);

        if (rsltS > 9) {
            rsltS = (rsltS/10) + (rsltS%10);
        }
    }

    rsltS = (digM*1000) + (rsltS*100) + (digL*10) + digR;

    printf("The hash value of your ID (%ld) is %d.\n", inDig, rsltS);

}