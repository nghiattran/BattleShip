package battleship.client.gui.component.impl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import battleship.client.gui.Application;
import battleship.client.gui.component.GameComponent;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.screen.impl.GameScreen;
import battleship.client.gui.screen.impl.SetupScreen;
import battleship.client.gui.structures.Alignment;
import battleship.client.gui.structures.GridTile;
import battleship.client.gui.structures.GridTile.GridTileRectangle;
import battleship.client.gui.structures.GridType;
import battleship.client.gui.structures.Hitmarker;
import battleship.client.gui.structures.Orientation;
import battleship.client.gui.structures.ShipType;
import battleship.client.system.FileSystem;

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
	private static BufferedImage xAxis = FileSystem.getImage("x_axis.png"), yAxis = FileSystem.getImage("y_axis.png");
	
	/**
	 * The grid
	 */
	private final Grid grid;
	
	/**
	 * The grid alignment
	 */
	private Alignment gridAlignment;
	
	/**
	 * BattleGrid
	 * @param x - the x position
	 * @param y - the y position
	 */
	public BattleGrid(int x, int y) {
		this(x, y, GridType.PLAYER, Alignment.LEFT);
	}
	
	/**
	 * BattleGrid
	 * @param x - the x position
	 * @param y - the y position
	 * @param gridType - the {GridType}
	 */
	public BattleGrid(int x, int y, GridType gridType, Alignment alignment) {
		super(535, 535, x, y);
		this.setVisible(true);
		this.setBackground(Color.BLACK);
		this.gridAlignment = alignment;
		grid = new Grid(gridType);
		grid.setLocation(gridAlignment == Alignment.LEFT ? 31 : 4, 31);
		this.add(grid);		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(xAxis, gridAlignment == Alignment.LEFT ? 50 : 20, 0, null);
		g2.drawImage(yAxis, gridAlignment == Alignment.LEFT ? 0 : 510, 40, null);
		g2.drawImage(FileSystem.getImage("gridoutline.png"), gridAlignment == Alignment.LEFT ? 27 : 0, 27, null);
	}
	
	/**
	 * Represents the actual grid 500x500
	 * @author Clayton Williams
	 * @date Feb 6, 2016
	 */
	public static class Grid extends GameComponent implements MouseMotionListener, MouseListener, DropTargetListener {
		
		/**
		 * Generated serial
		 */
		private static final long serialVersionUID = -8815651896590866290L;

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
		private BufferedImage gridImage;

		/**
		 * The mouse location (null if no location on grid)
		 */
		private Point mouseLocation = null;
		
		/**
		 * An array list used to highlight non hovered (async) highlights on the grid
		 */
		private List<GridTileRectangle> highlights = new ArrayList<GridTileRectangle>();
		
		/**
		 * The current drag hover
		 */
		private GridTile currentDragHover;
		
		/**
		 * The grid type
		 */
		private GridType gridType;
		
		/**
		 * The ship values
		 */
		private Ship[] ships = new Ship[ShipType.values().length];
		
		/**
		 * The grid tiles
		 */
		private GridTile[][] tiles = new GridTile[10][10];
		
		/**
		 * Grid
		 */
		public Grid() {
			this(GridType.PLAYER);
		}
		
		/**
		 * Grid
		 */
		public Grid(GridType gridType) {
			super(500, 500, 31, 31);
			this.gridType = gridType;
			this.setVisible(true);
			this.addMouseMotionListener(this);
			this.addMouseListener(this);
			gridImage = FileSystem.getImage(gridType.getFileName());
			new DropTarget(this, DnDConstants.ACTION_MOVE, this, true);
			for (int i = 0; i < tiles.length; i++) {
				for (int i2 = 0; i2 < tiles[i].length; i2++) {
					tiles[i][i2] = new GridTile(i, i2);
				}
			}
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g.drawImage(gridImage, 0, 0, null);
			if (mouseLocation != null && showHoverGuide && Application.getInstance().getScreen(GameScreen.class) != null) {
				if (Application.getInstance().getScreen(GameScreen.class).isTurn()) {
					this.setCursor(new Cursor(Cursor.HAND_CURSOR));
					GridTile tile = GridTile.forMouseLocation(mouseLocation);
					/** Horizontal Fill **/
					drawHighlightedArea(g, new GridTile(0, tile.getY()), new GridTile(tile.getX(), tile.getY()));
					/** Vertical Fill **/
					drawHighlightedArea(g, new GridTile(tile.getX(), 0), new GridTile(tile.getX(), tile.getY()));
					if (gridType == GridType.ENEMY) {
						if (tiles[tile.getX()][tile.getY()].getHitmarker() == Hitmarker.NONE)
							g.drawImage(FileSystem.getImage("fireButton.png"), (int) tile.getPixelBase().getX(), (int) tile.getPixelBase().getY(), null);
					} 
				}
			} else {
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			
			/**
			 * The async highlightables
			 */
			for (GridTileRectangle gtr : highlights) {
				drawHighlightedArea(g, gtr.getSouthWest(), gtr.getNorthEast());
			}
			
			/**
			 * Drawing hit and miss markers
			 */
			for (GridTile[] tileOuter : tiles) {
				for (GridTile tile : tileOuter) {
					if (tile.getHitmarker() != Hitmarker.NONE) {
						g.drawImage(FileSystem.getImage(tile.getHitmarker().name().toLowerCase() + ".png"), (int) tile.getPixelBase().getX(), (int) tile.getPixelBase().getY(), null);
					}
				}
			}
			
			if (Application.getInstance().getScreen(GameScreen.class) != null && this.gridType == GridType.ENEMY) {
				for (Ship s : ships) {
					if (s == null)
						continue;				
					int tileX = s.getX();
					int tileY = s.getY();
					int length = s.getShipType().getTileLength();
					Orientation o = s.getOrientation();
					int lengthX = o == Orientation.HORIZONTAL ? length : 1;
					int lengthY = o == Orientation.VERTICAL ? length : 1;
					boolean sunk = true;

					for (int x = tileX; x < lengthX + tileX; x++) {
						for (int y = tileY; y < lengthY + tileY; y++) {
							if (tiles[x][y].getHitmarker() != Hitmarker.HIT) {
								//System.out.println(tiles[x][y].getHitmarker().name());
								sunk = false;
							}
						}
					}

					if (sunk) {
						BufferedImage image = s.getShipType().getImage();
				        
				        if (o == Orientation.VERTICAL) {
				            AffineTransform at = new AffineTransform();
				            AffineTransform oldXForm = g2.getTransform();
				            at.translate(s.getWidth() / 2, s.getHeight() / 2);
				            at.rotate(Math.PI/2);
				            at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
				            g2.translate(s.getX() * GRID_SPACING, s.getY() * GRID_SPACING);
				        	g2.drawImage(image, at, null);
				            g2.setTransform(oldXForm);
				        } else {
				        	g2.drawImage(image, s.getX() * GRID_SPACING, s.getY() * GRID_SPACING, null);
				        }
					}
				}
			}
		}
		
		/**
		 * Draws a highlighted area from grid tile to grid tile
		 * @param g - the graphics drawable
		 * @param from - from tile
		 * @param to - to tile
		 */
		private void drawHighlightedArea(Graphics g, GridTile from, GridTile to) {
			if (from.getX() > to.getX() || from.getY() > to.getY()) {
				return;
			}

			Point pFrom = from.getPixelBase();
			Point pTo = to.getPixelBase();
			int width = (int) (pTo.getX() - pFrom.getX()) + GRID_SPACING;
			int height = (((int) (pTo.getY() - pFrom.getY()))) + GRID_SPACING;
			Color c = g.getColor();
			g.setColor(new Color(0, 0, 0, 0.15f));
			g.fillRect((int) pFrom.getX(), (int) pFrom.getY(), width, height);
			g.setColor(c);
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
			if (Application.getInstance().getScreen(GameScreen.class) != null && this.gridType == GridType.ENEMY) {
				GridTile tile = GridTile.forMouseLocation(e.getPoint());
				if (tiles[tile.getX()][tile.getY()].getHitmarker() == Hitmarker.NONE && Application.getInstance().getScreen(GameScreen.class).isTurn()) {
					ByteBuffer b = ByteBuffer.allocate(200);
					b.put((byte) 2);
					b.put((byte) tile.getX());
					b.put((byte) tile.getY());
					Application.getInstance().getConnector().write(b);
				}
			}
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
		
		/** DropTargetListener **/

		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			/** Check if drag type flavor is supported, if not - do nothing **/
			if (dtde.isDataFlavorSupported(GameComponent.FLAVOR)) {
				dtde.acceptDrag(DnDConstants.ACTION_MOVE);
			} else {
				dtde.rejectDrag();
			}
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			try {
				Object data = dtde.getTransferable().getTransferData(GameComponent.FLAVOR);
				if (data != null && data instanceof Ship) {
					Ship ship = (Ship) data;
					GridTile hoveredTile = GridTile.forMouseLocation(dtde.getLocation());
					if (currentDragHover == null || !currentDragHover.compareTo(hoveredTile)) {
						highlights.clear();
						currentDragHover = hoveredTile;
						GridTile dragPoint = ship.getDragPointTile();
						if (dragPoint != null) {
							int tileLength = ship.getShipType().getTileLength();
							Orientation orientation = ship.getOrientation();
							GridTile southWest = hoveredTile, northEast = hoveredTile;
							if (orientation == Orientation.HORIZONTAL) {
								southWest = southWest.transform(-(dragPoint.getX()), 0);
								northEast = northEast.transform((tileLength - 1) - dragPoint.getX(), 0);
							} else if (orientation == Orientation.VERTICAL) {
								southWest = southWest.transform(0, -(dragPoint.getY()));
								northEast = northEast.transform(0, (tileLength - 1) - dragPoint.getY());
							}
							highlights.add(new GridTileRectangle(southWest, northEast));
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
			// TODO Auto-generated method stub
		}

		@Override
		public void dragExit(DropTargetEvent dte) {
			resetDragVars();
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {
			if (dtde.isDataFlavorSupported(GameComponent.FLAVOR)) {
	            Transferable transferable = dtde.getTransferable();
	            try {
	            	Object data = transferable.getTransferData(GameComponent.FLAVOR);
	            	if (data instanceof Ship) {
	            		Ship ship = (Ship) data;
	            		
	            		/** Calculate tile placement **/
	            		GridTile droppedTile = GridTile.forMouseLocation(dtde.getLocation());
	            		GridTile newTile = droppedTile;
	            		GridTile boundsCheck = new GridTile(0, 0);
	            		if (ship.getOrientation() == Orientation.HORIZONTAL) {
	            			newTile = newTile.transform(-(ship.getDragPointTile().getX()), 0);
	            			boundsCheck = newTile.transform(ship.getShipType().getTileLength() - 1, 0);
 	            		} else if (ship.getOrientation() == Orientation.VERTICAL) {
 	            			newTile = newTile.transform(0, -(ship.getDragPointTile().getY()));
	            			boundsCheck = newTile.transform(0, ship.getShipType().getTileLength() - 1);
 	            		}
	            		if (newTile.getX() < 0 || newTile.getY() > 9 || boundsCheck.getX() > 9 || boundsCheck.getY() > 9) {
	            			dtde.dropComplete(false);
	            			resetDragVars();
	            			return;
	            		}
	            		/** Relocate and add component to grid **/
	            		ship.setLocation(newTile.getPixelBase());
	            		addShip(ship);	            	
	            		
	            		/** Accept the drop and complete the transfer **/
	            		dtde.acceptDrop(DnDConstants.ACTION_MOVE);
	            		dtde.dropComplete(true);
	            		
	            		boolean allShips = true;
	            		for (Ship s : getShips()) {
	            			if (s == null) {
	            				allShips = false;
	            			}
	            		}            		
	            		if (allShips) {
	            			Screen screen = Application.getInstance().getScreen();
	            			if (screen instanceof SetupScreen) {
	            				((SetupScreen) screen).setShowFindOpponent(true);
	            			}
	            		}          		
	            		resetDragVars();
	            		return;
	            	}
	            } catch (IOException e) { //data no longer available
	            	e.printStackTrace();
	            } catch (UnsupportedFlavorException e) { //unsupported type
					e.printStackTrace();
				}
			}
			dtde.rejectDrop();
			dtde.dropComplete(false);
			resetDragVars();
		}
		
		/**
		 * Adds a ship to the grid
		 * @param ship
		 */
		public void addShip(Ship ship) {
			ship.setupRotator();
			this.add(ship);
    		this.invalidate();
    		this.revalidate();
    		this.repaint();
    		ships[ship.getShipType().ordinal()] = ship;
		}
		
		/**
		 * Resets the draggable variables
		 */
		private void resetDragVars() {
			highlights.clear();
			currentDragHover = null;
		}
		
		/**
		 * Gets the ships
		 * @return Ship[]
		 */
		public Ship[] getShips() {
			return ships;
		}
		
		/**
		 * Gets the grid tiles
		 * @return GridTile[][]
		 */
		public GridTile[][] getTiles() {
			return tiles;
		}
	}
	
	
	/**
	 * Gets the grid
	 * @return {Grid}
	 */
	public Grid getGrid() {
		return grid;
	}

}
