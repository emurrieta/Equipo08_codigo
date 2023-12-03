/*
 * Clase: Manager
 *
 * Implementa el modelo Manager-Worker usando programación 
 * multihilos.
 *
 * Programador: Eduardo Murrieta
 * Revisores:   Donovan Montejano, Alejandro Salcido.
 *
 * ------------------------------------------------------------------------
 * Métodos públicos:
 * 	public Manager (int CPUs): constructor, indica la cantidad de workers
 * 			que se usarán.
 *
 * 	public void setProcessingModel(boolean parallel): intercambia entre 
 * 			el modelo de procesamiento serial o paralelo.
 *
 * 	public boolean processQuery(String query,CSV csv): inicia el análisis
 * 			del 'csv' usando el filtro 'query'. 
 *
 * 	public boolean saveQuery(CSV csv): Guarda el resultado del 'query' en 
 * 			el 'csv' de salida.
 *
 * 	public long[] timers(): Retorna las métricas de tiempo del trabajo de
 * 			los workers con las posiciones siguientes:
 * 			0 : Tiempo de segmentación serial del CSV
 * 			1 : Tiempo de filtrado del CSV
 * 			2 : Tiempo de integración de los segmentos de salida.
 *
 * 	public int getCPUs():  Retorna el total de CPUs en la arquitectura.
 *
 */
package hawc;

import java.lang.Runtime;
import java.util.concurrent.*;
import java.nio.file.*;
import java.io.*;

/*
 * Clase para implementar el modelo de procesamiento Manager-Worker.
 * Es responsable de crear la cantidad determinada de Workers, asignales
 * trabajo y esperar a que concluyan.
 */
public final class Manager implements InterfaceManager<CSV> {
	private int CPUs;
	private	DataFrame df[];
	private CSV CSVs[];
	private boolean parallel;
	private long freeMemory,totalMemory; 
	private StopWatch timers[];
	Path tmpDir;

	public Manager(int CPUs) {
		this.freeMemory = Runtime.getRuntime().freeMemory();
		this.totalMemory = Runtime.getRuntime().totalMemory();
		if (CPUs==0) 
			this.CPUs = Runtime.getRuntime().availableProcessors()*4;
		else
			this.CPUs = CPUs;
		this.parallel = this.CPUs>1? true: false;

		timers = new StopWatch[3];
		for (int i=0; i<3; i++) timers[i]=new StopWatch();
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

		iWorkers=this.CPUs;
		if (this.parallel) 
			Utils.println("Paralelo con "+iWorkers+ " hilos");
		else
			Utils.println("Serial con "+iWorkers+ " hilos");

		// Divide el CSV de entrada en fracciones
		this.CSVs = new CSV[iWorkers];
		timers[0].start();
		if (parallel) {
			System.out.println("Segmentando el archivo de entrada...");
			CSVs = csv.split(iWorkers);
			if (CSVs == null) 
				return false;
		} else {
			CSVs[0] = csv;
		}
		timers[0].stop();

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
		timers[1].start();
		System.out.print("Realizando Query...");
		for (int thread=0; thread<iWorkers; thread++)
			poolDataFrames.execute (new Worker(query,this.df[thread]));

		poolDataFrames.shutdown();
		while (!poolDataFrames.isTerminated()) { 
			try { 
		                System.out.print(".");
				Thread.sleep(100); 
			} catch (InterruptedException e) { 
			}
		}
		timers[1].stop();
		System.out.println(".");
		System.out.println("Query concluido");

		return (true);
	}

	/* 
	 * Guarda el resultado de un Query en el archivo path.
	 * Retorna True si el guardado fue correcto o False en
	 * caso contrario. 
	 */
	@Override
	public boolean saveQuery(CSV csv) {
		System.out.println("Integrando el archivo de salida...");
		timers[2].start();
		if (this.parallel) 
			if (csv.join(CSVs)==null) return false;
		timers[2].stop();

		return true;
	}

	/* 
	 * Retorna las metricas del procesamiento de los workers
	 * con las posiciones siguientes: 
	 *	0 : Tiempo de segmentación del CSV 
	 *	1 : Tiempo de filtrado del CSV 	
	 *	2 : Tiempo de integración de los segmentos de salida
	 */ 
	@Override
	public long[] timers() {
		long[] times;
		times = new long[3];
		for (int i=0; i<3; i++)
			times[i]=timers[i].elapsedTime();

		return (times);
	}

	// Retorna el total de CPUs en la arquitectura
	public int getCPUs() {
		return (this.CPUs);
	}
}

/*
 * Clase utilitaria para llevar el registro de los tiempos de 
 * procesamiento consumidos.
 */
class StopWatch {
	private long startTime;
	private long stopTime;

	protected StopWatch() {
		startTime=0;
		stopTime=0;
	}

	protected void start() {
		startTime = System.nanoTime();
	}

	protected void stop() {
		stopTime = System.nanoTime();
	}

	protected long elapsedTime() {
		return (stopTime-startTime);
	}
}

/*
 * Clase Worker
 * Complemento del Manager, asigna el trabajo que debe realizar
 * cada hilo con un DataFrame y CSV asociados.
 */
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
		dataFrame.executeQuery(strQuery);
	}
}

