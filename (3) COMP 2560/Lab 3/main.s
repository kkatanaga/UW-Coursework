	.file	"main.c"
	.text
	.section	.rodata.str1.1,"aMS",@progbits,1
.LC0:
	.string	"%i"
.LC1:
	.string	"%i \n"
	.section	.text.startup,"ax",@progbits
	.p2align 4
	.globl	main
	.type	main, @function
main:
.LFB23:
	.cfi_startproc
	subq	$24, %rsp
	.cfi_def_cfa_offset 32
	leaq	.LC0(%rip), %rdi
	movq	%fs:40, %rax
	movq	%rax, 8(%rsp)
	xorl	%eax, %eax
	leaq	4(%rsp), %rsi
	call	__isoc99_scanf@PLT
	movl	4(%rsp), %edi
	call	increment@PLT
	leaq	.LC1(%rip), %rsi
	movl	$1, %edi
	movl	%eax, 4(%rsp)
	movl	%eax, %edx
	xorl	%eax, %eax
	call	__printf_chk@PLT
	movq	8(%rsp), %rax
	subq	%fs:40, %rax
	jne	.L5
	xorl	%eax, %eax
	addq	$24, %rsp
	.cfi_remember_state
	.cfi_def_cfa_offset 8
	ret
.L5:
	.cfi_restore_state
	call	__stack_chk_fail@PLT
	.cfi_endproc
.LFE23:
	.size	main, .-main
	.ident	"GCC: (GNU) 10.3.0"
	.section	.note.GNU-stack,"",@progbits
