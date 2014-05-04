package jpt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Akash
 */
public class FileWorker {
    
    public boolean checkIfAlreadyExists(String url){
        FileReader fr = null;
        try {
            fr = new FileReader("file.txt");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null) {
                if(line.equals(url))
                    return true;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FileWorker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                Logger.getLogger(FileWorker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    public synchronized void indexThis(String url){
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("myfile.txt", true)))) {
            out.println(url);
            out.close();
        }catch (IOException e) {
        //exception handling left as an exercise for the reade
        }
    }
}
