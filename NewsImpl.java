import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;

public class NewsImpl extends UnicastRemoteObject implements NewsInterface {

    public NewsImpl() throws RemoteException {
        /* do nothing */
    }

    public String readNews(int readerId) {
        return "Read news with Id: " + readerId;
    }
    
    public String writeNews(int writerId) {
        return "Writer news with Id: " + writerId;
    }

}
