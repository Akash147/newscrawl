package newscrawl.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.lucene.document.DateTools;
import org.bson.types.ObjectId;

/**
 *
 * @author Akash
 */
public class MongoWorker {
    private final String host;
    private final int port;
    private final String dbName;
    private final String collectionName;
    private MongoClient mongoClient;
    private DBCollection collection;

    public MongoWorker(String host, int port, String dbName, String collectionName) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.collectionName = collectionName;
        this.establishConnection();
    }
    
    private boolean establishConnection(){
        try {
            mongoClient = new MongoClient( host , port );
            DB db = mongoClient.getDB(dbName);
            collection = db.getCollection(collectionName);
        } catch (UnknownHostException ex) {
            return false;
        }
        return true;
    }
    
    public String insert(String URL, String content, String title, Date date){
        BasicDBObject document = new BasicDBObject();
	document.put("URL", URL);
	document.put("Content", content);
	document.put("Title", title);
	document.put("Date", DateTools.dateToString(date, DateTools.Resolution.DAY));
        WriteResult insert = collection.insert(document);
        ObjectId id = (ObjectId) document.get( "_id" );
        return id.toString();
    }
    
    public void close(){
        mongoClient.close();
        System.out.println("Mongo Closed");
    }
    
    public boolean checkIfAlreadyExists(String url){
        BasicDBObject query = new BasicDBObject();
        query.put("URL", url);
        DBCursor result = collection.find(query);
        return ( result.count() > 0 );
    }
    
    public DBObject findDocumentById(String id) { // test
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBObject dbObj = collection.findOne(query);
        return dbObj;
    }
    
    public static void main(String[] args) { // test
        MongoWorker m = new MongoWorker("localhost", 27017, "mydb", "testData");
        String insert = m.insert("URL", "today", null, null);
        System.out.println(insert);
        DBObject x = m.findDocumentById(insert);
        System.out.println(x.get("Content"));
    }
    
}
