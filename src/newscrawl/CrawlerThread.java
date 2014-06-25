package newscrawl;

import ie.moguntia.threads.ControllableThread;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import jpt.FileWorker;
import newscrawl.utils.BaseURLManager;
import newscrawl.utils.ExtraStuff;
import newscrawl.utils.LuceneWorker;
import newscrawl.utils.MongoWorker;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import newscrawl.utils.Readability;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Akash
 */
public class CrawlerThread extends ControllableThread {
    FileWorker fw;
    BaseURLManager bMgr;
    LuceneWorker lw;
    MongoWorker mw;
    @Override
    public void process(Object o) {
        URL pageURL = (URL) o;
//        fw = ( (ExtraStuff)tc.getExtra() ).getFileWorker();
        bMgr = ( (ExtraStuff) tc.getExtra() ).getBaseURLManager();
        lw = ( (ExtraStuff) tc.getExtra() ).getLuceneWorker();
        mw = ( (ExtraStuff) tc.getExtra() ).getMongoWorker();
        
        if( shouldProcess(pageURL.toString()) ) {
            mr.receiveMessage("Crawling " +pageURL.toString(), id);
            try {
                Document doc = Jsoup.connect(pageURL.toString())
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36")
                        .get();

                Readability readability = null;
                if( !bMgr.isBaseURL(pageURL.toString()) ){
                    readability = new Readability(doc);
                    readability.init();
//                    fw.indexThis(pageURL.toString());
                    String testString = readability.luceneText();
                    if (shouldIndex(testString) && !mw.checkIfAlreadyExists(pageURL.toString())){ // some crawled content are just junk not news.. 
                        Date date = new Date();
                        String _id = mw.insert(pageURL.toString(), readability.mongoText(), doc.title(), date);
                        lw.indexThis(_id, readability.luceneText(), doc.title(), date);
                        System.out.println(pageURL.toString() + " INDEX CREATED");
                    }
                }
                
                // get all hyperlinks and push to queue@(level+1)
                Elements links = doc.select("a[href]");
		for (Element link : links) {
                    if(!mw.checkIfAlreadyExists(link.attr("abs:href")))
                        queue.push(link.attr("abs:href"), level + 1);
                }
            } catch (IOException ex) {
                mr.receiveMessage( ex.getMessage() + " while processing " + pageURL.toString(), id);
            }
        }
    }

    private boolean shouldProcess(String pageURL) {
        try {
            // shouldn't process the URL if it is out of domain and is junk
            return (bMgr.withInDomain(pageURL.toString()) && !bMgr.IsUrlJunk(pageURL.toString()));
        } catch (URISyntaxException ex) {
            mr.receiveMessage(ex.getReason() +"::" + ex.getMessage(), id);
        }
        return false;
    }
    
    private boolean shouldIndex(String content){
        if(content.contains("Sorry, readability was unable to parse this page for content."))
            return false;
        else if(content.length() < 100)
            return false;
        return true;
    }
        
}
