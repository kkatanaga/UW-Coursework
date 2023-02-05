The testing is done by going back and forth between two different shells (client windows) 
	and entering test values (authenticated and not authenticated).
	
Client names are added to clarify which client is working in the server, and also which 
	generation after unauthentication is in either of the client windows.

If the screenshots are unclear, the order of commands and inputs is written below:

The Server:
	cc server.c -o server
	./server 137.207.82.51 5597

Client Window 1:
	cc client.c -o client
	./client 137.207.82.51 5597	(Client A)
	
Client Window 2:
	./client 137.207.82.51 5597	(Client B)
	10
	5
	comp2560
	f2022
	(Print 15)

Client Window 1:	(Client A)
	1
	1
	comp2560
	wrongpass
	(Client A closed)

Client Window 2:	(Client B)
	24
	8
	comp2560
	f2022
	(Print 32)
	
Client Window 1:
	./client 137.207.82.51 5597	(Client C)
	34
	16
	comp2560
	f2022
	(Print 50)
	
Client Window 2:	(Client B)
	1
	1
	comp2560
	wrongpass
	(Client B closed)

Client Window 1:	(Client C)
	88
	2
	wronguser
	f2022
	(Client C closed)
	
	./client 137.207.82.51 5597	(Client D)
	53
	23
	comp2560
	f2022
	(Print 76)
	(Crtl + C), (Client D closed)

The Server:
	(Ctrl + C)
	
	

