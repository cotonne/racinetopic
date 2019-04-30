package processing

import fllemmatizer.FLLemmatizer

/**
  * Dans Stanford Core NLP (framework utilisé dans le TP fouille de données textuelles), l’implémentation de la
  * lemmatisation pour le français est en cours et donc pas disponible actuellement. On peut utiliser cependant
  * une solution basée sur Apache OpenNLP et les travaux de Ahmet Aker (voir référence plus haut) qui a développé
  * des outils d’étiquetage morpho-syntaxique et de lemmatisation pour plusieurs langues européennes (français,
  * hollandais, anglais, allemand, italien et espagnol).
  *
  */
object FrenchLemmaAndPOSAnnotator {
  private val lemmatizer: FLLemmatizer = new FLLemmatizer("fr")

  type RACINE = String
  type TAG_POS = String

  def transform(word: String): (RACINE, TAG_POS) = {
    import scala.collection.JavaConversions._
    val lemma = lemmatizer.lemmatize(word, true).flatten
    (lemma.get(2), lemma.get(1))
  }
}
