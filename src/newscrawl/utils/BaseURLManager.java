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

    public BaseURLManager() {
        baseURL = new ArrayList<>();
        domain = new ArrayList<>();
    }
    
    public void add(String URL) throws URISyntaxException{
        baseURL.add(URL);
        domain.add( (new URI(URL)).getHost());
    }
    
    public boolean isBaseURL(String URL){
        System.out.println("Check isBaseURL: " + baseURL.contains(URL));
        return baseURL.contains(URL);
    }
    
    public boolean withInDomain(String URL) throws URISyntaxException{
        URI t = new URI(URL);
        System.out.println("Check withinDomain: " + domain.contains( t.getHost() ));
        return domain.contains( t.getHost() );
    }

    public ArrayList<String> getBaseURL() {
        return baseURL;
    }
    
    /**
     * Check if the URL is css,js etc.... which we would not crawl
     */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g" 
                                                          + "|png|tiff?|mid|mp2|mp3|mp4"
                                                          + "|wav|avi|mov|mpeg|ram|m4v|pdf" 
                                                          + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
    public boolean IsUrlJunk(String URL){
        System.out.println("Check isURLJunk: " + FILTERS.matcher(URL).matches());
        return FILTERS.matcher(URL).matches();
    }
}
