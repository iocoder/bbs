import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Request {

    boolean isWrite;
    int id;
    int retVal;
    int rSeq;
    int sSeq;
    boolean done;

    public Request(int id, boolean isWrite) {
        this.id = id;
        this.isWrite = isWrite;
        this.done = false;
    }
}

