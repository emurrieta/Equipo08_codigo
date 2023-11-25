
package hawc;

/* 
     * Clase de utileria para trabajar el query
    */ 

public class Query {
    
    /* 
     * Regresa false si el query no cumple la sintaxis
     * Regresa true si cumple la sintaxis
     * se requiere select( nombreColumna1 , nombreColuma2,... )
    *  
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
        
        
        //si no es exactamente 2 entonces hay un false
        if (  contadorParentesis!= 2 ) {
            return false ;
        }
        
        //quitamos los unicos dos parentesis
        //ahora cadena deberia contener un string separado por comas
        cadena = cadena.substring(1, cadena.length()-1);
        
        //revisamos que la cadena sin parentesis contenga algo
        //no se puede select()
        if (cadena.isEmpty()) {
            return false;
        }
        
        //asumimos que si el query fue select(*, col1,col2,...)
        //entonces se piden todas las columnas y el query es
        //sintactimente correcto. Se pide que ninguna columna se llame
        // * o contenga ese simbolo
        if( cadena.contains("*") ){
            return true;
        }
  
        return  true;
    }
    
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
    
    
}
