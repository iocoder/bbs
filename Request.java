import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Semaphore;

public class Request {

    boolean isWrite;
    int id;
    int retVal;
    int rSeq;
    int sSeq;
    int rNum;
    Semaphore done;

    public Request(int id, boolean isWrite) {
        this.id = id;
        this.isWrite = isWrite;
        this.done = new Semaphore(0);
    }
}
