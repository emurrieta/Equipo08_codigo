import hawc.CSV;
import hawc.Manager;
import hawc.Utils;
import hawc.Query;


public class HAWCquery {

	public static void main(String[] args){
		Manager manager;
		CSV csv;
		int CPUs=0;  // Por default TotalCPUs * 4
		String query="";
		String csvSalida="";

		int arguments=args.length;
		for (int i=0; i<args.length; i++) {
			if (args[i].equals("-n")) {
				CPUs=Integer.parseInt(args[++i]);
				arguments-=2;
			}
			else if (args[i].equals("-v")) {
				Utils.setDebug(true);
				arguments-=1;
			}
			else if (args[i].equals("-o")) {
				csvSalida = args[++i];
				arguments-=2;
			}
		}

		if (arguments < 2) {
			System.err.println("Requiere los argumentos: [-n CPUs] [-o CSV_salida] [-v] CSV_entrada \"Query\"");
			System.exit(0);
		} 
		
		if (!Query.sintaxisQuery(args[args.length-arguments+1]))
		{
			System.err.println("Error de sintaxis en el Query"+args[args.length-arguments+1]);
			System.exit(0);
		}
		query = args[args.length-arguments+1];

		csv = new CSV();
		String csvEntrada=args[args.length-arguments];
		if ( !csv.inputFile(csvEntrada) ) {
			System.err.println("Error en la apertura del archivo de entrada");
			System.exit(0);
		}

		if (!Utils.createDir("resultados")) {
			System.err.println("Error al crear el directorio de resultados");
			System.exit(0);
		}

		if ( csvSalida=="" ) { 
			csvSalida = Utils.getOutputFilenameFor(csvEntrada);
		}

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

		// Recupera los tiempos de procesamiento
		// e imprime estadisticas
		long[] timers;
		long totalTime=0;
		timers = manager.timers();
		for (int i=0; i<3; i++) totalTime+=timers[i];

		System.out.println ("========================================================================");
		System.out.println ("*                               METRICAS                               *");
		System.out.println ("========================================================================");
		System.out.println ("Hilos: "+manager.getCPUs());
		if (manager.getCPUs()>1) { 
			System.out.println ("Segmentado de archivo en modo serial:\t"+String.format("%.2f",(double)timers[0]/1000000000)+" s"); 
			System.out.println ("Procesamiento del query en paralelo:\t"+String.format("%.2f",(double)timers[1]/1000000000)+" s"); 
			System.out.println ("Unificacion de salida en modo serial:\t"+String.format("%.2f",(double)timers[2]/1000000000)+" s");
		} else { 
			//System.out.println ("Segmentado de archivo en modo serial:\t"+(double)timers[0]/1000000000+" s"); 
			System.out.println ("Procesamiento del query en serie:\t"+String.format("%.2f",(double)timers[1]/1000000000)+" s"); 
			//System.out.println ("Unificacion de salida en modo serial:\t"+(double)timers[2]/1000000000+" s");
		}

		System.out.println ("Tiempo total:\t\t\t\t"+String.format("%.2f",(double)totalTime/1000000000)+" s");
		System.out.println ("========================================================================");

	}
}
