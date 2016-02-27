import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class Writer { 

    public static void main(String arg[]) { 
        String message = "blank";
        int id = Integer.parseInt(arg[0]);
        String serverIp = arg[1];
        int accessesN = Integer.parseInt(arg[2]);
        Random rand = new Random();
        try { 
            Registry registry = LocateRegistry.getRegistry(serverIp);
            NewsInterface obj = (NewsInterface) registry.lookup("News");
            while(accessesN-- > 0) {
                System.out.println(obj.writeNews(id)); 
                Thread.sleep(rand.nextInt(5000));
            }
        } catch (Exception e) { 
            System.out.println("Writer exception: " + e.getMessage()); 
            e.printStackTrace(); 
        }
    }
    
} 
 