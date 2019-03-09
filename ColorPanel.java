/*
 * @(#)ColorPanel.java   09/02/08
 * 
 * Copyright (c) 2007 312c
 *
 */



package C4;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

//~--- classes ----------------------------------------------------------------

/**
 * An interface for controlling the foreground and background colors
 *
 *
 */
public class ColorPanel extends javax.swing.JFrame
{
    private JButton       btnBackground;
    private JButton       btnClose;
    private JButton       btnForeground;
    private JColorChooser colorChooser;
    private JLabel        lblPreview;
    private AR_v4         owner;
    private JPanel        pnlBack;
    private JPanel        pnlButtons;

    //~--- constructors -------------------------------------------------------

    /**
     * Constructs a new frame to control colors
     *
     *
     * @param owner
     *            The frame which's background and foreground attributes
     *            will be controlled
     */
    public ColorPanel(AR_v4 owner)
    {
        super();
        this.owner = owner;
        initGUI();
    }

    //~--- methods ------------------------------------------------------------

    /**
     * Initializes the GUI
     *
     */
    private void initGUI()
    {
        try
        {
            setLocationRelativeTo(null);
            setTitle("Color Selection");
            setResizable(false);

            GridBagLayout pnlBackLayout = new GridBagLayout();

            pnlBackLayout.rowWeights    = new double[] { 0.1, 0.0, 0.1 };
            pnlBackLayout.rowHeights    = new int[] { 7, 344, 7 };
            pnlBackLayout.columnWeights = new double[] { 0.1 };
            pnlBackLayout.columnWidths  = new int[] { 7 };
            pnlBack                  = new JPanel();
            this.add(pnlBack);
            pnlBack.setLayout(pnlBackLayout);
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            {
                lblPreview = new JLabel();
                lblPreview.setForeground(owner.getForegroundColor());
                lblPreview.setOpaque(true);
                lblPreview.setBackground(owner.getBackgroundColor());
                pnlBack.add(lblPreview,
                            new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                                   GridBagConstraints.CENTER,
                                                   GridBagConstraints.BOTH,
                                                   new Insets(0, 0, 0, 0), 0,
                                                   0));
                lblPreview.setText("Color Changer");
                lblPreview.setHorizontalAlignment(JLabel.CENTER);
                lblPreview.setFont(new Font("SansSerif", Font.BOLD, 24));
            }

            {
                colorChooser = new JColorChooser();
                colorChooser.setBackground(owner.getBackgroundColor());
                pnlBack.add(colorChooser,
                            new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                                   GridBagConstraints.CENTER,
                                                   GridBagConstraints.NONE,
                                                   new Insets(0, 0, 0, 0), 0,
                                                   0));
            }

            {
                pnlButtons = new JPanel();
                pnlButtons.setBackground(owner.getBackgroundColor());

                GridBagLayout pnlButtonsLayout = new GridBagLayout();

                pnlButtonsLayout.rowWeights    = new double[] { 0.1 };
                pnlButtonsLayout.rowHeights    = new int[] { 7 };
                pnlButtonsLayout.columnWeights = new double[] { 0.1, 0.1, 0.1 };
                pnlButtonsLayout.columnWidths  = new int[] { 7, 7, 7 };
                pnlButtons.setLayout(pnlButtonsLayout);
                pnlBack.add(pnlButtons,
                            new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                                   GridBagConstraints.CENTER,
                                                   GridBagConstraints.BOTH,
                                                   new Insets(0, 0, 0, 0), 0,
                                                   0));
                {
                    btnForeground = new JButton();
                    pnlButtons.add(btnForeground,
                                   new GridBagConstraints(0, 0, 1, 1, 0.0,
                                       0.0, GridBagConstraints.CENTER,
                                       GridBagConstraints.NONE,
                                       new Insets(0, 0, 0, 0), 0, 0));
                    btnForeground.setText("Set Foreground");
                    btnForeground.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent arg0)
                        {
                            owner.setForegroundColor(colorChooser.getColor());
                            lblPreview.setForeground(colorChooser.getColor());
                        }
                    });
                }
                {
                    btnBackground = new JButton();
                    pnlButtons.add(btnBackground,
                                   new GridBagConstraints(1, 0, 1, 1, 0.0,
                                       0.0, GridBagConstraints.CENTER,
                                       GridBagConstraints.NONE,
                                       new Insets(0, 0, 0, 0), 0, 0));
                    btnBackground.setText("Set Background");
                    btnBackground.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent arg0)
                        {
                            owner.setBackgroundColor(colorChooser.getColor());
                            pnlBack.setBackground(colorChooser.getColor());
                            lblPreview.setBackground(colorChooser.getColor());
                            colorChooser.setBackground(
                                colorChooser.getColor());
                            pnlButtons.setBackground(colorChooser.getColor());
                        }
                    });
                }
                {
                    btnClose = new JButton();
                    pnlButtons.add(btnClose,
                                   new GridBagConstraints(2, 0, 1, 1, 0.0,
                                       0.0, GridBagConstraints.CENTER,
                                       GridBagConstraints.NONE,
                                       new Insets(0, 0, 0, 0), 0, 0));
                    btnClose.setText("Close");
                    btnClose.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent arg0)
                        {
                            setVisible(false);
                        }
                    });
                }
            }

            pack();
            this.setSize(400, 496);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
