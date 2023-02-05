#include <stdio.h>
#include <math.h>
#include <stdbool.h>
#include "complement.h"
#include "conversion.h"

#define MAX 8//Byte = 8 bits
#define HALF floor((pow(2, MAX) - 1) / 2)

// Returns true if the binary is a negative number in signed magnitude, else false
bool is_negative(int binary_one[]) {
	if (base_ten(binary_one) > HALF)
		return true;
	return false;
}

void func_signed_2s_addition(int a[], int b[], int result[]){
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
	if ( (is_negative(a) == false && is_negative(b) == false && is_negative(result) == true) || carry != 0 )
		puts("===/!\\===[Overflow detected!]===/!\\===");
}

void func_signed_2s_subtraction(int a[], int b[], int result[]){
	int new_b[MAX] = {};

	func_2s_comp_star(b, new_b);
	func_signed_2s_addition(a, new_b, result);
}
