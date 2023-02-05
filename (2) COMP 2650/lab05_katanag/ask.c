#include <stdio.h>
#define MAX 8

// Receives each digit of the binary one by one (delimited by \n)
void receive_binary(int binaryHolder[]) {
    char buffer;

    for(int i = 0; i < MAX; i++) {
        printf("x%d = ", i);
        while( ( !scanf("%d", &binaryHolder[i]) && scanf(" %c", &buffer) ) || (binaryHolder[i] != 0 && binaryHolder[i] != 1) )  // Loops if input is neither 0 nor 1
            printf("Error: x%d = ", i);
    }
}

// Prints a prompt asking for two binary numbers before receiving input
void ask_two_binaries(int binaryOne[], int binaryTwo[]) {
    puts("Enter your first binary number (0 or 1 only): ");
    receive_binary(binaryOne);

    puts("Enter your second binary number (0 or 1 only):");
    receive_binary(binaryTwo);
}

// Prints a prompt asking for a binary number before receiving input
void ask_one_binary(int binary[]) {
    puts("Enter your binary number (0 or 1 only): ");
    receive_binary(binary);
}


