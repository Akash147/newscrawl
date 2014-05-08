/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package jpt;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import newscrawl.utils.Readability;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Akash
 */
public class Test {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("http://shresthaakash.com.np/hadoop-mapreduce-startover-code-java/")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.76 Safari/537.36")
                    .get();
            Readability readability = new Readability(doc);
            readability.init();
            System.out.println(readability.mongoText());
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
