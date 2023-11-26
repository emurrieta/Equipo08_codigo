package hawc;

import java.nio.file.*;
import java.io.*;

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

	public static Path createTmpDir() {
		Path tmpDir = Paths.get("");
                //String subdirSymbol = FileSystems.getDefault().getSeparator();
                try {
                        tmpDir = Files.createTempDirectory(tmpDir,"tmp");
                        tmpDir.toFile().deleteOnExit();
                } catch (IOException ioError) {
                        Utils.println("Utils> Error al crear directorio temporal");
                }

		return tmpDir;
	}

	public static void deleteTmpDir(Path tmpDir) { 
		try {
			File dir = new File(tmpDir.toString());
			File filesList[] = dir.listFiles();
			for (File f: filesList) { 
				Files.deleteIfExists(Paths.get(f.getAbsolutePath()));
			}
		} catch (IOException ioError) {
			Utils.println("Utils> Error al borrar el directorio temporal");
		}
	}

}
