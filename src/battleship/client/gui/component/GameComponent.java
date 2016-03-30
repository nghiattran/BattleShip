package battleship.client.gui.component;

import java.awt.datatransfer.DataFlavor;

import javax.swing.JComponent;

import battleship.client.gui.structures.Orientation;

/**
 * Superclass for a gamepanel used on the screen
 * GamePanel.java
 * @author Clayton Williams
 * @date Feb 6, 2016
 */
public abstract class GameComponent extends JComponent { 
	
	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = 1554672947928575304L;
	
	/**
	 * The instance of the component flavor
	 */
	public static final ComponentFlavor FLAVOR = new ComponentFlavor();
	
	/**
	 * The horizontal width and height
	 */
	private final int width, height;

	/**
	 * Sets some basic needed configurations
	 * @param width - the width of this component
	 * @param height - the height of this component
	 * @param x - the x coordinate of this component
	 * @param y - the y coordinate of this component
	 */
	public GameComponent(int width, int height, int x, int y) {
		this.width = width;
		this.height = height;
		this.setSize(width, height);
		this.setLocation(x, y);
		this.setLayout(null);
	}
	
	/**
	 * Flips the component size
	 */
	protected void configureSize(Orientation orientation) {
		this.setSize(orientation == Orientation.HORIZONTAL ? width : height, orientation == Orientation.HORIZONTAL ? height : width);
		this.invalidate();
	}
	
	/**
	 * Represents a custom meta data flavor to be used by transfer handlers
	 * @author Clayton Williams
	 * @date Feb 28, 2016
	 */
	public static final class ComponentFlavor extends DataFlavor {

		/**
		 * ComponentFlavor
		 */
	    public ComponentFlavor() {
	        super(GameComponent.class, null);
	    }
	}

}
