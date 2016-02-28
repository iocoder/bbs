import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class BBSServer {

    public static void main(String args[]) { 
        try {
            int readersN = Integer.parseInt(args[0]);
            int writersN = Integer.parseInt(args[1]);
            int accessesN = Integer.parseInt(args[2]);
            NewsImpl obj = new NewsImpl((readersN+writersN)*accessesN); 
            // Bind this object instance to the name "HelloServer" 
            //Naming.rebind("News", obj); 
            Registry registry = LocateRegistry.getRegistry("localhost");
            registry.rebind("News", obj);
            obj.handleRequests();
            obj.reader_log.close();
            obj.writer_log.close();
            System.out.println("Terminating the server...");
            registry.unbind("News");
            System.exit(0);
        } catch (Exception e) { 
            System.out.println("NewsImpl err: " + e.getMessage()); 
            e.printStackTrace(); 
        }
    }
}

