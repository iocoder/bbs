START_CFILES=Start.c readconf.c ssh.c
START=bbs_start

all:
	gcc -o $(START) $(START_CFILES)
	javac *.java

install: all
	sudo mkdir -p /usr/local/share/bbs
	sudo cp *.class /usr/local/share/bbs
	sudo mkdir -p /etc/bbs
	sudo cp system.properties /etc/bbs
	sudo cp $(START) /usr/local/bin

run: install
	$(START)
