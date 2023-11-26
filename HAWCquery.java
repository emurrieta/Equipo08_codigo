import hawc.CSV;
import hawc.Manager;
import hawc.Utils;
import hawc.Query;


public class HAWCquery {

	public static void main(String[] args){
		Manager manager;
		CSV csv;

		if (args.length < 3) {
			System.err.println("Argumentos:  CSV_entrada CSV_salida \"Query\"");
			System.exit(0);
		} 
		
		if (!Query.sintaxisQuery(args[2]))
		{
			System.err.println("Error de sintaxis en el Query");
			System.exit(0);
		}

		// Activa/Desactiva el modo de mensajes de depuracion
		Utils.setDebug(false);

		csv = new CSV();
		if ( !csv.inputFile(args[0]) ) {
			System.err.println("Error en la apertura del archivo de entrada");
			System.exit(0);
		}

		if ( !csv.outputFile(args[1]) ) {
			System.err.println("Error en la apertura del archivo de salida");
			System.exit(0);
		}

		// Crea el manager para el control del
		// procesamiento serial/paralelo
		manager = new Manager(true);

		// Inicia el procesamiento del Query del
		// usuario.
		manager.processQuery(args[2], csv);

		// Guarda el resultado del Query en el
		// archivo indicado
		manager.saveQuery(csv);

		// Recupera los tiempos de procesamiento
		// e imprime estadisticas
		int[] aiTimers;
		aiTimers = manager.timers();
	}
}
