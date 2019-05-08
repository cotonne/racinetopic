
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-amd64
sbt assembly
/home/yvan/Documents/cnam/nfe204/datahub/spark-2.4.0-bin-hadoop2.7/bin/spark-submit --class racine.FindConceptProg --driver-memory 2g --master local[*] target/scala-2.11/racinetopic_2.11-2.4.0_0.1.0-SNAPSHOT.jar /home/yvan/Documents/racinetopic/src/test/resources/pg15790.html