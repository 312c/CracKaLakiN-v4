/*
 * @(#)LoadList.java   09/02/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

//~--- classes ----------------------------------------------------------------

/**
 * An ActionListener that when triggered adds items to a particular list
 * from a particular file
 *
 */
public class LoadList implements ActionListener
{
    DefaultListModel list;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new listener for adding to a list from a file
     *
     *
     * @param list
     *            The list to be added to
     */
    public LoadList(DefaultListModel list)
    {
        this.list = list;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Called when the listener is triggered
     *
     *
     * @param e
     *            The action event
     */
    public void actionPerformed(ActionEvent e)
    {
        final JFileChooser fc = new JFileChooser();

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            final File file = fc.getSelectedFile();

            addFileToList(file, list);
        }
    }

    /**
     * Adds each line in a file to the list as an item
     *
     *
     * @param file
     *            File to read from
     * @param list
     *            List to add to
     */
    private void addFileToList(final File file, final DefaultListModel list)
    {
        try
        {
            final BufferedReader br = new BufferedReader(
                                          new InputStreamReader(
                                              new FileInputStream(file)));
            String line;

            while ((line = br.readLine()) != null)
            {
                if (line.indexOf(":") == -1)
                {
                    list.addElement(line);
                }
            }

            br.close();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
    }
}
