
package hawc;

public class Query {
    
    /* 
     * Regresa false si el query no cumple la sintaxis
     * Regresa true si cumple la sintaxis
     * se requiere select( nombreColumna1 , nombreColuma2,... )
    */ 
    
    
    public boolean sintaxisQuery(String cadena) {
        
        //Ejemplo: select (nombre, edad, calificacion)) ) 
        
        String consulta = cadena.trim();
        
        
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
    
}
