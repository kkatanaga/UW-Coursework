main was compiled by following the instructions of the lab.

increment_2 is the actual resulting .s file from using the compiler in Replit,
	different from the increment.s in lab03_hfani.zip
		In Replit terminal:
			cc increment.c -S
		then increment.s is renamed to increment_2.s
	
From this, increment_2.s only has 1 line between .cfi_startproc and ret:
	leal	1(%rdi), %eax
but increment.s, which is copied from lab03_hfani.zip then optimized as instructed, 
	has 12 lines between .cfi_startproc and ret

I wasn't sure if increment_2.s is in the most efficiently written code thanks to 
	Replit's IDE or it's just a difference in compilers, so I included it in a 
	separate folder. increment.s is what is used to link to main.s