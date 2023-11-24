
package hawc;

/* 
     * Clase de utileria para trabajar el query
    */ 

public class Query {
    
    /* 
     * Regresa false si el query no cumple la sintaxis
     * Regresa true si cumple la sintaxis
     * se requiere select( nombreColumna1 , nombreColuma2,... )
    */ 
    
    
    public static boolean sintaxisQuery(String cadena) {
        
        //Ejemplo: select (nombre, edad, calificacion)) ) 
        
        String consulta = cadena.replaceAll("\\s","");
        //String consulta = cadena.trim();
        
        //el string debe contener la subcadena select
        boolean condicion1 = consulta.contains("select");
        
        //cuenta el numero de veces que aparece ) o (
        int contador = 0;
        
        //contamos el numero de veces que aparece ) o (
        for( char valor: cadena.toCharArray() ) {
            if (valor == '(' || valor== ')' ) {
                contador++;
            }  
        }
        
        
        boolean condicion2 = true; 
        
        //si no es exactamente 2 entonces hay un false
        if (  contador!= 2 ) {
            condicion2 = false ;
        }
  
        return condicion1 && condicion2 ;
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
