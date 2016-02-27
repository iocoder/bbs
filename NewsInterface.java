import java.rmi.*;
		
public interface NewsInterface extends java.rmi.Remote {
    public String readNews(int readerId) throws RemoteException;
    public String writeNews(int writerId) throws RemoteException;
}