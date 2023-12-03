/*
 * Clase: Query
 *
 * Clase de utileria para analizar el query.
 *
 * Programador: Alejandro Salcido
 * Correciones: Eduardo Murrieta
 */ 
package hawc;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


public class Query {

    /* 
     * Regresa false si el query no cumple la sintaxis
     * Regresa true si cumple la sintaxis
     * se requiere select( nombreColumna1 , nombreColuma2,... )
     *  where(columna1<45,columna2+columna5,...)
     */ 
    public static boolean sintaxisQuery(String cadena) {
        
        //elimina espacios en blanco antes del primer caracer y al ultimo
        cadena = cadena.trim();
        
        String instruccionSelect = cadena.substring(0, 6);
        
        //verificamos que las primeras letras formen la palabra select
        if (!instruccionSelect.contentEquals("select")) {
            return false;
        }
        
        //quitamos la cadena la palabra select
        cadena = cadena.substring(6);
        //eliminamos espacios en blanco
        cadena = cadena.replaceAll("\\s","");
        
       
        //cuenta el numero de veces que aparece ) o (
        int contadorParentesis = 0;
        
        //contamos el numero de veces que aparece ) o (
        for( char valor: cadena.toCharArray() ) {
            if (valor == '(' || valor== ')' ) {
                contadorParentesis++;
            }  
        }
        
        //si no es congruente con 2 entonces hay un false
        if ( ( contadorParentesis % 2) !=0 ) {
            return false ;
        }
        
        //quitamos los primeros dos parentesis
        //ahora cadena deberia contener un string separado por comas
        int parentesisInicialSelect = cadena.indexOf("(");
        int parentesisFinalSelect = cadena.indexOf(")");
        
        //si el paretesis inicial no es 0 y no tiene parentesis final 
        //regresa falso
        if(parentesisInicialSelect != 0 && parentesisFinalSelect== -1 ) {
            return false;
        }
        
          //revisamos que la cadena sin parentesis contenga algo
        //no se puede select()
        String argumentosSelect = cadena.substring(parentesisInicialSelect+1, parentesisFinalSelect);
        
        if ( argumentosSelect.isEmpty() ) {
            return false;
        }
        
        
        //cadena= "where(...)AddColum(..) o "where(...)" o "AddColumn(...)" 
        cadena = cadena.substring(parentesisFinalSelect+1);
        
        int presenciaWhere = Query.conteoCadenas("where", cadena);
        //int presenciaAddColumn = cadena.indexOf("AddColumn");
        
        if ( presenciaWhere >1 ) {
            return false;
        }
        else if (presenciaWhere == 1) {
            String inicioWhere;
	    try { 
		    inicioWhere = cadena.substring(0, "where(".length() );
	    } catch (StringIndexOutOfBoundsException e){
		    return false;
	    }
        
            if ( !inicioWhere.equals("where(") ){
              
                return false;
            }
             
            //revisamos si tenemos parentesis final
            int parentesisFinalWhere = cadena.indexOf(")"); 
            if( parentesisFinalWhere == -1 ) {
                return false;
            }
            
            
            String argumentosWhereCadena = cadena.substring("where(".length(), parentesisFinalWhere);
            
            //evitamos el select(...)where() vacio
            if ( argumentosWhereCadena.isEmpty() ) {
                return false;
            }

	    String [] argumentosWhere = argumentosWhereCadena.split(",") ;

	    String [] nombreColumnasWhere = Query.devolverNombresColWhere(argumentosWhere);
	    if (nombreColumnasWhere.length==0) return false;

	    String [] listaSimbolosComparacion = Query.devolverSimbolosWhere(argumentosWhere);
	    if (listaSimbolosComparacion.length==0) return false;

	    double [] listaNumerosComparacion;

	    try { 
		    listaNumerosComparacion = Query.devolverNumerosWhere(argumentosWhere) ;
	    } catch (Exception e) {
		    return false;
	    }
	    if (listaNumerosComparacion.length==0) return false;
            
            if( !cadena.substring(parentesisFinalWhere+1).isEmpty()) {
                //la cadena deberia terminar
                return false;
            }
            
            //seguiriamos si la cadena es del tipo select()where()Addcolum()
            return true;
        } else {
            
            //se construyen dos casos para addColumn
            return true;
        }
       
    } //sintaxisQuery
    
    /* 
     * Regresa las columnas en forma de un array de Strings
     * Es todo lo que se encuentre dentro de parentesis ()
    */ 
    
    public static String[] nombreColumas( String cadena) {
        
        
        //elimina espacios en blanco en la cadena
        cadena = cadena.replaceAll("\\s+","");
        
        int indiceApertura = cadena.indexOf("(");
        int indiceCierre = cadena.indexOf(")");
        
        String columnasComas = cadena.substring(indiceApertura+1, indiceCierre);
        
        String[] vectorNombresColumas = columnasComas.split(",");
        
        return vectorNombresColumas;
    }
    
    public static int[] numeroColumnas (String[] listaNombres, String[] headers ) throws NoSuchElementException {
            
            //List<String> nombres = Arrays.asList( nombreCol );
            
            //convertimos el vector de Strings a un array para buscar en el
            List<String> lista = Arrays.asList( headers );
            //@eml: lista->encabezados

	    //@eml: listaNombres->seleccionColumnas
            int[] indices = new int[listaNombres.length];
            
            int indice = 0;
            
            for (int i=0; i< listaNombres.length;i++) {
                
                indice = lista.indexOf( listaNombres[i] );
                
                if (indice == -1) {
                throw new NoSuchElementException("No se encontro elemento en "
                        + "la lista de variables de la base") ; 
                }
                indices[i] = indice;
		Utils.println("Utils.numeroColumnas>"+"listaNombres[i]="+listaNombres[i]+" indice="+indice+" headers[indice]="+lista.get(indice));
            }
            
            return indices;
        }
    
    public static int conteoCadenas(String busqueda, String cadena){
        
        if(cadena.length() ==0) {
            return 0;
        }
        
        int indicePrimeraOcurrencia = cadena.indexOf(busqueda);
        
        if( indicePrimeraOcurrencia != -1) {
            
            //rrecortamos la cadena
            String subcadena = cadena.substring(busqueda.length()+indicePrimeraOcurrencia);
            
            //aplicamos recursion
            return 1+ conteoCadenas( busqueda, subcadena);
        }
        else {
            return 0;
        }
    }//termina conteoCadenas 
    
    //devuelve los argumentos de select(...) por ejemplo
    //select(A,B,C) devuelve "A,B,C"
    public static String devolverSelect(String query) {
        query = query.replaceAll("\\s","");
        int instruccionSelect = query.indexOf("select");
        int parentesisCierre = query.indexOf(")");
        
        int longitudPalabraSelect = "select".length();
        
        String subcadena = new String();
        
        try {
            subcadena = query.substring(longitudPalabraSelect+instruccionSelect+1,parentesisCierre );
        } catch(java.lang.StringIndexOutOfBoundsException e) {
            subcadena = "";
        }
      
        return subcadena;
    }
    
    //devuelve los nombres en select(...) ejemplo
    //"A,B,C" devuelve [A,B,C]
    public static String[] devolverNombresSelect (String argumentosSelect) {
        String[] resultado = argumentosSelect.split(",");
        return resultado;
    }
    
    public static String devolverWhere(String query) {
        
        query = query.replaceAll("\\s","");
        int instruccionWhere = query.indexOf("where");
        
        int parentesisCierre = query.indexOf(")", query.indexOf(")")+1 );
        int longitudPalabraWhere = "where".length();
        
        String subcadena = new String();
        
        try {
            subcadena = query.substring(longitudPalabraWhere+instruccionWhere+1,parentesisCierre );
        } catch(java.lang.StringIndexOutOfBoundsException e) {
            subcadena = "" ;
        }
       
        return subcadena;
    }
    
    //devuelve el simbolo de un argumento de where(...) ejemplo
    //colname7=94.5  devuelve "="
    public static String devolverSimboloWhere( String argumentoWhere){
        
        return Query.signoCadena(argumentoWhere);
    }
    
    //version vectorial de devolverSimboloWhere
    public static String[] devolverSimbolosWhere( String[] argumentosWhere){
        String[] resultados = new String[argumentosWhere.length];
        
        for(int i=0; i< argumentosWhere.length; i++) {
            resultados[i] = devolverSimboloWhere(argumentosWhere[i]);
        }
        
        return resultados;
    }
    
    //devuelve el numero de un argumento de where(...) ejemplo
    //colname7=94.5  devuelve 94.5
    public static double devolverNumeroWhere( String argumentoWhere) throws Exception {
	  
        //guardamos el simbolo que trae el elemento del vector record
        String simbolo = Query.signoCadena(argumentoWhere);
        
        int indiceSimbolo = argumentoWhere.indexOf(simbolo);
	Utils.println("simbolo "+simbolo+" len="+simbolo.length());
        
        //el numero se encuentra despues de =, > o <
        String subcadena = argumentoWhere.substring(indiceSimbolo+simbolo.length());
         
        //convertimos el string de la subcadena a un double
	double numero = Double.parseDouble(subcadena) ;
            
        return numero;
    }
    
    //version vectorial de devolverNumeroWhere
    public static double[] devolverNumerosWhere( String[] argumentosWhere) throws Exception {
        double[] resultados = new double[argumentosWhere.length];
        
        for(int i=0; i< argumentosWhere.length; i++) {
            //try { 
		    resultados[i] = devolverNumeroWhere(argumentosWhere[i]);
	    /*} catch (Exception e) {
		    resultados[i] = Double.NaN;
		    System.err.println("Error "+e);
	    }*/
        }
        
        return resultados;
    }
    
    
    
    public static String devolverAddColumn(String query){
        query = query.replaceAll("\\s","");
        
        int instruccionAddColumn = query.indexOf("AddColumn");
        
        int segundaOcurrenciaParentesisCierre = query.indexOf(")", query.indexOf(")")+1 );
        
        int parentesisCierre = query.indexOf(")", segundaOcurrenciaParentesisCierre+1);
        
        int longitudPalabraAddColumn= "AddColumn".length();
      
        return query.substring(instruccionAddColumn+longitudPalabraAddColumn+1,parentesisCierre );
    }
    
    //devuelve el signo de comparacion presente en una cadena del tipo
    // colname1 > floante, colname4 = 7, colname1>colname3, etc
    public static String signoCadena(String cadena) {
        
        //int posicionSimboloIgual =  cadena.contentEquals("=") ? 1 : 0;
        //int posicionSimboloMenor = cadena.contentEquals("<") ? 1 : 0;;
        //int posicionSimboloMayor = cadena.contentEquals(">") ? 1 : 0;;
        String valor = "";
        
        if( cadena.contains(">=") ) {
            valor= ">=";
        } else if( cadena.contains("<=") ) {
            valor= "<=";
        } else if (cadena.contains("=") ) {
            valor = "=";
        } else if (cadena.contains("<") ) {
            valor =  "<";
        } else if( cadena.contains(">") ) {
            valor= ">";
        }

        return valor;
        
    }//cierra signoCadena
    
    public static int busquedaString (String valorBuscar, String[] vector) {
       
        int valor  = Arrays.asList( vector).indexOf(valorBuscar);
        
        return valor;
    }
    
    //devuelve el nombre de una columa en un argumento where(...) ejemplo
    //"col1>3.3" devuelve "col1"
    public static String devolverNombreColWhere(String argumentoWhere) {
        
        //guardamos el simbolo que trae el elemento del vector record
        String simbolo = Query.signoCadena(argumentoWhere);
        
        int indiceSimbolo = argumentoWhere.indexOf(simbolo);
        
        //el nombre se encuentra antes de =, > o <
        String subcadena = argumentoWhere.substring(0,indiceSimbolo);
         
        return subcadena;
    }
    
    //version vectorial de devolverNombreColWhere
    public static String[] devolverNombresColWhere (String[] argumentosWhere){
        
        String[] resultados = new String[argumentosWhere.length];
        
        for(int i=0; i< argumentosWhere.length; i++) {
            resultados[i] = devolverNombreColWhere(argumentosWhere[i]);
        }
        
        return resultados;
    }
   
    
}//cierra clase query

