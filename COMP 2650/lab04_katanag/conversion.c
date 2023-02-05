#include <math.h>
#define MAX 8

int base_ten(int binary[]) {
	int baseTen = 0;

	for(int i = 0; i < MAX; i++)
		baseTen += binary[i] * pow(2, MAX - i - 1);

	return baseTen;
}

void to_octal(int a[]) {
	int decimal = base_ten(a);

	for(int i = MAX - 1; i >= 0; i--) {
		a[i] = decimal % 8;
		decimal /= 8;
	}
}

void to_decimal(int a[]) {
	int decimal = base_ten(a);

	for(int i = MAX - 1; i >= 0; i--) {
		a[i] = decimal % 10;
		decimal /= 10;
	}
}

void to_hexadecimal(int a[]) {
	int decimal = base_ten(a);

	for(int i = MAX - 1; i >= 0; i--) {
		a[i] = decimal % 16;
		decimal /= 16;
	}
}
