#include <stdio.h>																													// C Standard Library for input/output functions
#include <math.h>																													// C Standard Library for math functions
#include <ctype.h>																													// C Standard Library for character testing/mapping functions

int main(void)
{
	float num1 = 'f', num2 = 'f';																									// Contains the 1st number, 2nd number
	float ae[7] = {'f', 'f', 'f', 'f', 'f', 'f', 'f'};																				// Variables to be specified values to in selection V; 'f' indicates an empty variable; ae[5] & ae[6] are for constant inputs
	int aeIn1 = 'f', aeIn2 = 'f';																									// Specifies which element of ae[]'s to use
	char buf, sel, op, bux, var1 = 'f', var2 = 'f';																					// Contains the buffer, selection, operation, selection of B, U, or X, and variable names

	printf("Welcome to the Command-Line Calculator (CLC)!\n");
	printf("Developer: Keigo Katanaga\n");
	printf("Version: 1.0\n");
	printf("Date: 11/13/2021\n");
	printf("--------------------------------------------------\n");
	printf("Select one of the following items:\n");
	printf("B) - Binary Mathematical Operations such as addition and subtraction.\n");
	printf("U) - Unary Mathematical Operations such as square root, and log.\n");
	printf("A) - Advanced Mathematical Operations using variables or arrays.\n");
	printf("V) – Define variables and assign them values.\n");
	printf("X) – Exit\n");
	
	do {																															// Exit; loops until 'X' or 'x' is entered
		scanf(" %c", &sel);																												// Receives the selection
		
		switch ( toupper(sel) ) {																										// Switches between different selections
			case 'B':																												// Binary Operations; activates when 'B' or 'b' is entered
			
				printf("Enter the 1st number:\n");																						// Asks for the first number
				while ( !scanf("%f", &num1)) {																									// Loops until a number is entered
					printf("Please enter a proper value:\n");																						// Asks for a non-character value
    				scanf(" %c", &buf);																												// Clears the buffer
				}																																		// *End while(num1 != float)
			
				printf("Select an operation ( + , - , * , /, %%, ^ ):\n");																// Asks for an operation
				while ( !scanf(" %c", &op) || (op != '+' && op != '-' && op != '*' && op != '/' && op != '%' && op != '^') ) {					// Loops until an operation is entered
					printf("Please enter a proper operation:\n");																					// Asks for a non-numeric, operation input
				}																																		// *End while(!operation)
            
				printf("Enter the 2nd number:\n");																						// Asks for the second number
				while ( !scanf("%f", &num2) || ( (op == '/' || op == '%') && num2 == 0 ) ) {													// Loops until a number in entered or division by 0 is avoided
					if ( isalpha( (char) num2 ) ) { 																								// Checks if the input is an alphabet
						printf("Please enter a proper value:\n");																						// Asks for a non-character value
						scanf(" %c", &buf);																												// Clears the buffer
					}																																		// *End if(alphabet)
					else {
						printf("Division by 0; please enter a non-zero number:\n");																	// Prints an error stating a division by 0
						num2 = 'a';																													// Resets num2 to 'a'
					}																																	// *End else
					
				}																																		// *End while(num2 != float || division by 0)
            
				switch (op) {																											// Switches between different operations
					case '+':
						printf("%.2f + %.2f = %.2f\n", num1, num2, num1 + num2);																// Prints the sum
						break;																														// *break '+'
					case '-':
						printf("%.2f - %.2f = %.2f\n", num1, num2, num1 - num2);																// Prints the difference
						break;																														// *break '-'
					case '*':
						printf("%.2f * %.2f = %.2f\n", num1, num2, num1 * num2);																// Prints the product
						break;																														// *break '*'
					case '/':
						printf("%.2f / %.2f = %.2f\n", num1, num2, num1 / num2);																// Prints the quotient
						break;																														// *break '/'
					case '%':
						printf("%.2f %% %.2f = %.2f\n", num1, num2, fmod(num1, num2));															// Prints the modulo (remainder)
						break;																														// *break '%'
					case '^':
						printf("%.2f ^ %.2f = %.2f\n", num1, num2, pow(num1, num2));															// Prints the power
						break;																														// *break '^'
				}																																	// *End switch(operation)

				num1 = num2 = op = 'a';																											// Resets the inputs to 'a'
				break;																																// *break 'B' or 'b'; binary math operations
			
			case 'U':																												// Unary Operations; activates when 'U' or 'u' is entered

				printf("Select an operation ( S(qrt), L(og), E(xponentiation), C(eiling), F(loor) )\n");								// Asks for an operation
				while ( !scanf(" %c", &op) || (op != 'S' && op != 'L' && op != 'E' && op != 'C' && op != 'F') ) {								// Loops until an operation is entered
					if (op == 's' || op == 'l' || op == 'e' || op == 'c' || op == 'f') {															// Checks if the input is lowercase
						op = toupper(op);																											// Changes characters to uppercase
						break;																														// *break out of the loop
					}																																	// *End if(lowercase(op))

					printf("Please enter a proper operation:\n");																					// Asks for a non-numeric, operation input
				}																																		// *End while(!char || !operation)

				printf("Enter a number:\n");																							// Asks for a number
				while ( !scanf("%f", &num1) || (op == 'S' && num1 < 0) || (op == 'L' && num1 <= 0 ) ) {											// Loops until a number is entered or the number is non-negative for sqrt, log, or exp
					if ( isalpha( (char) num1 ) ) {																									// Checks if the input is an alphabet
						printf("Please enter a proper value:\n");																					// Asks for a non-character value
						scanf(" %c", &buf);																											// Clears the buffer
					}																																	// *End if(alphabet)

					else {																															// Activates when sqrt() or log10() or log() is selected and num1 is negative
						printf("Please enter a non-negative number:\n");																			// Asks for a non-negative number
						num1 = 'f';																													// Resets num1 to 'a'
					}																																	// *End else
					
				}																																		// *End while(num1 != float)
				
				switch (op) {																											// Switches between different operations
					case 'S':
						printf("The square root of %.2f is: %.2f\n", num1, sqrt(num1));															// Prints the square root
						break;																														// *break 'S'
					case 'L':
						printf("The logarithm of %.2f is: %.2f\n", num1, log10(num1));															// Prints the log base 10
						break;																														// *break 'L'
					case 'E':
						printf("The exponentiation of %.2f is: %.2f\n", num1, exp(num1));														// Prints the exponentiation (e^num1)
						break;																														// *break 'E'
					case 'C':
						printf("The ceiling of %.2f is: %.2f\n", num1, ceil(num1));																// Prints the ceiling value
						break;																														// *break 'C'
					case 'F':
						printf("The floor of %.2f is: %.2f\n", num1, floor(num1));																// Prints the floor value
						break;																														// *break 'F'
				}																																	// *End switch(operation)
				op = num1 = 'f';																										// Resets inputs
				break;																															// *break 'U' or 'u'; unary math operations

			case 'A':																												// Advanced Operations; activates when 'A' or 'a' is entered
				printf("Please select one of the following selections:\n");
				printf("B) - Binary Mathematical Operations, such as addition and subtraction.\n");
				printf("U) - Unary Mathematical Operations, such as square root, and log.\n");
				printf("X) - Exit and back to the main menu.\n");
				do {																													// Loops if 'X' is not selected
					scanf(" %c", &bux);
					switch ( toupper(bux) ) {
						case 'B':																										// Advanced Binary Operation
							printf("Enter the first number or variable (names a-e):\n");													// Asks for the first constant or variable
							do {																													// Loops only when there is a possible error (such as an empty variable)
								while ( !scanf("%f", &ae[5]) ) {																						// Loops until a constant or specified variable is chosen
									if ( scanf(" %c", &var1) && (var1 == 'a' || var1 == 'b' || var1 == 'c' || var1 == 'd' || var1 == 'e') )					// Checks if a variable is entered instead of a constant
										break;
									printf("Please enter a number or a character between a to e (lower case):\n");											// Asks for a constant or a variable name between a and e
								}																																// *End while(!var)

								switch (var1) {																								// Switches between different first variables
									case 'a':
										aeIn1 = 0;																									// Sets the index = 0 to match variable 'a'
										break;																											// *break 'a'
									case 'b':
										aeIn1 = 1;																									// Sets the index = 1 to match variable 'b'
										break;																											// *break 'a'
									case 'c':
										aeIn1 = 2;																									// Sets the index = 2 to match variable 'c'
										break;																											// *break 'a'
									case 'd':
										aeIn1 = 3;																									// Sets the index = 3 to match variable 'd'
										break;																											// *break 'a'
									case 'e':
										aeIn1 = 4;																									// Sets the index = 4 to match variable 'e'
										break;																											// *break 'a'
									default:
										aeIn1 = 5;																									// Sets the index = 5 to match constant 1
										break;																											// *break default (float input)
								}																														// *End switch(variable name)

								if (ae[aeIn1] == 'f')
									printf("Variable is empty; please choose another one:\n");														// Asks for an initialized variable

							} while (ae[aeIn1] == 'f');																									// *Ends loop when the variable chosen is initialized
				
							printf("Select an operation ( + , - , * , /, %%, ^ ):\n");														// Asks for an operation
							while ( !scanf(" %c", &op) || (op != '+' && op != '-' && op != '*' && op != '/' && op != '%' && op != '^') ) {			// Loops until an operation is entered
								printf("Please enter a proper operation:\n");																			// Asks for a non-numeric, operation input
							}																																// *End while(!operation)
            
							printf("Enter the second number or variable (names a-e):\n");													// Asks for the second constant or variable
							do {																													//Loops only when there is a possible error (such as division by zero)
								while ( !scanf("%f", &ae[6]) ) {																						// Loops until a constant or specified variable is chosen
									if ( scanf(" %c", &var2) && (var2 == 'a' || var2 == 'b' || var2 == 'c' || var2 == 'd' || var2 == 'e') )					// Checks if a variable is entered instead of a constant
										break;
									printf("Please enter a number or a character between a to e (lower case):\n");											// Asks for a constant or a variable name between a and e
								}																																// *End while(!var)

								switch (var2) {																								// Switches between different second variables
									case 'a':
										aeIn2 = 0;																									// Sets the index = 0 to match variable 'a'
										break;																											// *break 'a'
									case 'b':
										aeIn2 = 1;																									// Sets the index = 1 to match variable 'b'
										break;																											// *break 'a'
									case 'c':
										aeIn2 = 2;																									// Sets the index = 2 to match variable 'c'
										break;																											// *break 'a'
									case 'd':
										aeIn2 = 3;																									// Sets the index = 3 to match variable 'd'
										break;																											// *break 'a'
									case 'e':
										aeIn2 = 4;																									// Sets the index = 4 to match variable 'e'
										break;																											// *break 'a'
									default:
										aeIn2 = 6;																									// Sets the index = 6 to match constant 2
										break;																											// *break default (float input)
								}																														// *End switch(variable name)

								if ( ae[aeIn2] == 'f' )
									printf("Variable is empty; please choose another one:\n");														// Asks for an initialized variable
								else if ((op == '/' || op == '%') && ae[aeIn2] == 0)
									printf("Division by 0; please choose a different variable:\n");													// Asks for a non-zero variable

							} while ( ae[aeIn2] == 'f' || ( (op == '/' || op == '%') && ae[aeIn2] == 0 ) );												// *Ends loop when the variable chosen is initialized or does not divide by 0
				
							switch (op) {																									// Switches between different operations
								case '+':
									printf("%.2f + %.2f = %.2f\n", ae[aeIn1], ae[aeIn2], ae[aeIn1] + ae[aeIn2]);									// Prints the sum
									break;																												// *break '+'
								case '-':
									printf("%.2f - %.2f = %.2f\n", ae[aeIn1], ae[aeIn2], ae[aeIn1] - ae[aeIn2]);									// Prints the difference
									break;																												// *break '-'
								case '*':
									printf("%.2f * %.2f = %.2f\n", ae[aeIn1], ae[aeIn2], ae[aeIn1] * ae[aeIn2]);									// Prints the product
									break;																												// *break '*'
								case '/':
									printf("%.2f / %.2f = %.2f\n", ae[aeIn1], ae[aeIn2], ae[aeIn1] / ae[aeIn2]);									// Prints the quotient
									break;																												// *break '/'
								case '%':
									printf("%.2f %% %.2f = %.2f\n", ae[aeIn1], ae[aeIn2], fmod(ae[aeIn1], ae[aeIn2]));								// Prints the modulo (remainder)
									break;																												// *break '%'
								case '^':
									printf("%.2f ^ %.2f = %.2f\n", ae[aeIn1], ae[aeIn2], pow(ae[aeIn1], ae[aeIn2]));								// Prints the power
									break;																												// *break '^'
							}																															// *End switch(operation)
							var1 = aeIn1 = op = aeIn2 = var2 = ae[5] = ae[6] = 'f';															// Resets the inputs
							break;																													// *break 'B'

						case 'U':																										// Advanced Unary Operation
							printf("Select an operation ( S(qrt), L(og), E(xponentiation), C(eiling), F(loor) )\n");						// Asks for an operation
							while ( !scanf(" %c", &op) || (op != 'S' && op != 'L' && op != 'E' && op != 'C' && op != 'F') ) {						// Loops until an operation is entered
								if (op == 's' || op == 'l' || op == 'e' || op == 'c' || op == 'f') {													// Checks if the input is lowercase
									op = toupper(op);																										// Changes characters to uppercase
									break;																														// *break out of the loop
								}																																// *End if(lowercase(op))

								printf("Please enter a proper operation:\n");																			// Asks for a non-numeric, operation input
							}																																// *End while(!char || !operation)

							printf("Enter a number or variable (names a-e):\n");															// Asks for a constant or variable
							do {																													// Loops only when there is a possible error (such as an empty variable)
								while ( !scanf("%f", &ae[5]) ) {																						// Loops until a constant or specified variable is chosen
									if ( scanf(" %c", &var1) && (var1 == 'a' || var1 == 'b' || var1 == 'c' || var1 == 'd' || var1 == 'e') )					// Checks if a variable is entered instead of a constant
										break;
									printf("Please enter a character between a to e (lower case):\n");														// Asks for a constant or a variable name between a and e
								}																																// *End while(!var)

								switch (var1) {																								// Switches between different variables
									case 'a':
										aeIn1 = 0;																									// Sets the index = 0 to match variable 'a'
										break;																											// *break 'a'
									case 'b':
										aeIn1 = 1;																									// Sets the index = 1 to match variable 'b'
										break;																											// *break 'b'
									case 'c':
										aeIn1 = 2;																									// Sets the index = 2 to match variable 'c'
										break;																											// *break 'c'
									case 'd':
										aeIn1 = 3;																									// Sets the index = 3 to match variable 'd'
										break;																											// *break 'd'
									case 'e':
										aeIn1 = 4;																									// Sets the index = 4 to match variable 'e'
										break;																											// *break 'e'
									default:
										aeIn1 = 5;																									// Sets the index = 5 to match constant 1
										break;																											// *break default (float input)
								}																														// *End switch(variable name)

								if (ae[aeIn1] == 'f')
									printf("Variable is empty; please choose another one:\n");														// Asks for an initialized variable
								else if (op == 'S' && ae[aeIn1] < 0)
									printf("Cannot square root a negative number; choose another variable:\n");										// Asks for a variable greater than or equal to 0
								else if (op == 'L' && ae[aeIn1] <= 0)
									printf("Cannot log a 0 or negative number; choose another variable:\n");										// Asks for a non-negative variable (excludes 0)

							} while ( ae[aeIn1] == 'f' || (op == 'S' && ae[aeIn1] < 0) || (op == 'L' && ae[aeIn1] <= 0) );								// *Ends loop when the variable chosen is initialized, avoids square root of negative, or avoids log of 0 or less

							switch (op) {																									// Switches between different operations
								case 'S':
									printf("The square root of %.2f is: %.2f\n", ae[aeIn1], sqrt(ae[aeIn1]));										// Prints the square root
									break;																												// *break 'S'
								case 'L':
									printf("The logarithm of %.2f is: %.2f\n", ae[aeIn1], log10(ae[aeIn1]));										// Prints the log base 10
									break;																												// *break 'L'
								case 'E':
									printf("The exponentiation of %.2f is: %.2f\n", ae[aeIn1], exp(ae[aeIn1]));										// Prints the exponentiation (e^ae[aeIn1])
									break;																												// *break 'E'
								case 'C':
									printf("The ceiling of %.2f is: %.2f\n", ae[aeIn1], ceil(ae[aeIn1]));											// Prints the ceiling value
									break;																												// *break 'C'
								case 'F':
									printf("The floor of %.2f is: %.2f\n", ae[aeIn1], floor(ae[aeIn1]));											// Prints the floor value
									break;																												// *break 'F'
							}																															// *End switch(operation)

							op = var1 = aeIn1 = ae[5] = 'f';																				// Resets the inputs
							break;																													// *break 'U'

						case 'X':																										// Exit to main menu
							printf("Going back to the main menu...\n");																		// Indicates a return to the main menu
							break;																													// *break 'X'

						default:																										// Unspecified selection
							printf("Sorry, that selection is unfamiliar.\n");																// Prints an error
							break;																													// *break
					}																																// *End switch(B, U, or X)

					if (toupper(bux) != 'X')																							// Activates when exit isn't selected
						printf("Please select B (Binary Operation), U (Unary Operation), or X (Exit to Main Menu):\n");						// Asks for a selection

				} while ( toupper(bux) != 'X');																							// Keeps looping until 'X' is selected
				bux = 'f';																												// Resets bux selection before ending advanced math operations
				break;																															// *break 'A' or 'a'; advanced math operations

			case 'V':																												// Variables; activates when 'V' or 'v' is entered
				printf("Choose a variable (named characters a-e):\n");																	// Asks for a variable name
				while ( !scanf(" %c", &var1) || (var1 != 'a' && var1 != 'b' && var1 != 'c' && var1 != 'd' && var1 != 'e') ) {					// Loops until a specified variable name is picked
					printf("Please enter a character between a to e (lower case):\n");																// Asks for a variable name between a and e
				}																																		// *End while(!var)

				printf("Enter a number for (%c):\n", var1);																				// Asks for a number for the chosen variable
				switch (var1) {																													// Switches between the 5 variables
					case 'a':
						while (!scanf("%f", &ae[0])) {																								// Loops until a number is entered
							printf("Please enter a proper value:\n");																						// Asks for a non-character value
							scanf(" %c", &buf);																												// Clears the buffer
						}																																		// *End while(e != number)
						printf("Variable (a) = %.2f\n", ae[0]);																						// Prints the designation result
						break;																															// *break 'a'
					case 'b':
						while (!scanf("%f", &ae[1])) {																								// Loops until a number is entered
							printf("Please enter a proper value:\n");																						// Asks for a non-character value
							scanf(" %c", &buf);																												// Clears the buffer
						}																																		// *End while(e != number)
						printf("Variable (b) = %.2f\n", ae[1]);																						// Prints the designation result
						break;																															// *break 'b'
					case 'c':
						while (!scanf("%f", &ae[2])) {																								// Loops until a number is entered
							printf("Please enter a proper value:\n");																						// Asks for a non-character value
							scanf(" %c", &buf);																												// Clears the buffer
						}																																		// *End while(c != number)
						printf("Variable (c) = %.2f\n", ae[2]);																						// Prints the designation result
						break;																															// *break 'c'
					case 'd':
						while (!scanf("%f", &ae[3])) {																								// Loops until a number is entered
							printf("Please enter a proper value:\n");																						// Asks for a non-character value
							scanf(" %c", &buf);																												// Clears the buffer
						}																																		// *End while(d != number)
						printf("Variable (d) = %.2f\n", ae[3]);																						// Prints the designation result
						break;																															// *break 'd'
					case 'e':
						while (!scanf("%f", &ae[4])) {																								// Loops until a number is entered
							printf("Please enter a proper value:\n");																						// Asks for a non-character value
							scanf(" %c", &buf);																												// Clears the buffer
						}																																		// *End while(e != number)
						printf("Variable (e) = %.2f\n", ae[4]);																						// Prints the designation result
						break;																															// *break 'e'
				}																																		// *End switch(variable name)

				var1 = 'f';																												// Resets var1
				break;																															// *break 'V' or 'v'; variables
		
			case 'X':																												// Activates when 'X' or 'x' is entered
				printf("Thank you for using my Simple Calculator. Goodbye!\n");																// Prints an exit thank you message
				break;																															// *break 'X' or 'x'; exit
			
			default:																												// Activates when an unspecified character is entered
				printf("Sorry, that selection is unfamiliar.\n");																		// Prints an error
				break;																															// *break default
		}																																		// *End switch(selection)
		
		if (toupper(sel) != 'X')																									// Activates when a selection other than 'X' or 'x' is chosen
			printf("Please select your option ( B (Binary Operation), U (Unary Operation), A (Advanced), V (Variables), X (Exit) )\n");		// Prints the selection again
		
	} while (toupper(sel) != 'X'); 																												// *End while(!'X' or !'x')
	
}																																				// *End main()