	.file	"increment.c"
	.text
	.p2align 4
	.globl	increment
	.type	increment, @function
increment:
.LFB0:
	.cfi_startproc
    pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movl	%edi, -20(%rbp)
	movl	-20(%rbp), %eax
	movl	%eax, -4(%rbp)
	incl	-4(%rbp)
	movl	-4(%rbp), %eax
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE0:
	.size	increment, .-increment
	.ident	"GCC: (GNU) 10.3.0"
	.section	.note.GNU-stack,"",@progbits
