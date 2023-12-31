package hawc;

import java.nio.file.*;
import java.io.*;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

	public static Path createTmpDir() throws IOException {
		Path tmpDir = Paths.get("");

                tmpDir = Files.createTempDirectory(tmpDir,"tmp");
                tmpDir.toFile().deleteOnExit();

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

	//regresa el indice de un nombre en un vector de strings ejemplo
        // "A" en ["D", "A", "z"] devuelve 1
        public static int nombreEnVector(String cadena, String nombres) {
            int indice = Arrays.asList(nombres).indexOf(cadena);
            return indice;
        }

        //version vectorial de nombreEnVector
        public static int[] nombresEnVector(String[] cadena, String nombres) {

            int[] resultado = new int[cadena.length];

            for(int i=0; i<nombres.length(); i++) {
                resultado[i] = nombreEnVector(cadena[i], nombres   );
            }

            return resultado;
        }


	public static boolean createDir (String dir) {
		Path directory = Paths.get(dir);
		if (Files.notExists(directory)) {
			try {
				Files.createDirectory(directory);
			} catch (IOException ioError) {
				return false;
			}
		} else {
			Utils.println("El directorio "+directory.toString()+" ya existe");
		}
		return true;
	}

	public static String getFileExtension(String name) {
		int dot = name.lastIndexOf(".");
		if (dot>0) 
			return name.substring(dot);
		else
			return null; 
	}

	public static String getOutputFilenameFor(String inputFile) {
		Path input;
		String slash = FileSystems.getDefault().getSeparator();
		String extension = getFileExtension(inputFile);

		if (extension!=null) 
			input = Paths.get(inputFile.replace(extension,""));
		else
		       	input = Paths.get(inputFile);
		LocalDateTime now = LocalDateTime.now(); 
		DateTimeFormatter format = DateTimeFormatter.ofPattern("(yyyyMMdd_HHmm)"); 
		String timeStamp = now.format(format);
		String output = "resultados"+slash+input.getFileName().toString()+"_filtered"+timeStamp+".csv";

		return output;
	}

}
