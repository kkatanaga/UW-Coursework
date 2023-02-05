#include <stdio.h>		// C Standard Library for input/output functions

int main()
{
	// integer variables num1, num2, and result
	// character variable for operation
	char selection; // variable for the selection of operation groups
	
	puts("Welcome to the Command-Line Calculator (CLC)!");
	puts("Developer: Keigo Katanaga");
	puts("Version: 1.0");
	puts("Date: 10/06/2021");
	puts("--------------------------------------------------");
	puts("Select one of the following items:");
	puts("B) - Binary Mathematical Operations such as addition and subtraction.");
	puts("U) - Unary Mathematical Operations, such as square root, and log.");
	puts("A) - Advanced Mathematical Operations, using variables, arrays.");
	puts("V) – Define variables and assign them values.");
	puts("X) – Exit");
	scanf("%c", &selection);						// Asks for an option to be selected
	printf("You've chosen %c\n", selection); 		// Prints the selected option
	
	/*
	Loop (until the item “X” is selected)
		Read (whatever is selected by the user)
		If (B is selected), then
			Print (“Enter the 1st number:”)
			Read (num1)

			Print (“Select an operation ( + , - , * , /, %, ^)”)
			Read (op); 

			Print (“Enter the 2nd number:”)
			Read (num2)
			If (+ is selected), then result = num1 + num2;
			Else if (- is selected), then result = num1 – num2;
			Else if (* is selected), then result = num1*num2;
			Else if (/ is selected), then result = num1/num2;
			Else if (% is selected), then result = num1%num2;
			Else if (^ is selected), then result = num1^num2;
		
			Print (“The result is: ” + result)
			Print (“Please select your option ( B , U , A , E , V, X )”)

		Else if (X is selected), then
			Print (“Thank you for using CLC. Goodbye!”)

		Else, 
			Print (“Sorry, that selection is currently not available or unfamiliar.”)
			Print (“Please select your option ( B , U , A , E , V, X )”)

		(Reset all variables used)
		
	End of the loop, when X is selected; end of program
	*/
	
}
