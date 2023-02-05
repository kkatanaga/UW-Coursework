main.c was compiled using gcc (MinGW) in VSCode; main.exe should run by itself (open a command prompt) without problems.

In the case of main.exe not working, compile* main.c by inputting the command:

gcc -Wall main.c arithmetic.c -o main

*this is assuming the user can compile C programs on their system.