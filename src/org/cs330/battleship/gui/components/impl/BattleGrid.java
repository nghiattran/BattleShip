package org.cs330.battleship.gui.components.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.cs330.battleship.gui.components.GameComponent;
import org.cs330.battleship.gui.structures.GridTile;
import org.cs330.battleship.tools.FileUtils;

/**
 * Represents the grid on which a player interacts or displays ship positioning
 * BattleGrid.java
 * @author Clayton Williams
 * @date Feb 6, 2016
 */
public class BattleGrid extends GameComponent {

	/**
	 * Battlegrid
	 */
	private static final long serialVersionUID = -2859372712458339799L;
	
	/**
	 * Buffered images for our labels
	 */
	private static BufferedImage xAxis = FileUtils.getSprite("x_axis.png"), yAxis = FileUtils.getSprite("y_axis.png");
	
	/**
	 * The grid
	 */
	private final Grid grid = new Grid();
	
	/**
	 * BattleGrid
	 */
	public BattleGrid(int x, int y) {
		this.setSize(535, 535);
		this.setLocation(x, y);
		this.setVisible(true);
		this.setBackground(Color.BLACK);
		this.add(grid);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(xAxis, 50, 0, null);
		g2.drawImage(yAxis, 0, 40, null);
		g2.drawImage(FileUtils.getSprite("gridoutline.png"), 27, 27, null);
	}
	
	/**
	 * Represents the actual grid 500x500
	 * @author Clayton Williams
	 * @date Feb 6, 2016
	 */
	public static class Grid extends JComponent implements MouseMotionListener, MouseListener {
		
		/**
		 * Generated serial
		 */
		private static final long serialVersionUID = 5438553265614689747L;
		
		/**
		 * The grid spacing
		 */
		public static final int GRID_SPACING = 50;

		/**
		 * If we should show the hover guide on mouse over
		 */
		private boolean showHoverGuide = true;
		
		/**
		 * The buffered grid image
		 */
		private static BufferedImage gridImage = FileUtils.getSprite("grid.png");

		/**
		 * The mouse location (null if no location on grid)
		 */
		private Point mouseLocation = null;
		
		/**
		 * Grid
		 */
		public Grid() {
			this.setSize(500, 500);
			this.setVisible(true);
			this.setLocation(31, 31);
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.drawImage(gridImage, 0, 0, null);
			if (mouseLocation != null && showHoverGuide) {
				GridTile tile = GridTile.forMouseLocation(mouseLocation);
				Point p = tile.getPixelBase();
				//System.out.println(p.toString() + " " + ((int) p.getX() + GRID_SPACING));
				g2.setColor(new Color(0, 0, 0, 0.2f));
				/** Horizontal Fill **/
				g2.fillRect(0, (int) p.getY(), (int) p.getX() + GRID_SPACING , GRID_SPACING); 
				/** Vertical Fill **/
				g2.fillRect((int) p.getX(), 0, GRID_SPACING, (int) p.getY() + GRID_SPACING); 
			}
		}
		
		/**
		 * Sets the show hover guide flag
		 * @param showHoverGuide
		 */
		public void setShowHoverGuide(boolean showHoverGuide) {
			this.showHoverGuide = showHoverGuide;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Point point = e.getPoint();
			this.mouseLocation = point;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			this.mouseLocation = null;
		}
		
		
	}

}
