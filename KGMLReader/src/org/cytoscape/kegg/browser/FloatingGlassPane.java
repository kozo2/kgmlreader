package org.cytoscape.kegg.browser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;

import cytoscape.CyNetwork;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;

/**
 * Floating window info panel for network view.
 * 
 * @author kono
 * 
 */
public class FloatingGlassPane extends JComponent {

	private static final long serialVersionUID = 9102072230023225077L;
	
	private static final Color TITLE_COLOR = Color.white;

	private static final Color WINDOW_COLOR = new Color(0f, 0f, 0f, 0.7f);
	private static final Color BORDER_COLOR = Color.DARK_GRAY;
	
	private static final Stroke BORDER = new BasicStroke(1f);
	
	private static final Font TITLE1 = new Font("SansSerif", Font.PLAIN, 18);
	
	private final CyAttributes attr;
	private final CyNetworkView view;
	
	public FloatingGlassPane(final CyNetworkView view) {
		attr = Cytoscape.getNetworkAttributes();
		this.view = view;
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		final CyNetwork network = view.getNetwork();
		
		String title = attr.getStringAttribute(network.getIdentifier(), "KEGG.fullName");
		if(title == null)
			title = network.getTitle();
		
		final Graphics2D g2d = (Graphics2D) g;
		final TextLayout layout = new TextLayout(title, TITLE1,
			    new FontRenderContext(new AffineTransform(),false,false));
		final Rectangle2D titleBound = layout.getBounds();
		
		final Rectangle clip = g2d.getClipBounds();
		g2d.setColor(WINDOW_COLOR);
		g2d.fillRect(
				clip.width-((int)titleBound.getWidth()+40), 
				clip.height-((int)titleBound.getHeight()+40),
				(int)titleBound.getWidth()+30,
				(int)titleBound.getHeight()+30);
		g2d.setStroke(BORDER);
		g2d.setColor(BORDER_COLOR);
		g2d.drawRect(
				clip.width-((int)titleBound.getWidth()+40), 
				clip.height-((int)titleBound.getHeight()+40),
				(int)titleBound.getWidth()+30,
				(int)titleBound.getHeight()+30);
		g2d.setColor(TITLE_COLOR);
		g2d.setFont(TITLE1);
		g2d.drawString(title, 
				clip.width-((int)titleBound.getWidth()+30),
				clip.height-((int)titleBound.getHeight()/2 + 15));
		
	}
}
