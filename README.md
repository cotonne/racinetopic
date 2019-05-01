# Pré-requis

Installation de la librairie  (myopennlp.jar)[http://cedric.cnam.fr/vertigo/Cours/RCP216/tpLemmatisationFr.html] 
avec :

    mvn install:install-file -Dfile=lib/myopennlp.jar \
     -DgroupId=fr.cnam \
     -DartifactId=myopennlp \
     -Dversion=1.0.0 \
     -Dpackaging=jar 
     
     
# Utilisation

    sbt assembly
    export JAVA_HOME=/path/to/jvm/java-1.8.0
    spark-submit --class racine.FindConceptProg \
     --driver-memory 2g \
     --master local[*] \
     --conf book=/path/to/book 
     target/scala-2.11/racinetopic_2.11-2.4.0_0.1.0-SNAPSHOT.jar
