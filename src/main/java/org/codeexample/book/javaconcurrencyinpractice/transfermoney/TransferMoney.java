package org.codeexample.book.javaconcurrencyinpractice.transfermoney;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TransferMoney
{

    /**
     * From book: Java Concurrency in Practice
     * <p>
     * Listing 13.3. Avoiding Lock-ordering Deadlock
     * <p>
     * Using trylock. Polled and Timed Lock Acquisition Using timed or polled
     * lock acquisition (TRyLock) lets you regain control if you cannot acquire
     * all the required locks, release the ones you did acquire, and try again.
     */
    public static boolean transferMoney(
            Account fromAcct, Account toAcct, DollarAmount amount, long timeout, TimeUnit unit)
            throws InsufficientFundsException, InterruptedException
    {
        Random rnd = new Random();
        long fixedDelay = getFixedDelayComponentNanos(timeout, unit);
        long randMod = getRandomDelayModulusNanos(timeout, unit);
        long stopTime = System.nanoTime() + unit.toNanos(timeout);
        while (true)
        {
            if (fromAcct.lock.tryLock())
            {
                try
                {
                    if (toAcct.lock.tryLock())
                    {
                        try
                        {
                            if (fromAcct.getBalance().compareTo(amount) < 0)
                                throw new InsufficientFundsException();
                            else
                            {
                                fromAcct.debit(amount);
                                toAcct.credit(amount);
                                return true;
                            }
                        }
                        finally
                        {
                            toAcct.lock.unlock();
                        }
                    }
                }
                finally
                {
                    fromAcct.lock.unlock();
                }
            }
            if (System.nanoTime() < stopTime)
                return false;
            TimeUnit.NANOSECONDS.sleep(fixedDelay + rnd.nextLong() % randMod);
        }
    }

    private static long getRandomDelayModulusNanos(
            long timeout, TimeUnit unit)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    private static long getFixedDelayComponentNanos(
            long timeout, TimeUnit unit)
    {
        // TODO Auto-generated method stub
        return 0;
    }
}
