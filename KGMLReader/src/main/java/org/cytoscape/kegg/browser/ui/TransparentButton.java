package org.cytoscape.kegg.browser.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

public class TransparentButton extends JButton {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6437691405862723641L;

	private static final Color BG = new Color(200, 200, 200, 150);
	
	
	public TransparentButton(String text) {
		super();
		this.setText(text);
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);

        Dimension originalSize = super.getPreferredSize();
        int gap = (int) (originalSize.height * 0.2);
        int x = gap;
        int y = gap;
        int width = originalSize.width - (gap * 2);
        int height = originalSize.height - (gap * 2);

        g.setColor(BG);
        g.fillRoundRect(x, y, width, height, 10, 10);
        g.setColor(Color.white);
        g.drawRoundRect(x, y, width, height, 10, 10);
        g.drawString(this.getText(), x+gap, y+gap);
    }
	

	@Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += size.height;
        return size;
    }
}
