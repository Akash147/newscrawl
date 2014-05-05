package newscrawl;

import ie.moguntia.threads.ControllableThread;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import newscrawl.utils.BaseURLManager;
import newscrawl.utils.ExtraStuff;

/**
 *
 * @author Akash
 */
public class CrawlerThread extends ControllableThread {

    @Override
    public void process(Object o) {
        URL pageURL = (URL) o;
        if( shouldProcess(pageURL) ) {
            
        }
    }

    private boolean shouldProcess(URL pageURL) {
        BaseURLManager baseMgr = ( (ExtraStuff)tc.getExtra() ).getBaseURLManager();
        try {
            return baseMgr.withInDomain(pageURL.toString());
        } catch (URISyntaxException ex) {
            mr.receiveMessage(ex, id);
        }
        return false;
    }
    
}
