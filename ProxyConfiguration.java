package C4;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ProxyConfiguration extends javax.swing.JFrame {
	private JMenuBar mnuProxy;
	private JMenu mnuFile;
	private JLabel jLabel1;
	private JLabel lblUnchecked;
	private JMenuItem mnuExit;
	private JList lstUnchecked;
	private JList lstChecked;
	private JSeparator sepFile;
	private JMenuItem mnuCheckProxies;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ProxyConfiguration inst = new ProxyConfiguration();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public ProxyConfiguration() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			GridBagLayout thisLayout = new GridBagLayout();
			this.setAlwaysOnTop(true);
			thisLayout.rowWeights = new double[] {0.1, 0.7, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7};
			thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0};
			thisLayout.columnWidths = new int[] {150, 20, 150};
			getContentPane().setLayout(thisLayout);
			{
				ListModel lstUncheckedModel = 
					new DefaultComboBoxModel(
							new String[] { "111.222.333.444:55555", "Item Two" });
				lstUnchecked = new JList();
				getContentPane().add(lstUnchecked, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				lstUnchecked.setModel(lstUncheckedModel);
				lstUnchecked.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
			{
				ListModel lstUnchecked = 
					new DefaultComboBoxModel(
							new String[] { "111.222.333.444:55555", "Item Two" });
				lstChecked = new JList();
				getContentPane().add(lstChecked, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
				lstChecked.setModel(lstUnchecked);
				lstChecked.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			}
			{
				lblUnchecked = new JLabel();
				getContentPane().add(lblUnchecked, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				lblUnchecked.setText("Unchecked / Invalid");
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				jLabel1.setText("Checked");
			}
			{
				mnuProxy = new JMenuBar();
				setJMenuBar(mnuProxy);
				{
					mnuFile = new JMenu();
					mnuProxy.add(mnuFile);
					mnuFile.setText("File");
					{
						mnuCheckProxies = new JMenuItem();
						mnuFile.add(mnuCheckProxies);
						mnuCheckProxies.setText("Check Proxies");
					}
					{
						sepFile = new JSeparator();
						mnuFile.add(sepFile);
					}
					{
						mnuExit = new JMenuItem();
						mnuFile.add(mnuExit);
						mnuExit.setText("Exit");
					}
				}
			}
			pack();
			this.setSize(340, 317);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
