package hawc;

import java.lang.Runtime;
import java.util.concurrent.*;

public final class Manager implements InterfaceManager<CSV> {
	private int CPUs;
	private	DataFrame df[];
	private CSV CSVs[];
	private boolean parallel;

	public Manager(boolean parallel) {
		this.CPUs = Runtime.getRuntime().availableProcessors();
		this.parallel = parallel;
	}

	/* 
	 * Indica si el procesamiento es serial o paralelo
	 * si 'parallel' es True el procesmiento es en paralelo
	 * si 'parallel' es False se realiza en forma serial.
	 */ 
	@Override
	public void setProcessingModel(boolean parallel) {
		this.parallel = parallel;
	}

	/* 
	 * Realiza el procesamiento de un query .
	 *
	 * Retorna True si el procesamiento fue exitoso o
	 * False si ocurre un error.
	 */ 
	@Override
	public boolean processQuery(String query,CSV csv) {
		int iWorkers;

		// Determina si el trabajo se realiza en paralelo o
		// en serie
		if (this.parallel) 
			iWorkers = this.CPUs;
		else
			iWorkers = 1;


		// Divide el CSV de entrada en fracciones
		this.CSVs = new CSV[iWorkers];
		if (parallel) 
			CSVs = csv.split(iWorkers);
		else {
			CSVs[0] = csv;
		}

		// Genera los DataFrames necesarios para el procesamiento
	       	this.df = new DataFrame[iWorkers];
		for (int thread=0; thread<iWorkers; thread++) {
			df[thread]=new DataFrame();
			df[thread].inputCSV(CSVs[thread]);
			df[thread].outputCSV(CSVs[thread]);
		}

                // Crea el pool de hilos
		ExecutorService poolDataFrames = Executors.newFixedThreadPool(iWorkers);

		// Inicia la ejecucion de los hilos con un Worker
		// que recibe el DataFrame a usar y el query a procesar.
		for (int thread=0; thread<iWorkers; thread++)
			poolDataFrames.execute (new Worker(query,this.df[thread]));

		poolDataFrames.shutdown();
		while (!poolDataFrames.isTerminated()) { 
			try { 
				Thread.sleep(100); 
			} catch (InterruptedException e) { 
			}
		}

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

}


final class Worker implements Runnable {
	String strQuery;
	DataFrame dataFrame;

	/*
	 * Constructor de la clase Worker
	 * Parametros:
	 * query -  Cadena con la expresion del proceso a realizar
	 * df - Objeto de la clase DataFrame para realizar query 
	 */
	Worker (String query,DataFrame df) {
		strQuery=query;
		dataFrame = df;
	}

	@Override
	public void run() {
		 try { 
			 dataFrame.executeQuery(strQuery);
			 Thread.sleep(10000); 
		 } catch (InterruptedException e) { 
		 }
	}
}
