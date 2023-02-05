#include <stdio.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>

int main(void) {
    char logFileName[] = "log.txt";
    int logFlag = O_WRONLY | O_CREAT | O_APPEND;
    mode_t logMode = S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH;
    int logFd = open(logFileName, logFlag, logMode);
    if (logFd < 0) {
        printf("Error: Could not open %s\n", logFileName);
    }
    
    char directory[] = "/dev/pts/0";
    int fd_in = open(directory, O_RDONLY);
    if (fd_in < 0) {
        printf("Error: Could not open %s\n", directory);
    }

    char buf[20] = {'\0'};
    ssize_t bytes_read;
    ssize_t bytes_written;
    
    while ((bytes_read = read(fd_in, buf, sizeof(buf))) && strcmp(buf, "END\n") != 0) {
        bytes_written = write(logFd, buf, strlen(buf));
        if (bytes_written < strlen(buf)) {
            printf("Error: Could not read\n");
        }
        memset(buf, '\0', sizeof(buf));
    }

    close(fd_in);
    close(logFd);
}