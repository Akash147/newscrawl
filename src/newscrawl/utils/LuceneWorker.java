package newscrawl.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
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
    
    /**
     * This creates index of the document provided as param.
     * @param newsID
     * @param content
     * @param title 
     */
    public void indexThis(String newsID, String content, String title){
        try {
            Document doc = new Document();
            doc.add(new StringField("newsID", newsID, Field.Store.YES));
            doc.add(new TextField("Content", content, Field.Store.YES));
            doc.add(new TextField("Title", title, Field.Store.YES));
            writer.addDocument(doc);
            writer.commit();
        } catch (IOException ex) {
            Logger.getLogger(LuceneWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close(){
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(LuceneWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
