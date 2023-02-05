	.file	"increment.c"
	.text
	.p2align 4
	.globl	increment
	.type	increment, @function
increment:
.LFB0:
	.cfi_startproc
	leal	1(%rdi), %eax
	ret
	.cfi_endproc
.LFE0:
	.size	increment, .-increment
	.ident	"GCC: (GNU) 10.3.0"
	.section	.note.GNU-stack,"",@progbits
