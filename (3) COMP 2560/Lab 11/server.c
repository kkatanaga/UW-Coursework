#include <stdlib.h>

#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#include <stdio.h>
#include <string.h>

#include <unistd.h>
#include <sys/wait.h>

int main(int argc, char* argv[]){
	int domain = AF_INET;//Network Protocol: TCP/IP
	int type = SOCK_STREAM;//Connection-Oriented
	int protocol = 0;//Default transport: TCP for Internet connection-oriented

	int server_sd;//socket descriptor ~= file descriptor
	server_sd = socket(domain, type, protocol);
	if (server_sd == -1){
		printf("Error in creating socket for The Server!\n");
		exit(1);
	}
	else
		printf("A socket has created for The Server with sd:%d\n", server_sd);

	//Binding to an address is a must for The Server!
	struct in_addr server_sin_address;
	server_sin_address.s_addr = inet_addr(argv[1]);//nslookup `hostname`
	int server_sin_port = htons(atoi(argv[2]));//larger than 1024

	struct sockaddr_in server_sin;
	server_sin.sin_family = domain;
	server_sin.sin_addr = server_sin_address;
	server_sin.sin_port = server_sin_port;
	int result = bind(server_sd, (struct sockaddr *) &server_sin, sizeof(server_sin));
	if (result == -1){
		printf("Error in binding The Server to the address:port = %d:%d\n", server_sin.sin_addr, server_sin.sin_port);
		exit(1);
	}
	else
		printf("The Server bound to the address:port = %d:%d\n", server_sin.sin_addr, server_sin.sin_port);

	//The Server ready to receive calls (up to 5 calls. More are rejected!) 
	if (listen(server_sd, 5) < 0) {
		perror("The Server's listening failed!\n");
		exit(1);
	}

	struct sockaddr_in client_sin;//I want to know who send the message
	int client_sin_len;
	int client_sd;
	
	int fork_pid;
	char clients[] = {'A', 'B', 'C', 'D', 'E'};
	char client_name = 'F';
	int client_count = 0;
	while(client_count <= 5)
	{
		client_sd = accept(server_sd, (struct sockaddr *) &client_sin, &client_sin_len);
		if (client_sd == -1){
			printf("Error in opening the request from client %d:%d !\n", client_sin.sin_addr, client_sin.sin_port);
			//exit(1);Do not exit. Go for the next client call
		} else {
	    client_name = clients[client_count++];
		  fork_pid = fork();// Parent focuses on accepting clients, child focuses on working with accepted clients
		  if (fork_pid == 0) {
		    
  		  int x, y, z;
  		  char number[11];
  		  char username[25];
  		  char password[25];
  		  
  		  do {
  		    strcpy(username, "garbage");
  		    strcpy(password, "garbage");
  		    
  		    recv(client_sd, number, 11, 0);
  		    x = atoi(number);
  		    
  		    recv(client_sd, number, 11, 0);
  		    y = atoi(number);
  		    
  		    recv(client_sd, username, 25, 0);
  		    
  		    recv(client_sd, password, 25, 0);
  		    
  		    if (strcmp(username, "comp2560") != 0 || strcmp(password, "f2022") != 0) {
  		      send(client_sd, "Authentication Failed", 22, 0);
  		      if (client_name >= 'F') {
  		        printf("Client unauthenticated.\n");
  		      } else {
  		        printf("Closing client %c...\n", client_name);
  		      }
  		      break;
  		    } else {
  		      int child_pid = fork();
  		      
  		      if (child_pid == 0) {
  		        printf("\tThis is the child program for client %c.\n", client_name);
  		        z = x + y;
  		        printf("\tSending result: %d\n", z);
  		        sprintf(number, "%d", z);
  		        send(client_sd, number, strlen(number) + 1, 0);
  		        printf("\tI am done!\n");
  		        exit(0);
  		      } 
  		    }
		    
  		  } while(strcmp(username, "comp2560") == 0 && strcmp(password, "f2022") == 0);
  		  exit(0);
		  } else {
			  printf("The Server opened the request from client %c\n", client_name);
		  }
		}
	}

}
