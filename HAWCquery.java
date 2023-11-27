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
		}

		if (arguments < 3) {
			System.err.println("Requiere los argumentos: [-n CPUs] [-v] CSV_entrada CSV_salida \"Query\"");
			System.exit(0);
		} 
		
		if (!Query.sintaxisQuery(args[args.length-arguments+2]))
		{
			System.err.println("Error de sintaxis en el Query");
			System.exit(0);
		}
		query = args[args.length-arguments+2];

		csv = new CSV();
		if ( !csv.inputFile(args[args.length-arguments]) ) {
			System.err.println("Error en la apertura del archivo de entrada");
			System.exit(0);
		}

		if ( !csv.outputFile(args[args.length-arguments+1]) ) {
			System.err.println("Error en la apertura del archivo de salida");
			System.exit(0);
		}

		// Crea el manager para el control del
		// procesamiento serial/paralelo
		manager = new Manager(CPUs);

		// Inicia el procesamiento del Query del
		// usuario.
		manager.processQuery(query, csv);

		// Guarda el resultado del Query en el
		// archivo indicado
		manager.saveQuery(csv);

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
		System.out.println ("Segmentado de archivo en modo serial:\t"+(double)timers[0]/1000000000+" s");
		System.out.println ("Procesamiento del query "+(manager.getCPUs()>1?"en paralelo:\t":"en serie:\t")+(double)timers[1]/1000000000+" s");
		System.out.println ("Unificacion de salida en modo serial:\t"+(double)timers[2]/1000000000+" s");

		System.out.println ("Tiempo total:\t\t\t\t"+(double)totalTime/1000000000+" s");
		System.out.println ("========================================================================");

	}
}
