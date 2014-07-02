package newscrawl;

import ie.moguntia.threads.MessageReceiver;
import ie.moguntia.threads.Queue;
import ie.moguntia.threads.ThreadController;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import jpt.FileWorker;
import newscrawl.utils.BaseURLManager;
import newscrawl.utils.ExtraStuff;
import newscrawl.utils.LuceneWorker;
import newscrawl.utils.MongoWorker;
import newscrawl.utils.URLQueue;

/**
 *
 * @author Akash
 */
public class Newscrawl implements MessageReceiver {
    private static FileWorker fileWorker;
    private static MongoWorker mongoWorker;
    private static BaseURLManager baseURLManager;
    private static LuceneWorker luceneWorker;
    private static Properties config;
    
    public Newscrawl(int _maxLevel, int _maxThreads, Queue _queue) throws InstantiationException, IllegalAccessException, InterruptedException {
        if (baseURLManager!=null && mongoWorker!=null && luceneWorker!=null){
            ExtraStuff extras = new ExtraStuff();
            extras.setBaseURLManager(baseURLManager);
    //        extras.setFileWorker(fileWorker);
            extras.setLuceneWorker(luceneWorker);
            extras.setMongoWorker(mongoWorker);
            ThreadController controller = new ThreadController(CrawlerThread.class, _maxThreads, _maxLevel, _queue, 0, this, extras);
            controller.putExtra(extras);
        }
    }
  
    public static void main(String[] args) {
        while(true){
            try {
                System.out.println("########Started to crawl#########");
                
                // Begin by reading config properties
                config = loadProperties();
                
                // List of base URLs
                String[] urls = config.getProperty("baseURL").split(",");
                baseURLManager = new BaseURLManager();
                for(String u : urls)
                    baseURLManager.add(u);
//                    System.out.println(u);
                
                // Lucene parameters
                String indexLocation = config.getProperty("indexLocation");
                // MongoDB Parameters
                String mongoHost = config.getProperty("mongoHost");
                int mongoPort = Integer.parseInt(config.getProperty("mongoPort"));
                String dbName = config.getProperty("mongoDB");
                String collName = config.getProperty("mongoCollection");
                // Rerun time in minutes
                long rerun = Long.parseLong(config.getProperty("rerunTime")) * (60*1000);
                
                // Initialization of workers
                luceneWorker = new LuceneWorker(indexLocation);
                mongoWorker = new MongoWorker(mongoHost, mongoPort, dbName, collName);
//                fileWorker = new FileWorker(); // @ TODO temporary
                
                // New Queue
                URLQueue queue = new URLQueue();
                
                queue.setFileWorker(mongoWorker); // @TODO temporary
                // Add seeds to queue
                for(String s: baseURLManager.getBaseURL())
                    queue.addSeed(s);
                int maxThreads = 10;
                int maxLevel = 1;
                Newscrawl newscrawl = new Newscrawl(maxLevel, maxThreads, queue);
                
                Thread.sleep(rerun); // after what time crawling needs to be reinitiated?
                
//                baseURLManager = null;
//                luceneWorker = null;
//                mongoWorker = null;
            } catch (InterruptedException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
            Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Newscrawl.class.getName()).log(Level.SEVERE, null, ex);
        }
        }
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
        // All worker needs to be closed
        luceneWorker.close();
        mongoWorker.close();
    }
    
    private static Properties loadProperties() throws FileNotFoundException, IOException{
        Properties conf = new Properties();
        InputStream input = new FileInputStream("crawler.conf");
        conf.load(input);
        input.close();
        return conf;
    }
}
