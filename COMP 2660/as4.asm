TITLE Assignment 4

; Name: Keigo Katanaga
; Date: 2023/03/11
; ID: 	110068805
; Description: Contains 3 programs in 3 separate procedures
; Notable procedures:
;		ProgramOne - GetLarger
;		ProgramTwo - GetSmallest
;		ProgramTri - ToLower
; The rest can be ignored as they are mainly for printing outputs

INCLUDE Irvine32.inc
INCLUDELIB Irvine32.lib

; these two lines are only necessary if you're not using Visual Studio
INCLUDELIB kernel32.lib
INCLUDELIB user32.lib

.data
	hexSign BYTE "h", 0
	separator BYTE ", ", 0
	tempByte BYTE ?, 0

	; Program 1 data
	p1Msg1 BYTE "Running program 1...", 0
	p1Msg2 BYTE "Look at EDX for the larger value of EAX and EBX", 0

	; Program 2 data
	p2Msg1 BYTE "Running program 2...", 0
	p2Msg2 BYTE "Look at AX for the smallest value of v1, v2, and v3", 0

	v1 WORD 0h
	v2 WORD 0h
	v3 WORD 0h

	v1Name BYTE "v1 = ", 0
	v2Name BYTE "v2 = ", 0
	v3Name BYTE "v3 = ", 0

	; Program 3 data
	p3Msg1 BYTE "Running program 3...", 0
	p3Msg2 BYTE "Look at AL for the letter case before and after the program", 0

	lowerAVar BYTE "a = ", 0
	upperAVar BYTE "A = ", 0
	lowerZVar BYTE "z = ", 0
	upperZVar BYTE "Z = ", 0

	beforeMsg BYTE "Before: ", 0
	afterMsg BYTE "After: ", 0
.code


main PROC
	call Clrscr

	; Call the 3 program assignments in sequence
	call programOne
	call WaitMsg
	call Clrscr

	call programTwo
	call WaitMsg
	call Clrscr

	call programTri
	call WaitMsg
	call Clrscr

	exit

main ENDP

; Program 1 code
programOne PROC
	mov edx, OFFSET p1Msg1
	call WriteMessage
	mov edx, OFFSET p1Msg2
	call WriteMessage

	; eax < ebx
	mov eax, 1111h
	mov ebx, 2222h
	; expect edx = 2222h
	call GetLarger

	; eax > ebx
	mov eax, 4444h
	mov ebx, 3333h
	; expect edx = 4444h
	call GetLarger

	; eax == ebx
	mov eax, 5555h
	mov ebx, 5555h
	; expect edx = 5555h
	call GetLarger

	ret								; return to main

	; input: eax, ebx
	; output: edx
	GetLarger PROC
		cmp eax, ebx
		jbe leq			; if eax <= ebx, then ebx is the larger value
		mov edx, eax	; else (eax > ebx) eax is the larger value
		jmp finally

		leq:
			mov edx, ebx

		finally:
			call DumpRegs
			ret
	GetLarger ENDP
programOne ENDP

; Program 2 code
programTwo PROC
	mov edx, OFFSET p2Msg1
	call WriteMessage
	mov edx, OFFSET p2Msg2
	call WriteMessage
	call Crlf

	; v1 is smallest: v1 < v2 < v3
	mov v1, 1111h
	mov v2, 2222h
	mov v3, 3333h
	call GetSmallest
	call WriteVars	; prints the variables
	call DumpRegs	; then the registers

	; v2 is smallest: v2 < v3 < v1
	mov v1, 5555h
	mov v2, 6666h
	mov v3, 4444h
	call GetSmallest
	call WriteVars
	call DumpRegs

	; v3 is smallest: v3 < v1 < v2
	mov v1, 8888h
	mov v2, 9999h
	mov v3, 7777h
	call GetSmallest
	call WriteVars
	call DumpRegs

	ret								; return to main

	; saves the smallest value among v1, v2, and v3 into ax
	GetSmallest PROC
		mov ax, v1			; assume v1 is smallest
		cmp ax, v2

		jbe checkv3			; if ax <= v2 then immediately check v3
		mov ax, v2			; else (ax > v2) assume v2 is smallest then check v3

		checkv3:
			cmp ax, v3

			jbe finally		; if ax <= v3 then we are done
			mov ax, v3		; else (ax > v3) v3 is smallest
			
		finally:
			ret
	GetSmallest ENDP

	; prints the variables and their values each separated by commas
	WriteVars PROC
		push edx
		push eax

		mov edx, OFFSET v1Name
		mov ax, v1
		call WriteVar
		call WriteSep
		
		mov edx, OFFSET v2Name
		mov ax, v2
		call WriteVar
		call WriteSep

		mov edx, OFFSET v3Name
		mov ax, v3
		call WriteVar
		call Crlf

		pop eax
		pop edx
		ret
	WriteVars ENDP
programTwo ENDP

; Program 3 code
programTri PROC
	mov edx, OFFSET p3Msg1
	call WriteMessage
	mov edx, OFFSET p3Msg2
	call WriteMessage
	call Crlf

	call WriteVals
	
	; test 'A' expect 'a'
	mov al, "A"
	call WriteBefore			; prints "Before: A"
	call ToLower				; converts input to lowercase
	call WriteAfter				; prints "After: a"

	; test 'a' expect 'a'
	mov al, "a"
	call WriteBefore
	call ToLower
	call WriteAfter

	; test 'Z' expect 'z'
	mov al, "Z"
	call WriteBefore
	call ToLower
	call WriteAfter

	ret								; return to main

	; input: al = letter
	; output: lower case input
	ToLower PROC
		or al, 00100000b
		ret
	ToLower ENDP

	; prints the variables and their values each separated by commas
	WriteVals PROC
		mov edx, OFFSET upperAVar
		mov eax, "A"
		call WriteVar
		call WriteSep

		mov edx, OFFSET lowerAVar
		mov eax, "a"
		call WriteVar
		call Crlf

		mov edx, OFFSET upperZVar
		mov eax, "Z"
		call WriteVar
		call WriteSep

		mov edx, OFFSET lowerZVar
		mov eax, "z"
		call WriteVar
		call Crlf

		call Crlf
		ret
	WriteVals ENDP

	WriteBefore PROC
		mov edx, OFFSET beforeMsg
		call WriteState
		ret
	WriteBefore ENDP

	WriteAfter PROC
		mov edx, OFFSET afterMsg
		call WriteState
		ret
	WriteAfter ENDP

	WriteState PROC
		call WriteString
		mov tempByte, al
		mov edx, OFFSET tempByte
		call WriteString
		call DumpRegs
		ret
	WriteState ENDP

programTri ENDP

; input: edx = offset of message
WriteMessage PROC
	call WriteString
	call Crlf
	ret
WriteMessage ENDP

; input: edx = offset of variable name, eax = variable value
WriteVar PROC
	call WriteString
	call WriteHex
	push edx
	mov edx, OFFSET hexSign
	call WriteString
	pop edx
	ret
WriteVar ENDP

; writes a separator ", "
WriteSep PROC
	push edx
	mov edx, OFFSET separator
	call WriteString
	pop edx
	ret
WriteSep ENDP

END main