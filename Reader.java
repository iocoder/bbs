import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Reader { 

    public static void main(String arg[]) { 
        String message = "blank";

        try { 
            Registry registry = LocateRegistry.getRegistry(arg[1]);
            NewsInterface obj = (NewsInterface) registry.lookup("News");
            System.out.println(obj.readNews(Integer.parseInt(arg[0]))); 
        } catch (Exception e) { 
            System.out.println("Reader exception: " + e.getMessage()); 
            e.printStackTrace(); 
        } 
    }
    
} 
 