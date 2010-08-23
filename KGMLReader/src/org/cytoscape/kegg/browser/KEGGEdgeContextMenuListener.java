package org.cytoscape.kegg.browser;

import giny.view.EdgeView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;
import cytoscape.view.NetworkViewManager;
import ding.view.EdgeContextMenuListener;

public class KEGGEdgeContextMenuListener implements EdgeContextMenuListener {

	private static final String MENU_TITLE = "KEGG Edge Options";

	private CyAttributes edgeAttr = Cytoscape.getEdgeAttributes();
	
	private FloatingGlassPane glass;
	private final CyNetworkView view;
	
	private boolean selection = false;
	
	public KEGGEdgeContextMenuListener(CyNetworkView view) {
		this.view = view;
	}

	@Override
	public void addEdgeContextMenuItems(final EdgeView ev, final JPopupMenu menu) {
		if (menu == null)
			return;

		final JMenu keggMenu = new JMenu(MENU_TITLE);
		final JCheckBox showFloat = new JCheckBox(
				"Show info windows", selection);
		showFloat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(showFloat.isSelected()) {
					showGlass();
					selection = true;
				} else {
					hideGlass();
					selection = false;
				}
			}
		});
		
		keggMenu.add(showFloat);

		menu.addSeparator();
		menu.add(keggMenu);
	}
	
	/**
	 * Show glass pane on the KEGG pathways.
	 */
	private void showGlass() {
		System.out.println("# Display Glass");
		final NetworkViewManager viewManager = Cytoscape.getDesktop().getNetworkViewManager();
		final JInternalFrame frame = viewManager.getInternalFrame(Cytoscape.getCurrentNetworkView());
	    
		if(frame.getGlassPane() != glass) {
			glass = new FloatingGlassPane(view);			
			frame.setGlassPane(glass);
		}
		frame.getGlassPane().setVisible(true);
		
	}
	
	private void hideGlass() {
		System.out.println("----Hiding");
		final NetworkViewManager viewManager = Cytoscape.getDesktop().getNetworkViewManager();
		final JInternalFrame frame = viewManager.getInternalFrame(Cytoscape.getCurrentNetworkView());
		frame.getGlassPane().setVisible(false);
	}
}
