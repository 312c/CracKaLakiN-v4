/*
 * @(#)SaveListToFile.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

//~--- classes ----------------------------------------------------------------

/**
 * An ActionListener that when triggered saves a particular list to a file
 *
 */
public class SaveListToFile implements ActionListener
{
    DefaultListModel list;

    //~--- constructors -------------------------------------------------------

    /**
     *     Constructs a new listener for saving a list to a file
     *    
     *    
     *     @param list
     *                The list to be saved
     */
    public SaveListToFile(DefaultListModel list)
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

        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            final File file = fc.getSelectedFile();

            try
            {
                final BufferedWriter bw = new BufferedWriter(
                                              new OutputStreamWriter(
                                                  new FileOutputStream(file)));

                for (int j = 0; j < list.getSize(); j++)
                {
                    final String line = (String) list.getElementAt(j);

                    bw.write(line, 0, line.length());
                    bw.newLine();
                }

                bw.close();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
