import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Writer { 

    public static void main(String arg[]) { 
        String message = "blank";

        try { 
            Registry registry = LocateRegistry.getRegistry(arg[1]);
            NewsInterface obj = (NewsInterface) registry.lookup("News");
            System.out.println(obj.writeNews(Integer.parseInt(arg[0]))); 
        } catch (Exception e) { 
            System.out.println("Writer exception: " + e.getMessage()); 
            e.printStackTrace(); 
        } 
    }
    
} 
 