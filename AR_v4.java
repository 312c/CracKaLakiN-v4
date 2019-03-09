/*
 * @(#)AR_v4.java   10/18/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

//~--- classes ----------------------------------------------------------------

/**
 * A class for recovering AIM and AOL accounts
 *
 *
 */
public class AR_v4 extends javax.swing.JFrame
{
    private JMenuItem              mnuAbout;
    private boolean                userAlert       = true;
    private Vector<JComponent>     allObjects      = new Vector<JComponent>();
    private final Image            back            =
        new ImageIcon("Background.png").getImage();
    private Color                  backgroundColor = Color.BLACK;
    private JButton                btnStart;
    private JButton                btnStop;
    private JCheckBoxMenuItem      chkAlert;
    private int                    configLine;
    private String                 configString;
    private final DefaultListModel recoveryListModel = new DefaultListModel();
    private int                    fileNum           = -1;
    private Color                  foregroundColor   = Color.WHITE;
    private final ImageIcon        imgIcon           =
        new ImageIcon("Icon.png");
    private boolean                isPaused          = false;
    private Boolean                isStarted         = false;
    private JLabel                 lblRecentAttempt;
    private JLabel                 lblRPM;
    private JLabel                 lblStats;
    private JLabel                 lblStatus;
    private JList                  lstRecovered;
    private JList                  lstPasswords;
    private JList                  lstUsernames;
    private JMenuItem              mnuAddPassword;
    private JMenuItem              mnuAddUsername;
    private JMenuItem              mnuClearRecovered;
    private JMenuItem              mnuClearPasswords;
    private JMenuItem              mnuClearUsernames;
    private JMenuItem              mnuColors;
    private JMenu                  mnuRecovery;
    private JMenuItem              mnuExit;
    private JMenu                  mnuFile;
    private JMenuItem              mnuLoadPasswordList;
    private JMenuItem              mnuLoadUsernameList;
    private JMenuBar               mnuMainMenu;
    private JMenu                  mnuMisc;
    private JMenuItem              mnuNumThreads;
    private JMenuItem              mnuPassFileName;
    private JMenu                  mnuPasswords;
    private JMenuItem              mnuRemovePassword;
    private JMenuItem              mnuRemoveUsername;
    private JMenuItem              mnuSaveRecovered;
    private JMenuItem              mnuSavePasswordList;
    private JMenuItem              mnuSaveUsernameList;
    private JMenu                  mnuUsernames;
    private Queue<Account>         recoveryQueue     = new Queue<Account>();
    private int                    recoverySuccesses = 0;
    private int                    recoveryAttempts  = 0;
    private File                   passFile          = null;
    private final DefaultListModel passwordListModel = new DefaultListModel();
    private JPanel                 pnlBackground;
    private Object[]               socketSelect;
    private QueueGenerator         queueGen = null;
    private JRadioButtonMenuItem   radPassFile;
    private JRadioButtonMenuItem   radPassList;
    private RecoveryThread[]       recoveryThreads   = null;
    private final DefaultListModel usernameListModel = new DefaultListModel();
    private JSeparator             sepRecovery;
    private JSeparator             sepPasswords;
    private JSeparator             sepPasswords2;
    private JSeparator             sepPasswords3;
    private JSeparator             sepUsernames;
    private JSeparator             sepUsernames2;
    private JScrollPane            spRecovered;
    private JScrollPane            spPasswords;
    private JScrollPane            spUsernames;
    private final TrayIcon         ti = new TrayIcon(imgIcon.getImage(),
                                            "AOL Account Recovery v4");
    private RpmTimer time            = new RpmTimer(this);
    private int      totalNumThreads = 35;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new instance of the program
     *
     */
    public AR_v4()
    {
        super();
        loadConfig();
        setSockets();
        setName("GUI");
        initGUI();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Main method for running the program
     *
     *
     * @param args
     *            command line arguments
     */
    public static void main(final String[] args)
    {
        final AR_v4 inst = new AR_v4();

        inst.setVisible(true);
    }

    /**
     * Initializes the GUI
     *
     */
    private void initGUI()
    {
        try
        {
            setTitle("AOL Account Recovery v4");

            final AR_v4      cms = this;
            final ColorPanel cp  = new ColorPanel(this);

            // Adds a listener to the frame
            addWindowListener(new WindowListener()
            {
                public void windowActivated(WindowEvent arg0)
                {
                    // Unused
                }
                public void windowClosed(WindowEvent arg0)
                {
                    System.exit(0);
                }
                public void windowClosing(WindowEvent arg0)
                {
                    // Unused
                }
                public void windowDeactivated(WindowEvent arg0)
                {
                    // Unused
                }
                public void windowDeiconified(WindowEvent arg0)
                {
                    // Unused
                }
                public void windowIconified(WindowEvent arg0)
                {
                    // Upon minimization, the frame is hidden and an icon
                    // is added to the system tray
                    ti.setImageAutoSize(true);

                    try
                    {
                        SystemTray.getSystemTray().add(ti);
                        cms.setVisible(false);
                        ti.addMouseListener(new MouseListener()
                        {
                            public void mouseClicked(MouseEvent arg0)
                            {
                                SystemTray.getSystemTray().remove(ti);
                                cms.setVisible(true);
                                cms.setState(JFrame.NORMAL);
                            }
                            public void mouseEntered(MouseEvent arg0)
                            {
                                // Unused
                            }
                            public void mouseExited(MouseEvent arg0)
                            {
                                // Unused
                            }
                            public void mousePressed(MouseEvent arg0)
                            {
                                // Unused
                            }
                            public void mouseReleased(MouseEvent arg0)
                            {
                                // Unused
                            }
                        });
                    }
                    catch (AWTException e)
                    {
                        e.printStackTrace();
                    }
                }
                public void windowOpened(WindowEvent arg0)
                {
                    // Unused
                }
            });
            setResizable(false);
            setIconImage(imgIcon.getImage());

            // Centers the window
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            {
                if ((new File("Background.png")).exists())
                {
                    pnlBackground = new JPanel()
                    {
                        // Overrides the default JPanel paint method to paint the
                        // background image
                        public void paintComponent(Graphics g)
                        {
                            g.drawImage(back, 0, 0, null);
                        }
                    };
                }
                else
                {
                    pnlBackground = new JPanel();
                }

                GridBagLayout thisLayout = new GridBagLayout();

                pnlBackground.setLayout(thisLayout);
                getContentPane().add(pnlBackground, BorderLayout.CENTER);
                {
                    lstUsernames = new JList();
                    spUsernames  = new JScrollPane(lstUsernames);
                    pnlBackground.add(spUsernames,
                                      new GridBagConstraints(1, 1, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    spUsernames.setOpaque(false);
                    spUsernames.getViewport().setOpaque(false);
                    spUsernames.setBorder(
                        BorderFactory.createLineBorder(
                            new java.awt.Color(0, 0, 0), 3));
                    lstUsernames.setModel(usernameListModel);
                    usernameListModel.addListDataListener(
                        new ListDataListener()
                    {
                        @Override public void contentsChanged(
                                ListDataEvent arg0)
                        {
                            mnuUsernames.setText("Usernames ["
                                                 + usernameListModel.size()
                                                 + "]");
                        }
                        @Override public void intervalAdded(ListDataEvent arg0)
                        {
                            mnuUsernames.setText("Usernames ["
                                                 + usernameListModel.size()
                                                 + "]");
                        }
                        @Override public void intervalRemoved(
                                ListDataEvent arg0)
                        {
                            mnuUsernames.setText("Usernames ["
                                                 + usernameListModel.size()
                                                 + "]");
                        }
                    });
                    lstUsernames.setOpaque(false);
                    allObjects.add(lstUsernames);
                }
                {
                    lstPasswords = new JList();
                    spPasswords  = new JScrollPane(lstPasswords);
                    pnlBackground.add(spPasswords,
                                      new GridBagConstraints(3, 1, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    lstPasswords.setModel(passwordListModel);
                    passwordListModel.addListDataListener(
                        new ListDataListener()
                    {
                        @Override public void contentsChanged(
                                ListDataEvent arg0)
                        {
                            mnuPasswords.setText("Passwords ["
                                                 + passwordListModel.size()
                                                 + "]");
                        }
                        @Override public void intervalAdded(ListDataEvent arg0)
                        {
                            mnuPasswords.setText("Passwords ["
                                                 + passwordListModel.size()
                                                 + "]");
                        }
                        @Override public void intervalRemoved(
                                ListDataEvent arg0)
                        {
                            mnuPasswords.setText("Passwords ["
                                                 + passwordListModel.size()
                                                 + "]");
                        }
                    });
                    spPasswords.setOpaque(false);
                    spPasswords.setBorder(
                        BorderFactory.createLineBorder(
                            new java.awt.Color(0, 0, 0), 3));
                    spPasswords.getViewport().setOpaque(false);
                    allObjects.add(lstPasswords);
                    lstPasswords.setOpaque(false);
                }
                {
                    lstRecovered = new JList();
                    spRecovered  = new JScrollPane(lstRecovered);
                    pnlBackground.add(spRecovered,
                                      new GridBagConstraints(5, 1, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.BOTH,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    spRecovered.setBorder(
                        BorderFactory.createLineBorder(
                            new java.awt.Color(0, 0, 0), 3));
                    lstRecovered.setModel(recoveryListModel);
                    spRecovered.setOpaque(false);
                    spRecovered.getViewport().setOpaque(false);
                    lstRecovered.setOpaque(false);
                    allObjects.add(lstRecovered);
                }
                {
                    if ((new File("Start.png")).exists())
                    {
                        btnStart = new JButton(new ImageIcon("Start.png"));
                        btnStart.setOpaque(false);
                    }
                    else
                    {
                        btnStart = new JButton("Start");
                    }

                    Dimension dS = new Dimension(62, 26);

                    btnStart.setSize(dS);
                    btnStart.setMaximumSize(dS);
                    btnStart.setMinimumSize(dS);
                    btnStart.setPreferredSize(dS);
                    btnStart.setBorder(null);
                    pnlBackground.add(btnStart,
                                      new GridBagConstraints(1, 3, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.NONE,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    allObjects.add(btnStart);
                    btnStart.addActionListener(new ActionListener()
                    {
                        @Override public void actionPerformed(ActionEvent e)
                        {
                            startRecovery();
                        }
                    });
                }
                {
                    if ((new File("Stop.png")).exists())
                    {
                        btnStop = new JButton(new ImageIcon("Stop.png"));
                        btnStop.setOpaque(false);
                    }
                    else
                    {
                        btnStop = new JButton("Stop");
                    }

                    pnlBackground.add(btnStop,
                                      new GridBagConstraints(3, 3, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.NONE,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    allObjects.add(btnStop);

                    Dimension dS = new Dimension(62, 26);

                    btnStop.setSize(dS);
                    btnStop.setMaximumSize(dS);
                    btnStop.setMinimumSize(dS);
                    btnStop.setPreferredSize(dS);
                    btnStop.setBorder(null);
                    btnStop.addActionListener(new ActionListener()
                    {
                        @Override public void actionPerformed(ActionEvent e)
                        {
                            stopRecovery();
                        }
                    });
                }
                {
                    lblRPM = new JLabel();
                    pnlBackground.add(lblRPM,
                                      new GridBagConstraints(1, 5, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    lblRPM.setText("Avg RPM: N/A");
                    lblRPM.setHorizontalAlignment(JLabel.CENTER);
                }
                {
                    lblStatus = new JLabel();
                    pnlBackground.add(lblStatus,
                                      new GridBagConstraints(3, 5, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    lblStatus.setText("Stopped");
                    lblStatus.setHorizontalAlignment(JLabel.CENTER);
                }
                {
                    lblStats = new JLabel();
                    pnlBackground.add(lblStats,
                                      new GridBagConstraints(5, 3, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    lblStats.setText("Attempts: " + recoveryAttempts
                                     + " - Successful: " + recoverySuccesses);
                    lblStats.setHorizontalAlignment(JLabel.CENTER);
                }
                {
                    lblRecentAttempt = new JLabel();
                    pnlBackground.add(lblRecentAttempt,
                                      new GridBagConstraints(5, 5, 1, 1, 0.0,
                                          0.0, GridBagConstraints.CENTER,
                                          GridBagConstraints.HORIZONTAL,
                                          new Insets(0, 0, 0, 0), 0, 0));
                    lblRecentAttempt.setText("Username:Password");
                    lblRecentAttempt.setHorizontalAlignment(JLabel.CENTER);
                }
                thisLayout.rowWeights    = new double[]
                {
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0
                };
                thisLayout.rowHeights    = new int[]
                {
                    34, 151, 7, 7, 7, 20
                };
                thisLayout.columnWeights = new double[]
                {
                    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
                };
                thisLayout.columnWidths  = new int[]
                {
                    26, 130, 8, 130, 8, 250, 23
                };
            }

            {
                mnuMainMenu = new JMenuBar();
                allObjects.add(mnuMainMenu);
                setJMenuBar(mnuMainMenu);
                {
                    mnuFile = new JMenu();
                    allObjects.add(mnuFile);
                    mnuMainMenu.add(mnuFile);
                    mnuFile.setText("File");
                    {
                        if ((new File("About.png")).exists())
                        {
                            mnuAbout =
                                new JMenuItem(null,
                                              new ImageIcon("About.png"));
                        }
                        else
                        {
                            mnuAbout = new JMenuItem("Coded by: 312c");
                        }

                        allObjects.add(mnuAbout);
                        mnuFile.add(mnuAbout);

                        JSeparator sepFile = new JSeparator();

                        allObjects.add(sepFile);
                        mnuFile.add(sepFile);
                    }
                    {
                        mnuExit = new JMenuItem();
                        allObjects.add(mnuExit);
                        mnuFile.add(mnuExit);
                        mnuExit.setText("Exit");
                        mnuExit.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(final ActionEvent evt)
                            {
                                SystemTray.getSystemTray().remove(ti);
                                System.exit(0);
                            }
                        });
                    }
                }
                {
                    mnuUsernames = new JMenu();
                    allObjects.add(mnuUsernames);
                    mnuMainMenu.add(mnuUsernames);
                    mnuUsernames.setText("Usernames");
                    {
                        mnuLoadUsernameList = new JMenuItem();
                        allObjects.add(mnuLoadUsernameList);
                        mnuUsernames.add(mnuLoadUsernameList);
                        mnuLoadUsernameList.setText("Load List");
                        mnuLoadUsernameList.addActionListener(
                            new LoadList(usernameListModel));
                    }
                    {
                        mnuSaveUsernameList = new JMenuItem();
                        allObjects.add(mnuSaveUsernameList);
                        mnuUsernames.add(mnuSaveUsernameList);
                        mnuSaveUsernameList.setText("Save List");
                        mnuSaveUsernameList.addActionListener(
                            new SaveListToFile(usernameListModel));
                    }
                    {
                        sepUsernames = new JSeparator();
                        allObjects.add(sepUsernames);
                        mnuUsernames.add(sepUsernames);
                    }
                    {
                        mnuAddUsername = new JMenuItem();
                        allObjects.add(mnuAddUsername);
                        mnuUsernames.add(mnuAddUsername);
                        mnuAddUsername.setText("Add Username");
                        mnuAddUsername.addActionListener(
                            new AddToList(usernameListModel));
                    }
                    {
                        mnuRemoveUsername = new JMenuItem();
                        allObjects.add(mnuRemoveUsername);
                        mnuUsernames.add(mnuRemoveUsername);
                        mnuRemoveUsername.setText("Remove Username");
                        mnuRemoveUsername.addActionListener(
                            new RemoveFromList(
                                usernameListModel, lstUsernames));
                    }
                    {
                        sepUsernames2 = new JSeparator();
                        allObjects.add(sepUsernames2);
                        mnuUsernames.add(sepUsernames2);
                    }
                    {
                        mnuClearUsernames = new JMenuItem();
                        allObjects.add(mnuClearUsernames);
                        mnuUsernames.add(mnuClearUsernames);
                        mnuClearUsernames.setText("Clear List");
                        mnuClearUsernames.addActionListener(
                            new ClearList(usernameListModel));
                    }
                }
                {
                    mnuPasswords = new JMenu();
                    allObjects.add(mnuPasswords);
                    mnuMainMenu.add(mnuPasswords);
                    mnuPasswords.setText("Passwords");
                    {
                        mnuLoadPasswordList = new JMenuItem();
                        allObjects.add(mnuLoadPasswordList);
                        mnuPasswords.add(mnuLoadPasswordList);
                        mnuLoadPasswordList.setText("Load List");
                        mnuLoadPasswordList.addActionListener(
                            new LoadList(passwordListModel));
                    }
                    {
                        mnuSavePasswordList = new JMenuItem();
                        allObjects.add(mnuSavePasswordList);
                        mnuPasswords.add(mnuSavePasswordList);
                        mnuSavePasswordList.setText("Save List");
                        mnuSavePasswordList.addActionListener(
                            new SaveListToFile(passwordListModel));
                    }
                    {
                        sepPasswords = new JSeparator();
                        allObjects.add(sepPasswords);
                        mnuPasswords.add(sepPasswords);
                    }
                    {
                        mnuAddPassword = new JMenuItem();
                        allObjects.add(mnuAddPassword);
                        mnuPasswords.add(mnuAddPassword);
                        mnuAddPassword.setText("Add Password");
                        mnuAddPassword.addActionListener(
                            new AddToList(passwordListModel));
                    }
                    {
                        mnuRemovePassword = new JMenuItem();
                        allObjects.add(mnuRemovePassword);
                        mnuPasswords.add(mnuRemovePassword);
                        mnuRemovePassword.setText("Remove Password");
                        mnuRemovePassword.addActionListener(
                            new RemoveFromList(
                                passwordListModel, lstPasswords));
                    }
                    {
                        sepPasswords2 = new JSeparator();
                        allObjects.add(sepPasswords2);
                        mnuPasswords.add(sepPasswords2);
                    }
                    {
                        mnuClearPasswords = new JMenuItem();
                        allObjects.add(mnuClearPasswords);
                        mnuPasswords.add(mnuClearPasswords);
                        mnuClearPasswords.setText("Clear List");
                        mnuClearPasswords.addActionListener(
                            new ClearList(passwordListModel));
                    }
                    {
                        sepPasswords3 = new JSeparator();
                        allObjects.add(sepPasswords3);
                        mnuPasswords.add(sepPasswords3);
                    }
                    {
                        radPassList = new JRadioButtonMenuItem();
                        mnuPasswords.add(radPassList);
                        radPassList.setText("From List");
                        radPassList.setSelected(true);
                        allObjects.add(radPassList);
                        radPassList.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent arg0)
                            {
                                mnuLoadPasswordList.setEnabled(true);
                                mnuSavePasswordList.setEnabled(true);
                                mnuAddPassword.setEnabled(true);
                                mnuRemovePassword.setEnabled(true);
                                mnuClearPasswords.setEnabled(true);
                                radPassList.setSelected(true);
                                radPassFile.setSelected(false);
                                mnuPassFileName.setVisible(false);
                                mnuPasswords.setText(
                                    "Passwords ["
                                    + passwordListModel.getSize() + "]");
                            }
                        });
                    }
                    {
                        radPassFile = new JRadioButtonMenuItem();
                        mnuPasswords.add(radPassFile);
                        radPassFile.setText("From File:");
                        allObjects.add(radPassFile);
                        radPassFile.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent arg0)
                            {
                                mnuLoadPasswordList.setEnabled(false);
                                mnuSavePasswordList.setEnabled(false);
                                mnuAddPassword.setEnabled(false);
                                mnuRemovePassword.setEnabled(false);
                                mnuClearPasswords.setEnabled(false);
                                radPassList.setSelected(false);
                                radPassFile.setSelected(true);
                                mnuPassFileName.setVisible(true);

                                if (passFile == null)
                                {
                                    selectPassFile();
                                }
                                else
                                {
                                    try
                                    {
                                        // Counts the lines in the file
                                        BufferedReader in = new BufferedReader(
                                                                new FileReader(
                                                                    passFile));

                                        for (double i = 0.0; ; i++)
                                        {
                                            String s = in.readLine();

                                            if (s == null)
                                            {
                                                break;
                                            }

                                            mnuPasswords.setText("Passwords ["
                                                                 + i + "]");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                    }
                                }
                            }
                        });
                    }
                    {
                        mnuPassFileName = new JMenuItem();
                        mnuPassFileName.setText("Selected: None");
                        mnuPasswords.add(mnuPassFileName);
                        allObjects.add(mnuPassFileName);
                        mnuPassFileName.setVisible(false);
                        mnuPassFileName.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent arg0)
                            {
                                selectPassFile();
                            }
                        });
                    }
                }
                {
                    mnuRecovery = new JMenu();
                    allObjects.add(mnuRecovery);
                    mnuMainMenu.add(mnuRecovery);
                    mnuRecovery.setText("Recoveries");
                    {
                        mnuNumThreads = new JMenuItem("Sockets: "
                                                      + totalNumThreads);
                        mnuNumThreads.addActionListener(new ActionListener()
                        {
                            @Override public void actionPerformed(
                                    ActionEvent arg0)
                            {
                                setSockets();
                                mnuNumThreads.setText("Sockets: "
                                                      + totalNumThreads);
                            }
                        });
                        allObjects.add(mnuNumThreads);
                        mnuRecovery.add(mnuNumThreads);
                    }
                    {
                        sepRecovery = new JSeparator();
                        allObjects.add(sepRecovery);
                        mnuRecovery.add(sepRecovery);
                    }
                    {
                        mnuSaveRecovered = new JMenuItem();
                        allObjects.add(mnuSaveRecovered);
                        mnuRecovery.add(mnuSaveRecovered);
                        mnuSaveRecovered.setText("Save List");
                        mnuSaveRecovered.addActionListener(
                            new SaveListToFile(recoveryListModel));
                    }
                    {
                        mnuClearRecovered = new JMenuItem();
                        allObjects.add(mnuClearRecovered);
                        mnuRecovery.add(mnuClearRecovered);
                        mnuClearRecovered.setText("Clear List");
                        mnuClearRecovered.addActionListener(
                            new ClearList(recoveryListModel));
                    }
                }
                {
                    mnuMisc = new JMenu();
                    allObjects.add(mnuMisc);
                    mnuMainMenu.add(mnuMisc);
                    mnuMisc.setText("Misc");
                    {
                        mnuColors = new JMenuItem("Colors");
                        mnuMisc.add(mnuColors);
                        allObjects.add(mnuColors);
                        mnuColors.addActionListener(new ActionListener()
                        {
                            public void actionPerformed(ActionEvent arg0)
                            {
                                cp.setVisible(true);
                            }
                        });
                    }
                    {
                        chkAlert = new JCheckBoxMenuItem("Alerts", true);
                        chkAlert.addActionListener(new ActionListener()
                        {
                            // Allows the user to select if he wishes to be
                            // notified of program events
                            public void actionPerformed(ActionEvent arg0)
                            {
                                userAlert = chkAlert.getState();
                            }
                        });
                        allObjects.add(chkAlert);
                        mnuMisc.add(chkAlert);
                    }
                }
            }

            pack();
            this.setSize(575, 293);
            paintAllObjects();
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads the configuration file which contains:
     * 1) Number of lines to read from the server's response
     * 2) What to look for in the server's response
     * 3) Available socket selection
     *
     * If the configuration file does not exist the program terminates
     *
     */
    private void loadConfig()
    {
        File config = new File("config.ini");

        if (config.exists())
        {
            try
            {
                BufferedReader in = new BufferedReader(new FileReader(config));

                configLine   = Integer.parseInt(in.readLine());
                configString = in.readLine();
                socketSelect = (in.readLine()).split(",");
                in.close();
            }
            catch (Exception e)
            {
                System.exit(1);
            }
        }
        else
        {
            System.exit(1);
        }
    }

    /**
     * Paints the foreground and background of all objects that are allowed to
     * be changed/controlled by the user
     *
     */
    private void paintAllObjects()
    {
        for (int i = 0; i < allObjects.size(); i++)
        {
            JComponent c = (JComponent) allObjects.get(i);

            c.setBackground(backgroundColor);
            c.setForeground(foregroundColor);
        }
    }

    /**
     * Prompts the user to select a file to use to read passwords from this is
     * primarily intended for very large lists of passwords or when memory is an
     * issue
     *
     */
    private void selectPassFile()
    {
        final JFileChooser fc = new JFileChooser();

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            final File file = fc.getSelectedFile();

            passFile = file;
            mnuPassFileName.setText("Selected: " + passFile.getName());

            try
            {
                BufferedReader in =
                    new BufferedReader(new FileReader(passFile));

                for (double i = 0.0; ; i++)
                {
                    String s = in.readLine();

                    if (s == null)
                    {
                        break;
                    }

                    mnuPasswords.setText("Passwords [" + i + "]");
                }
            }
            catch (Exception e)
            {
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Reverting to using list.",
                                          "No File Selected",
                                          JOptionPane.ERROR_MESSAGE);
            mnuLoadPasswordList.setEnabled(true);
            mnuSavePasswordList.setEnabled(true);
            mnuAddPassword.setEnabled(true);
            mnuRemovePassword.setEnabled(true);
            mnuClearPasswords.setEnabled(true);
            mnuPassFileName.setVisible(false);
            radPassFile.setSelected(false);
            radPassList.setSelected(true);
        }
    }

    /**
     * Begins recovering accounts
     *
     */
    private void startRecovery()
    {
        // If recovery is not already started and there is not zero
        // usernames or passwords then the recovery is started
        if (!isStarted && (usernameListModel.getSize() != 0)
                && ((passwordListModel.getSize() != 0) || (passFile != null)))
        {
            isStarted = true;
            fileNum   = -1;

            if (radPassList.isSelected())
            {
                queueGen = new QueueGenerator(recoveryQueue,
                                              new Component[] { mnuUsernames,
                        mnuPasswords, mnuNumThreads }, lstUsernames,
                        userAlert, lstPasswords);
            }
            else
            {
                queueGen = new QueueGenerator(recoveryQueue,
                                              new Component[] { mnuUsernames,
                        mnuPasswords, mnuNumThreads }, lstUsernames,
                        userAlert, passFile);
            }

            // Pauses for one second so that the queue may fill
            try
            {
                Thread.sleep(1000);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // Creates an array of recovery threads determined by the
            // number the user selected
            recoveryThreads = new RecoveryThread[totalNumThreads];

            for (int i = 0; i < totalNumThreads; i++)
            {
                recoveryThreads[i] = new RecoveryThread(this, configLine,
                        configString, recoveryQueue);
                recoveryThreads[i].setName("Socket " + i);
                recoveryThreads[i].start();
            }

            time.setPaused(false);

            if ((new File("Pause.png")).exists())
            {
                btnStart.setIcon(new ImageIcon("Pause.png"));
                btnStart.setOpaque(false);
            }
            else
            {
                btnStart.setText("Pause");
                btnStart.setOpaque(true);
            }

            lblStatus.setText("Running");
        }

        // If recovery has already been started it is then either:
        else
        {
            if (recoveryThreads != null)
            {
                // Paused
                if (!isPaused)
                {
                    isPaused = true;
                    time.setPaused(true);

                    for (int i = 0; i < totalNumThreads; i++)
                    {
                        recoveryThreads[i].setPaused(true);
                    }

                    if ((new File("Resume.png")).exists())
                    {
                        btnStart.setIcon(new ImageIcon("Resume.png"));
                        btnStart.setOpaque(false);
                    }
                    else
                    {
                        btnStart.setText("Resume");
                        btnStart.setOpaque(true);
                    }

                    lblStatus.setText("Paused");
                }

                // Or Unpaused
                else
                {
                    isPaused = false;
                    time.setPaused(false);

                    for (int i = 0; i < totalNumThreads; i++)
                    {
                        recoveryThreads[i].setPaused(false);
                    }

                    if ((new File("Pause.png")).exists())
                    {
                        btnStart.setIcon(new ImageIcon("Pause.png"));
                        btnStart.setOpaque(false);
                    }
                    else
                    {
                        btnStart.setText("Pause");
                        btnStart.setOpaque(true);
                    }

                    lblStatus.setText("Running");
                }
            }
        }
    }

    /**
     * Stops recovering accounts
     *
     */
    private void stopRecovery()
    {
        if (recoveryThreads != null)
        {
            for (int i = 0; i < totalNumThreads; i++)
            {
                recoveryThreads[i].stopRunning();
            }

            time.setPaused(true);

            if ((new File("Start.png")).exists())
            {
                btnStart.setIcon(new ImageIcon("Start.png"));
                btnStart.setOpaque(false);
            }
            else
            {
                btnStart.setText("Start");
                btnStart.setOpaque(true);
            }

            isPaused  = false;
            isStarted = false;
            queueGen.stopRunning();
            recoveryQueue.emptyQueue();
            lblStatus.setText("Stopped");
        }
    }

    /**
     * Updates the statistics display of the current run of the program
     *
     * @param recent Most recent recovery attempt
     * @param success Was the attempt successful
     */
    public void updateStats(Account recent, boolean success)
    {
        lblRecentAttempt.setText(recent.getUsername() + ":"
                                 + recent.getPassword());
        recoveryAttempts++;
        lblStats.setText("Attempts: " + recoveryAttempts + " - Successful: "
                         + recoverySuccesses);

        if (success)
        {
            recoverySuccesses++;
            recoveryListModel.addElement(recent.getUsername() + ":"
                                         + recent.getPassword());
            queueGen.setNewestRecovery(recent.getUsername());
            success = true;

            // Alerts the user of a successful recovery
            if (userAlert)
            {
                JOptionPane.showMessageDialog(
                    null, recent.getUsername() + " : " + recent.getPassword(),
                    "New Recovery", JOptionPane.INFORMATION_MESSAGE);
            }

            // Saves the successful recovery to a file
            try
            {
                File f = null;

                if (fileNum == -1)
                {
                    for (int i = 0; ; i++)
                    {
                        f = new File("Recoveries" + i + ".txt");

                        if (!f.exists())
                        {
                            fileNum = i;

                            break;
                        }
                    }
                }

                f = new File("Recoveries" + fileNum + ".txt");

                BufferedWriter outFile = new BufferedWriter(new FileWriter(f));

                for (int x = 0; x < recoveryListModel.size(); x++)
                {
                    outFile.write("" + recoveryListModel.get(x));
                    outFile.newLine();
                }

                outFile.close();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Updates the total time ran
     *
     */
    public void updateTime()
    {
        lblStatus.setText("Up: " + time);

        if (time.getTimeRan() >= 60)
        {
            lblRPM.setText("Avg RPM: "
                           + (int) (recoveryAttempts
                                    / (time.getTimeRan() / 60)));
            ti.setToolTip("AR v4 - "
                          + (int) (recoveryAttempts
                                   / (time.getTimeRan() / 60)) + " rpm");
        }
    }

    //~--- get methods --------------------------------------------------------

    /**
     * Returns the background color
     *
     *
     * @return The background color
     */
    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    /**
     * Returns the foreground color
     *
     *
     * @return The foreground color
     */
    public Color getForegroundColor()
    {
        return foregroundColor;
    }

    //~--- set methods --------------------------------------------------------

    /**
     * Sets the background color
     *
     *
     * @param background The background color
     */
    public void setBackgroundColor(Color background)
    {
        backgroundColor = background;
        paintAllObjects();
    }

    /**
     * Sets the foreground color
     *
     *
     * @param foreground The foreground color
     */
    public void setForegroundColor(Color foreground)
    {
        foregroundColor = foreground;
        paintAllObjects();
    }

    /**
     * Prompts the user to select the number of sockets to use
     *
     */
    private void setSockets()
    {
        String s = (String) JOptionPane.showInputDialog(null,
                       "Select Number of Sockets:", "Sockets",
                       JOptionPane.PLAIN_MESSAGE, null, socketSelect, "1");

        if ((s != null) && (s.length() > 0))
        {
            totalNumThreads = Integer.parseInt(s);
        }
        else
        {
            totalNumThreads = 1;
        }

        recoveryQueue.setMaxSize(totalNumThreads * 10);
    }
}
