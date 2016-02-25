START_CFILES=Start.c readconf.c ssh.c
SERVER_CFILES=server.c
CLIENT_CFILES=client.c
START=start
SERVER=server
CLIENT=client

all:
	gcc -o $(START) $(START_CFILES)
	gcc -o $(SERVER) $(SERVER_CFILES)
	gcc -o $(CLIENT) $(CLIENT_CFILES)

run: all
	./$(START)
