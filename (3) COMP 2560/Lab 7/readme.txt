The program was tested in both Replit and NoMachine environments.
	Replit has a different directory for cc, which can be found using "which cc" in the Replit shell.
	The directory in the program is changed to the default Linux cc directory (/usr/bin/cc) in order to work with Linux machines, hence tested in NoMachine/school computers.
	
When running exec_math_bs.o (i.e ./exec_math_bs), you need to include 3 arguments: the operator, 1st number, 2nd number.
	For the multiplication operator you need an escape character to pass it as an argument (i.e ./exec_math_bs \* 5 2)