/*
 * Clase: HAWCquery
 *
 * Clase principal de la aplicación HAWCquery.
 *
 * Programador: Eduardo Murrieta
 * Revisores:   Donovan Montejano, Alejandro Salcido.
 * ------------------------------------------------------------------------
 *
 * El programa recibe un archivo CSV de entrada y lo filtra
 * usando una consulta al estilo de un query SQL. 
 *
 * Los argumentos y opciones de entrada se obtienen ejecutando el
 * programa sin parámetros.
 *   
 *   $ java HAWCquery 
 *   Requiere los argumentos: [-n Hilos] [-o CSV_salida] [-d] CSV_entrada "Query"
 *
 *   [-n Hilos]	Opcional, indica el número de hilos a generar, por omisión
 *   		el número de hilos es igual al total de cores disponibles*4
 *   		-n 1 implica ejecución serial.
 *   		-n 4 usar 4 hilos
 *
 *   [-o CSV_salida] Opcional, porporciona un nombre alternativo para el
 *   		archivo de salida, por omisión el archivo de salida se 
 *   		ubica en el directorio 'resultados' con el nombre:
 *   		CSV_entrada_filtered(AAAAMMDD_HHMM).csv
 *
 *   [-d]	Activa los mensajes de depuración, es recomendable sólo con 
 *   		CSVs de menos de 100 registros.
 *
 *   CSV_entrada Indica el nombre del archivo CSV de entrada. En el directorio
 *   		data se encuentra un ejemplo con 100 registros
 *   			data/reco_run002054_00226_sample100.csv
 *   		Y una versión con 500 mil registros comprimida:
 *   			data/reco_run002054_00226_sample.csv.zip
 *   "Query"	Es una cadena entrecomillada con la siguiente sintaxis:
 *   			"select(columnaX,columnaY,...)"
 *   		Donde: 'columnaX','columnaY', etc. es el nombre de una 
 *   		columna en el CSV_entrada que será colocada en el CSV de salida.
 *   		
 *   		Opcionalmente se puede colocar después de la cláusula 'select'
 *   		una cláusula 'where' con la sintaxis:
 *   		    "where(columanX OPLOG valorX, columnaN OPLOG valorN)"
 *
 *   		en donde: OPLOG puede ser algún operando '<','>','=','<=','>=';
 *   		que se aplica entre el valor de 'columnaN' y 'valorN'.
 *
 *   		La sintaxis completa es:
 *   		    "select(columnaX,columnaY,..) where(columanX OPLOG valorX)"
 *
 * 
 */
import hawc.CSV;
import hawc.Manager;
import hawc.Utils;
import hawc.Query;


public class HAWCquery {

	// Método principal del programa HAWCquery
	public static void main(String[] args){
		Manager manager;
		CSV csv;
		int CPUs=0;  // Por default TotalCPUs * 4
		String query="";
		String csvSalida="";

		// Revisión de los argumentos del programa
		int arguments=args.length;
		for (int i=0; i<args.length; i++) {
			if (args[i].equals("-n")) {
				CPUs=Integer.parseInt(args[++i]);
				arguments-=2;
			}
			else if (args[i].equals("-d")) {
				Utils.setDebug(true);
				arguments-=1;
			}
			else if (args[i].equals("-o")) {
				csvSalida = args[++i];
				arguments-=2;
			}
		}

		if (arguments < 2) {
			System.err.println("Requiere los argumentos: [-n Hilos] [-o CSV_salida] [-d] CSV_entrada \"Query\"");
			System.exit(0);
		} 
		
		if (!Query.sintaxisQuery(args[args.length-arguments+1]))
		{
			System.err.println("Error de sintaxis en el Query: "+args[args.length-arguments+1]);
			System.exit(0);
		}
		query = args[args.length-arguments+1];

		// Se crea el objeto CSV para manejar los procesos de I/O
		csv = new CSV();
		String csvEntrada=args[args.length-arguments];
		if ( !csv.inputFile(csvEntrada) ) {
			System.err.println("Error en la apertura del archivo de entrada");
			System.exit(0);
		}

		// Crea el directorio 'resultados'
		if (!Utils.createDir("resultados")) {
			System.err.println("Error al crear el directorio de resultados");
			System.exit(0);
		}

		// Si no se proporciona un nombre para el CSV de salida, se genera
		// un nombre con el nombre estándar.
		if ( csvSalida=="" ) { 
			csvSalida = Utils.getOutputFilenameFor(csvEntrada);
		}

		// Se crea el CSV de salida
		if ( !csv.outputFile(csvSalida) ) {
			System.err.println("Error en la apertura del archivo de salida");
			System.exit(0);
		}

		// Crea el manager para el control del
		// procesamiento serial/paralelo
		manager = new Manager(CPUs);

		// Inicia el procesamiento del Query del
		// usuario.
		if (!manager.processQuery(query, csv)) {
			System.err.println("Error al procesar el Query");
			System.exit(-1);
		}

		// Guarda el resultado del Query en el
		// archivo indicado
		if (!manager.saveQuery(csv)) {
			System.err.println("Error al guardar el resultado del Query");
			System.exit(-1);
		}

		// Recupera los tiempos de procesamiento e imprime estadisticas
		long[] timers;
		long totalTime=0;
		timers = manager.timers();
		for (int i=0; i<3; i++) totalTime+=timers[i];

		System.out.println ("========================================================================");
		System.out.println ("*                               METRICAS                               *");
		System.out.println ("========================================================================");
		System.out.println ("Hilos: "+manager.getCPUs());
		long stat[] = csv.estadisticas();
		System.out.println ("Registros de entrada: "+String.format("%,d",stat[0]));
		System.out.println ("Registros de salida:  "+String.format("%,d",stat[1]));
		if (manager.getCPUs()>1) { 
			System.out.println ("Segmentado de archivo en modo serial:\t"+
					String.format("%.2f",(double)timers[0]/1000000000)+" s"); 
			System.out.println ("Procesamiento del query en paralelo:\t"+
					String.format("%.2f",(double)timers[1]/1000000000)+" s"); 
			System.out.println ("Unificacion de salida en modo serial:\t"+
					String.format("%.2f",(double)timers[2]/1000000000)+" s");
		} else { 
			System.out.println ("Procesamiento del query en serie:\t"+
					String.format("%.2f",(double)timers[1]/1000000000)+" s"); 
		}

		System.out.println ("Tiempo total:\t\t\t\t"+String.format("%.2f",(double)totalTime/1000000000)+" s");
		System.out.println ("========================================================================");

	}
}
