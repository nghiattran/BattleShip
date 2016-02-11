package org.cs330.battleship.tools;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.cs330.battleship.Constants;

/**
 * Various file related utilities
 * FileUtils.java
 * @author Clayton Williams
 * @date Feb 6, 2016
 */
public class FileUtils {
	
	/**
	 * The map of loaded sprites
	 */
	private static Map<String, BufferedImage> loadedSprites = new HashMap<String, BufferedImage>();
	
	/**
	 * Gets a sprite
	 * @param String - the resource to get
	 * @return {BufferedImage}
	 */
	public static BufferedImage getSprite(String resource) {
		try {
			if (loadedSprites.containsKey(resource))
				return loadedSprites.get(resource);		
			else {
				BufferedImage image = ImageIO.read(Constants.class.getResource(resource));
				if (image != null) {
					loadedSprites.put(resource, image);
					return image;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
