TITLE Assignment 2

; Name: Keigo Katanaga
; Date: 2023/02/03
; ID: 	110068805
; Description: 	Calculates the fibonacci sequence to the 7th sequence
;               Defines 6 different data types
;               Adds the sum of 4 DWORD elements in an array

INCLUDE Irvine32.inc
INCLUDELIB Irvine32.lib

INCLUDELIB kernel32.lib
INCLUDELIB user32.lib
.data
    n BYTE 7h               ; sequence number
    index DWORD 2           ; index starts at 2, skipping fib(1) and fib(2) or fib+0 and fib+1

    fibN WORD 1             ; starts as fib(2), ends as fib(n)

    fib BYTE 1, 1, 5 DUP(0) ; array of n fibonacci sequences with the first 2 already filled

    byteVar BYTE 55h
    sByteVar SBYTE -12h
    wordVar WORD 1111h
    sWordVar SWORD +1928h
    dWordVar DWORD 12345678h
    qWordVar QWORD 1122334455667788h

    array DWORD 10000h, 20000h, 30000h, 40000h

.code
main PROC
    sub n, 2                ; fib(n) will loop n-2 times since fib(1) and fib(2) are available by default

    COMMENT !   This fibonacci sequence calculates straight to fib(n) and throws away values before fib(n-1).
                fib(n) is saved in zero-extended register eax.
    !
    movzx ecx, n            ; loop counter
    mov ax, 1               ; al acts as fib(n-2), starting from fib(1)

    fibonacciN:             ; calculates straight to the 7th fibonacci sequence
        xchg fibN, ax       ; prepare fib(n-1) (fibN) for addition with fib(n-2) (al)
        add fibN, ax        ; add fib(n-1) to fib(n-2). fibN now contains fib(n)

        loop fibonacciN     ; loop n-2 times


    COMMENT !   This fibonacci sequence creates an array of up to the 7th sequence
                fib(n) or the 7th element of the array is saved in zero-extended register ebx.   
    !
    ;mov fib, 1              ; initialize fib(1) = 1
    ;mov fib+1, 1            ; initialize fib(2) = 1
    mov bl, fib             ; prepare fib(n-2)
    mov bh, fib+1           ; prepare fib(n-1)
    movzx ecx, n            ; loop counter
    
    fibonacciArray:
        mov eax, TYPE fib   ; move operand to scale index; redundant for BYTE
        mul index           ; multiply TYPE fib by the index, i.e., index * TYPE fib
        mov esi, eax        ; move product to scaled index

        mov fib[esi], bl    ; move fib(n-2) to prepare for the sum fib(n)
        add fib[esi], bh    ; add fib(n-1) to fib(n-2)
        xchg bl, bh         ; swap so that fib(n-1) becomes fib(n-2) for the next loop
        mov bh, fib[esi]    ; and fib(n-1) becomes fib(n)

        inc index           ; move to next index
        loop fibonacciArray ; loop n-2 times
    
    movzx eax, fibN         ; save fibonacciN's result to eax
    movzx ebx, fib[esi]     ; save fibonacciArray's result to ebx
    call DumpRegs

    COMMENT !   Calculates the sum of array initialized in the data directive.
                Total sum is saved in register eax.
    !
    mov esi, OFFSET array   ; find the pointer the first element of array
    mov eax, [esi]          ; prepare total sum using the first element

    mov ecx, LENGTHOF array ; use length of the array as the iteration count
    sub ecx, 1              ; sub 1 because the first element is already in eax
    
    sum:
        add esi, TYPE array ; move to next index
        add eax, [esi]      ; add element to the sum
        loop sum

    call DumpRegs
    exit

main ENDP
END main