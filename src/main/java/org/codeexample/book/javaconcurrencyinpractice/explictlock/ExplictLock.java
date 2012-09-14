package org.codeexample.book.javaconcurrencyinpractice.explictlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ExplictLock
{
    public Lock lock = new ReentrantLock();

    /**
     * Interruptible Lock Acquisition
     * <p>
     * Interruptible lock acquisition allows locking to be used within
     * cancellable activities The lockInterruptibly method allows you to try to
     * acquire a lock while remaining responsive to interruption.
     * <p>
     * The timed TRyLock is also responsive to interruption and so can be used
     * when you need both timed and interruptible lock acquisition.
     */
    public boolean sendOnSharedLine(
            String message) throws InterruptedException
    {
        lock.lockInterruptibly();
        try
        {
            return cancellableSendOnSharedLine(message);
        }
        finally
        {
            lock.unlock();
        }
    }

    private boolean cancellableSendOnSharedLine(
            String message) throws InterruptedException
    {
        return false;
    }
}
