/*
 * @(#)QueueGenerator.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JList;
import javax.swing.JOptionPane;

//~--- classes ----------------------------------------------------------------

/**
 * Process that generates a list of accounts to attempt to recover
 *
 */
public class QueueGenerator extends Thread
{
    private boolean        completionAlert;
    private Queue<Account> currentQueue;
    private boolean        isRunning = true;
    private JList          lstPasswords;
    private JList          lstUsernames;
    private String         newestRecovery = "";
    private File           passwordFile;
    private final int      QUEUE_FILE = 1;
    private final int      QUEUE_LIST = 0;
    private int            queueType;
    private Component[]    toDisable;

    //~--- constructors -------------------------------------------------------

    /**
     * Called by other constructors
     *
     *
     * @param currentQueue The queue to add items to
     * @param toDisable Components to disable while queuing
     * @param lstUsernames List containing usernames to use
     * @param completionAlert Alerts the user when the queue is finished
     */
    private QueueGenerator(Queue<Account> currentQueue, Component toDisable[],
                           JList lstUsernames, boolean completionAlert)
    {
        this.toDisable       = toDisable;
        this.lstUsernames    = lstUsernames;
        this.currentQueue    = currentQueue;
        this.completionAlert = completionAlert;
    }

    /**
     * Constructs A queue generator to generate a queue using the contents of a file as passwords
     *
     *
     * @param currentQueue The queue to add items to
     * @param toDisable Components to disable while queuing
     * @param lstUsernames List containing usernames to use
     * @param completionAlert Alerts the user when the queue is finished
     * @param passwordFile File containing passwords to use
     */
    public QueueGenerator(Queue<Account> currentQueue, Component toDisable[],
                          JList lstUsernames, boolean completionAlert,
                          File passwordFile)
    {
        this(currentQueue, toDisable, lstUsernames, completionAlert);
        this.passwordFile = passwordFile;
        queueType         = QUEUE_FILE;
        start();
    }

    /**
     * Constructs A queue generator to generate a queue using the contents of a lsit as passwords
     *
     *
     * @param currentQueue The queue to add items to
     * @param toDisable Components to disable while queuing
     * @param lstUsernames List containing usernames to use
     * @param completionAlert Alerts the user when the queue is finished
     * @param lstPasswords List containing passwords to use
     */
    public QueueGenerator(Queue<Account> currentQueue, Component toDisable[],
                          JList lstUsernames, boolean completionAlert,
                          JList lstPasswords)
    {
        this(currentQueue, toDisable, lstUsernames, completionAlert);
        this.lstPasswords = lstPasswords;
        queueType         = QUEUE_LIST;
        start();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Generates a queue using passwords based on the contents of a file
     *
     */
    private void fileGeneration()
    {
        // Begins from either the start of the username list
        // or from the selected username
        int i = lstUsernames.getSelectedIndex();

        if (i < 0)
        {
            i = 0;
        }

        for (; (i < lstUsernames.getModel().getSize()) && isRunning; i++)
        {
            try
            {
                BufferedReader in =
                    new BufferedReader(new FileReader(passwordFile));
                String line;

                // Stops at the end of the password file
                while ((line = in.readLine()) != null && isRunning)
                {
                    Account a =
                        new Account(
                            (String) lstUsernames.getModel().getElementAt(i),
                            line);

                    currentQueue.enqueue(a);

                    // Pauses when the queue is full in order
                    // to conserve memory
                    while (currentQueue.isFull() && isRunning)
                    {
                        try
                        {
                            Thread.sleep(100);
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }

                    if (!newestRecovery.equals(a.getUsername()))
                    {
                        break;
                    }
                }
            }
            catch (Exception e)
            {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                                              JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Generates a queue using passwords based on the contents of a list
     *
     */
    private void listGeneration()
    {
        // Begins from either the start of the username list
        // or from the selected username
        int idxUsername = lstUsernames.getSelectedIndex();

        if (idxUsername < 0)
        {
            idxUsername = 0;
        }

        for (; (idxUsername < lstUsernames.getModel().getSize()) && isRunning;
                idxUsername++)
        {
            for (int idxPassword = 0;
                    (idxPassword < lstPasswords.getModel().getSize())
                    && isRunning;
                    idxPassword++)
            {
                Account a =
                    new Account((String) lstUsernames.getModel()
                        .getElementAt(idxUsername), (String) lstPasswords
                        .getModel().getElementAt(idxPassword));

                currentQueue.enqueue(a);

                // Pauses when the queue is full in order
                // to conserve memory
                while (currentQueue.isFull() && isRunning)
                {
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                // Jumps to the next username if an account
                // has been recovered
                if (newestRecovery.equals(a.getUsername()))
                {
                    break;
                }
            }
        }
    }

    /**
     * Runs the thread
     *
     */
    public void run()
    {
        setName("Queue Generator");

        for (int i = 0; i < toDisable.length; i++)
        {
            toDisable[i].setEnabled(false);
        }

        // Set this thread as the most important
        setPriority(Thread.MAX_PRIORITY);

        switch (queueType)
        {
        case QUEUE_LIST :
            listGeneration();

            break;
        case QUEUE_FILE :
            fileGeneration();

            break;
        default :
            listGeneration();
        }

        for (int i = 0; i < toDisable.length; i++)
        {
            toDisable[i].setEnabled(true);
        }

        // Alerts the user when the queue is empty
        if (completionAlert)
        {
            JOptionPane.showMessageDialog(null, "Completing final items.",
                                          "Queuing Completed",
                                          JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Stops the thread
     *
     */
    public void stopRunning()
    {
        isRunning = false;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Set the newest successful recovery, used to stop adding more accounts to the queue with the same username
     *
     *
     * @param newestRecovery Newest successfully recovered account
     */
    public void setNewestRecovery(String newestRecovery)
    {
        this.newestRecovery = newestRecovery;
    }
}
