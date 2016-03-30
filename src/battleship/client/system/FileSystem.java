package battleship.client.system;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;

import battleship.client.system.sound.SoundUtils;

/**
 * Represents filesystem related tasks
 * FileSystem.java
 * @author Clayton Williams
 * @date Feb 25, 2016
 */
public class FileSystem {
	
	/**
	 * The file data
	 */
	private static Map<String, Object> fileData = new HashMap<String, Object>();
	
	/**
	 * Gets an image from a file name
	 * @param fileName
	 * @return
	 */
	public static BufferedImage getImage(String fileName) {
		if (fileData.containsKey(fileName) && fileData.get(fileName) instanceof BufferedImage)
			return (BufferedImage) fileData.get(fileName);			
		return null;
	}
	
	/**
	 * Loads a local directory recursively and parses needed file data
	 * @param directory - the root directory
	 */
    public static void loadLocal(File directory) {
    	try {
    		File[] list = directory.listFiles();
    		if (list == null) return;
    		for (File f : list) {
    			String fileName = f.getName();
    			String extension = null;
    			int i = fileName.lastIndexOf('.');
    			if (i > 0) {
    			    extension = fileName.substring(i + 1);
    			}
    			if (f.isDirectory()) {
    				loadLocal(f);
    			} else {
    				if (f.exists() && extension != null) {
    					Object object = null;
    					switch (extension) {
	    					case "png":
	    						byte[] data = Files.readAllBytes(f.toPath());
	        					InputStream in = new ByteArrayInputStream(data);
	        					object = ImageIO.read(in);
	    						break;
	    					case "wav":
	    						object = SoundUtils.getStream(f.toPath().toString());
	    						break;
	    					case "mp4":
//	    						byte[] data1 = Files.readAllBytes(f.toPath());
//	        					InputStream audioSrc = new ByteArrayInputStream(data1);
//	    						object = AudioSystem.getAudioInputStream(audioSrc);
	    						break;
	    					default:
	    						System.out.println("File extension " + extension + " not supported!");
	    						break;
    					}
    					fileData.put(fileName, object);
    				}
    			}  				
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

	public static AudioInputStream loadWav(String fileName) {
		if (fileData.containsKey(fileName) && fileData.get(fileName) instanceof AudioInputStream)
    		return (AudioInputStream) fileData.get(fileName);
		return null;
	}

}
