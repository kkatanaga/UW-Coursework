#include <stdio.h>																													// C Standard Library for input/output functions
#include <math.h>																													// C Standard Library for math functions
#include <ctype.h>																													// C Standard Library for character testing/mapping functions

char selList(int iteration, char selection);
float askNum();
char askVar();
char askOp(char function);
float doOp(char selection, float first, char operation, float second);

int main()
{
	float num1 = 'f';																									            // Contains the 1st number
    float num2 = 'f';																									            // Contains the 2nd number
	float ae[7] = {'f', 'f', 'f', 'f', 'f', 'f', 'f'};																				// Variables to be specified values to in selection V; 'f' indicates an empty variable; ae[5] & ae[6] are for constant inputs
	int run = 0, buxRun = 0;																													// Counts how many times the main selection occurs
	//int aeIn1 = 'f', aeIn2 = 'f';																									// Specifies which element of ae[]'s to use
	//char buf, sel, op, bux, var1 = 'f', var2 = 'f';																					// Contains the buffer, selection, operation, selection of B, U, or X, and variable names
    char sel, op;
    float mem[5] = {'f', 'f', 'f', 'f', 'f'};
    do {
        switch ( sel = selList(run, sel) ) {
            case 'B':																												// Binary Operations; activates when 'B' or 'b' is entered
                printf("Enter the 1st number:\n");																						// Asks for the first number
                num1 = askNum();

                op = askOp(sel);
                
                printf("Enter the 2nd number:\n");																						// Asks for the second number
                num2 = askNum();

                mem[run] = doOp(sel, num1, op, num2);
                printf("Memory %d:%.2f\n", run, mem[run]);
				num1 = num2 = op = 'f';																											// Resets the inputs to 'a'
                break;

            case 'U':																												// Unary Operations; activates when 'U' or 'u' is entered
                op = askOp(sel);

                printf("Enter a number:\n");
                num1 = askNum();

                mem[run] = doOp(sel, num1, op, 0);
                printf("Memory %d:%.2f\n", run, mem[run]);
                num1 = op = 'f';
                break;

            case 'A':																												// Advanced Operations; activates when 'A' or 'a' is entered
                break;
            case 'V':																												// Variables; activates when 'V' or 'v' is entered
                // var
            case 'M':

            case 'R':

            case 'X':
            break;

        }
        if (run < 5)
            run++;
    } while (sel != 'X');
}																																				// *End main()

char selList(int mainIteration, char selection)
{
    char in = 'f';

    do {
	    if (mainIteration == 0) {
            if (selection != 'A') {
                printf("Welcome to the Command-Line Calculator (CLC)!\n");
	            printf("Developer: Keigo Katanaga\n");
	            printf("Version: 1.0\n");
	            printf("Date: 11/13/2021\n");
	            printf("--------------------------------------------------\n");
            }
	        printf("Select one of the following items:\n");
	        printf("B) - Binary Mathematical Operations such as addition and subtraction.\n");
	        printf("U) - Unary Mathematical Operations such as square root, and log.\n");
            if (selection != 'A') {
	            printf("A) - Advanced Mathematical Operations using variables or arrays.\n");
	            printf("V) - Define variables and assign them values.\n");
                printf("M) - Check the memory.\n");
                printf("R) - Clear the memory.\n");
            }
	        printf("X) - Exit\n");
            mainIteration++;
        }
        else if (mainIteration > 0 && selection == 'A')
            printf("Please select your option ( B (Binary Operation), U (Unary Operation), X (Exit) )\n");		// Prints the selection again for advanced case
        else
            printf("Please select your option ( B (Binary Operation), U (Unary Operation), A (Advanced), V (Variables), M (Memory), R (Clear), X (Exit) )\n");		// Prints the selection again

        switch (selection){
            case 'A':
                if ( !scanf(" %c", &in) || (toupper(in) != 'B' && toupper(in) != 'U' && toupper(in) != 'X') )
                    printf("Sorry, that selection is unfamiliar.\n");
                break;
            
            default:
                if ( !scanf(" %c", &in) || (toupper(in) != 'B' && toupper(in) != 'U' && toupper(in) != 'A' && toupper(in) != 'V' && toupper(in) != 'M' && toupper(in) != 'R' && toupper(in) != 'X') )
                    printf("Sorry, that selection is unfamiliar.\n");
                break;
        }
        
        in = toupper(in);

    } while (in != 'B' && in != 'U' && in != 'A' && in != 'V' && in != 'M' && in != 'R' && in != 'X');    // Loops when the input is an unspecified selection
    return in;
}

float askNum()
{
    float num = 'f';
    char buf;

    while ( !scanf("%f", &num) ) {                                                                                                      // Loops until a number is entered
        printf("Please enter a proper value:\n");																						// Asks for a non-character value
    	scanf(" %c", &buf);																												// Clears the buffer
    }
    return num;
}

char askVar()
{
    char var;

    while ( !scanf(" %c", &var) || (var != 'a' && var != 'b' && var != 'c' && var != 'd' && var != 'e')) {
        printf("Please enter a character between a to e (lower case):\n");																// Asks for a variable name between a and e
	}																																		// *End while(!var)
    return var;
}

char askOp(char function)
{
    char op = 'f';
    
    switch (function) {
        case 'B':
            printf("Select an operation ( + , - , * , /, %%, ^ ):\n");																// Asks for an operation
            while ( !scanf(" %c", &op) || (op != '+' && op != '-' && op != '*' && op != '/' && op != '%' && op != '^') )
		        printf("Please enter a proper operation:\n");																					// Asks for a non-numeric, operation input
            break;

        case 'U':
            printf("Select an operation ( S(qrt), L(og), E(xponentiation), C(eiling), F(loor) )\n");
            while ( !scanf(" %c", &op) || (toupper(op) != 'S' && toupper(op) != 'L' && toupper(op) != 'E' && toupper(op) != 'C' && toupper(op) != 'F') )
				printf("Please enter a proper operation:\n");

            op = toupper(op);
            break;
    }
    return op;
}

float doOp(char selection, float first, char operation, float second)
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
			printf("%.2f %% %.2f = %.2f\n", first, second, fmod(first, second));														// Prints the modulo (remainder)
            return fmod(first, second);
			break;																														// *break '%'
		case '^':
			printf("%.2f ^ %.2f = %.2f\n", first, second, pow(first, second));															// Prints the power
            return pow(first, second);
			break;																														// *break '^'
        case 'S':
			printf("The square root of %.2f is: %.2f\n", first, sqrt(first));															// Prints the square root
            return sqrt(first);
			break;																														// *break 'S'
		case 'L':
			printf("The logarithm of %.2f is: %.2f\n", first, log10(first));															// Prints the log base 10
            return log10(first);
			break;																														// *break 'L'
		case 'E':
			printf("The exponentiation of %.2f is: %.2f\n", first, exp(first));														    // Prints the exponentiation (e^num1)
            return exp(first);
			break;																														// *break 'E'
		case 'C':
			printf("The ceiling of %.2f is: %.2f\n", first, ceil(first));																// Prints the ceiling value
            return ceil(first);
			break;																														// *break 'C'
		case 'F':
			printf("The floor of %.2f is: %.2f\n", first, floor(first));																// Prints the floor value
            return floor(first);
			break;																														// *break 'F'
	}																																	// *End switch(operation)
    return -1;
}

// memoryVar
// memoryRes
// askVar
// doBin
// doUna
