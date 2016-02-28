import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.Random;
import java.io.*;

public class NewsImpl extends UnicastRemoteObject implements NewsInterface {

    Queue<Request> requestsQueue;
    int rSeq, sSeq, val;
    Semaphore requestCount;
    Random rand;
    int acessesN;
    public PrintWriter reader_log;
    public PrintWriter writer_log;

    public NewsImpl(int acessesN) throws RemoteException, 
                                         FileNotFoundException,
                                         UnsupportedEncodingException {
        requestsQueue = new LinkedList<Request>();
        rSeq = 0;
        sSeq = 0;
        val = -1;
        this.acessesN = acessesN;
        requestCount = new Semaphore(0);
        rand = new Random();
        reader_log = new PrintWriter("/var/log/bbs/readers", "UTF-8");
        writer_log = new PrintWriter("/var/log/bbs/writers", "UTF-8");
        reader_log.println("sSeq\toVal\trID\trNum");
        writer_log.println("sSeq\toVal\twID");
    }

    public RetInfo readNews(int readerId) throws RemoteException, 
                                                 InterruptedException {
        Request req = new Request(readerId, false);
        enqueueRequest(req);
        // wait for request to be done
        req.done.acquire();
        return new RetInfo(req.retVal, req.rSeq, req.sSeq);
    }
    
    public RetInfo writeNews(int writerId) throws RemoteException, 
                                                  InterruptedException {
        Request req = new Request(writerId, true);
        enqueueRequest(req);
        // wait for request to be done
        req.done.acquire();
        return new RetInfo(req.retVal, req.rSeq, req.sSeq);
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
        int rNum = 0;
        requestCount.acquire();
        synchronized(requestsQueue) {
            if(requestsQueue.isEmpty())
                return null;
            /* get number of readers */
            for(Request i : requestsQueue) {
                if (!i.isWrite) {
                    rNum++;
                }
            }
            /* return info */
            Request req = requestsQueue.poll();
            sSeq ++;
            req.sSeq = sSeq;
            req.rNum = rNum;
            return req;
        }
    }

    public void handleRequests() throws InterruptedException {
        while(acessesN-- > 0) {
            Request req = dequeueRequest();
            if(req != null) {
                System.out.print("handling id: " + req.id + 
                                 ", rSeq: " + req.rSeq + 
                                 ", sSeq: " + req.sSeq + 
                                 " -- ");
                if(req.isWrite) {
                    System.out.println(" (write) ");
                    val = req.id;
                    req.retVal = val;
                    writer_log.println(req.sSeq + "\t" +
                                       val + "\t" +
                                       req.id + "\t");
                }
                else {
                    req.retVal = val;
                    System.out.println(" (read " + req.retVal + " ) ");
                    reader_log.println(req.sSeq + "\t" +
                                       val + "\t" +
                                       req.id + "\t" +
                                       req.rNum);
                }
                Thread.sleep(rand.nextInt(5000));               
                req.done.release();
            }
        }
    }
}
