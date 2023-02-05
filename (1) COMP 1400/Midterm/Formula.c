#include <stdio.h>

int main(void)
{
    int a = 0, b = 0, c = 0, x = 0;

    printf("Please enter four integer numbers a, b, c, and x (delimited by spaces):\n");
    scanf("%d %d %d %d", &a, &b, &c, &x);

    if (x <= 40000 && x >= -40001) {
        printf("Q(%d) = %d * %d * %d^2 + %d^2 * %d + %d = %d\n", x, a, b, x, b, x, c, (a*b*x*x)+(b*b*x)+c);
    }

    else {
        printf("Possible overflow.\n");
    }
    
}