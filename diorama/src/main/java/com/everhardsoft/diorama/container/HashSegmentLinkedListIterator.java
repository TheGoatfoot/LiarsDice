package com.everhardsoft.diorama.container;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by faisa on 12/9/2016.
 */

public class HashSegmentLinkedListIterator<E> implements Iterator<E> {
    private final ConcurrentHashMap<Class, LinkedHashSet<E>> concurrentHashMap;
    private final Enumeration<Class> keysEnumaration;
    private Iterator<E> currentHashSetIterator;
    public HashSegmentLinkedListIterator(ConcurrentHashMap<Class, LinkedHashSet<E>> concurrentHashMap) {
        this.concurrentHashMap = concurrentHashMap;
        keysEnumaration = concurrentHashMap.keys();
        currentHashSetIterator = concurrentHashMap.get(keysEnumaration.nextElement()).iterator();
    }
    @Override
    public boolean hasNext() {
        if(currentHashSetIterator.hasNext())
            return true;
        return keysEnumaration.hasMoreElements();
    }
    @Override
    public E next() {
        if(!currentHashSetIterator.hasNext()) {
            if (keysEnumaration.hasMoreElements())
                currentHashSetIterator = concurrentHashMap.get(keysEnumaration.nextElement()).iterator();
            else
                return null;
        }
        return currentHashSetIterator.next();
    }
}
