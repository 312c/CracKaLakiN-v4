/*
 * @(#)Queue.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

/**
 * Stores a queue of items, may be set to have a maximum length
 *
 *
 */
public class Queue<T>
{
    private Node first;
    private Node last;
    private int  maxSize = -1;
    private int  N;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new Queue with no maximum size
     *
     */
    public Queue()
    {
    }

    /**
     * Constructs a new queue that has a maximum length
     *
     *
     * @param maxSize Maximum allowed length for the queue
     */
    public Queue(int maxSize)
    {
        this.maxSize = maxSize;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Returns the first item in the queue
     * @return
     *
     *
     * @return The item at the front of the queue
     */
    public T dequeue()
    {
        if (!isEmpty())
        {
            T item = first.item;

            first = first.next;
            N--;

            return item;
        }
        else
        {
            return null;
        }
    }

    /**
     * Empties the contents of the queue
     *
     */
    public void emptyQueue()
    {
        first = null;
        last  = null;
        N     = 0;
    }

    /**
     *     Adds an item to the queue
     *    
     *    
     *     @param item
     *                The item to add to the queue
     *    
     *     @return True if the item was successfully added, otherwise return false
     */
    public boolean enqueue(T item)
    {
        if (!isFull())
        {
            Node x = new Node(item, null);

            if (isEmpty())
            {
                first = x;
                last  = x;
            }
            else
            {
                last.next = x;
                last      = x;
            }

            N++;

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the number of accounts in the queue
     *
     *
     * @return Length of the queue
     */
    public int length()
    {
        return N;
    }

    /**
     * Returns the size of the queue
     *
     *
     * @return Size of the queue
     */
    public int size()
    {
        return N;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Set the maximum size of the queue, a negative number is the same as having no max size
     *
     *
     * @param maxSize The maximum allowed size for the queue
     */
    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns if the queue is empty
     *
     *
     * @return True if the queue is empty, else False
     */
    public boolean isEmpty()
    {
        return first == null;
    }

    /**
     * Returns if the queue is full
     *
     *
     * @return True if the queue is full, false if it is not full or there is no max size
     */
    public boolean isFull()
    {
        return (maxSize < 0)
               ? false
               : !(length() < maxSize);
    }

    //~--- inner classes ------------------------------------------------------

    /**
     * Small wrapper for storing items in a queue
     *
     *
     */
    private class Node
    {
        private T    item;
        private Node next;

        //~--- constructors ---------------------------------------------------

        /**
         * Constructs a new Node for storing an item
         *
         *
         * @param item What to store
         * @param next The next node
         */
        Node(T item, Node next)
        {
            this.item = item;
            this.next = next;
        }
    }
}
