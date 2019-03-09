/*
 * @(#)ClearList.java   09/02/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;

//~--- classes ----------------------------------------------------------------

/**
 * An ActionListener that when triggered clears a particular list
 *
 */
public class ClearList implements ActionListener
{
    DefaultListModel list;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new listener for clearing a list
     *
     *
     * @param list
     *            The list to be cleared
     */
    public ClearList(DefaultListModel list)
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
        list.clear();
    }
}
