#include <stdio.h>

#include "proto.h"

#define READER_COUNT_PROP   "RW.numberOfReaders"
#define WRITER_COUNT_PROP   "RW.numberOfWriters"
#define READER_PROP         "RW.reader"
#define WRITER_PROP         "RW.writer"

int main() {
    int reader_cnt, writer_cnt, i;
    /* print splash */
    printf("**************************\n");
    printf("* Welcome to BBS system! *\n");
    printf("**************************\n");
    /* read configuration file */
    read_conf();
    /* read number of readers */
    reader_cnt = get_val_int(READER_COUNT_PROP, -1);
    if (reader_cnt < 0) {
        fprintf(stderr, "Error: can't read %s property.\n", READER_COUNT_PROP);
        return -1;
    }
    printf("Readers count: %d\n", reader_cnt);
    /* loop over readers */
    for (i = 0; i < reader_cnt; i++) {
        char *host = get_val_str(READER_PROP, i);
        if (!host) {
            fprintf(stderr, "Error: can't read %s%d property.\n", 
                            READER_PROP, i);
            return -1;
        }
        printf("SSH reader %s\n", host);
        exec_ssh(host, "/usr/local/bin/bbs_client", "hey", NULL);
    }
    /* read number of writers */
    writer_cnt = get_val_int(WRITER_COUNT_PROP, -1);
    if (writer_cnt < 0) {
        fprintf(stderr, "Error: can't read %s property.\n", WRITER_COUNT_PROP);
        return -1;
    }
    printf("Writers count: %d\n", writer_cnt);
    /* loop over writers */
    for (i = 0; i < writer_cnt; i++) {
        char *host = get_val_str(WRITER_PROP, i);
        if (!host) {
            fprintf(stderr, "Error: can't read %s%d property.\n", 
                            WRITER_PROP, i);
            return -1;
        }
        printf("SSH writer %s\n", host);
        exec_ssh(host, "/usr/local/bin/bbs_client", "you", NULL);
    }
    /* done */
    return 0;
}
