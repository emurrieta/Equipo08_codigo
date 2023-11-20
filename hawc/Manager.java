package hawc;

import java.lang.Runtime;

public class Manager implements InterfaceManager<CSV>, Runnable {
	private int CPUs;

	public Manager() {
		this.CPUs = Runtime.getRuntime().availableProcessors();
		System.out.println("\t Numero de CPUs \t" + CPUs);
	}

	/* 
	 * Indica si el procesamiento es serial o paralelo
	 * si 'parallel' es True el procesmiento es en paralelo
	 * si 'parallel' es False se realiza en forma serial.
	 */ 
	@Override
	public void setProcessingModel(boolean parallel) {

	}

	/* 
	 * Realiza el procesamiento de un query .
	 *
	 * Retorna True si el procesamiento fue exitoso o
	 * False si ocurre un error.
	 */ 
	@Override
	public boolean processQuery(String query,CSV csv) {
		return (true);
	}

	/* 
	 * Guarda el resultado de un Query en el archivo path.
	 * Retorna True si el guardado fue correcto o False en
	 * caso contrario. 
	 */ 
	@Override
	public boolean saveQuery(String path) {
		return (true);
	}

	/* 
	 * Retorna las metricas del procesamiento
	 */ 
	@Override
	public int[] timers() {
		int[] iTimers;
		iTimers = new int[1];

		return (iTimers);
	}

	@Override
	public void run() {

	}
}
