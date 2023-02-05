#include <stdio.h>

#define INPUT_VARIABLE_COUNT 3

void func_increment(int a[], int result[]) {
	int addend = 1;
	int carry = 0;

	for (int i = INPUT_VARIABLE_COUNT - 1; i >= 0; i--) {
		carry = a[i] & addend;
		result[i] = a[i] ^ addend;
		a[i] = result[i];
		addend = carry;
	}
}
