package processing

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

case class NlpPipeline() {
  type BagOfWords = Set[String]
  val stopWords: Set[String] = Source.fromInputStream(getClass.getResourceAsStream("/french-nltk-stopwords")).getLines().toSet
  val pipeline: StanfordCoreNLP = createNLPPipeline()

  private def createNLPPipeline(): StanfordCoreNLP = {
    val props = new Properties()
    // cf. https://stanfordnlp.github.io/CoreNLP/annotators.html
    val annotators = Seq(
      "tokenize", // This splits the text into roughly “words”, using rules or methods suitable for the language being processed.
      "ssplit", // Splits a sequence of tokens into sentences.
      "pos" //, // Labels tokens with their POS tag
      //"custom.lemma" // Generates the word lemmas for all tokens in the corpus.
    ).mkString(",")
    props.put("annotators", annotators)
    props.put("parse.model", "edu/stanford/nlp/models/lexparser/frenchFactored.ser.gz")
    props.put("pos.model", "edu/stanford/nlp/models/pos-tagger/french/french.tagger")
    props.put("props_fr", "StanfordCoreNLP-french.properties")
    props.put("tokenize.language", "fr")
    new StanfordCoreNLP(props)
  }

  private def isOnlyLetters(str: String): Boolean = {
    str.forall(c => Character.isLetter(c))
  }

  def normalize(text: String): BagOfWords = {
    val doc: Annotation = new Annotation(text)
    pipeline.annotate(doc)
    val annotationToStrings = toBagOfWords andThen
      toLowerCase andThen
      removeStopWords andThen
      lematize
    annotationToStrings(doc)
  }

  private val toBagOfWords: Annotation => BagOfWords = doc => {
    val lemmas = new ArrayBuffer[String]()
    val sentences = doc.get(classOf[SentencesAnnotation])
    for (sentence <- sentences.asScala;
         token <- sentence.get(classOf[TokensAnnotation]).asScala) {
      val lemma = token.get(classOf[TextAnnotation])
      lemmas += lemma
    }
    lemmas.toSet
  }

  private val toLowerCase: BagOfWords => BagOfWords = bagOfWords => bagOfWords.map(_.toLowerCase)

  private val removeStopWords: BagOfWords => BagOfWords = _.filter(lemma => lemma.length > 2 && !stopWords.contains(lemma)
    && isOnlyLetters(lemma))

  private val MORPHEM = Seq("NOUN", "VERB", "ADV", "ADJ")
  private val lematize: BagOfWords => BagOfWords = x => x.map(FrenchLemmaAndPOSAnnotator.transform)
    .filter(word => MORPHEM.contains(word._2))
    .map(_._1)
}
