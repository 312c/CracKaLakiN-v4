/*
 * @(#)rpmTimer.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

/**
 * Thread that acts as a timer for counting uptime and
 * calculating the average rate per minute
 *
 */
public class RpmTimer extends Thread
{
    private int     timeRan = 1;
    private boolean paused  = true;
    private boolean running = true;
    private AR_v4   owner;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs A new timer
     *
     *
     * @param newOwner Owner of the timer
     */
    public RpmTimer(AR_v4 newOwner)
    {
        owner = newOwner;
        setName("RPM Timer");
        start();
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the total time ran
     *
     *
     * @return Time thread has been running
     */
    public int getTimeRan()
    {
        return timeRan;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Returns the hours, minutes, and seconds the timer has been running
     *
     *
     * @return Time running
     */
    public String toString()
    {
        int hr  = (int) timeRan / 3600;
        int min = (int) timeRan / 60 - hr * 60;
        int sec = (int) timeRan % 60;

        return hr + ":" + min + ":" + sec;
    }

    /**
     * Stops the thread
     *
     */
    public void stopRunning()
    {
        running = false;
    }

    /**
     * Runs the thread
     *
     */
    public void run()
    {
        while (running)
        {
            if (!paused)
            {
                try
                {
                    sleep(1000);
                    timeRan++;
                    owner.updateTime();
                }
                catch (final InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Toggles if the thread is paused
     *
     *
     * @param newPaused Pause state of the thread
     */
    public void setPaused(boolean newPaused)
    {
        paused = newPaused;
    }
}
