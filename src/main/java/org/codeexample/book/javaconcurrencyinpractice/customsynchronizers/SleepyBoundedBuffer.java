package org.codeexample.book.javaconcurrencyinpractice.customsynchronizers;

//@ThreadSafe
/**
 * Bounded Buffer Using Crude Blocking
 * <p>
 * A condition queue gets its name because it gives a group of threadscalled the
 * wait seta way to wait for a specific condition to become true. Unlike typical
 * queues in which the elements are data items, the elements of a condition
 * queue are the threads waiting for the condition.
 * <p>
 * Just as each Java object can act as a lock, each object can also act as a
 * condition queue, and the wait, notify, and notifyAll methods in Object
 * constitute the API for intrinsic condition queues.
 * <p>
 * Object.wait atomically releases the lock and asks the OS to suspend the
 * current thread, allowing other threads to acquire the lock and therefore
 * modify the object state. Upon waking, it reacquires the lock before
 * returning. Intuitively, calling wait means "I want to go to sleep, but wake
 * me when something interesting happens",
 * <p>
 */
public class SleepyBoundedBuffer<V> extends BaseBoundedBuffer<V>
{
    private static final long SLEEP_GRANULARITY = 1000;

    public SleepyBoundedBuffer(int size)
    {
        super(size);
    }

    public void put(
            V v) throws InterruptedException
    {
        while (true)
        {
            synchronized (this)
            {
                if (!isFull())
                {
                    doPut(v);
                    return;
                }
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }

    public V take() throws InterruptedException
    {
        while (true)
        {
            synchronized (this)
            {
                if (!isEmpty())
                    return doTake();
            }
            Thread.sleep(SLEEP_GRANULARITY);
        }
    }
}