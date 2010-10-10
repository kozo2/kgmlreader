package org.cytoscape.kegg.browser.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.border.Border;

public class RoundRectBorder implements Border {

	private static final int PAD = 5;
	
	private static final Color c1 = new Color(0, 0, 0, 150);
	private static final Color c2 = new Color(255, 255, 255, 190);
	
	private Color borderColor;
	private Color backgroundColor;
	
	public RoundRectBorder() {
		this(c1, c2);
	}
	
	
	public RoundRectBorder(Color background, Color border) {
		super();
		this.borderColor = border;
		this.backgroundColor = background;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(PAD, PAD, PAD, PAD);
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		final Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		final Insets insets = getBorderInsets(c);

		g2d.setColor(borderColor);
		g2d.setStroke(new BasicStroke(2));
		g2d.drawRoundRect(x + insets.left, y + insets.top, width - PAD * 2,
				height - PAD * 2, 30, 30);

		
		final Paint gp = new GradientPaint(width / 2, y + insets.top, c1,
				width / 2, height, c2);

		g2d.setPaint(backgroundColor);
		g2d.fillRoundRect(x + insets.left, y + insets.top, width - PAD * 2,
				height - PAD * 2, 30, 30);
	}
}