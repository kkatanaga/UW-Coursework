#include <stdio.h>
#include <math.h>
#include <stdbool.h>
#include "complement.h"
#include "conversion.h"

#define MAX 8//Byte = 8 bits
#define HALF floor((pow(2, MAX) - 1) / 2)

void func_signed_mag_addition(int a[], int b[], int result[]){
	int carry = 0;

	for (int i = MAX - 1; i >= 0; i--) {
		result[i] = a[i] + b[i] + carry;
		if (result[i] == 2) {
			carry = 1;
			result[i] = 0;
		} else if (result[i] == 3) {
			carry = 1;
			result[i] = 1;
		} else
			carry = 0;
	}
	if (carry > 0)
		puts("===/!\\===[Overflow detected!]===/!\\===");
}

void func_signed_mag_subtraction(int a[], int b[], int result[]){
	int new_b[MAX] = {};

	func_2s_comp_star(b, new_b);
	func_signed_mag_addition(a, new_b, result);
}

// Returns true if the binary is a negative number in signed magnitude, else false
bool check_sign(int binary_one[]) {
	int copy[MAX] = {};
	int one_base_ten = base_ten(binary_one);

	if (one_base_ten > HALF) {
		for (int i = 0; i < MAX; i++)
			copy[i] = binary_one[i];

		func_2s_comp_star(copy, binary_one);
		return true;
	}
	return false;
}
