package battleship.client.gui.component.impl;

import java.awt.Graphics;
import battleship.client.gui.component.GameComponent;
import battleship.client.gui.structures.ShipType;

/**
 * Represents a bay for the available ships to choose from (drag and drop)
 * ShipBay.java
 * @author Clayton Williams
 * @date Feb 16, 2016
 */
public class ShipBay extends GameComponent {

	/**
	 * Generated serial
	 */
	private static final long serialVersionUID = -5033789449741029554L;
	
	/**
	 * The ship values
	 */
	private Ship[] ships = new Ship[ShipType.values().length];
	
	/**
	 * The ship bay
	 * @param x - the x placement coordinate
	 * @param y - the y placement coordinate
	 */
	public ShipBay(int x, int y) {
		super(450, 400, x, y);
		for(int i = 0; i < ships.length; i++) {
			ships[i] = new Ship(0, i * 60, ShipType.values()[ships.length - 1 - i]);
			this.add(ships[i]);
			this.revalidate();
		}
		this.setVisible(true);
	}
	
	/**
	 * Get ship for battle grid. They should use the same ships
	 * @return ships
	 */
	public Ship[] getShips() {
		return ships;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

}
