#define BYTE 8

// Converts the input decimal to binary
void to_binary(int decimal, int binary[]) {
	for(int i = BYTE - 1; i >= 0; i--) {
        binary[i] = decimal % 2;
        decimal /= 2;
    }
}