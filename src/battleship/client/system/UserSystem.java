package battleship.client.system;

/**
 * The usersystem
 * UserSystem.java
 * @author Clayton Williams
 * @date Feb 25, 2016
 */
public class UserSystem {
	
	/**
	 * The operating system of the management server
	 */
	public static final OperatingSystem OS = java.lang.System.getProperty("os.name").toUpperCase().contains("WIN") ? OperatingSystem.WINDOWS : OperatingSystem.UNIX;
    
    
    /**
     * The operating systems - for file path fixing
     * @author Clayton Williams
     */
    public static enum OperatingSystem {
    	
    	UNIX,
    	WINDOWS;

    	public static OperatingSystem get(String type) {
    		if (type.contains("UNIX")) {
    			return UNIX;
    		}
    		return WINDOWS;
    	}
    }


}
