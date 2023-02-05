#include "logic.h"
#define MAX 8

// Applies 1's complement to a binary (using func_not())
void func_1s_comp(int a[], int result[]) {
    func_not(a, result);
}

// Applies 2's complement to a binary (using func_1s_comp())
void func_2s_comp(int a[], int result[]) {
    int baseTen = 0;
    func_1s_comp(a, result);

    for(int i = 0; i < MAX; i++)
        baseTen += result[i] * pow(2, MAX - i - 1);

    baseTen += 1;

    for(int i = MAX - 1; i >= 0; i--) {
        result[i] = baseTen % 2;
        baseTen /= 2;
    }
}

// Applies 2's complement* to a binary (using func_not())
void func_2s_comp_star(int a[], int result[]) {
    int index = MAX;
    func_not(a, result);

    while(a[--index] == 0);

    for(int i = index; i < MAX; i++)
        result[i] = a[i];
}