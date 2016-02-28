import java.rmi.*;
		
public interface NewsInterface extends java.rmi.Remote {
    public RetInfo readNews (int readerId) throws RemoteException, 
                                                  InterruptedException;
    public RetInfo writeNews(int writerId) throws RemoteException, 
                                                  InterruptedException;
}
