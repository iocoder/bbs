import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.Random;

public class NewsImpl extends UnicastRemoteObject implements NewsInterface {

    Queue<Request> requestsQueue;
    int rSeq, sSeq, val;
    Semaphore requestCount;
    Random rand;
    int acessesN;

    public NewsImpl(int acessesN) throws RemoteException {
        requestsQueue = new LinkedList<Request>();
        rSeq = 0;
        sSeq = 0;
        val = -1;
        this.acessesN = acessesN;
        requestCount = new Semaphore(0);
        rand = new Random();
    }

    public String readNews(int readerId) throws RemoteException, InterruptedException {
        Request req = new Request(readerId, false);
        enqueueRequest(req);
        // wait for request to be done
        req.done.acquire();
        return "rSeq: " + req.rSeq + ", sSeq: " + req.sSeq + ", retVal: " + req.retVal;
    }
    
    public String writeNews(int writerId) throws RemoteException, InterruptedException {
        Request req = new Request(writerId, true);
        enqueueRequest(req);
        // wait for request to be done
        req.done.acquire();
        return "rSeq: " + req.rSeq + ", sSeq: " + req.sSeq;
    }

    public void enqueueRequest(Request req) throws InterruptedException {
        synchronized(requestsQueue) {
            rSeq ++;
            req.rSeq = rSeq;
            requestsQueue.offer(req);
        }
        requestCount.release();
    }

    public Request dequeueRequest() throws InterruptedException {
        requestCount.acquire();
        synchronized(requestsQueue) {
            if(requestsQueue.isEmpty())
                return null;
            Request req = requestsQueue.poll();
            sSeq ++;
            req.sSeq = sSeq;
            return req;
        }
    }

    public void handleRequests() throws InterruptedException {
        while(acessesN-- > 0) {
            Request req = dequeueRequest();
            if(req != null) {
                System.out.print("handling id: " + req.id + ", rSeq: " + req.rSeq + ", sSeq: " + req.sSeq + " -- ");
                if(req.isWrite) {
                    System.out.println(" (write) ");
                    val = req.id;
                }
                else {
                    req.retVal = val;
                    System.out.println(" (read " + req.retVal + " ) ");
                }
                Thread.sleep(rand.nextInt(5000));               
                req.done.release();
            }
        }
    }
}
