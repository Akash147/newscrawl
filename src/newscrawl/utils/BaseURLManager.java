package newscrawl.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * BaseURLManager manages everything related to the seed URL. It keeps list of
 * base URLs, their domain names.
 * @author Akash
 */
public class BaseURLManager {
    private ArrayList<String> baseURL;
    private ArrayList<String> domain;

    public BaseURLManager() {
        baseURL = new ArrayList<>();
        domain = new ArrayList<>();
    }
    
    /**
     * Used for adding a new seed URL.
     * Appends it to baseURL arraylist and determines the domain name of 
     * corresponding base URL, for using to it check whether crawler is going out
     * of domain or not. There is no use in crawling outside than the provided 
     * website domain name.
     * @param URL
     * @throws URISyntaxException 
     */
    public void add(String URL) throws URISyntaxException{
        baseURL.add(URL);
        domain.add( (new URI(URL)).getHost());
    }
    
    /**
     * Checks if the provided URL is one of the base URL.
     * We don't want to save content from base URL. So it is checked before storing.
     * @param URL
     * @return 
     */
    public boolean isBaseURL(String URL){
//        System.out.println("Check isBaseURL: " + URL + " : " + baseURL.contains(URL));
        return baseURL.contains(URL);
    }
    
    /**
     * Is provided URL within domain?
     * @param URL
     * @return
     * @throws URISyntaxException 
     */
    public boolean withInDomain(String URL) throws URISyntaxException{
        URI t = new URI(URL);
//        System.out.println("Check withinDomain: " + URL + " : " + domain.contains( t.getHost() ));
        return domain.contains( t.getHost() );
    }
    
    /**
     * Get list of seeds.
     * @return 
     */
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
    /**
     * Matches the above pattern to check whether URL is junky. URLs with css, js,
     * jpeg... at the end are junky. We don't crawl them.
     * @param URL
     * @return 
     */
    public boolean IsUrlJunk(String URL){
//        System.out.println("Check isURLJunk: " + URL + " : " + FILTERS.matcher(URL).matches());
        return FILTERS.matcher(URL).matches();
    }
}
