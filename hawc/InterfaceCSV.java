package hawc;

public interface InterfaceCSV<CSV extends InterfaceCSV> {
	/* 
	 * Define la ruta del archivo CSV de entrada
	 * Si la ruta es valida se apertura el archivo 
	 * para lectura y retorna True.
	 * Si la ruta es invalida se retorna False.
	 */ 
	public boolean inputFile(String inputPath);

	/* 
	 * Define la ruta del archivo CSV de salida
	 * Si la ruta es valida se apertura el archivo 
	 * para escritura y retorna True.
	 * Si la ruta es invalida se retorna False.
	 */ 
	public boolean outputFile(String outputPath);

	/* 
	 * Divide el archivo de entrada en la cantidad 
	 * de 'segments' indicada.
	 *
	 * Retorna un arreglo de objetos CSV con
	 * 'segments' elementos en los que el archivo
	 * de entrada es el segmento correspondiente.
	 */ 
	public CSV[] split(int segments);

	/* 
	 * Une todos los CSVs del arreglo 'segments'
	 * en un unico CSV y lo retorna.
	 *
	 * Retorna un arreglo de objetos CSV con
	 * 'segments' elementos en los que el archivo
	 * de entrada es el segmento correspondiente.
	 */ 
	public CSV join (CSV[] segments, String outputPath);

	/*
	 * Retorna una cadena con el siguiente registro
	 * en el archivo de entrada o cadena vacia si 
	 * ya no existen registros en el archivo.
	 */
	public String getRecord();

	/*
	 * Guarda un registro o cadena en el archivo
	 * CSV de salida.
	 */
	public void putRecord(String record);
}
