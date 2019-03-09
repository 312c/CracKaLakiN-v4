/*
 * @(#)AddToList.java   09/02/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

//~--- classes ----------------------------------------------------------------

/**
 * An ActionListener that when triggered prompts the user to add a new item
 * to a particular list
 *
 */
public class AddToList implements ActionListener
{
    private DefaultListModel list;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new listener for adding to a list
     *
     *
     * @param list
     *            The list for items to be added to
     */
    public AddToList(DefaultListModel list)
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
        String s = "";

        do
        {
            s = JOptionPane.showInputDialog(null, "Please enter an item: ",
                                            "AR 4.0",
                                            JOptionPane.QUESTION_MESSAGE);

            if (s != null)
            {
                list.addElement(s);
            }
        }
        while (s != null);
    }
}
