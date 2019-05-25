package processing

import java.util

import dk.dren.hunspell.Hunspell
import fllemmatizer.FLLemmatizer
import processing.FrenchLemmaAndPOSAnnotator.{RACINE, TAG_POS}

object FrenchLemmaAndPOSAnnotator {
  type RACINE = String
  type TAG_POS = String
}

/**
  * Dans Stanford Core NLP (framework utilisé dans le TP fouille de données textuelles), l’implémentation de la
  * lemmatisation pour le français est en cours et donc pas disponible actuellement. On peut utiliser cependant
  * une solution basée sur Apache OpenNLP et les travaux de Ahmet Aker (voir référence plus haut) qui a développé
  * des outils d’étiquetage morpho-syntaxique et de lemmatisation pour plusieurs langues européennes (français,
  * hollandais, anglais, allemand, italien et espagnol).
  *
  */
class FrenchLemmaAndPOSAnnotator(lemmatizer: CustomFLLemmatizer, hunspellDictionary: Hunspell#Dictionary) {

  def transform(wordAndMorphem: (String, String)): (RACINE, TAG_POS) = {
    import scala.collection.JavaConversions._
    val morph = lemmatizer.genericTypes(wordAndMorphem._2.toLowerCase)
    val lemme = wordAndMorphem._1
    val isNamedEntity = lemmatizer.dictionaries.getOrDefault("ENTITY", Map.empty[String, String]).contains(lemme)
    val word = if (!isNamedEntity && hunspellDictionary.misspelled(lemme)) {
      hunspellDictionary.suggest(lemme).get(0)
    } else lemme
    val lemma = lemmatizer.dictionaries
      .getOrDefault(morph, Map.empty[String, String])
      .getOrDefault(word, word)
    (lemma, morph)
  }
}

class CustomFLLemmatizer() {
  private val l = new FLLemmatizer("fr")

  val dictionaries: Map[String, util.Map[String, String]] = Seq("entity", "noun", "adj", "adv", "verb", "det", "pronoun")
    .map(morph => (morph.toUpperCase, l.loadDictionary(s"ressources/dictionaries/fr/${morph}Dic.txt")))
    .toMap
  val genericTypes: util.Map[RACINE, RACINE] = l.getFileContentAsMap("ressources/universal-pos-tags/frPOSMapping.txt", "######", true)
}
