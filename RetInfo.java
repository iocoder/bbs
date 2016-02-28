import java.io.*;
import java.util.*;

public class RetInfo implements Serializable {

    int retVal;
    int rSeq;
    int sSeq;

    public RetInfo(int retVal, int rSeq, int sSeq) {
        this.retVal = retVal;
        this.rSeq = rSeq;
        this.sSeq = sSeq;
    }

    public String toString() {
        return "hey you!";
    }

}
