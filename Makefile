%.class: %.java
	javac $<

# Compila todas las clases que conforman la apicacion
HAWCquery.class: HAWCquery.java hawc/CSV.class hawc/DataFrame.class hawc/InterfaceCSV.class hawc/InterfaceDataFrame.class \
	hawc/InterfaceManager.class hawc/Manager.class hawc/Query.class hawc/StopWatch.class hawc/Utils.class hawc/Worker.class
	javac HAWCquery.java

# Compila el programa principal
HAWCquery: HAWCquery.class

# Elimina todos los archivos compilados y el directorio de resultados
clean:
	rm -f hawc/*.class HAWCquery.class
	rm -f data/reco_run010459_00001-00004.csv
	rm -f data/bigSample.zip
	rm -rf resultados

# Descarga el archivo de pruebas y lo descomprime
download: 
	[ -f data/bigSample.zip ] || curl -L -o data/bigSample.zip 'https://docs.google.com/uc?export=download&id=1pB67QdNKIygB1V9Mv2QqPaATN7cZJ-mF&confirm=no_antivirus'
	unzip -u -d data data/bigSample.zip

data/reco_run010459_00001-00004.csv: download


# Ejecuta el ejemplo basico
shortdemo:   HAWCquery.class 
	java HAWCquery data/reco_run002054_00226_sample100.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01)"

bigdemo: HAWCquery.class data/reco_run010459_00001-00004.csv
	java HAWCquery data/reco_run010459_00001-00004.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01)"

serialdemo: HAWCquery.class data/reco_run010459_00001-00004.csv
	java HAWCquery -n 1 data/reco_run010459_00001-00004.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01)"

wheredemo: HAWCquery.class data/reco_run010459_00001-00004.csv
	java HAWCquery data/reco_run010459_00001-00004.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01) where(rec.eventID/U/1>=200,rec.coreX/F/0.1>=0)"

notebook: Postprocesamiento.ipynb
	jupyter notebook --NotebookApp.token='' --NotebookApp.password='' #Postprocesamiento.ipynb
