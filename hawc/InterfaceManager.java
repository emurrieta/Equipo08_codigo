package hawc;

interface InterfaceManager<CSV> {
	/* 
	 * Indica si el procesamiento es serial o paralelo
	 * si 'parallel' es True el procesmiento es en paralelo
	 * si 'parallel' es False se realiza en forma serial.
	 */ 
	public void setProcessingModel(boolean parallel);

	/* 
	 * Realiza el procesamiento de un query .
	 *
	 * Retorna True si el procesamiento fue exitoso o
	 * False si ocurre un error.
	 */ 
	public boolean processQuery(String query,CSV csv);

	/* 
	 * Guarda el resultado de un Query en el archivo path.
	 * Retorna True si el guardado fue correcto o False en
	 * caso contrario. 
	 */ 
	public boolean saveQuery(CSV csv);

	/* 
	 * Retorna las metricas del procesamiento
	 */ 
	public long[] timers();
}
