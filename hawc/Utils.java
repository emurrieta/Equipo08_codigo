package hawc;

public class Utils {
	private static boolean debugMode=false;

	public static void setDebug(boolean mode) {
		debugMode=mode;
	}

	public static void println (String message) {
		if (debugMode) 
			System.out.println(message);
	}

	public static void print (String message) {
		if (debugMode) 
			System.out.print(message);
	}
}
