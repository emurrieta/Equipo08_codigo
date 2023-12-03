/*
 * Clase: DataFrame
 *
 * Clase para el filtrado de columnas en un archivo CSV.
 *
 * Programador: Alejandro Salcido
 * Revisiones: Eduardo Murrieta
 */
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
                boolean conCondicional=false;
		boolean condicion=false;
                String[] argumentosWhere;
                String[] listaSimbolosComparacion = new String[0];
                String[] nombreColumnasWhere = new String[0];
                int [] listaDeIndicesWhere = new int[0];
                double[] listaNumerosComparacion=new double[0];
                
                //quitamos los espacios en blanco del query
                query = query.replaceAll("\\s","");
                
                //obtenemos el nombre de las columnas en select()
                String[] nombreColumnas = Query.nombreColumas(query);
                int[] listaDeIndicesSelect = new int[nombreColumnas.length];
                
                //devolvemos argumentos dentro del where en forma de String
                //esta separado por comas
                String argumentosWhereCadena = Query.devolverWhere(query);

		if (argumentosWhereCadena.length()>0) { 
			conCondicional = true; 
			Utils.println("executeQuery> argumentosWhereCadena="+argumentosWhereCadena); 
			argumentosWhere =  argumentosWhereCadena.split(",") ; 
			Utils.println("executeQuery> argumentosWhere[0]="+argumentosWhere[0]); 
			
			//guardamoslasColumnas involucradas en el where 
			nombreColumnasWhere = Query.devolverNombresColWhere(argumentosWhere); 
			Utils.println("executeQuery> nombreColumnasWhere[0]="+nombreColumnasWhere[0]); 
			listaDeIndicesWhere = new int[nombreColumnasWhere.length]; 
			
			//guardamos los simbolos de comparacion involucrados en where 
			listaSimbolosComparacion = Query.devolverSimbolosWhere(argumentosWhere); 
			Utils.println("Comparacion "+listaSimbolosComparacion[0]); 
			
			//guardamos los numeros involucrados en el where 
			try {
			listaNumerosComparacion =  Query.devolverNumerosWhere(argumentosWhere) ; 
			} catch (Exception e) {
				System.out.println("Error de sintaxis: "+e);
				return false;
			}
			Utils.println("Numeros comparacion "+listaNumerosComparacion[0]);
		}
                
                //revisamos que los nombres en select()where() esten en la lista
                //de columnas del csv via el metodo Query.numeroColumnas
                try {
                    listaDeIndicesSelect = Query.numeroColumnas(nombreColumnas, this.colnames);
                    listaDeIndicesWhere = Query.numeroColumnas(nombreColumnasWhere, this.colnames);
                }
                catch(NoSuchElementException e) {
                    System.err.println("El nombre de una columa en el query no existe en la base");
		    System.exit(-1);
                }
                
                //obtenemos el encabezado
		String encabezado = String.join(",",nombreColumnas);
                this.archivoSalida.putRecord(encabezado);
                
                //empezamos a guardar las lineas del csv en el arreglo
                String record = archivoEntrada.getRecord();

                //recorremos el csv
                //while( !record.isEmpty() ) {
                while( record != null ) {
                    
                    //eliminamos todos los espacions en blanco del record
                    record = record.replaceAll("\\s","");
                    
                    //dividimos el record en comas
                    String[] vectorRecord = record.split(",");
                    
                    //creamos el vector de salida que almacenara los campos
                    //requeridos en el query
		    Utils.println("DataFrame.executeQuery> vectorRecord.length"+vectorRecord.length);

		    //REVISAMOS SI EL RECORD CUMPLE CON LAS CONDICIONES DE WHERE
		    if (conCondicional) 
			    condicion = aceptaRechazaRecord(vectorRecord,
                            listaDeIndicesWhere,
                            listaSimbolosComparacion ,
                            listaNumerosComparacion);
		    else condicion = true;

                    if ( condicion == true ) {

		    //@eml: vectorSalida no es igual en tamaño al registro leido, es igual
		    //@eml: al tamaño de las columnas selecionadas
                    //@eml: String[] vectorSalida = new String[vectorRecord.length];
                    String[] vectorSalida = new String[nombreColumnas.length];
                    
                    //obtenemos del vector de strings vectorRecord los indices
                    //que requerimos y que estan almacenados en el vector
                    //listaDeIndices
		    //@eml: El ciclo es sólo para el total de columnas seleccionadas
		    //@eml: no sobre el total de columnas en el registro
                    //@eml: for(int i=0; i< vectorRecord.length;i++) {
                    for(int i=0; i< vectorSalida.length;i++) {
			vectorSalida[i] = vectorRecord[ listaDeIndicesSelect[i] ];
                    }
                    
                    //unimos las entradas del vector de salida vectorSalida en
                    //un solo string y se lo pasamos a la funcion putRecord
                    String recordSalida = String.join(",", vectorSalida);
                    this.archivoSalida.putRecord(recordSalida);
		    } // Condicion
                    
                    
                    //volvemos a leer el siguiente record que nos proporciona
                    //el archivo de entrada
                    record = archivoEntrada.getRecord(); 
                }
                
                archivoSalida.flush();

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


        // Revisa si un record (doubles) cumple las condiciones dadas
        //el record esta leido directamente de la base aun sin selec columnas
        private boolean recordCondicionesWhere (double[] record, int[] numColumasComparacion, String[] simbolos, double[] numeros) {

            //la longitud de numColumasComparacion, simbolos y numeros
            //debe ser la misma

            //recorremos las columnas especificadas en numColumasComparacion
            for (int i=0;i < numColumasComparacion.length;i++) {

                //abrimos caso para los simbolos
                switch( simbolos[i] ) {
                    case "=":
                        if ( record[i] != numeros[i] ) {
                            return false;
                        }
                    case "<":
                        if ( !(record[i] < numeros[i]) ) {
                            return false;
                        }
                    case ">":
                        if ( !(record[i] > numeros[i]) ) {
                            return false;
                        }
                }//termina switch
            }

            //si todas las columnas cumplen los requisitos devolvemos true
            return true;
        }

        private String[][] splitComparativo(String[] comparaciones){

            //guardamoslasColumnas involucradas en el where
            String[] nombreColumnasWhere = new String[comparaciones.length];

            //guardamos los simbolos de comparacion involucrados en where
            String[] simboloComparacion = new String[comparaciones.length];

            //guardamos las cantidad involucradas en el where
            String[] numeroComparacion = new String[comparaciones.length];

            int indiceSimbolo =0;

            for( int j=0; j<comparaciones.length; j++ ) {
                //guardamos el simbolo
                simboloComparacion[j] = Query.signoCadena(comparaciones[j]);

                //guardamos el indice donde se encuentra el simbolo
                indiceSimbolo = comparaciones[j].indexOf(simboloComparacion[j]);

                //tomamos la subcadena que corresponde al nombre de
                //la columna
                nombreColumnasWhere[j] = comparaciones[j].substring(0, indiceSimbolo);

                //guardamos el numero de comparacion
                numeroComparacion[j] = comparaciones[j].substring(indiceSimbolo+1);
            }

            String[][] matriz =  new String[comparaciones.length][3];

            return new String[10][3];
        }

        //pasamos UN argumento de where(...) ejemplo colName7 > 980.4
        //regresa el numero de colName7 es decir 7
        private int columnaInvolucradaWhere(String argumentoWhere) {
            //guardamos el simbolo que trae el elemento del vector record
            String simbolo = Query.signoCadena(argumentoWhere);
            int indiceSimbolo = argumentoWhere.indexOf(simbolo);

            int numero = numeroColuma(argumentoWhere.substring(0, indiceSimbolo));

            return numero;
        }

        //version vectorial de columnaInvolucrada
        private int[] columnasInvolucradas(String[] argumentosWhere){
            int[] resultados = new int[argumentosWhere.length];
            for(int i=0; i< argumentosWhere.length; i++) {
                resultados[i] = columnaInvolucradaWhere(argumentosWhere[i]);
            }

            return resultados;
        }



        private boolean aceptaRechazaRecord(String[] vectorRecord, int[] indiceColWhere, String[] simbolos, double[] numeros ) {
            //recorremos los indices en indiceColWhere de vectorRecord y vemos
            //si cumple la condicion
            for (int i=0; i< indiceColWhere.length;i++) {
                int indice = indiceColWhere[i];
                String simbolo = simbolos[i];
                double numero = numeros[i];

                //vectorRecord solo tiene Strings pero todos son double
                double record = Double.parseDouble(vectorRecord[indice]);

                switch( simbolo ) {
                    case "=":
                        if ( record != numero ) return false; 
			break;
                    case "<":
                        if ( !(record < numero) ) return false; 
			break;
                    case ">":
                        if ( !(record > numero) ) return false; 
			break;
                    case "<=":
                        if ( !(record <= numero) ) return false; 
			break;
                    case ">=":
                        if ( !(record >= numero) ) return false; 
			break;
                }//termina switch

                //al terminar switch signifca que la entrada i de vectorRecord
                //cumple la condicion.La entrada i esta dada por indiceColWhere
            }//termina for
            return true;
        }//termina aceptaRechazaEntrada

        private int numeroColuma(String nombreColumna) {

            return Query.busquedaString(nombreColumna, this.colnames);
        }


}

