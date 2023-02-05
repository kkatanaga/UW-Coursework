#include <stdio.h>																													// C Standard Library for input/output functions
#include <math.h>																													// C Standard Library for math functions
#include <ctype.h>																													// C Standard Library for character testing/mapping functions

char selList(int mainIteration, char selection);
float askNum();
char askVar();
float askNumOrVar();
int varIndex(char variable);
char askOp(char function);
float doBinary(float first, float second, char operation);
float doUnary(float first, char operation);

/*\=============================================================|
|   Program Name: clc_v3.c                                  	|
|   Name: Keigo Katanaga                                        |
|   Student ID: 110068805                                       |
|   Date: 11/22/2021                                            |
|   Description:                |
|=============================================================\*/

float mem[5];
float ae[5] = {'f', 'f', 'f', 'f', 'f'};																						// Variables to be specified values to in selection V; 'f' indicates an empty variable

int main(void)
{
	float num1 = 'f', num2 = 'f';																									// Contains the 1st number, 2nd number
	int run = 0, advRun = 0;
	char sel, op, advSel, var = 'f';																								// Contains the selection, operation, selection of B, U, or X, and variable names

	do {																															// Exit; loops until 'X' or 'x' is entered
		switch ( sel = selList(run, 'f') ) {																						// Switches between different selections
			case 'B':																												// Binary Operations; activates when 'B' or 'b' is entered
				puts("Enter the 1st number:");																						// Asks for the first number
				num1 = askNum();
			
				op = askOp(sel);
            
				puts("Enter the 2nd number:");																						// Asks for the second number
				while ( (num2 = askNum()) == 0 && (op == '/' || op == '%') )
					puts("Error: Division by 0; please enter a non-zero value:");																	// Prints an error stating a division by 0
            
				mem[run] = doBinary(num1, num2, op);
				break;																																// *break 'B' or 'b'; binary math operations
			
			case 'U':																												// Unary Operations; activates when 'U' or 'u' is entered
				op = askOp(sel);

				puts("Enter a number:");																							// Asks for a number
				while ( ( (num1 = askNum()) < 0 && op == 'S' ) || (num1 <= 0 && op == 'L') ) {											// Loops until a number is entered or the number is non-negative for sqrt, log, or exp																													// Activates when sqrt() or log10() or log() is selected and num1 is negative
					if (num1 < 0 && op == 'S')
						puts("Error: Square root of negative number; please enter a non-negative value:");							// Prints an error stating a square root of a negative number
					else if (num1 <= 0 && op == 'L')
						puts("Error: Log of 0 or negative number; please enter a value greater than 0:");							// Prints an error stating a log of 0 or less
				}

				mem[run] = doUnary(num1, op);
				break;																															// *break 'U' or 'u'; unary math operations

			case 'A':																												// Advanced Operations; activates when 'A' or 'a' is entered
				do {																													// Loops if 'X' is not selected
					switch ( advSel = selList(advRun, sel) ) {
						case 'B':																										// Advanced Binary Operation
							puts("Enter the first number or variable (names a-e):");													// Asks for the first constant or variable
							num1 = askNumOrVar();

							op = askOp(advSel);
            
							puts("Enter the second number or variable (names a-e):");													// Asks for the second constant or variable
							while ( (num2 = askNumOrVar()) == 0 && (op == '/' || op == '%') )
								puts("Error: Division by 0; please choose a different variable:");													// Asks for a non-zero variable
							
							mem[run] = doBinary(num1, num2, op);
							break;																													// *break 'B'

						case 'U':																										// Advanced Unary Operation
							op = askOp(advSel);

							puts("Enter a number or variable (names a-e):");															// Asks for a constant or variable
							while ( ( (num1 = askNumOrVar()) < 0 && op == 'S' ) || (num1 <= 0 && op == 'L') ) {
								if (num1 < 0 && op == 'S')
									puts("Error: Square root of negative number; please enter a non-negative value or another variable:");										// Asks for a variable greater than or equal to 0
								else if (num1 <= 0 && op == 'L')
									puts("Error: Log of 0 or negative number; please enter a value greater than 0 or another variable:");										// Asks for a non-negative variable (excludes 0)
							}

							mem[run] = doUnary(num1, op);
							break;																													// *break 'U'

						case 'X':																										// Exit to main menu
							puts("Going back to the main menu...");																		// Indicates a return to the main menu
							break;																													// *break 'X'
					}																																// *End switch(B, U, or X)
					advRun++;
					run++;
				} while ( advSel != 'X');																							// Keeps looping until 'X' is selected
				advRun = 0;
				break;																															// *break 'A' or 'a'; advanced math operations

			case 'V':																												// Variables; activates when 'V' or 'v' is entered
				puts("Choose a variable (named characters a-e):");																	// Asks for a variable name
				var = askVar();

				printf("Enter a number for variable (%c):\n", var);
				ae[varIndex(var)] = askNum();

				printf("Variable (%c) = %.2f\n", var, ae[varIndex(var)]);															// Prints the designation result
				break;																															// *break 'V' or 'v'; variables
		
			case 'M':
				break;
			case 'R':
				break;
			case 'X':																												// Activates when 'X' or 'x' is entered
				puts("Thank you for using my Simple Calculator. Goodbye!");																// Prints an exit thank you message
				break;																															// *break 'X' or 'x'; exit
		}																																		// *End switch(selection)

		if (run < 5 && sel != 'A' && sel != 'V')
			run++;

	} while (sel != 'X'); 																												// *End while(!'X' or !'x')
}																																				// *End main()

char selList(int mainIteration, char selection)
{
    char in = 'f';

	if (mainIteration == 0) {
        if (selection != 'A') {
            puts("Welcome to the Command-Line Calculator (CLC)!");
	        puts("Developer: Keigo Katanaga");
	        puts("Version: 1.0");
	        puts("Date: 11/22/2021");
	        puts("--------------------------------------------------");
        }
	    puts("Select one of the following items:");
	    puts("B) - Binary Mathematical Operations such as addition and subtraction.");
	    puts("U) - Unary Mathematical Operations such as square root, and log.");
        if (selection != 'A') {
	        puts("A) - Advanced Mathematical Operations using variables or arrays.");
	        puts("V) - Define variables and assign them values.");
            puts("M) - Check the memory.");
            puts("R) - Clear the memory.");
        }
	    puts("X) - Exit");
        mainIteration++;
    }
	else if (mainIteration > 0 && selection == 'A')
		puts("Please select your option ( B (Binary Operation), U (Unary Operation), X (Exit) )");		// Prints the selection again for advanced case
	else
		puts("Please select your option ( B (Binary Operation), U (Unary Operation), A (Advanced), V (Variables), M (Memory), R (Clear), X (Exit) )");		// Prints the selection again

    while ( !scanf(" %c", &in) || ( toupper(in) != 'B' && toupper(in) != 'U' && toupper(in) != 'X' && ( selection == 'A' || (toupper(in) != 'A' && toupper(in) != 'V' && toupper(in) != 'M' && toupper(in) != 'R') ) ) ) {
		puts("Sorry, that selection is unfamiliar.");
		if (selection == 'A')
			puts("Please select your option ( B (Binary Operation), U (Unary Operation), X (Exit) )");		// Prints the selection again for advanced case
		else
			puts("Please select your option ( B (Binary Operation), U (Unary Operation), A (Advanced), V (Variables), M (Memory), R (Clear), X (Exit) )");		// Prints the selection again
	}
        
    in = toupper(in);
    return in;
}

float askNum()
{
	float num;
	char buf;

	while ( !scanf("%f", &num) ) {                                                                                                      // Loops until a number is entered
		puts("Please enter a proper value:");
		scanf(" %c", &buf);
    }
	return num;
}

char askVar()
{
	char var;

	while ( !scanf(" %c", &var) || (var != 'a' && var != 'b' && var != 'c' && var != 'd' && var != 'e') ) {		// Loops until a constant or specified variable is chosen
        puts("Please enter a variable (names a-e):");																// Asks for a non-character value
    }
	return var;
}

float askNumOrVar()
{
	float num = 'f';
	char var = 'f';

	while ( !scanf("%f", &num) ) {																						// Loops until a constant or specified variable is chosen
		if ( scanf(" %c", &var) && (var == 'a' || var == 'b' || var == 'c' || var == 'd' || var == 'e') ) {				// Checks if a variable is entered instead of a constant
			if (ae[varIndex(var)] != 'f') {
				num = ae[varIndex(var)];
				break;
			}
			else {
				puts("Variable is empty; please choose another one:");														// Asks for an initialized variable
				continue;
			}
		}

		puts("Please enter a number or a character between a to e (lower case):");											// Asks for a constant or a variable name between a and e
	}																																// *End while(!var)
	return num;
}

char askOp(char selection)
{
    char op = 'f';
    
    switch (selection) {
        case 'B':
            puts("Select an operation ( + , - , * , /, %, ^ ):");																// Asks for an operation
            while ( !scanf(" %c", &op) || (op != '+' && op != '-' && op != '*' && op != '/' && op != '%' && op != '^') )
		        puts("Please enter a proper operation:");																					// Asks for a non-numeric, operation input
            break;

        case 'U':
            puts("Select an operation ( S(qrt), L(og), E(xponentiation), C(eiling), F(loor) )");
            while ( !scanf(" %c", &op) || (toupper(op) != 'S' && toupper(op) != 'L' && toupper(op) != 'E' && toupper(op) != 'C' && toupper(op) != 'F') )
				puts("Please enter a proper operation:");

            op = toupper(op);
            break;
    }
    return op;
}

int varIndex(char variable)
{
	switch(variable) {
		case 'a':
			return 0;
			break;																															// *break 'a'
		case 'b':
			return 1;
			break;																															// *break 'b'
		case 'c':
			return 2;
			break;																															// *break 'c'
		case 'd':
			return 3;
			break;																															// *break 'd'
		case 'e':
			return 4;
			break;																															// *break 'e'
	}
	return 5;
}

float doBinary(float first, float second, char operation) 
{
	switch (operation) {																											// Switches between different operations
		case '+':
			printf("%.2f + %.2f = %.2f\n", first, second, first + second);																// Prints the sum
			return first + second;
			break;																														// *break '+'
		case '-':
			printf("%.2f - %.2f = %.2f\n", first, second, first - second);																// Prints the difference
			return first - second;
			break;																														// *break '-'
		case '*':
			printf("%.2f * %.2f = %.2f\n", first, second, first * second);																// Prints the product
			return first * second;
			break;																														// *break '*'
		case '/':
			printf("%.2f / %.2f = %.2f\n", first, second, first / second);																// Prints the quotient
			return first / second;
			break;																														// *break '/'
		case '%':
			printf("%.2f %% %.2f = %.2f\n", first, second, fmod(first, second));															// Prints the modulo (remainder)
			return fmod(first, second);
			break;																														// *break '%'
		case '^':
			printf("%.2f ^ %.2f = %.2f\n", first, second, pow(first, second));															// Prints the power
			return pow(first, second);
			break;																														// *break '^'
	}																																	// *End switch(operation)

	return -1;
}

float doUnary(float num, char operation) 
{
	switch (operation) {																											// Switches between different operations
		case 'S':
			printf("The square root of %.2f is: %.2f\n", num, sqrt(num));															// Prints the square root
			return sqrt(num);
			break;																														// *break 'S'
		case 'L':
			printf("The logarithm of %.2f is: %.2f\n", num, log10(num));															// Prints the log base 10
			return log10(num);
			break;																														// *break 'L'
		case 'E':
			printf("The exponentiation of %.2f is: %.2f\n", num, exp(num));														// Prints the exponentiation (e^num1)
			return exp(num);
			break;																														// *break 'E'
		case 'C':
			printf("The ceiling of %.2f is: %.2f\n", num, ceil(num));																// Prints the ceiling value
			return ceil(num);
			break;																														// *break 'C'
			case 'F':
			printf("The floor of %.2f is: %.2f\n", num, floor(num));																// Prints the floor value
			return ceil(num);
			break;																														// *break 'F'
	}																																	// *End switch(operation)
	return -1;
}