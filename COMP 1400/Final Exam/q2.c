#include <stdio.h>

int main(void)
{
    int input;
    int holder;
    int counter = 0;

    puts("Enter an integer number:");
    scanf("%d", &input);

    for (int x = 0; x < 10; x++) {
        holder = input;
        while (holder % 10 != 0 && holder / 10 != 0) {
            if (x == holder % 10)
                counter++;
            
            holder /= 10;
        }
        
        if (x == holder)
            counter++;

        if (counter == 0)
            continue;
        else {
            if (counter == 1)
                printf("Digit %d has been repeated %d time\n", x, counter);
            else
                printf("Digit %d has been repeated %d times\n", x, counter);
            counter = 0;
        }
    }
}