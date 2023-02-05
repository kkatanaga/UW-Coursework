#define MAX 8

// Applies AND to two binaries
void func_and(int a[], int b[], int result[]) {
	for(int i = 0; i < MAX; i++)
		result[i] = a[i] & b[i];
}

// Applies OR to two binaries
void func_or(int a[], int b[], int result[]) {
	for(int i = 0; i < MAX; i++)
		result[i] = a[i] | b[i];
}

// Applies NOT to a binary
void func_not(int a[], int result[]) {
    for(int i = 0; i < MAX; i++)
		result[i] = !a[i];
}