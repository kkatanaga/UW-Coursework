#include <stdio.h>
#include <ctype.h>
#define MAXSIZE 7   // Assuming every input has at maximum 6 characters

int charToNum(char c[], int size);
void numToChar(int num, char c[]);

int main(void)
{
    char input1[MAXSIZE];
    char input2[MAXSIZE];
    char resultString[MAXSIZE];

    int sizeInput1 = 0;
    int sizeInput2 = 0;
    int result = 0;

    puts("Guideline: Z=0, O=1, T=2, H=3, F=4, V=5, X=6, S=7, E=8, N=9");
    printf("Enter the first set of characters: ");
    scanf("%s", input1);

    do {
        input1[sizeInput1] = toupper(input1[sizeInput1]);
    } while (input1[++sizeInput1] != '\0');
    
    result = charToNum(input1, sizeInput1);

    printf("Enter the second set of characters: ");
    scanf("%s", input2);

    do {
        input2[sizeInput2] = toupper(input2[sizeInput2]);
    } while (input2[++sizeInput2] != '\0');

    result += charToNum(input2, sizeInput2);

    numToChar(result, resultString);

    printf("%s + %s = %s\n", input1, input2, resultString);
}

int charToNum(char c[], int size)
{
    int value = 0;
    int result = 0;

    for (int x = 0; x < size; x++) {
        switch(c[x]) {
            case 'Z':
                value = 0;
                break;
            case 'O':
                value = 1;
                break;
            case 'T':
                value = 2;
                break;
            case 'H':
                value = 3;
                break;
            case 'F':
                value = 4;
                break;
            case 'V':
                value = 5;
                break;
            case 'X':
                value = 6;
                break;
            case 'S':
                value = 7;
                break;
            case 'E':
                value = 8;
                break;
            case 'N':
                value = 9;
                break;
            case '\0':
                break;
            default:
                return -1;
                break;
        }
        result += value;
        if (x < size - 1)
            result *= 10;
    }
    return result;
}

void numToChar(int num, char c[])
{
    int size = 0;
    sprintf(c, "%d", num);

    while (c[size] != '\0')
        size++;

    for (int x = 0; x < size; x++) {
        switch (c[x]) {
            case '0':
                c[x] = 'Z';
                break;
            case '1':
                c[x] = 'O';
                break;
            case '2':
                c[x] = 'T';
                break;
            case '3':
                c[x] = 'H';
                break;
            case '4':
                c[x] = 'F';
                break;
            case '5':
                c[x] = 'V';
                break;
            case '6':
                c[x] = 'X';
                break;
            case '7':
                c[x] = 'S';
                break;
            case '8':
                c[x] = 'E';
                break;
            case '9':
                c[x] = 'N';
                break;
            default:
                break;
        }
    }
}