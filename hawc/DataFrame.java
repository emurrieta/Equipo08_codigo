package hawc;

import java.util.ArrayList;

public class DataFrame implements InterfaceDataFrame<CSV> {
    
   
   private String[] colnames; //
   private CSV archivoEntrada;
   private CSV archivoSalida;
    
    
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
                
                
                int numRenglones = 500; //numero de renglones que se usaran
                
                String cadenaNombres = archivoEntrada.getRecord(); //obtenemos el header
                this.colnames = cadenaNombres.split(","); //dividimos la cadena
                
                //aqui guardamos las lineas del archivo (strings)
                ArrayList<String> arreglo = new ArrayList<>();
                ArrayList<String> arregloSalida = new ArrayList<>();
                
                //empezamos a guardar las lineas del csv en el arreglo
                String record = archivoEntrada.getRecord();
                while( !record.isEmpty() ) {
                    arreglo.add(record);
                    record = archivoEntrada.getRecord(); //pedimos la siguiente linea
                }
                
                //en cada entrada del arreglo de strings hacemos 
                //aplicamos los criterios del query
                
                for (String valor: arreglo) {
                    
                    //obtenemos una linea del csv
                    String[] linea = valor.split(",");
                    
                    //aplicamos el query a la linea
                    try {
                        System.out.println("Ejecutamos query en linea");
                        
                    }
                    catch(Exception e) {
                        System.out.println("Error al procesar query");
                        return false; //termina la funcion
                    }
                    
                    String consulta = "2,2,3,3,4,a";
                    
                    //guardamos la consulta al arreglo de salidas
                    arregloSalida.add(consulta );
                      
                } //termina de recorrer el arreglo
                
                //el arreglo de salida lo descargamos en el csv de salida
                for(String valor: arregloSalida) {
                    archivoSalida.putRecord(  valor   );
                } 
		return true;       
	} // termina executeQuery

	/* 
	 * Retorna un arreglo con los encabezados del DataFrame.
	 */ 
   @Override
	public String[] headers() { 
                
            return this.colnames;
        
        }

	/* 
	 * Define el CSV de entrada para el DataFrame
	 */ 
	public void inputCSV(CSV csv) { 
            
            this.archivoEntrada = csv;
            
            //leemos la primera linea del csv
            String cadena =  csv.getRecord();
            
            //eliminamos todos los espacios en blanco
            cadena = cadena.replaceAll("\\s","");
             
            //dividimos la cadena por comas y lo asignamos a arreglo de Strings 
            this.colnames = cadena.split(",");
            
      
              
        }

	/* 
	 * Define el CSV para guardar la salida del procesamiento
	 * del DataFrame.
	 */ 
	public void outputCSV(CSV csv) { 
            
            this.archivoSalida = csv;        
        
        }
}
