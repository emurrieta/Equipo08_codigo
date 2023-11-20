import hawc.CSV;
import hawc.Manager;

public class HAWCquery {

	public static void main(String[] args){
		Manager manager;
		CSV csv;

		if (args.length < 3) {
			System.out.println("Argumentos:  HAWCquery CSV_entrada CSV_salida Query");
			System.exit(0);
		} 

		csv = new CSV();
		if ( !csv.inputFile(args[0]) ) {
			System.out.println("Error en la apertura del archivo de entrada");
			System.exit(0);
		}

		manager = new Manager();
		manager.processQuery(args[2], csv);
	}
}
