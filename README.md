Installation de la librairie  (myopennlp.jar)[http://cedric.cnam.fr/vertigo/Cours/RCP216/tpLemmatisationFr.html] 
avec :

    mvn install:install-file -Dfile=lib/myopennlp.jar \
     -DgroupId=fr.cnam \
     -DartifactId=myopennlp \
     -Dversion=1.0.0 \
     -Dpackaging=jar 
     
