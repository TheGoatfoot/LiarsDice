package com.everhardsoft.diorama.container;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by faisa on 12/10/2016.
 */

public class DioramaElementCollection<E> implements Collection<E> {
    private class Cell {
        public E body;
        public E tail;
    }
    private int SIZE = 0;
    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        return SIZE == 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @NonNull
    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @NonNull
    @Override
    public <T> T[] toArray(T[] ts) {
        return null;
    }

    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> collection) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return false;
    }

    @Override
    public void clear() {

    }
}
