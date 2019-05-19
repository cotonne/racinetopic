# Pré-requis

## myopennlp

## Récupération de myopennlp.jar

    wget http://cedric.cnam.fr/vertigo/Cours/RCP216/tpLemmatisationFr.html

## Amélioration du dictionnaire

    wget http://www.dicollecte.org/download/fr/lexique-dicollecte-fr-v6.4.1.zip
    unzip lexique-dicollecte-fr-v6.4.1.zip
    tail -n +16 lexique-dicollecte-fr-v6.4.1.txt  | cut -d$'\t' -f4,3,5 > lexique-dicollecte-fr-v6.4.1-simple.txt
    jar -xf myopennlp.jar fllemmatizer/ressources/dictionaries/fr/
    python3 scripts/lexique.py adv
    jar -uf myopennlp.jar fllemmatizer/

## Installation

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
     target/scala-2.11/racinetopic_2.11-2.4.0_0.1.0-SNAPSHOT.jar /path/to/book 

