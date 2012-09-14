package org.codeexample.book.javaconcurrencyinpractice.customsynchronizers;

//@ThreadSafe
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V>
{
    public GrumpyBoundedBuffer(int size)
    {
        super(size);
    }

    /**
     * Exceptions are supposed to be for exceptional conditions [EJ Item 39].
     * "Buffer is full
     * " is not an exceptional condition for a bounded buffer any more than "
     * red" is an exceptional condition for a traffic signal.
     */
    public synchronized void put(
            V v) throws BufferFullException
    {
        if (isFull())
            throw new BufferFullException();
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException
    {
        if (isEmpty())
            throw new BufferEmptyException();
        return doTake();
    }
}