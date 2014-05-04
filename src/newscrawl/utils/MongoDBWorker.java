package newscrawl.utils;

/**
 *
 * @author Akash
 */
public class MongoDBWorker {
    private final String host;
    private final int port;
    private final String user;
    private final String pass;
    private final String db;

    public MongoDBWorker(String _host, int _port, String _user, String _pass, String _db) {
        this.host = _host;
        this.port = _port;
        this.user = _user;
        this.pass = _pass;
        this.db = _db;
        this.establishConnection();
    }
    
    private void establishConnection(){
        
    }

    
}
