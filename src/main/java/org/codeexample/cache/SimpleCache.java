package org.codeexample.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class SimpleCache {

  public void useGuavaCache() {

  }

  @Test
  public void testSimpleCache() {
    final int MAX_SIZE = 5;
    Set<Integer> lruCache = new LRUCacheSet<Integer>(MAX_SIZE);
    verifyLRU(lruCache);
    lruCache.clear();

    lruCache = Collections.newSetFromMap(new LRUCacheMap<Integer, Boolean>(
        MAX_SIZE));
    verifyLRU(lruCache);
    lruCache.clear();

    lruCache = Collections.newSetFromMap(new LinkedHashMap<Integer, Boolean>() {
      private static final long serialVersionUID = 1L;

      protected boolean removeEldestEntry(Map.Entry<Integer, Boolean> eldest) {
        return size() > MAX_SIZE;
      }
    });
    verifyLRU(lruCache);
  }

  public void verifyLRU(Set<Integer> lruCache) {
    for (int i = 0; i < 10; i++) {
      lruCache.add(i);
    }
    Assert.assertArrayEquals(new Integer[] { 5, 6, 7, 8, 9 },
        lruCache.toArray());
  }

  // class CacheSet2<E> extends HashSet<E> {
  // private static final long serialVersionUID = 1L;
  // private int capacity;
  // LinkedHashMap<E, Void> cacheMap;
  //
  // public CacheSet2(final int capacity) {
  // this.capacity = capacity;
  // cacheMap = new LinkedHashMap<E, Void>() {
  // private static final long serialVersionUID = 1L;
  //
  // protected boolean removeEldestEntry(Map.Entry<E, Void> eldest) {
  // return size() > capacity;
  // }
  // };
  // }
  //
  // }

  class LRUCacheSet<E> extends LinkedHashSet<E> {
    private static final long serialVersionUID = 1L;
    private int capacity;

    public LRUCacheSet(int capacity) {
      this.capacity = capacity;
    }

    @Override
    public boolean add(E e) {
      if (size() >= capacity) {
        // here, we can do anything.
        // 1. LRU cache, delete the eldest one(the first one) then add the new
        // item.
        Iterator<E> it = this.iterator();
        it.next();
        it.remove();

        // 2. We can do nothing, just return false: this will discard the new
        // item.
        // return false;
      }

      return super.add(e);

    }
  }

  class LRUCacheMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;
    private int capacity;

    public LRUCacheMap(int capacity) {
      this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
      return size() > capacity;
    }
  }

  // class CacheSet<E> implements Set<E> {
  // private Set<E> set;
  //
  // private static final long serialVersionUID = 1L;
  //
  // public CacheSet(final int capacity) {
  // set = Collections.newSetFromMap(new LinkedHashMap<E, Boolean>() {
  // private static final long serialVersionUID = 1L;
  //
  // protected boolean removeEldestEntry(Map.Entry<E, Boolean> eldest) {
  // return size() > capacity;
  // }
  // });
  // }
  // // private int capacity;
  //
  // @Override
  // public int size() {
  // // TODO Auto-generated method stub
  // return 0;
  // }
  //
  // @Override
  // public boolean isEmpty() {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public boolean contains(Object o) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public Iterator<E> iterator() {
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // @Override
  // public Object[] toArray() {
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // @Override
  // public <T> T[] toArray(T[] a) {
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  // @Override
  // public boolean add(E e) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public boolean remove(Object o) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public boolean containsAll(Collection<?> c) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public boolean addAll(Collection<? extends E> c) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public boolean retainAll(Collection<?> c) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public boolean removeAll(Collection<?> c) {
  // // TODO Auto-generated method stub
  // return false;
  // }
  //
  // @Override
  // public void clear() {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // // public CacheSet(final int capacity) {
  // // // this.capacity = capacity;
  // // super(Collections.newSetFromMap(new LinkedHashMap<E, Boolean>() {
  // // private static final long serialVersionUID = 1L;
  // //
  // // @Override
  // // protected boolean removeEldestEntry(
  // // java.util.Map.Entry<E, Boolean> eldest) {
  // // return size() > capacity;
  // // }
  // // }));
  // // }
  //
  // }
}
