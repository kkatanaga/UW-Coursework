#include <stdio.h>

int main(void)
{
    int d1 = 0, d2 = 0, d3 = 0, d4 = 0;
    int newD1 = 0, newD2 = 0, newD3 = 0, newD4 = 0;

    printf("Please enter a 4-digit integer:\n");
    scanf("%1d%1d%1d%1d", &d1, &d2, &d3, &d4);

    newD1 = (d3 + 7) % 10;
    newD3 = (d1 + 7) % 10;

    newD2 = (d4 + 7) % 10;
    newD4 = (d2 + 7) % 10;
    
    printf("Data (%d%d%d%d) encrypted to: %d%d%d%d\n", d1, d2, d3, d4, newD1, newD2, newD3, newD4);
    
}