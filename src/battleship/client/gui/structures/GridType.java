package battleship.client.gui.structures;

/**
 * Represents a grid type
 * GridType.java
 * @author Clayton Williams
 * @date Feb 23, 2016
 *
 */
public enum GridType {
	
	PLAYER("grid.png"),
	ENEMY("enemy_grid.png");
	
	/**
	 * The file name of our grid background
	 */
	private final String fileName;
	
	/**
	 * GridType
	 * @param fileName
	 */
	GridType(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the file name
	 * @return
	 */
	public final String getFileName() {
		return fileName;
	}
	
	

}
