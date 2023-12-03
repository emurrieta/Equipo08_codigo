# HAWCquery
Repositorio del proyecto de Programación Avanzada del PCIC UNAM Gen. 2024-1

El presente repositorio contienen el desarrollo del proyecto final del curso
de Programación Avanzada del Posgrado en Ciencia e Ingeniería de la Computación
de la UNAM Gen. 2024-1.

El programa principal (main) se encuentra en el archivo HAWCquery.java. 
Existen dos directorios: data y hawc.
El directorio hawc contiene el resto de las clases del programa y se encuentran
agrupadas en el paquete hawc.
El directorio data contiene un ejemplo con pocos registros (100) de un archivo
csv de entrada y otro con 500 mil registros.

100 registros:      data/reco_run002054_00226_sample100.csv
500 mil registros:  data/reco_run002054_00226_sample.csv.zip

El código se compila con:

make 

o con 

javac HAWCquery.java

Ejemplos de ejecución:

Sin argumentos
$ java HAWCquery 
Requiere los argumentos: [-n Hilos] [-o CSV_salida] [-d] CSV_entrada "Query"

El programa muestra que para ejecutarse existen argumentos que son obligatorios
entre corchetes [] y definitivos sin corchetes.

[-n Hilos] : inidica el número de hilos a usar, si es 1 la ejecución es serial,
             si es mayor a uno se ejecuta con el numero de hilos indicado. Por
             omisión el número de hilos es igual al total de cores disponibles
             multiplicado por 4.

[-o CSV_salida] : Indica un nombre para el archivo de salida, en caso de no
             indicarlo los resultados se guardan en el directorio resultados
             (se crea si no existe) con el nombre:
                                     CSV_entrada_filtered(AAAAMMDD_HHMM).csv
[-d] :       Habilita el modo de depuración, imprime mensajes utilizados para
             dar seguimiento al flujo del proceso.

CSV_entrada : Nombre del archivo CSV de entrada.

"Query" :    Entrecomillado, indica los campos que deben seleccionarse del
             CSV como por ejemplo "select(rec.eventID/U/1,rec.logNPE/F/0.01)"
             indica que se deben seleccionar las columnas "rec.eventID/U/1"
             y "rec.logNPE/F/0.01".
             En caso de que exista algún error en la sintaxis del Query o
             en el nombre de algún campo, se notifica de un error y el 
             programa termina.

Una ejecución de ejemplo:

make demo

o con:

java HAWCquery data/reco_run002054_00226_sample100.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01)"

Con condicionales:
 
java HAWCquery data/reco_run002054_00226_sample100.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01) where(rec.eventID/U/1<=100,rec.coreX/F/0.1>=0)" 

Al terminar el programa se muestran las mediciones del tiempo invertido en
cada etapa del procesamiento como se muestra a continuación.

                                METRICAS                                

Hilos: 96

Registros de entrada: 5,952,076

Registros de salida:  694,535

Segmentado de archivo en modo serial:	10.53 s

Procesamiento del query en paralelo:	4.46 s

Unificacion de salida en modo serial:	0.23 s

Tiempo total:				                  15.22 s
