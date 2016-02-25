#ifndef __PROTO
#define __PROTO

/* configuration routines */
void read_conf();
char *get_val_str(char *name, int index);
int get_val_int(char *name, int index);

/* ssh routines */
int exec_ssh(char *hostname, char *cmd, ...);

#endif
