#include <stdio.h>
#include <stdlib.h>
#include <time.h>

int main(void)
{
    srand(time());
    int goal = rand() % 50 + 1;
    int guess = 0, wrong = 0;

    while (guess != goal && wrong++ < 10) {
        printf("Enter your guess (numbers between 1 and 50): ");
        scanf("%d", &guess);

        if (guess == goal) {
            printf("Correct, the number was %d.\n", goal);
            break;
        }
        else if (wrong == 10) {
            printf("Sorry, the number was %d.\n", goal);
            break;
        }
        else if (guess < goal)
            printf("Too low!\n");
        else if (guess > goal)
            printf("Too high!\n");
    }

}