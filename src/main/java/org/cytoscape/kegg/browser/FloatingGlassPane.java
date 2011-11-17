package org.cytoscape.kegg.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.cytoscape.kegg.browser.ui.RoundRectBorder;

import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.view.CyNetworkView;

/**
 * Floating window info panel for network view.
 * 
 * THIS CODE IS STILL EXPERIMENTAL.
 * 
 * @author kono
 * 
 */
public class FloatingGlassPane extends JPanel {

	private static final long serialVersionUID = 6172075571502342320L;
	
	private static final Color TEXT_COLOR1 = new Color(255, 255, 255, 230);

	private final CyAttributes attr;
	
	private final CyNetworkView view;
	
	private JPanel commandPanel;
	private JPanel infoPanel;
	
	public FloatingGlassPane(final CyNetworkView view) {
		attr = Cytoscape.getNetworkAttributes();
		this.view = view;
		
		initComponents();
	}


	private void initComponents() {

//		commandPanel = new JPanel();
//		commandPanel.setPreferredSize(new Dimension(2000, 50));
//		commandPanel.setBackground(new Color(50, 50, 50, 180));
//		//commandPanel.addMouseListener(new GListener());
//		JButton modButton = new TransparentButton("Test Button");
//		
//		commandPanel.add(modButton);
		
		final String title = view.getTitle();
		
		infoPanel = new JPanel();
		infoPanel.setOpaque(false);
		infoPanel.setPreferredSize(new Dimension(400, 2000));
		//infoPanel.setBorder(new RoundRectBorder());
		infoPanel.setLayout(new GridLayout(3, 1));

		this.setOpaque(false);
		this.setLayout(new BorderLayout());

		JPanel textPanel1 = new JPanel();
		textPanel1.setOpaque(false);
		textPanel1.setBorder(new RoundRectBorder());
		
		JTextArea text1 = new JTextArea();
		text1.setPreferredSize(new Dimension(360, 200));
		text1.setOpaque(false);
		Font textFont = new Font("SansSerif", Font.BOLD, 14);
		text1.setFont(textFont);
		text1.setForeground(TEXT_COLOR1);
		text1.setText("\nNetwork Title:\n\n  " + title);
		textPanel1.add(text1);
		
		infoPanel.add(textPanel1);
		
		JPanel textPanel2 = new JPanel();
		textPanel2.setOpaque(false);
		textPanel2.setBorder(new RoundRectBorder());
		
		JTextArea text2 = new JTextArea();
		text2.setPreferredSize(new Dimension(360, 200));
		//text2.setBorder(new RoundRectBorder());
		text2.setOpaque(false);
		text2.setFont(textFont);
		text2.setForeground(TEXT_COLOR1);
		text2.setText("\nNetwork Statistics:");
		
		textPanel2.add(text2);
		
		infoPanel.add(textPanel2);
		
		JPanel textPanel3 = new JPanel();
		textPanel3.setLayout(new BorderLayout());
		textPanel3.setOpaque(false);
		textPanel3.setBorder(new RoundRectBorder());
		
		BufferedImage image = null;
		
//		try {
//			 image = ImageIO.read(new File("/Users/kono/Documents/wsC2/KGMLReader/testData/chart4.png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		JLabel imageLabel = new JLabel();
//		imageLabel.setIcon(new ImageIcon(image));
//		imageLabel.setPreferredSize(new Dimension(360, 500));
//		//text2.setBorder(new RoundRectBorder());
//		imageLabel.setOpaque(false);
//		imageLabel.setFont(textFont);
//		imageLabel.setForeground(TEXT_COLOR1);
		
		
//		textPanel3.add(imageLabel, BorderLayout.CENTER);
		
		infoPanel.add(textPanel3);
		

		JPanel p1 = new JPanel();
		p1.setBackground(new Color(0, 100, 0, 100));
		p1.setPreferredSize(new Dimension(1500, 900));

		//this.add(commandPanel, BorderLayout.PAGE_END);
		this.add(infoPanel, BorderLayout.LINE_END);
	}

	// protected void paintComponent(Graphics g) {
	//
	// final Graphics2D g2d = (Graphics2D) g;
	// g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
	// RenderingHints.VALUE_RENDER_QUALITY);
	// g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	// RenderingHints.VALUE_ANTIALIAS_ON);
	// g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
	// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	//
	// int pad = 20;
	//
	// final Rectangle originalClip = g2d.getClipBounds();
	// int boxWidth = 400;
	// int boxHeight = originalClip.height - pad * 2;
	//
	// int centerX = originalClip.x + originalClip.width / 2;
	// int centerY = originalClip.y + originalClip.height / 2;
	//
	// int newX = centerX - boxWidth / 2;
	// int newY = originalClip.y + pad;
	//
	// final Rectangle clip = new Rectangle(newX, newY, boxWidth, boxHeight);
	//
	// final Color c1 = new Color(0, 0, 0, 150);
	// final Color c2 = new Color(255, 255, 255, 100);
	// final Paint gp = new GradientPaint(centerX, centerY - boxHeight / 2,
	// c1, centerX, centerY + boxHeight / 2, c2);
	//
	// g2d.setPaint(gp);
	// g2d.fillRoundRect(clip.x, clip.y, clip.width, clip.height, 20, 20);
	// g2d.setColor(Color.DARK_GRAY);
	// g2d.setStroke(new BasicStroke(3));
	// g2d.drawRoundRect(clip.x, clip.y, clip.width, clip.height, 20, 20);
	// }

	class GListener extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			System.out.println("###### GP Click2");

		}

	}

	// @Override
	// protected void paintComponent(Graphics g) {
	// final CyNetwork network = view.getNetwork();
	//
	// String title = attr.getStringAttribute(network.getIdentifier(),
	// "KEGG.fullName");
	// if(title == null)
	// title = network.getTitle();
	//
	// final Graphics2D g2d = (Graphics2D) g;
	// final TextLayout layout = new TextLayout(title, TITLE1,
	// new FontRenderContext(new AffineTransform(),false,false));
	// final Rectangle2D titleBound = layout.getBounds();
	//
	// final Rectangle clip = g2d.getClipBounds();
	// g2d.setColor(WINDOW_COLOR);
	// g2d.fillRect(
	// clip.width-((int)titleBound.getWidth()+40),
	// clip.height-((int)titleBound.getHeight()+40),
	// (int)titleBound.getWidth()+30,
	// (int)titleBound.getHeight()+30);
	// g2d.setStroke(BORDER);
	// g2d.setColor(BORDER_COLOR);
	// g2d.drawRect(
	// clip.width-((int)titleBound.getWidth()+40),
	// clip.height-((int)titleBound.getHeight()+40),
	// (int)titleBound.getWidth()+30,
	// (int)titleBound.getHeight()+30);
	// g2d.setColor(TITLE_COLOR);
	// g2d.setFont(TITLE1);
	// g2d.drawString(title,
	// clip.width-((int)titleBound.getWidth()+30),
	// clip.height-((int)titleBound.getHeight()/2 + 15));
	//
	// }
}
