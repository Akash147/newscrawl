/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newscrawl.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Akash
 */
public class URLQueue implements ie.moguntia.threads.Queue {
    private MongoWorker fileWorker;
    private LinkedList evenQueue;
    private LinkedList oddQueue;
    
    public void setFileWorker(MongoWorker _fileWorker){
        fileWorker = _fileWorker;
    }

    public URLQueue() {
        evenQueue = new LinkedList();
        oddQueue = new LinkedList();
    }

    /**
     * Add base URLs for crawling
     * @param baseURL 
     */
    public void addSeed(String _baseURL){
        this.push(_baseURL, 0); // push the baseURL to level zero
    }
    
    @Override
    public Set getGatheredElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set getProcessedElements() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getQueueSize(int level) {
		if (level % 2 == 0) {
			return evenQueue.size();
		} else {
			return oddQueue.size();
		}
    }

    @Override
    public int getProcessedSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getGatheredSize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMaxElements(int elements) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object pop(int level) {
        String s;
        // try to get element from the appropriate queue
        // is the queue is empty, return null
        if (level % 2 == 0) {
            if (evenQueue.size() == 0) {
                return null;
            } else {
                s = (String) evenQueue.removeFirst();
            }
        } else {
            if (oddQueue.size() == 0) {
                return null;
            } else {
                s = (String) oddQueue.removeFirst();
            }
        }
        // convert the string to a url and add to the set of processed links
        try {
            URL url = new URL(s);
            return url;
        } catch (MalformedURLException e) {
            // shouldn't happen, as only URLs can be pushed
            return null;
        }
    }

    @Override
    public boolean push(Object url, int level) {
        String s = (String) url;
        if (!fileWorker.checkIfAlreadyExists(s)) {
            // has not been in set yet, so add to the appropriate queue
            if (level % 2 == 0) {
                evenQueue.addLast(s);
            } else {
                oddQueue.addLast(s);
            }
            return true;
        } else {
            // this link has already been gathered
            return false;
        }
    }

    @Override
    public void clear() {
        evenQueue.clear();
        oddQueue.clear();
    }
    
}
