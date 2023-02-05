#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>
#include <errno.h>

int main(int argc, char *argv[]) {
    char *cc_directory = "/usr/bin/cc";
    extern char **environ;

    char *program = "#include <stdio.h>\n"
                    "#include <stdlib.h>\n"
                    "#include <unistd.h>\n\n"
    
                    "int main(int argc, char *argv[]) {\n"
                    "\tprintf(\"math pid: %d\\n\", getpid());\n"
                    "\tchar operation = argv[1][0];\n"
                    "\tint a = atoi(argv[2]);\n"
                    "\tint b = atoi(argv[3]);\n"
                    "\tint result = 0;\n\n"

                    "\tprintf(\"%d\", a);\n"
                    "\tswitch(operation) {\n"
                    "\t\tcase '+':\n"
                    "\t\t\tresult = a + b;\n"
                    "\t\t\tprintf(\" + \");\n"
                    "\t\t\tbreak;\n\n"
    
                    "\t\tcase '-':\n"
                    "\t\t\tresult = a - b;\n"
                    "\t\t\tprintf(\" - \");\n"
                    "\t\t\tbreak;\n\n"
                    
                    "\t\tcase '*':\n"
                    "\t\t\tresult = a * b;\n"
                    "\t\t\tprintf(\" * \");\n"
                    "\t\t\tbreak;\n\n"
                    
                    "\t\tcase '/':\n"
                    "\t\t\tresult = a / b;\n"
                    "\t\t\tprintf(\" / \");\n"
                    "\t\t\tbreak;\n"
                    "\t}\n"
                    "\tprintf(\"%d = %d\\n\", b, result);\n\n"
                    "\tprintf(\"--------------------------------\\n\");\n"
                    "\treturn 0;\n"
                    "}\n";

    int fd = open("./math.c", O_CREAT | O_RDWR, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
    printf("fd for math.c file: %d\n", fd);
    int written = write(fd, program, strlen(program));
    if (written != strlen(program)) {
        printf("Error: Could not write everthing\n");
        exit(1);
    }
    
    int child_pid = fork();

    if (child_pid == -1) {
        printf("Fork Failed...\n");
        exit(1);
    }

    if (child_pid == 0) {
        printf("math.c (child) pid: %d\n", getpid());
        
        int cc_fd = open(cc_directory, O_RDONLY, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
        printf("fd for cc file: %d\n\n", cc_fd);
        char *cc_argv[] = {cc_directory, "math.c", "-o", "math", NULL};
        if (fexecve(cc_fd, cc_argv, environ) < 0) {
            printf("%s\n", strerror(errno));
            exit(1);
        }
        
        exit(0);
    }

    printf("--------------------------------\n");
    printf("exec_math_bs.o (parent) pid: %d\n\n", getpid());
    
    int *child_exit;
    wait(child_exit);

    int math_fd = open("./math", O_RDONLY, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH);
    printf("fd for math.o file: %d\n", math_fd);
    char *math_argv[] = {"./math", argv[1], argv[2], argv[3], NULL};
    
    if (fexecve(math_fd, math_argv, environ) < 0) {
        printf("%s\n", strerror(errno));
        exit(1);
    }
    
    exit(*child_exit);
}