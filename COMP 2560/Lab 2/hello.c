#include <stdio.h>
#include <time.h>
#include <stdlib.h>
int main()
{
  printf("Hello World! This is katanag@uwindsor.ca, StudentID: 110068805\n");
  time_t t = time(NULL);
  struct tm tm = *localtime(&t);
  printf("Now: %d-%02d-%02d %02d:%02d:%02d\n", tm.tm_year + 1900, tm.tm_mon + 1, tm.tm_mday, tm.tm_hour, tm.tm_min, tm.tm_sec);
  printf("%s@shell:%s$\n", getenv("USER"),getenv("PWD"));
  
  char temp;
  scanf("%c", &temp); // Stops the program from closing when opened with the executable; ends when anything is entered.
}
//int main(void){
//	printf("Hello World!\n");
//	return 0;
//}
