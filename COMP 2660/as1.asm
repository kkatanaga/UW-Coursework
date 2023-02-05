TITLE Assignment 1

; Name: Keigo Katanaga
; Date: 2023/01/19
; ID: 	110068805
; Description: 	Does two calculations: x = (y + 4) * 3 and (base 16) 20000h + 30000h - 10000h.
;				Results are saved in the EAX register as shown with DumpRegs

INCLUDE Irvine32.inc
INCLUDELIB Irvine32.lib

; these two lines are only necessary if you're not using Visual Studio
INCLUDELIB kernel32.lib
INCLUDELIB user32.lib

.data
    
	; data declarations go here
	y DWORD 0d
	x DWORD 0d

.code
main PROC
	
	; code goes here
	mov eax,y
	add eax,4

	mov ebx,eax
	add eax,ebx
	add eax,ebx
	mov ebx,0

	mov [x],eax

	call DumpRegs ; displays registers in console

	mov eax,20000h
	add eax,30000h
	sub eax,10000h

	call DumpRegs

	exit

main ENDP
END main
