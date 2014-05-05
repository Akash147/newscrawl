package newscrawl.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author Akash
 */
public class BaseURLManager {
    private ArrayList<String> baseURL;
    private ArrayList<String> domain;
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                          + "|png|tiff?|mid|mp2|mp3|mp4"
                                                          + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                          + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    public BaseURLManager() {
        baseURL = new ArrayList<>();
        domain = new ArrayList<>();
    }
    
    public void add(String URL) throws URISyntaxException{
        baseURL.add(URL);
        domain.add( (new URI(URL)).getHost());
    }
    
    public boolean isBaseURL(String URL){
        return baseURL.contains(URL);
    }
    
    public boolean withInDomain(String URL) throws URISyntaxException{
        URI t = new URI(URL);
        return domain.contains( t.getHost() );
    }

    public ArrayList<String> getBaseURL() {
        return baseURL;
    }
    
    
}
