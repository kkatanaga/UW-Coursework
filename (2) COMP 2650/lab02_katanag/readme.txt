main.c was compiled using gcc; main.exe should run by itself (open a command prompt) without problems.

In the case of main.exe not working, compile* main.c using:

	gcc -Wall main.c -o main.exe

and run

	./main.exe
	
	or
	
	main.exe

in the command prompt.

*this is assuming the user can compile C programs on their system.