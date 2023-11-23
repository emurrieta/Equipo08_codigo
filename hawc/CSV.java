package hawc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class CSV implements InterfaceCSV<CSV> {
	 @Override
    public boolean inputFile(String inputPath) {
        try {
            // Ingresar ruta de entrada del archivo 
            if (inputPath != null && !inputPath.isEmpty()) {
                System.out.println("Ruta de entrada proporcionada: " + inputPath);
            } else {
                // Solicitar al usuario que ingrese manualmente la ruta del archivo
                try (Scanner scanner = new Scanner(System.in)) {
                    System.out.println("Ingrese la ruta del archivo de entrada:");
                    inputPath = scanner.nextLine();
                }
            }
            // Verificar si la ruta es nula o vacía
            if (inputPath == null || inputPath.isEmpty()) {
                throw new IllegalArgumentException("La ruta de entrada no puede ser nula o vacía.");
            }
            // Verificar si el archivo en la ruta existe
            File inputFile = new File(inputPath);
            if (!inputFile.exists() || !inputFile.isFile()) {
                throw new FileNotFoundException("El archivo de entrada no existe o no es un archivo válido.");
            }
            // Verificar si se puede leer el archivo
            if (!inputFile.canRead()) {
                throw new SecurityException("No se puede leer el archivo de entrada debido a permisos insuficientes.");
            }
            // Verificar si se puede escribir en el archivo
            if (!inputFile.canWrite()) {
                throw new SecurityException("No se puede escribir en el archivo de entrada debido a permisos insuficientes.");
            }
            
            System.out.println("Archivo de entrada abierto correctamente: " + inputPath);
            return true;

        } catch (IllegalArgumentException | SecurityException | FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

	@Override
public boolean outputFile(String outputPath) {
    try {
        // Ingresar ruta de salida del archivo 
        if (outputPath == null || outputPath.isEmpty()) {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Ingrese la ruta del archivo de salida:");
                outputPath = scanner.nextLine();
            }
        }

        // Verificar si la ruta de salida es nula o vacía
        if (outputPath == null || outputPath.isEmpty()) {
            throw new IllegalArgumentException("La ruta de salida no puede ser nula o vacía.");
        }

        // Crear un objeto File para la ruta de salida
        File outputFile = new File(outputPath);

        // Verificar si el archivo en la ruta de salida ya existe
        if (outputFile.exists()) {
            throw new IllegalArgumentException("El archivo de salida ya existe. Por favor, proporciona una ruta de salida válida.");
        }

        // Crear el archivo de salida
        if (!outputFile.createNewFile()) {
            throw new IOException("No se pudo crear el archivo de salida.");
        }

        // Verificar si se puede escribir en el archivo de salida
        if (!outputFile.canWrite()) {
            throw new SecurityException("No se puede escribir en el archivo de salida debido a permisos insuficientes.");
        }

        System.out.println("Archivo de salida creado correctamente: " + outputPath);
        return true;

    } catch (IllegalArgumentException | SecurityException | IOException e) {
        System.err.println("Error al crear el archivo de salida: " + e.getMessage());
        return false;
    }
}

	public CSV[] split(int segments)  {
		CSV[] csvs = new CSV[segments];  
		for (int i=0; i<segments; i++) {
			// Configurar cada CSV
			csvs[i] = new CSV();
		}

		return(csvs);
	}

	public CSV join(CSV[] segments, String outputPath) {
		CSV outputCSV;
		outputCSV = new CSV();
		outputCSV.outputFile (outputPath);

		return (outputCSV);
	}

	public String getRecord() { 
		return ("");

	}

	public void putRecord(String record){

	}
}


