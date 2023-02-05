Program is written using Visual Studio Code and compiled and run with Easy-MASM. System used is Windows 10.
Using the terminal in the .../easy-masm/ directory, run:
	.\run as2
	
Expected output: 2 outputs of DumpRegs
	First DumpRegs prints the resulting fib(7) in eax and ebx.
	I was unsure of what the instructions wanted so I made 2 versions for the fibonacci sequence.
		First fib(7) is saved in eax which directly solves for it.
		Second fib(7) is saved in ebx and saves every sequence leading up to 7 in an array.
	
	Second DumpRegs prints the total sum of the given array. The result is in eax.