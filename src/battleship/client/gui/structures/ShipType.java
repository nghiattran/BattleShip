package battleship.client.gui.structures;

import java.awt.image.BufferedImage;

import battleship.client.system.FileSystem;

/**
 * The 5 ship types
 * ShipType.java
 * @author Clayton Williams
 * @date Feb 22, 2016
 */
public enum ShipType {
	
	CARRIER(5),
	BATTLESHIP(4),
	CRUISER(3),
	SUBMARINE(3),
	DESTROYER(2);
	
	/**
	 * The tile length of this ship
	 */
	private final int tileLength;
	
	/**
	 * ShipType
	 * @param tileLength - the length (in tiles) that this ship spans across
	 */
	ShipType(int tileLength) {
		this.tileLength = tileLength;
	}
	
	/**
	 * Gets the ship image
	 * @return {BuferedImate}
	 */
	public BufferedImage getImage() {
		return FileSystem.getImage(name().toLowerCase() + ".png");
	}
	
	/**
	 * Gets the tile length
	 * @return int - tile length of this ship
	 */
	public int getTileLength() {
		return this.tileLength;
	}

}
