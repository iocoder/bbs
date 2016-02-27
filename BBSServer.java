import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BBSServer {

    public static void main(String args[]) { 
        try {
            NewsImpl obj = new NewsImpl(); 
            // Bind this object instance to the name "HelloServer" 
            //Naming.rebind("News", obj); 
            Registry registry = LocateRegistry.getRegistry("localhost");
            registry.rebind("News", obj);
            obj.handleRequests();
        } catch (Exception e) { 
            System.out.println("NewsImpl err: " + e.getMessage()); 
            e.printStackTrace(); 
        }
    }
}

