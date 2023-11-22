package hawc;

public class DataFrame implements InterfaceDataFrame<CSV> {

	public DataFrame() {

	}
	/* 
	 * Recibe una cadena con una expresion 'query' que indica
	 * la accion a realizar con las columnas del DataFrame.
	 *
	 * Retorna Falso si hay un error al procesar el query o
	 * True si todo se realizo correctamente.
	 */ 
	public boolean executeQuery(String query) { 
		System.out.println("Executing Query: "+query);
		return (true); 
	}

	/* 
	 * Retorna un arreglo con los encabezados del DataFrame.
	 */ 
	public String[] headers() { String[] hdr = new String[2]; return (hdr); }

	/* 
	 * Define el CSV de entrada para el DataFrame
	 */ 
	public void inputCSV(CSV csv) { }

	/* 
	 * Define el CSV para guardar la salida del procesamiento
	 * del DataFrame.
	 */ 
	public void outputCSV(CSV csv) { }
	
	// comentariooooooooo
	
}
