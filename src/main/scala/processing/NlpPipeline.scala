package processing

import java.util.Properties

import edu.stanford.nlp.ling.CoreAnnotations._
import edu.stanford.nlp.pipeline.{Annotation, StanfordCoreNLP}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.io.Source

case class NlpPipeline() {
  type BagOfWords = Set[String]
  type POSBagOfWords = Map[String, String]
  val stopWords: Set[String] = Source.fromInputStream(getClass.getResourceAsStream("/stopwords-fr.txt")).getLines().toSet
  val pipeline: StanfordCoreNLP = createNLPPipeline()
  private val lemmatizer: CustomFLLemmatizer = new CustomFLLemmatizer()

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
      lemmatize
    annotationToStrings(doc)
  }

  private val toBagOfWords: Annotation => POSBagOfWords = doc => {
    val lemmas = mutable.Map[String, String]()
    val sentences = doc.get(classOf[SentencesAnnotation])
    for (sentence <- sentences.asScala;
         token <- sentence.get(classOf[TokensAnnotation]).asScala) {
      val lemma = token.get(classOf[TextAnnotation])
      val morph = token.get(classOf[PartOfSpeechAnnotation])
      lemmas.+=((lemma, morph))
    }
    lemmas.toMap
  }

  private val toLowerCase: POSBagOfWords => POSBagOfWords = _.map(tuple => (tuple._1.toLowerCase, tuple._2))

  private val removeStopWords: POSBagOfWords => POSBagOfWords = bow => bow.filter(tuple => tuple._1.length > 2 && !stopWords.contains(tuple._1)
    && isOnlyLetters(tuple._1))

  private val MORPHEM = Seq("NOUN", "VERB", "ADV", "ADJ")
  private val lemmatize: POSBagOfWords => BagOfWords = posBoW => {
    val annotator = new FrenchLemmaAndPOSAnnotator(lemmatizer)
    posBoW.map(annotator.transform)
      .filter(word => MORPHEM.contains(word._2))
      .keySet
  }
}
