#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>

#include "proto.h"

#define SERVER_PROP         "RW.server"
#define READER_COUNT_PROP   "RW.numberOfReaders"
#define WRITER_COUNT_PROP   "RW.numberOfWriters"
#define READER_PROP         "RW.reader"
#define WRITER_PROP         "RW.writer"
#define ACCESS_PROP         "RW.numberOfAccesses"

int main() {
    int i;
    int reader_cnt, writer_cnt, access_cnt;
    char *serverip;
    char num[20];
    char reader_cnt_str[20];
    char writer_cnt_str[20];
    char access_cnt_str[20];
    /* print splash */
    printf("**************************\n");
    printf("* Welcome to BBS system! *\n");
    printf("**************************\n");
    /* read configuration file */
    read_conf();
    /* read number of accesses */
    access_cnt = get_val_int(ACCESS_PROP, -1);
    if (access_cnt < 0) {
        fprintf(stderr, "Error: can't read %s property.\n", ACCESS_PROP);
        return -1;
    }
    sprintf(access_cnt_str, "%d", access_cnt);
    /* read number of readers */
    reader_cnt = get_val_int(READER_COUNT_PROP, -1);
    if (reader_cnt < 0) {
        fprintf(stderr, "Error: can't read %s property.\n", READER_COUNT_PROP);
        return -1;
    }
    sprintf(reader_cnt_str, "%d", reader_cnt);
    printf("Readers count: %d\n", reader_cnt);
    /* read number of writers */
    writer_cnt = get_val_int(WRITER_COUNT_PROP, -1);
    if (writer_cnt < 0) {
        fprintf(stderr, "Error: can't read %s property.\n", WRITER_COUNT_PROP);
        return -1;
    }
    sprintf(writer_cnt_str, "%d", writer_cnt);
    printf("Writers count: %d\n", writer_cnt);
    /* read server IP */
    serverip = get_val_str(SERVER_PROP, -1);
    if (!serverip) {
        fprintf(stderr, "Error: can't read %s property.\n", SERVER_PROP);
        return -1;
    }
    /* start RMI registry */
    printf("Starting RMI registry on %s...\n", serverip);
    exec_ssh(serverip, "/usr/local/bin/bbs_runrmi", NULL);
    wait(NULL);
    /* execute server */
    printf("Starting BBS server on %s...\n", serverip);
    exec_ssh(serverip, "/usr/local/bin/bbs_server", reader_cnt_str, writer_cnt_str, 
                       access_cnt_str, NULL);
    system("sleep 15");
    /* loop over readers */
    for (i = 0; i < reader_cnt; i++) {
        char *host = get_val_str(READER_PROP, i);
        if (!host) {
            fprintf(stderr, "Error: can't read %s%d property.\n", 
                            READER_PROP, i);
            return -1;
        }
        printf("SSH reader %d on %s\n", i, host);
        sprintf(num, "%d", i);
        exec_ssh(host, "java", "-classpath","/usr/local/share/bbs", 
                       "Reader", num, serverip, access_cnt_str, NULL);
    }
    /* loop over writers */
    for (i = 0; i < writer_cnt; i++) {
        char *host = get_val_str(WRITER_PROP, i);
        if (!host) {
            fprintf(stderr, "Error: can't read %s%d property.\n", 
                            WRITER_PROP, i);
            return -1;
        }
        printf("SSH writer %d on %s\n", i+reader_cnt, host);
        sprintf(num, "%d", i+reader_cnt);
        exec_ssh(host, "java", "-classpath","/usr/local/share/bbs", 
                       "Writer", num, serverip, access_cnt_str, NULL);
    }
    /* optional: wait until all children exit */
    while (wait(NULL) > 0);
    /* done */
    return 0;
}
