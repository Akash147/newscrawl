package newscrawl;

import ie.moguntia.threads.MessageReceiver;
import ie.moguntia.threads.Queue;
import ie.moguntia.threads.ThreadController;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpt.FileWorker;
import newscrawl.utils.BaseURLManager;
import newscrawl.utils.ExtraStuff;
import newscrawl.utils.LuceneWorker;
import newscrawl.utils.URLQueue;

/**
 *
 * @author Akash
 */
public class Newscrawl implements MessageReceiver {
    private static FileWorker fileWorker;
    private static BaseURLManager baseURLManager;
    private static LuceneWorker luceneWorker;
    
    public Newscrawl(int _maxLevel, int _maxThreads, Queue _queue) throws InstantiationException, IllegalAccessException, InterruptedException {
        ExtraStuff extras = new ExtraStuff();
        extras.setBaseURLManager(baseURLManager);
        extras.setFileWorker(fileWorker);
        extras.setLuceneWorker(luceneWorker);
//        extras.setMongoWorker(mongoWorker);
        ThreadController controller = new ThreadController(CrawlerThread.class, _maxThreads, _maxLevel, _queue, 0, this);
        Thread.sleep(10000);
        controller.putExtra(extras);
    }
  
    public static void main(String[] args) {
//        while(true){
            try {
                System.out.println("########Started to crawl#########");
                
                // Begin by reading config properties
                // List of base URLs
                baseURLManager = new BaseURLManager();
                baseURLManager.add("http://www.football365.com/premier-league/");
                baseURLManager.add("http://edition.cnn.com/SPORT/football/archive/");
                
                // New Queue
                fileWorker = new FileWorker(); // @ TODO temporary
                URLQueue queue = new URLQueue();
                
                queue.setFileWorker(fileWorker); // @TODO temporary
                // Add seeds to queue
                for(String s: baseURLManager.getBaseURL())
                    queue.addSeed(s);
                int maxThreads = 10;
                int maxLevel = 1;
                
                new Newscrawl(maxLevel, maxThreads, queue);
                
                Thread.sleep(10000); // after what time crawling needs to be reinitiated?
            } catch (InterruptedException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            }
//        }
    }

    @Override
    public void receiveMessage(Object theMessage, int threadId) {
        System.out.println(theMessage.toString() + " from thread " + threadId);
    }

    @Override
    public void finished(int threadId) {
        
    }

    @Override
    public void finishedAll() {
        System.out.println("All finished and sleeping");
    }
}
