#include <stdio.h>
#include <math.h>
#define MAX 8

/*\=============================================================================|
|   Program Name: main.c                                  					    |
|   Name: Keigo Katanaga                                        				|
|   Student ID: 110068805                                       				|
|   Date: 02/06/2022                                            			    |
|   Description: Boolean operation program. Compares one or two binary inputs;  |
|   AND, OR, NOT, 1's Complement, 2's Complement, and modified 2's Complement   |
|   can be selected.                                                            |
|   Binary inputs of 8 positions only.                                          |
|=============================================================================\*/

// Receives each digit of the binary one by one (delimited by \n)
void receive_binary(int binaryHolder[]) {
    char buffer;

    for(int i = 0; i < MAX; i++) {
        printf("x%d = ", i);
        while( ( !scanf("%d", &binaryHolder[i]) && scanf(" %c", &buffer) ) || (binaryHolder[i] != 0 && binaryHolder[i] != 1) )  // Loops if input is neither 0 nor 1
            printf("Error: x%d = ", i);
    }
}

// Prints a prompt asking for a binary number before receiving input
void ask_one_binary(int binary[]) {
    puts("Enter your binary number (0 or 1 only): ");
    receive_binary(binary);
}

// Prints a prompt asking for two binary numbers before receiving input
void ask_two_binary(int binaryOne[], int binaryTwo[]) {
    puts("Enter your first binary number (0 or 1 only): ");
    receive_binary(binaryOne);
                
    puts("Enter your second binary number (0 or 1 only):");
    receive_binary(binaryTwo);
}

// Prints a binary
void print_binary(int binary[]) {
    for(int i = 0; i < MAX; i++)
        printf("%d", binary[i]);
}

// Prints the inputs and their resulting output
void print_binary_result(int binaryOne[], char type[], int binaryTwo[], int result[]) {
    print_binary(binaryOne);
    printf(" %s ", type);
    print_binary(binaryTwo);
    puts(" yields:");
    print_binary(result);
}

// Prints the input and its resulting output
void print_unary_result(int binary[], char type[], int result[]) {
    printf("%s ", type);
    print_binary(binary);
    puts(" yields:");
    print_binary(result);
}

// Applies AND to two binaries
void func_and(int a[], int b[], int result[]){
	for(int i = 0; i < MAX; i++)
		result[i] = a[i] & b[i];
}

// Applies OR to two binaries
void func_or(int a[], int b[], int result[]){
	for(int i = 0; i < MAX; i++)
		result[i] = a[i] | b[i];
}

// Applies NOT to a binary
void func_not(int a[], int result[]) {
    for(int i = 0; i < MAX; i++) {
		if(a[i] == 0)
            result[i] = 1;
        else
		    result[i] = 0;
    }
}

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

int main(void) {
    setbuf(stdout, NULL);

    int x[MAX], y[MAX], z[MAX];                                 // x contains the first binary, y contains the second binary, z contains the comparison between x and y
    int command = 0;
    char c;

    do {                                                        // Loops until 0 (command for exit) is entered
        puts("Choose a command number:");
        puts("0) Exit");
        puts("1) AND");
        puts("2) OR");
        puts("3) NOT");
        puts("4) 1's Complement");
        puts("5) 2's Complement");
        puts("6) 2's Complement*");
        printf("Input: ");

        // Loops when the input is not any of the command numbers
        while( (!scanf("%d", &command) && scanf(" %c", &c)) || (command != 0 && command != 1 && command != 2 && command != 3 && command != 4 && command != 5 && command != 6) )
            printf("Error: Please enter a specified selection: ");

        switch(command) {
            case 0:                                             // Exit case
                puts("Exiting the program...");
                return 0;

                break;
            case 1:                                             // AND case
                ask_two_binary(x, y);

                func_and(x, y, z);
                print_binary_result(x, "AND", y, z);

                break;
            case 2:                                             // OR case
                ask_two_binary(x, y);

                func_or(x, y, z);
                print_binary_result(x, "OR", y, z);

                break;
            case 3:                                             // NOT case
                ask_one_binary(x);

                func_not(x, z);
                print_unary_result(x, "NOT", z);

                break;
            case 4:                                             // 1's Complement case
                ask_one_binary(x);

                func_1s_comp(x, z);
                print_unary_result(x, "1's Complement of", z);

                break;
            case 5:                                             // 2's Complement case
                ask_one_binary(x);

                func_2s_comp(x, z);
                print_unary_result(x, "2's Complement of", z);

                break;
            case 6:                                             // 2's Complement* case
                ask_one_binary(x);

                func_2s_comp_star(x, z);
                print_unary_result(x, "2's Complement* of", z);

                break;
        }
        puts("\n--------------------------------");             // Separates the result from the main menu
    } while(command != 0);
}