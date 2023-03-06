TITLE Assignment 3

; Name: Keigo Katanaga
; Date: 2023/03/05
; ID: 	110068805
; Description: Contains 3 programs in 3 separate procedures

INCLUDE Irvine32.inc
INCLUDELIB Irvine32.lib

; these two lines are only necessary if you're not using Visual Studio
INCLUDELIB kernel32.lib
INCLUDELIB user32.lib

.data
	; Program 1 data
	colCount BYTE 0h
	positionX BYTE 0h
	positionY BYTE 0h
	promptOne BYTE "Enter the first integer: ", 0
	promptTwo BYTE "Enter the second integer: ", 0
	resultStr BYTE "Your sum is: ", 0
	intOne DWORD ?
	intTwo DWORD ?
	resSum DWORD ?

	; Program 2 data
	colorArray DWORD lightGreen, cyan, blue, magenta
	sameStr BYTE "The Same String", 0

	; Program 3 data
	progTriArray BYTE 1h,2h,3h,4h
	dWordBuffer DWORD 0h

	msgBeforeLoop BYTE "===Registers before the loop===", 0
	msgAfterLoop BYTE "===Registers after the loop===", 0
	msgBeforePush BYTE "Registers before push: ", 0
	msgAfterPush BYTE "Registers after push: ", 0
	msgBeforePop BYTE "Registers before pop: ", 0
	msgAfterPop BYTE "Registers after pop: ", 0

.code


main PROC
	; Calls the 3 program assignments
	call programOne
	call programTwo
	call programTri
	exit

	; Program 1 code
	programOne PROC
		call Clrscr
		call GetMaxXY		; number of rows is 0 since the screen was just cleared, so set it to 5 to estimate the center
		mov colCount, dl

		; Calculate the center column
		mov edx, 0h			; clear high dividend
		movzx ax, colCount	; column count as low dividend
		mov ebx, 2h			; 2 as divisor
		div bx
		mov dl, al
		sub dl, 17h			; promptOne is 25 characters long, so we move the text to the left
		mov dh, 5h
		mov positionX, dl	; save column location
		mov positionY, dh	; save row location

		call GoToXY

		; Get first int
		mov edx, OFFSET promptOne
		call WriteString				; print prompt for second int
		call ReadInt					; read the second int
		mov intOne, eax					; save the second int

		call moveNext

		; Get second int
		mov edx, OFFSET promptTwo
		call WriteString				; print prompt for second int
		call ReadInt					; read the second int
		mov intTwo, eax					; save the second int

		call moveNext

		; Calculate result
		add eax, intOne
		mov resSum, eax

		; Print result
		mov edx, OFFSET resultStr
		call WriteString
		mov edx, resSum
		call WriteInt

		call moveNext
		call WaitMsg

		ret								; return to main

		; moves the cursor to the next line, centered
		moveNext PROC
			call Crlf
			mov dl, positionX
			add positionY, 1h
			mov dh, positionY
			call GoToXY
			ret
		moveNext ENDP

	programOne ENDP

	programTwo PROC
		call Clrscr

		mov ecx, 4						; loop 4 times
		mov esi, OFFSET colorArray		; access array

		colorChanger:					; changes color of text 4 times
			mov eax, [esi]
			call SetTextColor			; set the color of text

			mov edx, OFFSET sameStr
			call WriteString			; print colored text
			call Crlf					; move to next line

			add esi, TYPE colorArray	; move to next element
		loop colorChanger

		mov eax, lightGray				; change back to default text color
		call SetTextColor

		call WaitMsg					; wait before moving to the next program

		ret								; return to main
	programTwo ENDP

	programTri PROC
		call Clrscr

		mov esi, OFFSET progTriArray	; initialize random pointer
		mov ecx, 4h						; initialize random counter

		mov edx, OFFSET msgBeforeLoop
		call writeStrln					; state of registers BEFORE the loop

		mov edx, OFFSET msgBeforePush
		call writeStrReg				; prints the state of registers BEFORE the push
		
		push esi				; save esi to stack
		push ecx				; save ecx to stack
		mov eax, 0

		mov edx, OFFSET msgAfterPush
		call writeStrReg				; prints the state of registers AFTER the push
		
		; this loop adds the sum of every element in esi until ecx reaches 0, then returns esi and ecx to their initial states before the loop
		newLabel:				; conditional loop
			add eax, [esi]		; add the element esi is pointing to with eax
			add esi, 4			; increment pointer
			sub ecx, 1			; subtract 1 from ecx
			cmp ecx, 0			; compare ecx to 0
		ja newLabel				; loop if ecx > 0

		call Crlf
		mov edx, OFFSET msgAfterLoop
		call writeStrln					; state of registers AFTER the loop

		mov edx, OFFSET msgBeforePop
		call writeStrReg				; prints the state of registers BEFORE the pop

		pop ecx					; return ecx from stack
		pop esi					; return esi from stack

		mov edx, OFFSET msgAfterPop
		call writeStrReg				; prints the state of registers AFTER the pop

		call WaitMsg
		call Clrscr
		
		ret						; return to main

		writeStrReg:			; prints the string moved to edx, the entire register, with 
			call WriteString
			call DumpRegs
		ret

		writeStrln:
			call WriteString
			call Crlf
		ret
	programTri ENDP
main ENDP
END main