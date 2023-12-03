%.class: %.java
	javac $<

HAWCquery.class: HAWCquery.java hawc/CSV.class hawc/DataFrame.class hawc/InterfaceCSV.class hawc/InterfaceDataFrame.class hawc/InterfaceManager.class hawc/Manager.class hawc/Query.class hawc/StopWatch.class hawc/Utils.class hawc/Worker.class
	javac HAWCquery.java

HAWCquery: HAWCquery.class

clean:
	rm -f hawc/*.class HAWCquery.class
	rm -rf resultados

demo:   HAWCquery.class
	java HAWCquery data/reco_run002054_00226_sample100.csv "select(rec.eventID/U/1,rec.coreX/F/0.1,rec.coreY/F/0.1,rec.logNPE/F/0.01)"
