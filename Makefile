START_CFILES=Start.c readconf.c ssh.c
START=bbs_start
SERVER=bbs_server
RUNRMI=bbs_runrmi

all:
	gcc -o $(START) $(START_CFILES)
	javac *.java

install: all
	sudo mkdir -p /usr/local/share/bbs
	sudo cp *.class /usr/local/share/bbs
	sudo mkdir -p /etc/bbs
	sudo cp system.properties /etc/bbs
	sudo cp $(START) /usr/local/bin
	sudo cp $(SERVER) /usr/local/bin
	sudo cp $(RUNRMI) /usr/local/bin
	sudo mkdir -p /var/log/bbs
	sudo chown bbs /var/log/bbs

run: install
	$(START)
