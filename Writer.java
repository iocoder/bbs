import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;
import java.io.PrintWriter;

public class Writer { 

    public static void main(String arg[]) { 
        String message = "blank";
        int id = Integer.parseInt(arg[0]);
        String serverIp = arg[1];
        int accessesN = Integer.parseInt(arg[2]);
        Random rand = new Random();
        PrintWriter log = null;
        /* try to open log file */
        try {
            log = new PrintWriter("/var/log/bbs/log" + id, "UTF-8");
        } catch (Exception e) {
            System.out.println("Can't open log file: " + e.getMessage()); 
            return;
        }
        log.println("Client type: Writer");
        log.println("Client name: " + id);
        log.println("rSeq\tsSeq");
        try { 
            Registry registry = LocateRegistry.getRegistry(serverIp);
            NewsInterface obj = (NewsInterface) registry.lookup("News");
            while(accessesN-- > 0) {
                RetInfo info = obj.writeNews(id);  
                log.println(info.rSeq + "\t" + info.sSeq); 
                Thread.sleep(rand.nextInt(5000));
            }
            log.close();
            System.exit(0);
        } catch (Exception e) { 
            System.out.println("Writer exception: " + e.getMessage()); 
            e.printStackTrace(); 
        }
    }
    
} 
 