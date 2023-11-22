package hawc;

//interface InterfaceDataFrame<DataFrame extends InterfaceDataFrame, CSV> {
interface InterfaceDataFrame<CSV> {
	/* 
	 * Recibe una cadena con una expresion 'query' que indica
	 * la accion a realizar con las columnas del DataFrame.
	 *
	 * Retorna Falso si hay un error al procesar el query o
	 * True si todo se realizo correctamente.
	 */ 
	public boolean executeQuery(String query);

	/* 
	 * Retorna un arreglo con los encabezados del DataFrame.
	 */ 
	public String[] headers();

	/* 
	 * Define el CSV de entrada para el DataFrame
	 */ 
	public void inputCSV(CSV csv);

	/* 
	 * Define el CSV para guardar la salida del procesamiento
	 * del DataFrame.
	 */ 
	public void outputCSV(CSV csv);
	
	// comentariooooooooo
	

}
