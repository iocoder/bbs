import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.LinkedList;

public class NewsImpl extends UnicastRemoteObject implements NewsInterface {

    Queue<Request> requestsQueue;
    int rSeq, sSeq, val;

    public NewsImpl() throws RemoteException {
        requestsQueue = new LinkedList<Request>();
        rSeq = 0;
        sSeq = 0;
        val = -1;
    }

    public String readNews(int readerId) {
        Request req = new Request(readerId, false);
        enqueueRequest(req);
        // wait for request to be done
        while(!req.done);
        return "rSeq: " + req.rSeq + ", sSeq: " + req.sSeq + ", retVal: " + req.retVal;
    }
    
    public String writeNews(int writerId) {
        Request req = new Request(writerId, true);
        enqueueRequest(req);
        // wait for request to be done
        while(!req.done);
        return "rSeq: " + req.rSeq + ", sSeq: " + req.sSeq;
    }

    public void enqueueRequest(Request req) {
        synchronized(requestsQueue) {
            rSeq ++;
            req.rSeq = rSeq;
            requestsQueue.offer(req);
        }
    }

    public Request dequeueRequest() {
        synchronized(requestsQueue) {
            if(requestsQueue.isEmpty())
                return null;
            Request req = requestsQueue.poll();
            sSeq ++;
            req.sSeq = sSeq;
            return req;
        }
    }

    public void handleRequests() {
        while(true) {
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
                // sleep for random time
                req.done = true;
            }
            // sleep 
        }
    }
}
