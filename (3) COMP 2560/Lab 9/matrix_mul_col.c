#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <unistd.h>

int main(int argc, char *argv[]) {
  char *shared_file_name = "temp.txt";
  int shared_flag_write = O_WRONLY;
  int shared_flag_read = O_RDONLY;
  mode_t shared_mode = S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH;
  creat(shared_file_name, shared_mode);
  int shared_fd_write = open(shared_file_name, shared_flag_write, shared_mode);

  int row = atoi(argv[1]);
  int col = atoi(argv[2]);

  int *matrix = (int *)malloc((row * col) * sizeof(int));

  int i, j, n;

  printf("Enter elements:\n");
  for (i = 0; i < row; ++i) {
    for (j = 0; j < col; ++j) {
      printf("matrix[%d][%d]: ", i, j);
      scanf("%d", matrix + (i * col) + j);
    }
  }

  printf("Enter a scalar: ");
  scanf("%d", &n);

  printf("\nInitial Matrix:\n");
  for (i = 0; i < row; ++i) {
    printf("[");
    for (j = 0; j < col - 1; ++j) {
      printf("%d, ", *(matrix + (i * col) + j));
    }
    printf("%d]\n", *(matrix + (i * col) + (col - 1)));
  }

  pid_t child_pid, row_pid,
      col_pid; // pid for scalar multiplication, pid for row forks, pid for
               // column forks per row fork
  int intMaxLength = 10; // ( (2^64)/2 ) - 1 has 10 digits
  char *intBuf = malloc((intMaxLength + 1) *
                        sizeof(char)); // buffer to contain int as strings
  memset(
      intBuf, '\0',
      (intMaxLength + 1) *
          sizeof(
              char)); // cleans buffer, otherwise garbage will be written/read

  child_pid = fork();
  // parent: waits for children to finish scalar multiplication; children scalar
  // multiplies rows per column (individually)
  if (child_pid == 0) {
    for (j = col - 1; j >= 0; --j) {
      col_pid = fork();
      if (col_pid != 0) {
        wait(0);

        for (i = 0; i < row; ++i) {
          sprintf(intBuf, "%d", n * *(matrix + (i * col) + j));
          write(shared_fd_write, intBuf, strlen(intBuf));
          write(shared_fd_write, ",", strlen(","));
          memset(intBuf, '\0', (intMaxLength + 1) * sizeof(char));
        }

        exit(0);
      }
    }
    if (col_pid == 0) {
      exit(0); // Last child acts as a stopper for forking
    }
  } else {
    wait(0); // Wait for everything to be written
  }
  close(shared_fd_write);

  int shared_fd_read = open(shared_file_name, shared_flag_read, shared_mode);

  char buf[2] = {'\0'};
  char size[1];
  ssize_t bytes_read = 0;
  ssize_t temp_read;
  ssize_t bytes_written = 0;
  ssize_t temp_written;

  for (j = 0; j < col; ++j) {
    for (i = 0; i < row; ++i) {
      while (1) { // Read characters until a , is reached
        read(shared_fd_read, buf, sizeof(size));
        if (buf[0] == ',')
          break;

        strcat(intBuf, buf);
      }

      *(matrix + (i * col) + j) = atoi(intBuf);

      memset(intBuf, '\0', strlen(intBuf) * sizeof(char));
      memset(buf, '\0', strlen(buf) * sizeof(char));
    }
  }

  printf("\nScaled Matrix:\n");
  for (i = 0; i < row; ++i) {
    printf("[");
    for (j = 0; j < col - 1; ++j) {
      printf("%d, ", *(matrix + (i * col) + j));
    }
    printf("%d]\n", *(matrix + (i * col) + (col - 1)));
  }

  printf("----------------\n");
  exit(0);
}