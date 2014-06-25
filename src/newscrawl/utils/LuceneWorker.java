package newscrawl.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 *
 * @author Akash
 */
public class LuceneWorker {
    private final String indexLocation;
    private Directory dir;
    private Analyzer analyzer;
    private IndexWriterConfig config;
    private IndexWriter writer;

    public LuceneWorker(String _indexLocation) {
        this.indexLocation = _indexLocation;
        this.init();
    }
    
    /**
     * Initializer called by constructor
     */
    private void init(){
        try {
            dir = FSDirectory.open(new File(indexLocation));
            analyzer = new StandardAnalyzer(Version.LUCENE_47);
            config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            
            config.setRAMBufferSizeMB(0.02);
            writer = new IndexWriter(dir, config);
        } catch (IOException ex) {
            Logger.getLogger(LuceneWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void indexThis(String newsID, String content, String title, Date date){
        try {
            Document doc = new Document();
            doc.add(new StringField("_id", newsID, Field.Store.YES));
            doc.add(new TextField("Content", content, Field.Store.NO));
            doc.add(new TextField("Title", title, Field.Store.NO));
            doc.add(new Field("Date",DateTools.dateToString(date, DateTools.Resolution.DAY), Field.Store.NO, Field.Index.NOT_ANALYZED));
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException ex) {
            Logger.getLogger(LuceneWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    public boolean checkIfAlreadyExists(String url){
//        
//    }
    
    public void close(){
        try {
            writer.close();
            System.out.println("Lucene Closed");
        } catch (IOException ex) {
            Logger.getLogger(LuceneWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
