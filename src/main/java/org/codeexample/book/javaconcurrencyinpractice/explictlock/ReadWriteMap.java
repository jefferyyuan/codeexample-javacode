package org.codeexample.book.javaconcurrencyinpractice.explictlock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Read-write Locks a resource can be accessed by multiple readers or a single
 * writer at a time, but not both. ReadWriteLock, exposes two Lock objectsone
 * for reading and one for writing. To read data guarded by a ReadWriteLock you
 * must first acquire the read lock, and to modify data guarded by a
 * ReadWriteLock you must first acquire the write lock. While there may appear
 * to be two separate locks, the read lock and write lock are simply different
 * views of an integrated readwrite lock object.
 */
public class ReadWriteMap<K, V>
{
    private final Map<K, V> map;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock r = lock.readLock();
    private final Lock w = lock.writeLock();

    public ReadWriteMap(Map<K, V> map)
    {
        this.map = map;
    }

    public V put(
            K key, V value)
    {
        w.lock();
        try
        {
            return map.put(key, value);
        }
        finally
        {
            w.unlock();
        }
    }// Do the same for remove(), putAll(), clear()

    public V get(
            Object key)
    {
        r.lock();
        try
        {
            return map.get(key);
        }
        finally
        {
            r.unlock();
        }
    }
    // Do the same for other read-only Map methods
}