package hawc;
import java.io.*;
import java.util.Scanner;

public class CSV implements InterfaceCSV<CSV> {
    private BufferedReader reader;
    private BufferedWriter writer;
    private String inputFilePath;
    private String outputFilePath;
    
    /* 
     * Define la ruta del archivo CSV de entrada
     * Si la ruta es valida se apertura el archivo 
     * para lectura y retorna True.
     * Si la ruta es invalida se retorna False.
     */      
    
     @Override
         public boolean inputFile(String inputPath) {
             try {
           // Ingresar ruta de entrada del archivo 
            if (inputPath != null && !inputPath.isEmpty()) {
                Utils.println("Ruta de entrada proporcionada: " + inputPath);
            } else {
                // Solicitar al usuario que ingrese manualmente la ruta del archivo
                try (Scanner scanner = new Scanner(System.in)) {
                    Utils.println("Ingrese la ruta del archivo de entrada:");
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
            //Asignación de ruta de archivo 
               inputFilePath = inputPath;
            //Lector de archivos 
               reader = new BufferedReader(new FileReader(inputFilePath));
                    Utils.println("Archivo de entrada abierto correctamente: " + inputFilePath);
            return true;

        } catch (IllegalArgumentException | SecurityException | FileNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
  
     /* 
     * Define la ruta del archivo CSV de salida
     * Si la ruta es valida se apertura el archivo 
     * para escritura y retorna True.
     * Si la ruta es invalida se retorna False.
     */ 
    
     @Override
         public boolean outputFile(String outputPath) {
             try {
        // Ingresar ruta de salida del archivo 
        if (outputPath == null || outputPath.isEmpty()) {
            try (Scanner scanner = new Scanner(System.in)) {
                Utils.println("Ingrese la ruta del archivo de salida:");
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
            throw new IllegalArgumentException("El archivo de salida ya existe. Proporciona una ruta de salida válida.");
        }

        // Crear el archivo de salida
        if (!outputFile.createNewFile()) {
            throw new IOException("No se pudo crear el archivo de salida.");
        }

        // Verificar si se puede escribir en el archivo de salida
        if (!outputFile.canWrite()) {
            throw new SecurityException("No se puede escribir en el archivo de salida debido a permisos insuficientes.");
        }
        // Asignación ruta del archivo 
            outputFilePath = outputPath;
        // Escritura del archivo 
            writer = new BufferedWriter(new FileWriter(outputFilePath));
             Utils.println("Archivo de salida creado correctamente: " + outputPath);
        return true;

    } catch (IllegalArgumentException | SecurityException | IOException e) {
        System.err.println("Error al crear el archivo de salida: " + e.getMessage());
        return false;
    }
}

    /* 
     * Divide el archivo de entrada en la cantidad 
     * de 'segments' indicada.
     * Retorna un arreglo de objetos CSV con
     * 'segments' elementos en los que el archivo
     * de entrada es el segmento correspondiente.
     */ 
          
     @Override
         public CSV[] split(int segments) {
              try {
        if (reader == null) {
            throw new IllegalStateException("Debes abrir el archivo de entrada antes de intentar dividirlo.");
        }

        // Crear la matriz para almacenar los segmentos
        CSV[] segmentArray = new CSV[segments];

        // Crear un nuevo objeto CSV para cada segmento
        for (int i = 0; i < segments; i++) {
            segmentArray[i] = new CSV();
            segmentArray[i].inputFilePath = this.inputFilePath;
            segmentArray[i].outputFilePath = this.outputFilePath.replace(".csv", "_segment" + i + ".csv");
            segmentArray[i].reader = new BufferedReader(new FileReader(segmentArray[i].inputFilePath));
            segmentArray[i].writer = new BufferedWriter(new FileWriter(segmentArray[i].outputFilePath));
        }

        int currentSegment = 0;

        // Leer y escribir filas en los segmentos de forma cíclica
        String record;
        while ((record = getRecord()) != null) {
            segmentArray[currentSegment].putRecord(record);
            currentSegment = (currentSegment + 1) % segments;
        }

        // Cerrar lectores y escritores de todos los segmentos
        for (CSV segment : segmentArray) {
            segment.reader.close();
            segment.writer.close();
        }

        Utils.println("Archivo dividido en " + segments + " segmentos.");
        return segmentArray;

    } catch (IOException e) {
        System.err.println("Error al dividir el archivo: " + e.getMessage());
        return null;
    }
     
}

     /* 
     * Une todos los CSVs del arreglo 'segments'
     * en un unico CSV y lo retorna.
     * Retorna un arreglo de objetos CSV con
     * 'segments' elementos en los que el archivo
     * de entrada es el segmento correspondiente.
     */      
         
     @Override
         public CSV join(CSV[] segments, String outputPath) {
               try {
            // Verificar los segmentos y que no están vacíos
            if (segments == null || segments.length == 0) {
                throw new IllegalArgumentException("Se requieren segmentos para realizar la unión.");
            }

            // Verificar si la ruta de salida es proporcionada
            if (outputPath == null || outputPath.isEmpty()) {
                throw new IllegalArgumentException("La ruta de salida no puede ser nula o vacía.");
            }

            // Crear un nuevo objeto CSV para el archivo de salida combinado
            CSV combinedCSV = new CSV();

            // Asignar la ruta de entrada como la primera ruta de segmento
            combinedCSV.inputFilePath = segments[0].inputFilePath;

            // Asignar la ruta de salida proporcionada
            combinedCSV.outputFilePath = outputPath;

            // Escribe los datos en el nuevo Csv
            combinedCSV.writer = new BufferedWriter(new FileWriter(combinedCSV.outputFilePath));

            // Unir los segmentos en el archivo de salida combinado
            for (CSV segment : segments) {
                // Crear el lector para el segmento  
                segment.reader = new BufferedReader(new FileReader(segment.outputFilePath));

                // Leer y escribir cada registro del segmento
                String record = segment.getRecord();
                while (record != null) {
                    combinedCSV.putRecord(record);
                    record = segment.getRecord();
                }

                // Cerrar el lector del segmento actual
                segment.reader.close();
            }

            // Cerrar el escritor para el archivo de salida combinado
            combinedCSV.writer.close();

            Utils.println("Archivos combinados correctamente en: " + combinedCSV.outputFilePath);
            return combinedCSV;

        } catch (IOException e) {
            System.err.println("Error al realizar la unión de archivos: " + e.getMessage());
            return null;
        }
    }

     /*
     * Retorna una cadena con el siguiente registro
     * en el archivo de entrada o cadena vacia si 
     * ya no existen registros en el archivo.
     */  
            
     @Override
         public String getRecord() {
             try {
        if (reader != null) {
            //Lectrura del archivo 
            String record = reader.readLine();
            if (record != null) {
                Utils.println("Registro leído: " + record);
                return record;
            } else {
                // Cerrar el lector cuando se alcanza el final del archivo
                reader.close();
                Utils.println("Fin del archivo alcanzado.");
                return null;
            }
        } else {
            throw new IllegalStateException("Debes abrir el archivo de entrada antes de intentar leer registros.");
        }

    } catch (IOException e) {
        System.err.println("Error al leer el registro: " + e.getMessage());
        return null;
    }
}
     /*
     * Guarda un registro o cadena en el archivo
     * CSV de salida.
     */
         
     @Override
         public void putRecord(String record) {
             try {
            if (writer != null) {
                // Escribir el registro en el archivo de salida
                writer.write(record);
                writer.write("\n"); //Agregar un salto de linea 
                writer.flush(); // Forzar la escritura del buffer al archivo

                Utils.println("Registro escrito en el archivo de salida: " + record);
            } else {
                throw new IllegalStateException("Debes abrir el archivo de salida antes de intentar escribir registros.");
            }

        } catch (IOException e) {
            System.err.println("Error al escribir el registro: " + e.getMessage());
        }
    }
}