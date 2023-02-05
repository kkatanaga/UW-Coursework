#include <stdio.h>																												// C Standard Library for input/output functions
#include <math.h>																												// C Standard Library for math functions
#include <ctype.h>																												// C Standard Library for character testing/mapping functions
#define SIZE 100																												// Holds the size of the memory

char mainList(int iteration);																									// iteration = number of runs in the main block
float askNumOrVar(char selection);																								// selection = the selection from the main block; binary, unary, etc.
char askOp(char selection);
char advList(int iteration);																									// iteration = number of runs in the advanced selection of the main block
float varMem(char selection, int index);																						// index = the corresponding index of a variable
int varIndex(char variable);																									// variable = variable name
float doBinary(float first, float second, char operator);																		// first = the first number, second = the second number, operator = the operator
float doUnary(float first, char operator);																						// first = a number, operator = the operator
int mainMem(float result, char selection);																						// result = result from binary, unary, etc.
void memList(float resultMemory[SIZE]);																							// resultMemory[] = the main memory containing results
int clearMem(float resultMemory[SIZE]);

/*\=====================================================================================|
|   Program Name: clc_v3.c                                  							|
|   Name: Keigo Katanaga                                        						|
|   Student ID: 110068805                                       						|
|   Date: 11/22/2021                                            						|
|   Description: A calculator program. It can do constant or variable calcuations in	|
|	binary and unary operations. Variables can be specified and used. The calculator	|
|	saves each result into memory and can be accessed or cleared.						|
|=====================================================================================\*/

int main(void)
{
	float num1;																													// Contains the 1st number
	float num2;																													// Contains the 2nd number
	
	int run = 0;																												// Indicates the first run in the main loop (0 for 1st run, else not 1st)
	int advRun = 0;																												// Indicates the first run in the advanced selection (0 for 1st run, else not 1st)

	char sel;																													// Contains the selection
	char op;																													// Contains the operation
	char advSel;																												// Contains the advanced selection of B, U, or X
	char var = 'f';																												// Contains the variable name

	do {																																// Exit; loops until 'X' or 'x' is entered
		switch ( sel = mainList(run++) ) {																								// Switches between different selections
			case 'B':																											// Binary Operation
				puts("Enter the 1st number:");
				num1 = askNumOrVar(sel);																								// Asks for and saves the first number
			
				op = askOp(sel);																										// Asks for and saves the operation
            
				puts("Enter the 2nd number:");
				while ( (num2 = askNumOrVar(sel)) == 0 && (op == '/' || op == '%') )													// Asks for and saves the second number
					puts("Error: Division by 0; please enter a non-zero value:");															// Prints an error stating a division by 0
            
				mainMem( doBinary(num1, num2, op), sel );																				// Calculates and saves the binary operation based on prior inputs
				break;																															// *break; end of Binary Operation
			
			case 'U':																											// Unary Operation
				op = askOp(sel);																										// Asks for and saves the operation

				puts("Enter a number:");
				while ( ( (num1 = askNumOrVar(sel)) < 0 && op == 'S' ) || (num1 <= 0 && op == 'L') ) {									// Asks for and saves a number
					if (num1 < 0 && op == 'S')
						puts("Error: Square root of negative number; please enter a non-negative value:");									// Prints an error stating a square root of a negative number
					else if (num1 <= 0 && op == 'L')
						puts("Error: Log of 0 or negative number; please enter a value greater than 0:");									// Prints an error stating a log of 0 or less
				}

				mainMem( doUnary(num1, op), sel );																						// Calculates and saves the unary operation based on prior inputs
				break;																															// *break; end of Unary Operation

			case 'A':																											// Advanced Operations
				do {																													// Loops if 'X' is not selected
					switch ( advSel = advList(advRun++) ) {																				// Compares between B(inary), U(nary), and E(X)it
						case 'B':																									// Advanced Binary Operation
							puts("Enter the first number or variable (names a-e):");
							num1 = askNumOrVar(sel);																						// Asks for and saves the first value (constant input or variable)

							op = askOp(advSel);																								// Asks for and saves the operation
            
							puts("Enter the second number or variable (names a-e):");
							while ( (num2 = askNumOrVar(sel)) == 0 && (op == '/' || op == '%') )											// Asks for and saves the second value (constant input or variable)
								puts("Error: Division by 0; please enter a different value or variable:");										// Prints an error stating a division by 0
							
							mainMem( doBinary(num1, num2, op), sel);																		// Calculates and saves the advanced binary operation based on prior inputs
							break;																													// *break; end of Advanced Binary Operation

						case 'U':																									// Advanced Unary Operation
							op = askOp(advSel);																								// Asks for and saves the operation

							puts("Enter a number or variable (names a-e):");
							while ( ( (num1 = askNumOrVar(sel)) < 0 && op == 'S' ) || (num1 <= 0 && op == 'L') ) {							// Asks for and saves a value (constant input or variable)
								if (num1 < 0 && op == 'S')
									puts("Error: Square root of negative number; please enter a different value or variable:");					// Prints an error stating a square root of a negative number
								else if (num1 <= 0 && op == 'L')
									puts("Error: Log of 0 or negative number; please enter a different value or variable:");					// Prints an error stating a log of 0 or less
							}

							mainMem( doUnary(num1, op), sel );																				// Calculates and saves the advanced unary operation based on prior inputs
							break;																													// *break; end of Advanced Unary Operation

						case 'X':																								// Exit (to main menu, from Advanced Operations)
							puts("Going back to the main menu...");																			// Indicates a return to the main menu
							break;																													// *break; exit to Main Menu
					}
				} while ( advSel != 'X');																								// Keeps looping until 'X' is selected
				advRun = 0;																												// Resets the number of advanced selection iterations
				break;																															// *break; end of Advanced Operations

			case 'V':																											// Variables
				puts("Choose a variable (named characters a-e):");
				while ( !scanf(" %c", &var) || (var != 'a' && var != 'b' && var != 'c' && var != 'd' && var != 'e') )						// Asks for a variable name
        			puts("Error: Please enter a variable name (from a-e):");																	// Prints an error stating a non-specified input

				printf("Enter a number for variable '%c':\n", var);
				num1 = varMem( sel, varIndex(var) );																							// Asks for and saves a number into the specified variable

				printf("Variable '%c' = %.2f\n", var, num1);																				// Prints the designation result
				break;																																// *break; end of Variables
		
			case 'M':																											// Memory
				mainMem(0, sel);																											// Prints whatever is specified
				break;																																	// *break; end of Memory

			case 'R':																											// Clear Memory
				mainMem(0, sel);																											// Resets the memory and sets memIn = 0 (index 0)
				break;																																// *break; end of Clear

			case 'X':																											// Exit (Program)
				puts("Thank you for using my Simple Calculator. Goodbye!");																	// Prints an exit thank you message
				break;																																// *break; end of program
		}
	} while (sel != 'X');
}																																		// *End; end of main()

char mainList(int iteration)
{
	char input;																													// Contains the selection input to be returned

	do {
		if (iteration++ == 0) {																									// Activates only on the first iteration of the main program
			puts("Welcome to the Command-Line Calculator (CLC)!");
			puts("Developer: Keigo Katanaga");
			puts("Version: 3.0");
			puts("Date: 11/22/2021");
			puts("--------------------------------------------------");
			puts("Select one of the following items:");
			puts("B) - Binary Mathematical Operations such as addition and subtraction.");
	    	puts("U) - Unary Mathematical Operations such as square root, and log.");
			puts("A) - Advanced Mathematical Operations using variables or arrays.");
	    	puts("V) - Define variables and assign them values.");
        	puts("M) - Check the memory.");
        	puts("R) - Clear the memory.");
			puts("X) - Exit");
		}
		else
			puts("Please select your option ( B (Binary Operation), U (Unary Operation), A (Advanced), V (Variables), M (Memory), R (Clear), X (Exit) )");

		if ( !scanf(" %c", &input) || ( (input = toupper(input)) != 'B' && input != 'U' && input != 'A' && input != 'V' && input != 'M' && input != 'R' && input != 'X' ) )
			puts("Sorry, that selection is unfamiliar.");																				// Prints an error stating an undesignated input

	} while ( input != 'B' && input != 'U' && input != 'A' && input != 'V' && input != 'M' && input != 'R' && input != 'X' );	// Keeps looping until a designated input is entered

	return input;																												// Returns the selection
}																																		// *End; end of mainList()

float askNumOrVar(char selection)
{
	float num = 'f';																											// Contains the number to be returned
	char var = 'f';																												// Specifies which variable to take the number from

	while ( !scanf("%f", &num) && scanf(" %c", &var) ) {
		if (selection == 'B' || selection == 'U' || selection == 'V')															// Activates when the input is for Binary, Unary, or Variable selection
			puts("Please enter a proper value:");																						// Asks for a constant

		else if (selection == 'A') {																							// Activates when the input is for Advanced selection
			if (var == 'a' || var == 'b' || var == 'c' || var == 'd' || var == 'e') {
				if (varMem(selection, varIndex(var)) != 'f')																			// Checks the variable's value in varMem
					return varMem(selection, varIndex(var));																				// Returns the value inside the variable
				else
					puts("Variable is empty; please choose another one:");																	// Prints an error stating that an empty variable was chosen
			}
			else
				puts("Please enter a specified variable (characters a-e, lower case):");												// Prints an error stating a non-variable character input
		}
	}
	return num;																													// Returns the value (a constant or value of the variable)
}																																		// *End; end of askNumberOrVariable()

char askOp(char selection)
{
    char op = 'f';																												// Contains the operation to be returned
    
    switch (selection) {
        case 'B':																												// Binary Operators
            puts("Select an operation ( + , - , * , /, %, ^ ):");
            while ( !scanf(" %c", &op) || (op != '+' && op != '-' && op != '*' && op != '/' && op != '%' && op != '^') )
		        puts("Please enter a proper operator:");																				// Prints an error stating a non-binary operation or unknown input
            break;																																// *break; end of Binary Operators

        case 'U':																												// Unary Operators
            puts("Select an operation ( S(qrt), L(og), E(xponentiation), C(eiling), F(loor) )");
            while ( !scanf(" %c", &op) || (toupper(op) != 'S' && toupper(op) != 'L' && toupper(op) != 'E' && toupper(op) != 'C' && toupper(op) != 'F') )
				puts("Please enter a proper operator:");																				// Prints an error stating a non-unary operation or unknown input

            op = toupper(op);																											// Converts the operator to uppercase
            break;																																// *break; end of Unary Operators
    }
    return op;																													// Returns the operator
}																																		// *End; end of askOperator()

char advList(int iteration)
{
	char input;

	do {
		if (iteration++ == 0) {																									// Activates only on the first iteration of Advanced Operations
			puts("Select one of the following items:");
			puts("B) - Binary Mathematical Operations such as addition and subtraction.");
	    	puts("U) - Unary Mathematical Operations such as square root, and log.");
			puts("X) - Exit");
		}
		else
			puts("Please select your option ( B (Binary Operation), U (Unary Operation), X (Exit) )");

		if ( !scanf(" %c", &input) || ( (input = toupper(input)) != 'B' && input != 'U' && input != 'X' ) )
			puts("Sorry, that selection is unfamiliar.");																			// Prints an error stating an undesignated input

	} while ( input != 'B' && input != 'U' && input != 'X' );																	// Keeps looping until a designated input is entered

	return input;																												// Returns the selection
}																																		// *End; end of advancedList()

float varMem(char selection, int index)
{
	static float ae[5] = {'f', 'f', 'f', 'f', 'f'};																				// Variables to put values in; 'f' indicates an empty variable

	if (selection == 'V') {
		ae[index] = askNumOrVar(selection);																								// Asks and receives the value of a selected variable
	}

	return ae[index];																											// Returns the value of the variable
}																																		// *End; end of variableMemory()

int varIndex(char variable)
{
	switch(variable) {																											// Compares the variable names
		case 'a':
			return 0;																													// Returns the corresponding index of variable 'a'
			break;
		case 'b':
			return 1;																													// Returns the corresponding index of variable 'b'
			break;
		case 'c':
			return 2;																													// Returns the corresponding index of variable 'c'
			break;
		case 'd':
			return 3;																													// Returns the corresponding index of variable 'd'
			break;
		case 'e':
			return 4;																													// Returns the corresponding index of variable 'e'
			break;
	}
	return 0;																													// Returns the default index
}																																		// *End; end of variableIndex

float doBinary(float first, float second, char operator) 
{
	switch (operator) {																											// Switches between different operators
		case '+':
			printf("%.2f + %.2f = %.2f\n", first, second, first + second);
			return first + second;																										// Returns the sum
			break;
		case '-':
			printf("%.2f - %.2f = %.2f\n", first, second, first - second);
			return first - second;																										// Returns the difference
			break;
		case '*':
			printf("%.2f * %.2f = %.2f\n", first, second, first * second);
			return first * second;																										// Returns the product
			break;
		case '/':
			printf("%.2f / %.2f = %.2f\n", first, second, first / second);
			return first / second;																										// Returns the quotient
			break;
		case '%':
			printf("%.2f %% %.2f = %.2f\n", first, second, fmod(first, second));
			return fmod(first, second);																									// Returns the modulo (remainder)
			break;
		case '^':
			printf("%.2f ^ %.2f = %.2f\n", first, second, pow(first, second));
			return pow(first, second);																									// Returns the power
			break;
	}
	return -1;																															// Returns -1 for failure
}																																				// *End; end of doBinary()

float doUnary(float num, char operator) 
{
	switch (operator) {																											// Switches between different operators
		case 'S':
			printf("The square root of %.2f is: %.2f\n", num, sqrt(num));
			return sqrt(num);																											// Returns the square root
			break;
		case 'L':
			printf("The logarithm of %.2f is: %.2f\n", num, log10(num));
			return log10(num);																											// Returns the log base 10
			break;
		case 'E':
			printf("The exponentiation of %.2f is: %.2f\n", num, exp(num));
			return exp(num);																											// Returns the exponentiation (e^num1)
			break;
		case 'C':
			printf("The ceiling of %.2f is: %.2f\n", num, ceil(num));
			return ceil(num);																											// Returns the ceiling value
			break;
		case 'F':
			printf("The floor of %.2f is: %.2f\n", num, floor(num));
			return ceil(num);																											// Returns the floor value
			break;
	}
	return -1;																																	// *End; end of doUnary()
}

int mainMem(float result, char selection)
{
	static float resMem[SIZE];																									// Contains the result of every operation
	static int index = 0;																										// Contains the index of the next empty slot
	if (index == 0)
		clearMem(resMem);																											// Clears the memory before use

	switch (selection) {
		case 'M':																												// "Memory" Selection
			if (index == 1)
				puts("You have 1 item in memory.");																						// States that there is only 1 item in the memory
			else
				printf("You have %d items in memory.\n", index);																		// Else, state that there are memIn number of items (including 0)
			
			if (index != 0) {																											// Activates only if the memory isn't empty
				memList(resMem);																											// Asks for and prints whatever is in the slot entered
				puts("Going back to the main menu...");																						// Indicates a return to the main menu
			}
			break;																																	// *break; end of Memory
		case 'R':																												// "Clear" Selection
			puts("Clearing memory...");																									// Indicates the memory wipe
			index = clearMem(resMem);																									// Resets the memory and sets memIn = 0 (index 0)
			break;																																// *break; end of Clear
		default:																												// Default; saves results from binary, unary, and advanced selections
			if (index < SIZE)
				resMem[index++] = result;																								// Saves a result
			else
				puts("Warning: Memory Full!");																							// Prints an error stating a full memory
			break;																																// *break; end of default
	}
	return index;																												// Returns the current index/number of items in the memory
}																																		// *End; end of mainMemory()

void memList(float resultMemory[SIZE])
{
	int position;																												// Contains the position of the slot
	char selection = '0';																										// Contains the selection 'X'

	do {
		puts("Select your option ( 0 (Whole Memory), 1-100 (Specific Memory), X (Exit) )");										// Prints the selection for for memory case

		if ( ( !scanf("%d", &position) && scanf(" %c", &selection) ) || position < 0 || position > SIZE ) {						// Checks if the input is proper or not
			if ( (selection = toupper(selection)) != 'X' )
				puts("Sorry, that selection is unfamiliar.");																		// Prints an error stating an unspecified input
			else if ( selection == 'X' )
				return;																												// Immediately returns if 'X' is entered
		}
		else {
			if (position > 0) {																									// Checks if the input is position-specific
				if (resultMemory[position - 1] != 'f')
					printf("Position %d: %.2f\n", position, resultMemory[position - 1]);											// Prints a non-empty slot
				else 
					printf("Position %d: Empty\n", position);																		// Prints an empty slot
			}
			else {																												// Else, print everything
				while (resultMemory[position++] != 'f') 
					printf("Position %d: %.2f\n", position, resultMemory[position - 1]);											// Prints a non-empty slot
			}
		}
	} while (selection != 'X');																									// Keeps looping until 'X' is selected
}																																		// *End; end of memoryList()

int clearMem(float resultMemory[SIZE])
{
	for (int i = 0; i < SIZE; i++) {																							// Keeps looping until the end of the array
		if (resultMemory[i] == 'f')
			break;																														// Stops the loop once an empty slot is found
		resultMemory[i] = 'f';																										// Clears the slot
	}
	return 0;																													// Returns index 0
}																																		// *End clearMem()