TITLE Assignment 5

; Name: Keigo Katanaga
; Date: 2023/03/25
; ID: 	110068805
; Description: Contains 9 programs in 9 separate procedures
; Notable procedures:
;		Act5Div - Code from Activity 5
;		Act5DivRem - Modified code with the modulo
; Procedures after main and the procedures mentioned above are for printing results and can be ignored

INCLUDE Irvine32.inc
INCLUDELIB Irvine32.lib

INCLUDELIB kernel32.lib
INCLUDELIB user32.lib

.data
	; Activity 1 data
	array DWORD 648B2165h,8C943A29h,6DFA4B86h,91F76C04h,8BAF9857h

	; Activity 3 data
	val1 QWORD 0h
	val2 QWORD 0h
	result QWORD 0

	; Activity 5 data
	var1 DWORD 0h
	var2 DWORD 0h
	var4 DWORD 0h

	varS1 SDWORD 0h
	varS2 SDWORD 0h
	varS3 SDWORD 0h
	varS4 SDWORD 0h

	notANumber BYTE "NaN", 0
	act1Msg BYTE "Activity 1", 0
	act2Msg BYTE "Activity 2", 0
	act3Msg BYTE "Activity 3", 0
	act4Msg BYTE "Activity 4", 0
	act5Msg BYTE "Activity 5", 0
.code
main PROC
	call Clrscr

	; Activity 1 code (Shifts left)
	mov edx, OFFSET act1Msg
	call WriteAct

	mov bl,4 ; shift count
	mov esi,OFFSET array ; offset of the array
	mov ecx,(LENGTHOF array) ; number of array elements
	dec ecx
	
	L1: push ecx ; save loop counter
		mov eax,[esi + TYPE DWORD]

		push eax
		mov eax, [esi]
		call WriteHex
		call Crlf
		call WriteBin			; print element before the shift
		call Crlf
		pop eax

		mov cl,bl 				; shift count
		shld [esi],eax,cl 			; shift EAX into high bits of [ESI]
		call DumpRegs			; show registers after shrd

		push eax
		mov eax, [esi]
		call WriteBin			; print element after the shift
		call Crlf
		call WriteHex
		call Crlf
		pop eax

		add esi,TYPE DWORD 		; point to next doubleword pair
		pop ecx 				; restore loop counter
		loop L1

	push eax
	mov eax, [esi]
	call WriteHex
	call Crlf
	call WriteBin				; print last element before the shift
	call Crlf
	pop eax

	shl DWORD PTR [esi],1 			; shift the last doubleword
	call DumpRegs				; show registers after shr

	push eax
	mov eax, [esi]
	call WriteBin				; print last element after the shift
	call Crlf
	call WriteHex
	call Crlf
	pop eax

	call WaitNext

	; Activity 2
	mov edx, OFFSET act2Msg
	call WriteAct

	mov eax, 0

	mov al,0D4h
	call WriteBin		; Base value
	call Crlf
	shr al,1
	call WriteBin		; a value
	call Crlf

	mov al,0D4h
	sar al,1
	call WriteBin		; b value
	call Crlf

	mov al,0D4h
	sar al,4
	call WriteBin		; c value
	call Crlf

	mov al,0D4h
	rol al,1
	call WriteBin		; d value
	call Crlf

	call WaitNext

	; Activity 3
	mov edx, OFFSET act3Msg
	call WriteAct

	; Original values from lab 7:
	; val1 = 20403004362047A1h
	; val2 = 055210304A2630B2h
	; result = 1BEE20D4ECFA17EF
		; Modify these values to change val1 and val2
	mov DWORD PTR [val1+TYPE DWORD], -8E92BA32h		; Left part of val1
	mov DWORD PTR [val1], 0C827A821h				; Right part of val1
	call WriteVal1		; val1 = 716D45CEC827A821h

	mov DWORD PTR [val2+TYPE DWORD], 991E0D87h		; Left part of val2
	mov DWORD PTR [val2], 12345678h					; Right part of val2
	call WriteVal2		; val2 = 991E0D8712345678

	mov esi, OFFSET val1 ; set index to start
	mov edi,OFFSET val2
	clc ; clear Carry flag
	mov ecx,8 ; loop counter
	top:
		mov al,BYTE PTR[esi] ; get first number
		sub al,BYTE PTR[edi] ; subtract second
		mov BYTE PTR[esi],al ; store the result
		inc esi
		inc edi
		loop top

	call WriteVal1		; result = D84F3847B6F352A9h
						; correct result = D84F3847B5F35000h

	call WaitNext

	; Activity 4
	mov edx, OFFSET act4Msg
	call WriteAct

	mov dx,0
	mov ax,222h
	mov cx,100h
	mul cx
	call DumpRegs

	mov ax,63h
	mov bl,10h
	div bl
	call DumpRegs

	call WaitNext

	; Activity 5
	mov edx, OFFSET act5Msg
	call WriteAct

	mov var1, 5h
	mov var2, 2h
	call Act5Div		; var1 = 5h, var2 = 2h

	mov var1, 43h
	mov var2, 22h
	call Act5Div		; var1 = 43h, var2 = 22h

	call Crlf

	; Activity 5 Q2
	mov varS1, -5h
	mov varS2, 2h
	mov varS3, 26h
	call Act5DivRem		; varS1 = -5h, varS2 = 2h, varS3 = 26h

	mov varS1, 43h
	mov varS2, -22h
	mov varS3, -5h
	call Act5DivRem		; varS1 = 43h, varS2 = -22h, varS3 = -5h

	call WaitNext
	exit
main ENDP

Act5Div PROC USES eax ebx
	mov eax,var1 		; left side
	mov ebx,5
	mul ebx 			; EDX:EAX = product

	mov ebx,var2 		; right side
	sub ebx,3

	div ebx 			; final division
	mov var4,eax
	call DumpRegs		; show quotient (eax) and remainder (edx)
	ret
Act5Div ENDP

Act5DivRem PROC USES eax ebx edx
	mov eax, 0h
	mov edx, 0h
	mov eax,varS1 		; left side
	mov ebx,-5h
	imul ebx 			; EDX:EAX = product
	push edx			; save left side product
	push eax

	mov edx, 0h
	mov eax,varS2 		; right side
	neg eax
	cdq
	mov ebx,varS3
	idiv ebx			; EDX = remainder
	mov ebx, edx		; move remainder
	cmp ebx, 0h
	je divByZero
	
	pop eax
	pop edx				; EDX:EAX = left side product
	cdq
	idiv ebx 			; final division
	mov varS4,eax

	call DumpRegs		; show quotient (eax) and remainder (edx)
	jmp finally
	divByZero:
		call WriteNan
	finally:
	ret
Act5DivRem ENDP

;============================================================================================

;============================================================================================

; Ignore these. These are used for printing results
WriteVal1 PROC USES eax
	mov eax, DWORD PTR [val1+TYPE DWORD]			; print left half of val1
	call WriteHex
	mov eax, DWORD PTR val1							; print right half of val1
	call WriteHex
	call Crlf
	ret
WriteVal1 ENDP

WriteVal2 PROC USES eax
	mov eax, DWORD PTR [val2+TYPE DWORD]			; print left half of val2
	call WriteHex
	mov eax, DWORD PTR val2							; print right half of val2
	call WriteHex
	call Crlf
	ret
WriteVal2 ENDP

WriteAct PROC
	call Clrscr
	call WriteString
	call Crlf
	ret
WriteAct ENDP

WriteNan PROC USES edx
	mov edx, OFFSET notANumber
	call WriteString
	call Crlf
	ret
WriteNan ENDP

WaitNext PROC
	call Crlf
	call WaitMsg
	call Clrscr
	ret
WaitNext ENDP

END main