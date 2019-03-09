/*
 * @(#)Account.java   09/04/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

/**
 * Simple class for storing the username and password of an account
 *
 */
public class Account
{
    private String password = "";
    private String username = "";

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new account
     *
     *
     * @param username
     *            Username associated with the account
     * @param password
     *            Password associated with the account
     */
    public Account(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Returns a representation of the account
     *
     *
     * @return The username and password as a String
     */
    public String toString()
    {
        return password.equals("")
               ? username
               : username + ":" + password;
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the password of the account
     *
     *
     * @return The account's password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Returns the username of the account
     *
     *
     * @return The account's username
     */
    public String getUsername()
    {
        return username;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the password
     *
     *
     * @param password
     *            The account's password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Sets the username
     *
     *
     * @param username
     *            The account's username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }
}
