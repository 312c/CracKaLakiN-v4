/*
 * @(#)RemoveFromList.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JList;

//~--- classes ----------------------------------------------------------------

/**
 * An ActionListener that when triggered removes selected items from a
 * particular list
 *
 */
public class RemoveFromList implements ActionListener
{
    DefaultListModel list;
    JList            refList;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new listener for removing items from a list
     *
     *
     * @param list
     *            The list for items to be removed from
     * @param refList
     *            The visual representation of the list
     */
    public RemoveFromList(DefaultListModel list, JList refList)
    {
        this.list    = list;
        this.refList = refList;
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
        final int indexes[] = refList.getSelectedIndices();

        for (int i = 0; i < indexes.length; i++)
        {
            list.remove(indexes[i] - i);
        }

        list.trimToSize();
    }
}
