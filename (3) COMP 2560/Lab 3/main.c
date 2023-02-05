#include <stdio.h>

int increment(int a);

int main(void) {
    int a;
    int temp = scanf("%i", &a);
    a = increment(a);
    printf("%i \n", a);
    return 0;
}