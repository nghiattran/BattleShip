package battleship.client.gui.component.impl;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JButton;

import battleship.client.gui.Application;
import battleship.client.gui.component.GameComponent;
import battleship.client.gui.component.impl.BattleGrid.Grid;
import battleship.client.gui.screen.Screen;
import battleship.client.gui.screen.impl.SetupScreen;
import battleship.client.gui.structures.GridTile;
import battleship.client.gui.structures.Orientation;
import battleship.client.gui.structures.ShipType;

/**
 * Represents a ship component
 * Ship.java
 * @author Clayton Williams
 * @date Feb 28, 2016
 */
public class Ship extends GameComponent implements Transferable, DragGestureListener, DragSourceListener, MouseListener {
	
	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -8243980874582458768L;
	
	/**
	 * The ship type
	 */
	private final ShipType shipType;
	
	/**
	 * The drag gesture recognizer
	 */
	private DragGestureRecognizer dragRecognizer;
	
	/**
	 * The original parent
	 */
	private transient Container originalParent; 
	
	/**
	 * The current ship orientation
	 */
	private Orientation orientation = Orientation.HORIZONTAL;
	
	/**
	 * The tile inside the ship which we initiated the drag with
	 */
	private GridTile dragPointTile;
	
	/**
	 * The rotate button
	 */
	private JButton rotate = new JButton("R");
	
	/**
	 * Ship
	 * @param shipType
	 */
	public Ship(final ShipType shipType) {
		this(0, 0, shipType);
	}
	
	/**
	 * Ship
	 * @param x
	 * @param y
	 * @param shipType
	 */
	public Ship(int x, int y, final ShipType shipType) {
		super(shipType.getImage().getWidth(), shipType.getImage().getHeight(), x, y);
		this.shipType = shipType;
		this.setVisible(true);
		this.addMouseListener(this);
	}
	
	/**
	 * Sets up the rotator button
	 */
	public void setupRotator() {
		this.rotate.setBounds(0, 0, 25, 25);
		this.rotate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (Application.getInstance().getScreen(SetupScreen.class) != null) {
					orientation = orientation == Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL;
					setSize((int) getSize().getHeight(), (int) getSize().getWidth());
				} else {
					rotate.setVisible(false);
				}
			}
			@Override
			public void mouseExited(MouseEvent e) {
				rotate.setVisible(false);
			}
		});
		this.rotate.setVisible(false);
		this.add(rotate);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		BufferedImage image = shipType.getImage();
        
        if (orientation == Orientation.VERTICAL) {
            AffineTransform at = new AffineTransform();
            at.translate(getWidth() / 2, getHeight() / 2);
            at.rotate(Math.PI/2);
            at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
        
        	g2.drawImage(image, at, null);
        } else {
        	g2.drawImage(image, 0, 0, null);
        }
	}
	
	/**
	 * Gets the ship type
	 * @return {ShipType}
	 */
	public ShipType getShipType() {
		return this.shipType;
	}
	
	/**
	 * Gets the ship orientation
	 * @return {Orientation}
	 */
	public Orientation getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Sets the orientation
	 * @param orientation
	 */
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
		configureSize(orientation);
	}
	
	/**
	 * Gets the drag point tile
	 * @return {GridTile}
	 */
	public GridTile getDragPointTile() {
		return this.dragPointTile;
	}

	/**
	 * Gets the original parent
	 * @return {Component}
	 */
	public Component getOriginalParent() {
		return this.originalParent;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[]{ GameComponent.FLAVOR };
	}
	
	/**
	 * Gets this ship object
	 * @return {Ship}
	 */
	private Ship getThis() {
		return this;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		for (DataFlavor fl : getTransferDataFlavors()) {
			if (fl.equals(flavor)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		return isDataFlavorSupported(flavor) ? getThis() : null;
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if (dragRecognizer == null) {
			dragRecognizer = DragSource.getDefaultDragSource().createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, this);
		}
	}
	
	@Override
	public void removeNotify() {
		if (dragRecognizer != null) {
            dragRecognizer.removeDragGestureListener(this);
        }
		dragRecognizer = null;
		dragPointTile = null;
        super.removeNotify();
	}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		if (!dsde.getDropSuccess()) {
			//TODO
		} else {
			this.originalParent.remove(this);
			this.originalParent.invalidate();
			this.originalParent.revalidate();
			this.originalParent.repaint();
		}
		dragPointTile = null;
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		Screen screen = Application.getInstance().getScreen();
		if (!(screen instanceof SetupScreen)) {
			return;
		}
		Container parent = getThis().getParent();
        this.originalParent = parent;
        DragSource ds = dge.getDragSource();
        dragPointTile = GridTile.forMouseLocation(dge.getDragOrigin());
        ds.startDrag(dge, Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR), this, this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//TODO
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (this.getParent() instanceof Grid) {
			if (Application.getInstance().getScreen(SetupScreen.class) != null) {
				rotate.setVisible(true);
			} else {
				rotate.setVisible(false);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (this.getParent() instanceof Grid) {
			if (Application.getInstance().getScreen(SetupScreen.class) != null) {
				if ((e.getPoint().getX() <= 0 || e.getPoint().getX() > rotate.getSize().getWidth()) && (e.getPoint().getY() <= 0 || e.getPoint().getY() > rotate.getSize().getHeight()))
					rotate.setVisible(false);
			}
		}
	}
}
