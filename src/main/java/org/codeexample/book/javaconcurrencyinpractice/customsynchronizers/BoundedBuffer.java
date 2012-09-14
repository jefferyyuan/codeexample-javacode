package org.codeexample.book.javaconcurrencyinpractice.customsynchronizers;

//@ThreadSafe
/**
 * Listing 14.6. Bounded Buffer Using Condition Queues.
 * <p>
 */
public class BoundedBuffer<V> extends BaseBoundedBuffer<V>
{
    // CONDITION PREDICATE: not-full (!isFull())
    // CONDITION PREDICATE: not-empty (!isEmpty())
    public BoundedBuffer(int size)
    {
        super(size);
    }

    // BLOCKS-UNTIL: not-full
    public synchronized void put(
            V v) throws InterruptedException
    {
        while (isFull())
            wait();
        doPut(v);
        notifyAll();
    }// BLOCKS-UNTIL: not-empty

    public synchronized V take() throws InterruptedException
    {
        while (isEmpty())
            wait();
        V v = doTake();
        notifyAll();
        return v;
    }

    /**
     * Listing 14.8. Using Conditional Notification in BoundedBuffer.put.
     */
    public synchronized void put2(
            V v) throws InterruptedException
    {
        while (isFull())
            wait();
        boolean wasEmpty = isEmpty();
        doPut(v);
        if (wasEmpty)
            notifyAll();
    }
}