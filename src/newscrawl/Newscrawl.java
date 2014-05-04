package newscrawl;

import ie.moguntia.threads.MessageReceiver;
import ie.moguntia.threads.Queue;
import ie.moguntia.threads.ThreadController;
import java.util.logging.Level;
import java.util.logging.Logger;
import newscrawl.utils.URLQueue;

/**
 *
 * @author Akash
 */
public class Newscrawl implements MessageReceiver {

    public Newscrawl(int _maxLevel, int _maxThreads, Queue _queue) throws InstantiationException, IllegalAccessException {
        ThreadController controller = new ThreadController(CrawlerThread.class, _maxThreads, _maxLevel, _queue, 0, this);
    }
    

    public static void main(String[] args) {
        while(true){
            try {
                System.out.println("########Started to crawl#########");
                
                // Begin by reading config properties
                // List of base URLs
                // New Queue
                URLQueue queue = new URLQueue();
                // queue.addSeed();                

                int maxThreads = 10;
                int maxLevel = 1;
                
                new Newscrawl(maxLevel, maxThreads, queue);
                
                Thread.sleep(10); // after what time crawling needs to be reinitiated?
            } catch (InterruptedException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void receiveMessage(Object theMessage, int threadId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void finished(int threadId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void finishedAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
