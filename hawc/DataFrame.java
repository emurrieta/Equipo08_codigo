package hawc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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
                
                int numRenglones = 500; //numero de renglones que se usaran
                
                String[] nombreColumnas = Query.nombreColumas(query);
                
                int[] listaDeIndices = Query.numeroColumnas(nombreColumnas, this.colnames);
                
                //String cadenaNombres = archivoEntrada.getRecord(); //obtenemos el header
                //this.colnames = cadenaNombres.split(","); //dividimos la cadena
                
                //aqui guardamos el conjunto de las lineas del archivo (strings)
                //ArrayList<String> arregloEntrada = new ArrayList<>();
                //ArrayList<String> arregloSalida = new ArrayList<>();
                
                //empezamos a guardar las lineas del csv en el arreglo
                String record = archivoEntrada.getRecord();
                
                //recorremos el csv
                while( !record.isEmpty() ) {
                    
                    //eliminamos todos los espacions en blanco del record
                    record = record.replaceAll("\\s","");
                    
                    //dividimos el record en comas
                    String[] vectorRecord = record.split(",");
                    
                    //creamos el vector de salida que almacenara los campos
                    //requeridos en el query
                    String[] vectorSalida = new String[vectorRecord.length];
                    
                    //obtenemos del vector de strings vectorRecord los indices
                    //que requerimos y que estan almacenados en el vector
                    //listaDeIndices
                    for(int i=0; i< vectorRecord.length;i++) {
                        vectorSalida[i] = vectorRecord[ listaDeIndices[i] ];
                    }
                    
                    //unimos las entradas del vector de salida vectorSalida en
                    //un solo string y se lo pasamos a la funcion putRecord
                    String recordSalida = String.join(",", vectorSalida);
                    this.archivoSalida.putRecord(recordSalida);
                    
                    
                    //volvemos a leer el siguiente record que nos proporciona
                    //el archivo de entrada
                    record = archivoEntrada.getRecord(); 
                }
                
                System.out.println("Executing Query: " + String.join(",", nombreColumnas) );
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
        
        private int[] numeroColumnasDF (String[] columnas) throws NoSuchElementException {
            
            List<String> nombres = Arrays.asList( this.colnames);
            int[] indices = new int[columnas.length];
            
            int indice = 0;
            
            for (int i=0; i< columnas.length;i++) {
                indice = nombres.indexOf( columnas[i] );
                
                if (indice == -1) {
                throw new NoSuchElementException("empty list") ; 
                }
                indices[i] = indice;
            }
            
            return indices;
        }
}
