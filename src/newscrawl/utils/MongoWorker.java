package newscrawl.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import java.net.UnknownHostException;
import java.util.Date;
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
            MongoClient mongoClient = new MongoClient( host , port );
            DB db = mongoClient.getDB(dbName);
            collection = db.getCollection(collectionName);
        } catch (UnknownHostException ex) {
            return false;
        }
        return true;
    }
    
    private String insert(String URL, String content, String title, String date){
        BasicDBObject document = new BasicDBObject();
	document.put("URL", URL);
	document.put("Content", content);
	document.put("Title", title);
	document.put("Date", new Date());
        WriteResult insert = collection.insert(document);
        ObjectId id = (ObjectId) document.get( "_id" );
        return id.toString();
    }
    
    public DBObject findDocumentById(String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        DBObject dbObj = collection.findOne(query);
        return dbObj;
    }
    
    public static void main(String[] args) {
        MongoWorker m = new MongoWorker("localhost", 27017, "mydb", "testData");
        String insert = m.insert("URL", "alu khau", null, null);
        System.out.println(insert);
        DBObject x = m.findDocumentById(insert);
        System.out.println(x.get("URL"));
    }
    
}
