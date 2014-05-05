package newscrawl;

import ie.moguntia.threads.ControllableThread;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import jpt.FileWorker;
import newscrawl.utils.BaseURLManager;
import newscrawl.utils.ExtraStuff;
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

    @Override
    public void process(Object o) {
        URL pageURL = (URL) o;
        if( shouldProcess(pageURL.toString()) ) {
            mr.receiveMessage("Crawling " +pageURL.toString(), id);
            try {
                FileWorker fw = ( (ExtraStuff)tc.getExtra() ).getFileWorker();
                BaseURLManager bMgr = ( (ExtraStuff) tc.getExtra() ).getBaseURLManager();
                
                Document doc = Jsoup.connect(pageURL.toString())
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36")
                        .get();

                Readability readability = null;
                if( !bMgr.isBaseURL(pageURL.toString()) ){
                    readability = new Readability(doc);
                    fw.indexThis(pageURL.toString());
                    System.out.println(pageURL.toString() + " written to file");
                }
                
                // get all hyperlinks and push to queue@(level+1)
                Elements links = doc.select("a[href]");
		for (Element link : links) {
                    queue.push(link.attr("abs:href"), level + 1);
                }
            } catch (IOException ex) {
                mr.receiveMessage( ex.getMessage() + " while processing " + pageURL.toString(), id);
            }
        }
    }

    private boolean shouldProcess(String pageURL) {
        BaseURLManager baseMgr = ( (ExtraStuff)tc.getExtra() ).getBaseURLManager();
        try {
            // shouldn't process the URL if it is out of domain and is junk
            return (baseMgr.withInDomain(pageURL.toString()) && !baseMgr.IsUrlJunk(pageURL.toString()));
        } catch (URISyntaxException ex) {
            mr.receiveMessage(ex.getReason() +"::" + ex.getMessage(), id);
        }
        return false;
    }
        
}
