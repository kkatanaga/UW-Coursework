#include <stdio.h>

int main(void)
{
	int num1;												// Container for the 1st number
	int num2;												// Container for the 2nd number
	int rslt;												// Container for the result
	
	printf("Keigo Katanaga,\n110068805,\n");				// Prints name and student id in separate lines
	
	printf("Enter first number: ");							// Asks for the first number
 	scanf("%d", &num1);										// Takes input as num1
	
 	printf("Enter second number: ");						// Asks for the second number
 	scanf("%d", &num2);										// Takes input as num2
 	
 	printf("-----------------------------------------\n");	// Prints top border
 	printf("|\t\t\t\t\t|\n");								// Prints space between the top border and the formulas
	
 	rslt = num1 + num2;										// Calculates the sum
 	printf("|\t\t%d + %d = %d\t\t|\n", num1, num2, rslt);	// Prints the formula and the sum
	
 	rslt = num1 - num2;										// Calculates the difference
 	printf("|\t\t%d - %d = %d\t\t|\n", num1, num2, rslt);	// Prints the formula and the difference
	
 	rslt = num1 * num2;										// Calculates the product
 	printf("|\t\t%d * %d = %d\t\t|\n", num1, num2, rslt);	// Prints the formula and the product
	
 	rslt = num1 / num2;										// Calculates the quotient
 	printf("|\t\t%d / %d = %d\t\t|\n", num1, num2, rslt);	// Prints the formula and the quotient
	
 	printf("|\t\t\t\t\t|\n");								// Prints space bewteen the formulas and the bottom border
 	printf("-----------------------------------------\n");	// Prints the bottom border
}
