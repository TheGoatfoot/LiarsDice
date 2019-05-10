package com.everhardsoft.diorama.container;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by faisa on 12/9/2016.
 */

public class HashSegmentLinkedList<E> implements Collection<E> {
    private ConcurrentHashMap<Class, LinkedHashSet<E>> concurrentHashMap = new ConcurrentHashMap<>();
    private int SIZE = 0;
    @Override
    public int size() {
        return SIZE;
    }
    @Override
    public boolean isEmpty() {
        return (SIZE == 0);
    }
    @Override
    public boolean contains(Object o) {
        return concurrentHashMap.get(o.getClass()).contains(o);
    }
    @NonNull
    @Override
    public Iterator<E> iterator() {
        return new HashSegmentLinkedListIterator<>(concurrentHashMap);
    }
    @NonNull
    @Override
    public Object[] toArray() {
        Object[] output = new Object[SIZE];
        Iterator iterator = this.iterator();
        for(int i = 0; i < SIZE; i++)
            output[i] = iterator.next();
        return output;
    }
    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        T[] output = (T[]) new Object[SIZE];
        Iterator iterator = this.iterator();
        for(int i = 0; i < SIZE; i++)
            output[i] = (T) iterator.next();
        return output;
    }
    @Override
    public boolean add(E e) {
        if(concurrentHashMap.get(e.getClass()).add(e)) {
            SIZE++;
            return true;
        }
        return false;
    }
    private LinkedHashSet<E> tempHashSet;
    @Override
    public boolean remove(Object o) {
        tempHashSet = concurrentHashMap.get(o.getClass());
        if(tempHashSet.remove(o)) {
            SIZE--;
            if(tempHashSet.isEmpty())
                concurrentHashMap.remove(o.getClass());
            return true;
        }
        return false;
    }
    @Override
    public boolean containsAll(Collection<?> collection) {
        //TODO
        return false;
    }
    @Override
    public boolean addAll(Collection<? extends E> collection) {
        //TODO
        return false;
    }
    @Override
    public boolean removeAll(Collection<?> collection) {
        //TODO
        return false;
    }
    @Override
    public boolean retainAll(Collection<?> collection) {
        //TODO
        return false;
    }
    @Override
    public void clear() {
        concurrentHashMap.clear();
    }

    public LinkedHashSet<E> get(Class c) {
        return concurrentHashMap.get(c);
    }
}
