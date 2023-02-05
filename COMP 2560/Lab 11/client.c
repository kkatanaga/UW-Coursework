#include <stdlib.h>

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include <stdio.h>
#include <string.h>

int main(int argc, char* argv[]){
	int domain = AF_INET;//Network Protocol: TCP/IP
	int type = SOCK_STREAM;//Connection-Oriented
	int protocol = 0;//Default transport: TCP for Internet connection-oriented

	int client_sd;//socket descriptor ~= file descriptor
	client_sd = socket(domain, type, protocol);
	if (client_sd == -1){
		printf("Error in creating socket for the client!\n");
		exit(1);
	}
	else
		printf("A socket has created for the client with sd:%d\n", client_sd);

	//Binding to an address is optional for the client. 
	//We don't do that because the client might be in any other computers
	//We let the kernel does that for us for the host computer of the client
	
	struct in_addr server_sin_address;
	server_sin_address.s_addr = inet_addr(argv[1]);//ask!
	int server_sin_port = htons(atoi(argv[2]));//larger than 1024

	struct sockaddr_in server_sin;
	server_sin.sin_family = domain;
	server_sin.sin_addr = server_sin_address;
	server_sin.sin_port = server_sin_port;
	int connection_state = connect(client_sd, (struct sockaddr *) &server_sin, sizeof(server_sin));
	if (connection_state == -1){
		printf("Error in connecting to The Server at address:port = %d:%d\n", server_sin.sin_addr, server_sin.sin_port);
		exit(1);
	}
	else
		printf("You are now connected to The Server at address:port = %d:%d\n", server_sin.sin_addr, server_sin.sin_port);
	  char msg[65];
	  
	  while (1) {
	    printf("Enter your first number: ");
	    scanf("%10s", msg);                     // Gets string version of x
      send(client_sd, msg, strlen(msg) + 1, 0);
      
	    printf("Enter your second number: ");
	    scanf("%10s", msg);                     // Gets string version of y
      send(client_sd, msg, strlen(msg) + 1, 0);
      
	    printf("Enter your username: ");
	    scanf("%24s", msg);                     // Gets username
      send(client_sd, msg, strlen(msg) + 1, 0);
      
	    printf("Enter your password: ");
	    scanf("%24s", msg);                     // Gets password
      send(client_sd, msg, strlen(msg) + 1, 0);
  	  
  	  recv(client_sd, msg, 65, 0);            // msg = "Authentication Failed" if username and/or password don't match
  	                                          // else msg = x + y
  	  if (strcmp(msg, "Authentication Failed") == 0) {
  	    printf("%s\n", msg);
  	    exit(0);
  	  }
  	  printf("Result: %s\n", msg);
  	  
	  }
}
