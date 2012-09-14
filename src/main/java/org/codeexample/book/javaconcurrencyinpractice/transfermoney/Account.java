package org.codeexample.book.javaconcurrencyinpractice.transfermoney;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account
{

    public Lock lock = new ReentrantLock();

    public DollarAmount getBalance()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void debit(
            DollarAmount amount)
    {
        // TODO Auto-generated method stub
        
    }

    public void credit(
            DollarAmount amount)
    {
        // TODO Auto-generated method stub
        
    }

}
