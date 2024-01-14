TITLE Assignment 5

; Name: Keigo Katanaga
; Date: 2023/03/18
; ID: 	110068805
; Description: Contains 9 programs in 9 separate procedures
; Notable procedures:
;		a1P56 - Activity 1 Part 5/6 Code
;		a1P7  - Activity 1 Part 7 Code
;		a2PA  - Activity 2 Part A Code
;		...
;		a2PF  - Activity 2 Part F Code
;		a3 	  - Activity 3 Code
; The section after these procedures are testing procedures which 
; 	send inputs to the procedures mentioned above. They are free
;	to be modified for further testin.
; The rest can be ignored as they are mainly for printing outputs

INCLUDE Irvine32.inc
INCLUDELIB Irvine32.lib

INCLUDELIB kernel32.lib
INCLUDELIB user32.lib

.data
	; Activity 1 Part 5/6 data
    array1 SWORD 1,2,3
	sentinel1 SWORD 0
	message1a BYTE "Activity 1 Part 5/6 Result: ", 0

	; Activity 1 Part 7 data
    array2 SWORD 0,0,3,2,1
	sentinel2 SWORD 0
	message1b BYTE "Activity 1 Part 7 Result: ", 0

	; Activity 2 Part A-F data
	var1 SDWORD 0h
	var2 SDWORD 0h
	var3 SDWORD 0h
	var4 SDWORD 0h

	message3 BYTE "Activity 2 Part A Results:", 0
	message3a BYTE "(var1 < var2)", 0
	message3b BYTE "(var1 == var2)", 0
	message3c BYTE "(var1 > var2)", 0
	msgVar1 BYTE "var1 = ", 0
	msgVar2 BYTE "var2 = ", 0
	msgVar3 BYTE "var3 = ", 0
	msgVar4 BYTE "var4 = ", 0

	message4 BYTE "Activity 2 Part B Results:", 0
	message4a BYTE "(op1 < op2)", 0
	message4b BYTE "(op1 == op2)", 0
	message4c BYTE "(op1 > op2)", 0

	message5 BYTE "Activity 2 Part C Results:", 0
	message5a BYTE "(X < Y) ", 0
	message5b BYTE "(X == Y) ", 0
	message5c BYTE "(op1 != op2) ", 0
	msgR1 BYTE "Executed Routine 1...", 0
	msgR2 BYTE "Executed Routine 2...", 0
	msgR3 BYTE "Executed Routine 3...", 0

	msgOp1 BYTE "op1 = ", 0
	msgOp2 BYTE "op2 = ", 0
	msgX BYTE "X = ", 0
	msgY BYTE "Y = ", 0
	space BYTE " ", 0

	message6 BYTE "Activity 2 Part D Results:", 0
	message6a BYTE "(ebx < val1):", 0
	message6b BYTE "(ebx == val1)", 0
	message6c BYTE "(ebx > val1)", 0
	msgBef BYTE "Before: ", 0
	msgAft BYTE "After: ", 0
	msgEbx BYTE "ebx = ", 0
	msgVal1 BYTE "val1 = ", 0

	X DWORD 0
	op1 DWORD 2
	op2 DWORD 4
	op3 DWORD 5
	message7 BYTE "Activity 2 Part E Results:", 0
	msgOp3 BYTE "op3 = ", 0

	message8 BYTE "Activity 2 Part F Results:", 0
	message8a BYTE "(edx > eax)", 0
	message8b BYTE "(ebx > ecx AND ebx > edx)", 0
	message8c BYTE "(ebx <= ecx AND ebx > edx)", 0
	message8d BYTE "(ebx > ecx AND ebx <= edx)", 0
	msgEax BYTE "eax = ", 0
	msgEcx BYTE "ecx = ", 0
	msgEdx BYTE "edx = ", 0

	; Activity 3 data
	message9 BYTE "Activity 3 Results:", 0
	message9a BYTE "Inputs: Array = 1,2,3,4,5,6,7,8 Sample = 2", 0
	message9b BYTE "Inputs: Array = 9,2,-3,4,1,-5,11,-2 Sample = -1", 0
	array3 DWORD 8 DUP (?)
	sample DWORD 0h
	sum DWORD 0h
	msgSum BYTE "Sum = ", 0

.code


main PROC
	call Clrscr

	call a1P56	; Start Activity 1 Part 5/6 code
	call WaitNext

	call a1P7	; Start Activity 1 Part 7 code
	call WaitNext

	call a2PA	; Start Activity 2 Part A code
	call WaitNext

	call a2PB	; Start Activity 2 Part B code
	call WaitNext

	call a2PC	; Start Activity 2 Part C code
	call WaitNext

	call a2PD	; Start Activity 2 Part D code
	call WaitNext

	call a2PE	; Start Activity 2 Part E code
	call WaitNext

	call a2PF	; Start Activity 2 Part F code
	call WaitNext

	call a3		; Start Activity 3 code
	call WaitNext

	jmp endMain

	; Activity 1 Part 5/6 code
	a1P56 PROC
		mov esi,OFFSET array1
		mov ecx,LENGTHOF array1
		L1: test WORD PTR [esi],8000h
			pushfd
			add esi,TYPE array1
			popfd
			loopnz L1

		call a1P56Res
		ret
	a1P56 ENDP

	; Activity 1 Part 7 code
	a1P7 PROC
		mov esi,OFFSET array2
		mov ecx,LENGTHOF array2
		L2: test WORD PTR [esi],0FFFFh	; changed bit mask to 0FFFFh
			pushfd
			add esi,TYPE array2
			popfd
			loopz L2					; loops when test is clear (ZF = 1)
		sub esi,TYPE array2				; goes back to the previous element

		call a1P7Res					; print results
		ret
	a1P7 ENDP

	; Activity 2 Part A code
	a2PA PROC
		call a2PATests				; run test cases
		jmp finally
		a2PACode PROC				; actual code for the activity
			mov eax, var1
			cmp eax, var2
			jg greaterThan			; jump to else (var1 > var2)
			mov var3, 10			; if (var1 <= var2)
			jmp then				; skip else
			greaterThan:			; else (var1 > var2)
				mov var3, 6	
				mov var4, 7
			then:
			call a2PARes			; print results
			ret
		a2PACode ENDP
		finally:
		ret
	a2PA ENDP

	; Activity 2 Part B code
	a2PB PROC
		call a2PBTests				; run test cases
		jmp finally
		a2PBCode PROC				; actual code for the activity
			mov eax, var1
			cmp eax, var2
			jne inequal				; jump to else (var1 != var2)
			mov var3, 1h			; if (var1 == var2)
			jmp then				; skip else
			inequal:				; else (var1 == var2)
				mov var3, 2h
			then:
			call a2PBRes			; print results
			ret
		a2PBCode ENDP
		finally:
		ret
	a2PB ENDP

	; Activity 2 Part C code
	a2PC PROC
		call a2PCTests				; run test cases
		jmp finally
		a2PCCode PROC				; actual code for the activity
			mov eax, var1
			cmp eax, var2
			jne inequal				; jump to else (op1 != op2)
			mov eax, var3			; if (op1 == op2)
			cmp eax, var4
			jle lessEq					; jump to else (X <= Y)
			call Routine1				; if (X > Y)
			jmp then					; skip to the end
			lessEq:						; else (X <= Y)
				call Routine2
				jmp then				; skip to the end
			inequal:				; else (op1 != op2)
				call Routine3
			then:
			call a2PCRes			; print results
			ret
		a2PCCode ENDP
		finally:
		ret
	a2PC ENDP

	; Activity 2 Part D code
	a2PD PROC
		call a2PDTest				; run test cases
		jmp finally
		a2PDCode PROC				; actual code for the activity
			call a2PDResB			; print initial state
			cmp ebx, var1
			jg then					; skip loop if ebx > val1
			whileLE:				; while (ebx <= val1)
				add ebx, 5
				sub var1, 1
				cmp ebx, var1
				jle whileLE			; loop when ebx <= val1
			then:
			call a2PDResA			; print final state
			ret
		a2PDCode ENDP
		finally:
		ret
	a2PD ENDP

	; Activity 2 Part E code
	a2PE PROC
		call a2PETest				; run single test case
		jmp finally
		a2PECode PROC				; actual code for the activity
			call a2PEResB			; print initial state
			mov eax, op1
			cmp eax, op2
			jae then				; skip loop if op1 >= op2
			whileE:
				inc op1
				mov eax, op1
				cmp eax, op3
				jne inequal			; jump to else (op1 != op2)
				mov X, 2			; if (op1 == op2)
				jmp after			; skip else
				inequal:			; else (op1 != op2)
					mov X, 3
				after:
				mov eax, op1
				cmp eax, op2
				jb whileE			; continue loop if op1 < op2
			then:
			call a2PEResA			; print final state
			ret
		a2PECode ENDP
		finally:
		ret
	a2PE ENDP

	; Activity 2 Part F code
	a2PF PROC
		call a2PFTest
		jmp finally
		a2PFCode PROC
			call a2PFRegs
			call a2PFXB
			; (edx > eax) OR (ebx > ecx AND ebx > edx)
			cmp edx, eax
			jg success				; jump to success if (edx > eax)
			cmp ebx, ecx			; otherwise check AND operands
			jle fail				; jump to fail if (ebx <= ecx)
			cmp ebx, edx			; otherwise check other operand
			jle fail				; jump to fail if (ebx <= edx)
			success:				; otherwise success
				mov X, 1
				jmp then			; skip fail
			fail:
				mov X, 2
			then:
			call a2PFXA
			ret
		a2PFCode ENDP
		finally:
		ret
	a2PF ENDP

	a3 PROC
		call a3Test
		jmp finally
		a3Code PROC
			mov eax, 0
			mov edx, sample
			mov ecx, LENGTHOF array3
			mov esi, 0

			cmp esi, ecx
			jge L5							; skip loop when esi >= ecx
			L1:								; loop when esi < ecx
				L2:
					cmp array3[esi*TYPE DWORD], edx
					jle L4					; jump to after if when array[esi] <= edx
				L3:							; if (array[esi] > edx)
					add eax, array3[esi*TYPE DWORD]
				L4:							; after if
					inc esi
				cmp esi, ecx
				jl L1						; exit loop when esi >= ecx
			L5:
				mov sum, eax
			call a3Res
			ret
		a3Code ENDP
		finally:
		ret
	a3 ENDP

	endMain:
	exit

main ENDP

;============================================================================================

; Test procedures. Feel free to change the inputs for further testing
; Tests for Activity 2 Part A
a2PATests PROC
	mov edx, OFFSET message3
	call WriteString
	call Crlf

	mov edx, OFFSET message3a
	call WriteString
	call Crlf
	mov var1, 2
	mov var2, 3
	mov var3, -1
	mov var4, -1
	call a2PACode	; var1 < var2

	mov edx, OFFSET message3b
	call WriteString
	call Crlf
	mov var1, -3
	mov var2, -3
	mov var3, -1
	mov var4, -1
	call a2PACode	; var1 == var2

	mov edx, OFFSET message3c
	call WriteString
	call Crlf
	mov var1, 6
	mov var2, -2
	mov var3, -1
	mov var4, -1
	call a2PACode	; var1 > var2

	ret
a2PATests ENDP

; Tests for Activity 2 Part B
a2PBTests PROC
	mov edx, OFFSET message4
	call WriteString
	call Crlf

	mov edx, OFFSET message4a
	call WriteString
	call Crlf
	mov var1, -5
	mov var2, 5
	mov var3, 0
	call a2PBCode	; op1 < op2

	mov edx, OFFSET message4b
	call WriteString
	call Crlf
	mov var1, 3
	mov var2, 3
	mov var3, 0
	call a2PBCode	; op1 == op2

	mov edx, OFFSET message4c
	call WriteString
	call Crlf
	mov var1, 15
	mov var2, -2
	mov var3, 0
	call a2PBCode	; op1 > op2
	
	ret
a2PBTests ENDP

; Tests for Activity 2 Part C
a2PCTests PROC
	mov edx, OFFSET message5
	call WriteString
	call Crlf

	mov edx, OFFSET message5a
	call WriteString
	mov var1, 10
	mov var2, 10
	mov var3, 30
	mov var4, 40
	call a2PCCode

	mov edx, OFFSET message5b
	call WriteString
	mov var1, 10
	mov var2, 10
	mov var3, 40
	mov var4, 30
	call a2PCCode

	mov edx, OFFSET message5c
	call WriteString
	mov var1, 10
	mov var2, 20
	mov var3, 30
	mov var4, 40
	call a2PCCode

	ret
a2PCTests ENDP

; Tests for Activity 2 Part D
a2PDTest PROC
	mov edx, OFFSET message6
	call WriteString
	call Crlf

	mov edx, OFFSET message6a
	call WriteString
	call Crlf
	mov ebx, 3
	mov var1, 6
	call a2PDCode

	mov edx, OFFSET message6b
	call WriteString
	call Crlf
	mov ebx, 4
	mov var1, 4
	call a2PDCode

	mov edx, OFFSET message6c
	call WriteString
	call Crlf
	mov ebx, 10
	mov var1, 9
	call a2PDCode

	ret
a2PDTest ENDP

; Tests for Activity 2 Part E
a2PETest PROC
	mov edx, OFFSET message7
	call WriteString
	call Crlf

	call a2PECode

	ret
a2PETest ENDP

; Tests for Activity 2 Part F
a2PFTest PROC
	mov edx, OFFSET message8
	call WriteString
	call Crlf

	mov eax, 1
	mov ebx, 1
	mov ecx, 1
	mov edx, 4
	mov X, 0
	pushad
	mov edx, OFFSET message8a
	call WriteString
	call Crlf
	popad
	call a2PFCode

	mov eax, 0
	mov ebx, 5
	mov ecx, 3
	mov edx, -4
	mov X, 0
	pushad
	mov edx, OFFSET message8b
	call WriteString
	call Crlf
	popad
	call a2PFCode

	mov eax, -3
	mov ebx, 4
	mov ecx, 5
	mov edx, -3
	mov X, 0
	pushad
	mov edx, OFFSET message8c
	call WriteString
	call Crlf
	popad
	call a2PFCode

	mov eax, 6
	mov ebx, 4
	mov ecx, -1
	mov edx, 5
	mov X, 0
	pushad
	mov edx, OFFSET message8d
	call WriteString
	call Crlf
	popad
	call a2PFCode

	ret
a2PFTest ENDP

; Tests for Activity 3
a3Test PROC
	mov edx, OFFSET message9
	call WriteString
	call Crlf

	mov ecx, LENGTHOF array3
	mov esi, 0h
	mov eax, 1h
	increasing:
		mov array3[esi], eax
		inc eax
		add esi, TYPE DWORD
		loop increasing
	
	mov sample, 2
	mov edx, OFFSET message9a
	call WriteString
	call Crlf
	call a3Code

	mov esi, 0h
	mov array3[esi], 9h

	add esi, TYPE DWORD
	mov array3[esi], 2h

	add esi, TYPE DWORD
	mov array3[esi], -3h

	add esi, TYPE DWORD
	mov array3[esi], 4h

	add esi, TYPE DWORD
	mov array3[esi], 1h

	add esi, TYPE DWORD
	mov array3[esi], -5h

	add esi, TYPE DWORD
	mov array3[esi], 11d

	add esi, TYPE DWORD
	mov array3[esi], -2h

	mov sample, -1
	mov edx, OFFSET message9b
	call WriteString
	call Crlf
	call a3Code

	ret
a3Test ENDP

;============================================================================================

; dummy routines
Routine1 PROC USES edx
	mov edx, OFFSET msgR1
	call WriteString
	call Crlf
	ret
Routine1 ENDP
Routine2 PROC USES edx
	mov edx, OFFSET msgR2
	call WriteString
	call Crlf
	ret
Routine2 ENDP
Routine3 PROC USES edx
	mov edx, OFFSET msgR3
	call WriteString
	call Crlf
	ret
Routine3 ENDP

; Ignore these. They are used for printing results
WaitNext PROC
	call WaitMsg
	call Clrscr
	ret
WaitNext ENDP

WriteVarln PROC
	call WriteString
	call WriteInt
	call Crlf
	ret
WriteVarln ENDP

WriteVar PROC
	call WriteString
	call WriteInt
	call WriteSpace
	ret
WriteVar ENDP

WriteSpace PROC USES edx
	mov edx, OFFSET space
	call WriteString
	ret
WriteSpace ENDP

WriteStringln PROC
	call WriteString
	call Crlf
	ret
WriteStringln ENDP

a1P56Res PROC
	mov edx, OFFSET message1a
	mov eax, 0
	mov ax, [esi]
	call WriteVarln
	call Crlf
	ret
a1P56Res ENDP

a1P7Res PROC
	mov edx, OFFSET message1b
	mov eax, 0
	mov ax, [esi]
	call WriteVarln
	call Crlf
	ret
a1P7Res ENDP

a2PARes PROC
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgVar1
	mov eax, var1
	call WriteVar

	mov edx, OFFSET msgVar2
	mov eax, var2
	call WriteVarln

	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgVar3
	mov eax, var3
	call WriteVar

	mov edx, OFFSET msgVar4
	mov eax, var4
	call WriteVarln

	call Crlf

	ret
a2PARes ENDP

a2PBRes PROC
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgOp1
	mov eax, var1
	call WriteVar

	mov edx, OFFSET msgOp2
	mov eax, var2
	call WriteVarln

	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgX
	mov eax, var3
	call WriteVarln

	call Crlf

	ret
a2PBRes ENDP

a2PCRes PROC
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgOp1
	mov eax, var1
	call WriteVar

	mov edx, OFFSET msgOp2
	mov eax, var2
	call WriteVarln

	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgX
	mov eax, var3
	call WriteVar

	mov edx, OFFSET msgY
	mov eax, var4
	call WriteVarln

	call Crlf

	ret

a2PCRes ENDP

a2PDResB PROC	; ebx & val1 before
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgBef
	call WriteString
	call a2PDRes
	ret
a2PDResB ENDP
a2PDResA PROC	; ebx & val1 after
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgAft
	call WriteString
	call a2PDRes
	call Crlf
	ret
a2PDResA ENDP
a2PDRes PROC	; variables
	mov edx, OFFSET msgEbx
	mov eax, ebx
	call WriteVar

	mov edx, OFFSET msgVal1
	mov eax, var1
	call WriteVarln

	ret
a2PDRes ENDP

a2PEResB PROC	; op1, op2, op3, and X before
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgBef
	call WriteString
	call a2PERes
	ret
a2PEResB ENDP
a2PEResA PROC	; op1, op2, op3, and X after
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgAft
	call WriteString
	call a2PERes
	call Crlf
	ret
a2PEResA ENDP
a2PERes PROC	; print variables
	mov edx, OFFSET msgOp1
	mov eax, op1
	call WriteVar

	mov edx, OFFSET msgOp2
	mov eax, op2
	call WriteVar

	mov edx, OFFSET msgOp3
	mov eax, op3
	call WriteVar

	mov edx, OFFSET msgX
	mov eax, X
	call WriteVarln

	ret
a2PERes ENDP

a2PFXB PROC USES eax edx
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgBef
	call WriteString
	mov edx, OFFSET msgX
	mov eax, X
	call WriteVarln

	ret
a2PFXB ENDP
a2PFXA PROC USES eax edx
	call WriteSpace
	call WriteSpace
	mov edx, OFFSET msgAft
	call WriteString
	mov edx, OFFSET msgX
	mov eax, X
	call WriteVarln
	call Crlf

	ret
a2PFXA ENDP
a2PFRegs PROC
	pushad
	call WriteSpace
	call WriteSpace
	popad

	pushad
	mov edx, OFFSET msgEax
	call WriteVar
	popad

	pushad
	mov eax, ebx
	mov edx, OFFSET msgEbx
	call WriteVar
	popad
	
	pushad
	mov eax, ecx
	mov edx, OFFSET msgEcx
	call WriteVar
	popad

	pushad
	mov eax, edx
	mov edx, OFFSET msgEdx
	call WriteVarln
	popad

	ret
a2PFRegs ENDP

a3Res PROC
	mov eax, Sum
	mov edx, OFFSET msgSum
	call WriteVarln
	call Crlf
	ret
a3Res ENDP

END main